package main;

import imir.invertedindex.InvertedIndex;
import imir.knn.*;
import imir.wordcount.WordCount;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalDirAllocator;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.lib.MultipleOutputs;

public class IMIRReducer extends MapReduceBase implements Reducer<Text, MapOutput, Text, Text> {

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
	private Configuration jobinfo;
	public  IntWritable On = new IntWritable(1);
	public void configure(JobConf job) 
	{
		KnnReducer.KnnConfigure(job);
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		writeDate("Reducer_Start",df.format(date));
		/*numberOfPartition = job.getInt("numberOfPatition", 2);
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
		 */
	}
	public void writeDate(String type, String time1){
		try {
			FileWriter fstream = new FileWriter(type+".txt");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(time1);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	@Override
	public void reduce(Text key, Iterator<MapOutput> values,
			OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
		// TODO Auto-generated method stub
		String iikey= key.toString();
		
		if(iikey.substring(0, 5) == "WC:::" ){
			WordCount.countReducer(key, values, output, reporter);
		}
		else
		if(iikey.substring(0, 5) == "II:::" ){
			InvertedIndex.indexReduce(key, values, output, reporter);
		}
		else
		if(iikey.substring(0, 5) == "NN:::" ){
			KnnReducer.KnnReduce(key, values, output, reporter);
		}
		
		
		
	}

}
