package main;


import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
//import java.lang.Long;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.mapred.lib.MultipleOutputs;




/** 
 * Phase1 of Hadoop Block Nested Loop KNN Join (H-BNLJ).
 */
public class Main extends Configured implements Tool 
{
	  
	static int printUsage() 
	{
		System.out.println(
			"NPhase1 [-m <maps>] [-r <reduces>] [-p <numberOfPartitions>] " 
			+ "[-d <dimension>] [-k <knn>] [-b <blockSize(#records) for R>] " 
			+ "<input (R)> <input (S)> <output>");
		ToolRunner.printGenericCommandUsage(System.out);
		return -1;
	}
  
	/**
	 * The main driver for H-BNLJ program.
	 * Invoke this method to submit the map/reduce job.
	 * @throws IOException When there is communication problems with the 
	 * job tracker.
	 */
	public int run(String[] args) throws Exception 
	{
		int numberOfPartition = 2;
		//boolean self_join = false;
		JobConf conf = new JobConf(getConf(), Main.class);
		conf.setJobName("IMIR");

		conf.setMapperClass(IMIRMapper.class);        
		conf.setReducerClass(IMIRReducer.class);
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(MapOutput.class);
		
		List<String> other_args = new ArrayList<String>();
		for(int i=0; i < args.length; ++i) 
		{
			try {
				if ("-m".equals(args[i])) {
					//conf.setNumMapTasks(Integer.parseInt(args[++i]));
					++i;
				} else if ("-r".equals(args[i])) {
					conf.setNumReduceTasks(Integer.parseInt(args[++i]));
				} else if ("-p".equals(args[i])) {
					numberOfPartition = Integer.parseInt(args[++i]);
					conf.setInt("numberOfPartition", numberOfPartition);
				} else if ("-d".equals(args[i])) {
					conf.setInt("dimension", Integer.parseInt(args[++i]));
				} else if ("-k".equals(args[i])) {
					conf.setInt("knn", Integer.parseInt(args[++i]));
				} else if ("-b".equals(args[i])) {
					conf.setInt("blockSize", Integer.parseInt(args[++i]));
				} else {
					other_args.add(args[i]);
				}
				// set the number of reducers
				conf.setNumReduceTasks(numberOfPartition * numberOfPartition);
			} 
			catch (NumberFormatException except) {
				System.out.println("ERROR: Integer expected instead of " + args[i]);
				return printUsage();
			} catch (ArrayIndexOutOfBoundsException except) {
				System.out.println("ERROR: Required parameter missing from " + args[i-1]);
				return printUsage();
		  	}
		}

		//if (args.length != 2) {
		if (other_args.size() != 3) {
			System.out.println("ERROR: Wrong number of parameters: " + other_args.size() + " instead of 3.");
		  return printUsage();
		}

		FileInputFormat.setInputPaths(conf, other_args.get(0));
		FileInputFormat.setInputPaths(conf, other_args.get(1));
		//System.out.println("set R to  the input path");
		//FileInputFormat.addInputPaths(conf, args[2]);
		//System.out.println("set S to  the input path");
		FileOutputFormat.setOutputPath(conf, new Path(other_args.get(2)));
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		writeDate("Start_Time",df.format(date));
		
		JobClient.runJob(conf);
		date = new Date();
		writeDate("Complete_runTime",df.format(date));
		return 0;
	} // run
  
	/*public static void main(String[] args) throws Exception 
	{
		int res = ToolRunner.run(new Configuration(), new KNNStep1(), args);
		System.exit(res);
	}*/
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
	
	
} //NPhase1
