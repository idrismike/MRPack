package imir.kmeans;

import java.util.*;
import java.io.*;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;

public class InstanceMapper2 extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text>{
	
	Clusters clusters = new Clusters();
	
	public void configure(JobConf job){
		try{
			Path [] clustersFiles = DistributedCache.getLocalCacheFiles(job);
			for (Path clustersFile : clustersFiles){
				clusters.addclusters(clustersFile); 
			}	
		}
		catch(Exception ex){
				ex.printStackTrace();
				System.err.println("Caught exception while parsing the cache file!");
		}
	}

	public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException{
		String line = value.toString();
		Instance instance = new Instance(line);
		double currentdis = Double.MAX_VALUE;
		double newdis; 
		ClusterId idresult = null;
		try{
			Set<Map.Entry<ClusterId, Cluster>> entries = clusters.clusters.entrySet();
			for (Map.Entry<ClusterId, Cluster> en: entries) {
				if ( ( newdis = KmeansUtil.euclideandistance(en.getValue().data, instance.data) ) < currentdis ){
				idresult = en.getKey();
				currentdis = newdis;	
	   			}
			}

		output.collect(new Text(instance.instanceId), idresult.toText());
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
