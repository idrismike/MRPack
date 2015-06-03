package imir.knn;

import java.io.IOException;
import java.util.Random;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class MapClass extends MapReduceBase
implements Mapper<LongWritable, Text, IntWritable, KNNStep1Val> 
{
	private int numberOfPartition;
	private int dimension;
	private int scale = 1000;
	private int fileId = 0;
	private String inputFile;
	//private String mapTaskId;
	private Random r;
	private int recIdOffset;
	private int coordOffset;

	public void configure(JobConf job) 
	{
		inputFile = job.get("map.input.file");
		//mapTaskId = job.get("mapred.task.id");
		numberOfPartition = job.getInt("numberOfPartition", 2);
		dimension = job.getInt("dimension", 2);

		recIdOffset = 0;
		coordOffset = recIdOffset + 1;

		r = new Random();

		if (inputFile.indexOf("outer") != -1)  
			fileId = 0;
		else if (inputFile.indexOf("inner") != -1)
			fileId = 1;
		else 
		{
			System.out.println(inputFile.indexOf("inner"));
			System.out.println(inputFile.indexOf("outer"));
			System.out.println("Invalid input file source@NPhase1");
			System.exit(-1);
		}
	} // configure
	
	/**
	 * Partition the input data sets (R and S) into multiple buckets. 
	 */  
	public void map(LongWritable key, Text value, 
	OutputCollector<IntWritable, KNNStep1Val> output, 
	Reporter reporter) throws IOException 
	{
		String[] parts = value.toString().split(" +");	
		String recId = parts[recIdOffset];
		int recIdInt = Integer.parseInt(recId);
		float[] coord = new float[dimension];
		for (int i = 0; i < dimension; i++) 
			coord[i] = Float.parseFloat(parts[coordOffset + i]);	

		// Required if we want to compare the results with H-zKNNJ
		int[] converted = new int[dimension];
		float[] tmp_coord = new float[dimension];
		for (int i = 0; i < dimension; i++) {
			tmp_coord[i] = coord[i];
			converted[i] = (int) tmp_coord[i];
			tmp_coord[i] -= converted[i];
			converted[i] *=scale;
			//converted[i] += (int) (tmp_coord[i] * scale);
			converted[i] += (tmp_coord[i] * scale);
		}

		// Use scaled data sets
		for (int i = 0; i < dimension; i++) 
			coord[i] = (float) converted[i];	

		KNNStep1Val np1v = new KNNStep1Val(recIdInt, coord, (byte) fileId, dimension);

		//Random generate a partition ID for an input record
		int partID = r.nextInt(numberOfPartition);
		int groupID = 0;

		for (int i = 0; i < numberOfPartition; i++) 
		{
			if (fileId == 0)
				groupID = partID * numberOfPartition + i;
			else if (fileId == 1)
				groupID = partID + i * numberOfPartition;
			else 
			{
				System.out.println("The record comes from unknow file!!!");	
				System.exit(-1);
			}

			IntWritable	mapKey = new IntWritable(groupID);
			output.collect(mapKey, np1v);
		} 
	} // map
} // MapClass

