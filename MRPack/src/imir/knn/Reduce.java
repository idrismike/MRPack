package imir.knn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalDirAllocator;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.lib.MultipleOutputs;

public class Reduce extends MapReduceBase
implements Reducer<IntWritable, KNNStep1Val, NullWritable, Text> 
{
	private int bufferSize = 8 * 1024 * 1024; 
	private MultipleOutputs mos;

	private LocalDirAllocator lDirAlloc =
		new LocalDirAllocator("mapred.local.dir");
	private FSDataOutputStream out;
	private FileSystem localFs;	
	private FileSystem lfs;	
	private Path file1;
	private Path file2;

	private int numberOfPartition;
	private int dimension;
	private int blockSize;
	private int knn;
	//private boolean self_join;

	private Configuration jobinfo;
	
	public void configure(JobConf job) 
	{
		numberOfPartition = job.getInt("numberOfPatition", 2);
		dimension = job.getInt("dimension", 2);
		blockSize = job.getInt("blockSize", 1024);
		knn = job.getInt("knn", 1024);
		//self_join = Boolean.valueOf(job.get("self_join"));
		
		// Get the local file system
		try 
		{
			localFs = FileSystem.getLocal(job);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}

		lfs = ((LocalFileSystem) localFs).getRaw();
		jobinfo = job;
		//mos = new MultipleOutputs(job);
	}
	
	public void reduce(IntWritable key, Iterator<KNNStep1Val> values,
	OutputCollector<NullWritable, Text> output, 
	Reporter reporter) throws IOException 
	{
		String algorithm = "nested_loop";
		String prefix_dir = algorithm + "-" + Integer.toString(numberOfPartition) + "-" + key.toString();

		try {
			file1 = lDirAlloc.getLocalPathForWrite(prefix_dir + "/" + "outer", jobinfo); 
			file2 = lDirAlloc.getLocalPathForWrite(prefix_dir + "/" + "inner", jobinfo);
			lfs.create(file1);
			lfs.create(file2);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		String outerTable = file1.toString();
		String innerTable = file2.toString();
		FileWriter fwForR	= new FileWriter(outerTable);
		FileWriter fwForS	= new FileWriter(innerTable);
		BufferedWriter bwForR = new BufferedWriter(fwForR, bufferSize);
		BufferedWriter bwForS = new BufferedWriter(fwForS, bufferSize);

		while (values.hasNext()) 
		{
			// Value format <rid, coord, src>
			KNNStep1Val np1v = values.next();
			String record = np1v.getFirst().toString();
			String[] parts = np1v.getSecond().toStrings();
			int srcId = (int)np1v.getThird().get();

			for (int i = 0; i < dimension; i++)
				record = record + " " + parts[i];

			if (srcId == 0) {
				bwForR.write(record + "\n");
			} else if (srcId == 1) {
				bwForS.write(record + "\n");
			} else {
				System.out.println("unknown file number");
				System.exit(-1);
			}
		} 

		reporter.progress();
		bwForR.close();
		bwForS.close();
		fwForR.close();	
		fwForS.close();

		FileReader frForR = new FileReader(outerTable);
		BufferedReader brForR = new BufferedReader(frForR, bufferSize);

		// initialize for R
		int number = blockSize;
		int[] idR = new int[number];
		float[][] coordR = new float[number][dimension];
		ArrayList<PriorityQueue> knnQueueR = new ArrayList<PriorityQueue>(number);

		// Create priority queue with specified comparator
		Comparator<ListContents> rc = new KNNComparator();
		for (int j = 0; j < number; j++) 
		{
			PriorityQueue<ListContents> knnQueue = new PriorityQueue<ListContents>(knn + 1, rc);
			knnQueueR.add(knnQueue);
		}

		boolean flag = true;
		while (flag) 
		{
			// Read a block of R
			for (int ii = 0; ii < number; ii++) 
			{
				String line = brForR.readLine();
				if (line == null) 
				{
					flag = false;     //Going to end
					number = ii;
					break;
				}

				String parts[] = line.split(" +");
				int id1 = Integer.valueOf(parts[0]);
				idR[ii] = id1;

				int st = 1;
				float[] x = coordR[ii];	
				for (int i = 0; i < dimension; i++) 
					x[i] = Float.valueOf(parts[st + i]);
			}

			//if (self_join) innerTable = outerTable;
			// For all records in a block of R, the following carries out knn-join with S
			FileReader frForS = new FileReader(innerTable);
			BufferedReader brForS = new BufferedReader(frForS, bufferSize);

			while(true) 
			{
				String line = brForS.readLine();
				if (line == null) break;
				String parts[] = line.split(" ");
				int id2 = Integer.valueOf(parts[0]);

				int st = 1;
				float[] y = new float[dimension];
				for (int i = 0; i < dimension; i++)
					y[i] = Float.valueOf(parts[st + i]);

				float[] distArray = new float[number];
				for (int i = 0; i < number; i++) 
				{
					distArray[i] = 0;
					float[] x = coordR[i];
					for (int k = 0; k < dimension; k++) 
						distArray[i] += (x[k] - y[k]) * (x[k] - y[k]);
					
					ListContents ne = new ListContents(dimension, distArray[i], id2);
					PriorityQueue<ListContents> knnQueue = knnQueueR.get(i);
					knnQueue.add(ne);
					if (knnQueue.size() > knn) 
						knnQueue.poll();
				} // [0 . . number - 1]
			} // while - inner

			brForS.close(); 
			frForS.close();	

			for (int j = 0; j < number; j ++) 
			{
				PriorityQueue<ListContents> knnQueue = knnQueueR.get(j);
				int id1 = idR[j];
				for (int i = 0; i < knn; i++) 
				{
					ListContents e = knnQueue.poll();
					output.collect(
						NullWritable.get(), 
						new Text( id1 + " " + Integer.toString(e.getId()) + " " + Float.toString(e.getDist()) )
					);
				} // for
			}
			reporter.progress();
		} // while - outer
		brForR.close();
		frForR.close();
	} // reduce

    public void close() throws IOException 
	{
		//mos.close();
	}
					        
} // Reducer

