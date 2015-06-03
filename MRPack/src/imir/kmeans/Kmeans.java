/**
 *  Kmeans algorithm implemented in map-reduce framework using Hadoop.
 *
 */

package imir.kmeans;

import java.io.*;
import java.util.*;
import java.net.URI;
 	
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
 	
public class Kmeans extends Configured implements Tool{
	
	private Kmeans(){} //singleton	

 	public int run(String[] args) {
	       if (args.length != 5) {
		/*	System.out.println(args.length);			
			for (String s: args)
				System.out.println(s);
                */
      			System.out.println("Kmeans <in> <out> <init> <error>");
      			ToolRunner.printGenericCommandUsage(System.out);
      			return -1;
    	       }

	       try{

                        FileSystem fs = FileSystem.get(getConf());
			
			Clusters oldclusters = null;
			Clusters newclusters = null;  

			Path input = new Path(args[1]);
			Path output = new Path(args[2]);
			Path init = new Path(args[3]);
			double error = Double.parseDouble(args[4]);

			System.out.println(input.toString());
			System.out.println(output.toString());
			System.out.println(init.toString());
			System.out.println(error);
			
			fs.delete(output, true);			


			
			do {
				oldclusters = newclusters;				

				JobConf conf = new JobConf(getConf(), Kmeans.class);
				conf.setJobName("kmeans");
				URI [] cachefiles = getFiles(init, fs);
				for (URI uri: cachefiles){
					DistributedCache.addCacheFile(uri, conf); 			
				}
				
				conf.setMapOutputKeyClass(ClusterId.class);
				conf.setMapOutputValueClass(Instance.class);

				conf.setOutputKeyClass(Text.class);
				conf.setOutputValueClass(Text.class);				
				
				conf.setMapperClass(InstanceMapper.class);
    				conf.setCombinerClass(InstanceCombiner.class);
    				conf.setReducerClass(InstanceReducer.class);

				conf.setInputFormat(TextInputFormat.class);
 	                        conf.setOutputFormat(TextOutputFormat.class);
 	
		        	FileInputFormat.setInputPaths(conf, input);
 	                        FileOutputFormat.setOutputPath(conf, output);
    				
				JobClient.runJob(conf);
				fs.delete(init, true);
				fs.rename(output, init);
			
				newclusters = new Clusters(init, fs);	
				
			} while( newclusters.isequals(oldclusters, error) == false );
			
			JobConf conf = new JobConf(getConf(), Kmeans.class);
			conf.setJobName("kmeans_result");
			URI [] cachefiles = getFiles(init, fs);
			for (URI uri: cachefiles){
					DistributedCache.addCacheFile(uri, conf); 			
			}
				
			conf.setMapOutputKeyClass(Text.class);
			conf.setMapOutputValueClass(Text.class);
								
			conf.setMapperClass(InstanceMapper2.class);
			conf.setNumReduceTasks(0);

			conf.setInputFormat(TextInputFormat.class);
 	                conf.setOutputFormat(TextOutputFormat.class);
			conf.setOutputKeyComparatorClass(Text.Comparator.class); 			
	
		        FileInputFormat.setInputPaths(conf, input);
 	                FileOutputFormat.setOutputPath(conf, output);
    				
			JobClient.runJob(conf);		
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Kmeans Program Failed!");
		}
		return 0;
	}

	URI [] getFiles(Path path, FileSystem fs) throws Exception{

		FileStatus [] files = fs.listStatus(path);
		ArrayList<URI> urifiles = new ArrayList<URI>();
		for (int i=0; i < files.length; i++){
			Path p = files[i].getPath();
			if (p.getName().startsWith("part")){
				URI uri = p.toUri();
				urifiles.add(uri);
			}
					
		}
		URI [] uris = new URI[urifiles.size()];
		urifiles.toArray(uris);
		return uris;
		
	}  

	public static void main(String [] args){
		try{
			int result = ToolRunner.run(new Configuration(), new Kmeans(), args);
			System.exit(result);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}














