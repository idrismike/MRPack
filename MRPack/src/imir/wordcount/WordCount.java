package imir.wordcount;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import main.MapOutput;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class WordCount {
	
	public static Text word= new Text();
	public static IntWritable one1 = new IntWritable(1);
	public static MapOutput mwordcount = null;//new MapOutput();
	public static IntWritable val = new IntWritable();
	
	public WordCount(){
		
	}
	
	public static void setOutput(){
		mwordcount = null;
		mwordcount.setWCflag(true);
		mwordcount.setIIflag(false);
		mwordcount.setKNNflag(false);
		mwordcount.setKmeanflag(false);
		mwordcount.set45flag(false);
	}

	public static void count(Text value, OutputCollector<Text, MapOutput> output ) throws IOException{
		String line = value.toString();
		StringTokenizer tokenizer = new StringTokenizer(line);
		while(tokenizer.hasMoreTokens()){
			word.set("wc:::"+tokenizer.nextToken());
			//val.set(Integer.parseInt(word.toString()));
			mwordcount.one =  one1;
			output.collect(word, mwordcount);
		}
	}
	
	public static void countReducer(Text key, Iterator<MapOutput> values,
			OutputCollector<Text, Text> output, Reporter reporter) throws IOException{
		int sum = 0;
	     while (values.hasNext()) {
	    	 if(values.next().one != null)
	    		 sum += values.next().one.get();
	     }
	     output.collect(keySetter(key), new Text(""+sum));
	}
	
	private static Text keySetter(Text key){
		return new Text(key.toString().substring(5));
	}
}
