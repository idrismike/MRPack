package imir.knn;
import java.io.*;
import java.util.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Phase2 of Hadoop Block Nested Loop KNN Join (H-BNLJ).
 */
public class KNNStep2 extends Configured implements Tool 
{
	public static class MapClass extends MapReduceBase 
	implements Mapper<LongWritable, Text, IntWritable, KNNStep2Val> 
	{

		public void map(LongWritable key, Text value, 
		OutputCollector<IntWritable, KNNStep2Val> output, 
		Reporter reporter) 	throws IOException 
		{
			String line = value.toString();
			String[] parts = line.split(" +");
			// key format <rid1>
			IntWritable mapKey = new IntWritable(Integer.valueOf(parts[0]));
			// value format <rid2, dist>
			KNNStep2Val np2v = new KNNStep2Val(Integer.valueOf(parts[1]), Float.valueOf(parts[2]));
			output.collect(mapKey, np2v);
		}
	}
  
	public static class Reduce extends MapReduceBase
	implements Reducer<IntWritable, KNNStep2Val, NullWritable, Text> 
	{
		int numberOfPartition;	
		int knn;
		
		class Record 
		{
			public int id2;
			public float dist;

			Record(int id2, float dist) 
			{
				this.id2 = id2;
				this.dist = dist;
			}

			public String toString() 
			{
				return Integer.toString(id2) + " " + Float.toString(dist);	
			} 
		}

		class RecordComparator implements Comparator<Record> 
		{
			public int compare(Record o1, Record o2) 
			{
				int ret = 0;
				float dist = o1.dist - o2.dist;

				if (Math.abs(dist) < 1E-6)
					ret = o1.id2 - o2.id2;
				else if (dist > 0)
					ret = 1;
				else 
					ret = -1;

				return -ret;
			}	
		}

		public void configure(JobConf job) 
		{
			numberOfPartition = job.getInt("numberOfPartition", 2);	
			knn = job.getInt("knn", 3);
		}	

		public void reduce(IntWritable key, Iterator<KNNStep2Val> values, 
		OutputCollector<NullWritable, Text> output, 
		Reporter reporter) throws IOException 
		{
			//initialize the pq
			RecordComparator rc = new RecordComparator();
			PriorityQueue<Record> pq = new PriorityQueue<Record>(knn + 1, rc);

			// For each record we have a reduce task
			// value format <rid1, rid2, dist>
			while (values.hasNext()) 
			{
				KNNStep2Val np2v = values.next();

				int id2 = np2v.getFirst().get();
				float dist = np2v.getSecond().get();
				Record record = new Record(id2, dist);
				pq.add(record);
				if (pq.size() > knn)
					pq.poll();
			}

			while(pq.size() > 0) 
			{
				output.collect(NullWritable.get(), new Text(key.toString() + " " + pq.poll().toString()));
				//break; // only ouput the first record
			}

		} // reduce
	} // Reducer
 
	static int printUsage() 
	{
		System.out.println(
			"NPhase1 [-m <maps>] [-r <reduces>] [-p <numberOfPartitions>] " 
			+ "[-k <knn>] " + "<input> <output>");
		ToolRunner.printGenericCommandUsage(System.out);
		return -1;
	}
  
	/**
	 * The main driver for H-BNLJ program.
	 * Invoke this method to submit the map/reduce job.
	 * @throws IOException When there is communication problems with the 
	 *                     job tracker.
	 */
	public int run(String[] args) throws Exception {
		JobConf conf = new JobConf(getConf(), KNNStep2.class);
		conf.setJobName("NPhase2");

		conf.setMapOutputKeyClass(IntWritable.class);
		conf.setMapOutputValueClass(KNNStep2Val.class);
		conf.setOutputKeyClass(NullWritable.class);
		conf.setOutputValueClass(Text.class);		

		conf.setMapperClass(MapClass.class);        
		conf.setReducerClass(Reduce.class);
	
		int numberOfPartition = 0;	
		List<String> other_args = new ArrayList<String>();

		for(int i = 0; i < args.length; ++i) 
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
				} else if ("-k".equals(args[i])) {
					int knn = Integer.parseInt(args[++i]);
					conf.setInt("knn", knn);
					System.out.println(knn);
				} else {
					other_args.add(args[i]);
 	 			}
				conf.setNumReduceTasks(numberOfPartition * numberOfPartition);
				//conf.setNumReduceTasks(1);
			} catch (NumberFormatException except) {
				System.out.println("ERROR: Integer expected instead of " + args[i]);
				return printUsage();
			} catch (ArrayIndexOutOfBoundsException except) {
				System.out.println("ERROR: Required parameter missing from " + args[i-1]);
				return printUsage();
			}
		} 

		// Make sure there are exactly 2 parameters left.
		if (other_args.size() != 2) {
			System.out.println("ERROR: Wrong number of parameters: " +
				other_args.size() + " instead of 2.");
			return printUsage();
		}

		FileInputFormat.setInputPaths(conf, other_args.get(0));
		FileOutputFormat.setOutputPath(conf, new Path(other_args.get(1)));

		JobClient.runJob(conf);
		return 0;
	}
  
	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new KNNStep2(), args);
	}
} // NPhase2

