package main;

import imir.kmeans.*;
import imir.invertedindex.*;

import imir.knn.KNNStep1Val;
import imir.knn.KnnMapper;
import imir.wordcount.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class IMIRMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, MapOutput> {

	/*private int numberOfPartition;
	private int dimension;
	private int scale = 1000;
	private int fileId = 0;
	private String inputFile;
	//private String mapTaskId;
	private Random r;
	private int recIdOffset;
	private int coordOffset;*/

	public void configure(JobConf job) 
	{
		KnnMapper.configureKNN(job);
		InstanceMapper.configureKMean(job);
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		writeDate("Mapper_Start",df.format(date));
		
		/*inputFile = job.get("map.input.file");
		
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
		}*/
	} // configure
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
	public void map(LongWritable arg0, Text value,
			OutputCollector<Text, MapOutput> output, Reporter arg3)
			throws IOException {
		// TODO Auto-generated method stub
		/*   IMIR WordCount Section   */
		WordCount.setOutput();
		WordCount.count(value, output);
		
		/*   IMIR InvertedIndex Section   */
		InvertedIndex.setOutput();
		InvertedIndex.index(value, output, arg3);
		/*   IMIR KNN Section   */
		if (KnnMapper.inputFile.indexOf("outer") != -1 || KnnMapper.inputFile.indexOf("inner") != -1 )
			KnnMapper.knnMap(value, output);
			/*   IMIR KMeans Section   */
			InstanceMapper.setOutput();
			InstanceMapper.KMeanMap(value, output);
			/*   IMIR C45 Section   */
			C405.setOutput();
			C405.C405Map(value, output);
		
	}
	

}
