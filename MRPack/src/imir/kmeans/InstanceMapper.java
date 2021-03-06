package imir.kmeans;

import java.util.*;
import java.io.*;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;

public class InstanceMapper extends MapReduceBase implements Mapper<LongWritable, Text, ClusterId, Instance>{
	
	Clusters clusters = new Clusters();
	
	public void configureKMean(JobConf job){
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
	public static void setOutput(){
		mwordcount = null;
		mwordcount.setWCflag(false);
		mwordcount.setIIflag(false);
		mwordcount.setKNNflag(false);
		mwordcount.setKmeanflag(true);
		mwordcount.set45flag(false);
	}

	/*public void map(LongWritable key, Text value, OutputCollector<ClusterId, Instance> output, Reporter reporter) throws IOException{
		
	}*/
	public void knnMap(Text value, OutputCollector<Text, MapOutput> output){
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
		output.collect(idresult, instance);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		}

}
