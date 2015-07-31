package imir.knn;

import java.io.IOException;
import java.util.Random;

import main.MapOutput;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;

public class KnnMapper {
	
	public static int numberOfPartition;
	public static int dimension;
	public static int scale = 1000;
	public static int fileId = 0;
	public static String inputFile;
	//private String mapTaskId;
	public static Random r;
	public static int recIdOffset;
	public static int coordOffset;
	
	public static void configureKNN(JobConf job){
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
		/*else 
		{
			System.out.println(inputFile.indexOf("inner"));
			System.out.println(inputFile.indexOf("outer"));
			System.out.println("Invalid input file source@NPhase1");
			System.exit(-1);
		}*/
	}
	public static void setOutput(){
		mwordcount = null;
		mwordcount.setWCflag(false);
		mwordcount.setIIflag(false);
		mwordcount.setKNNflag(false);
		mwordcount.setKmeanflag(true);
		mwordcount.set45flag(false);
	}
	public static void knnMap(Text value, OutputCollector<Text, MapOutput> output ) throws IOException{
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

		MapOutput np1v = new MapOutput(recIdInt, coord, (byte) fileId, dimension);

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
			output.collect(new Text("NN:::"+mapKey.toString()), np1v);
		} 
	}

	KnnMapper(){
		
	}
	

}
