package imir.invertedindex;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import main.MapOutput;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class InvertedIndex {

	//public static IntWritable zero = new IntWritable(0);
	public static MapOutput localoutput= null;//new MapOutput();
	public InvertedIndex(){
		
	}
	
	public static void setOutput(){
		localoutput = null;
		//localoutput.one = zero;
		localoutput.setIIflag(true);
		localoutput.setWCflag(false);
	}
	
	public static void index(Text value, OutputCollector<Text, MapOutput> output, Reporter reporter) throws IOException{
		FileSplit filesplit = (FileSplit) reporter.getInputSplit();
        String fileName = filesplit.getPath().getName();

        String line = value.toString();
        
        StringTokenizer tokenizer = new StringTokenizer(line);
        while(tokenizer.hasMoreTokens()){
                String token = tokenizer.nextToken();
                localoutput.setFileName(fileName);
                output.collect(new Text("II:::"+token.toLowerCase()), localoutput);
        }

	}
	public static void indexReduce(Text key, Iterator<MapOutput> values,
			OutputCollector<Text, Text> output, Reporter reporter) throws IOException{
		boolean first = true;
	     StringBuilder toReturn = new StringBuilder();
	     while (values.hasNext()){
	       if (!first)
	         toReturn.append(", ");
	       first=false;
	       toReturn.append(values.next().toString());
	     }
	     
	     output.collect(keySetter(key), new Text(toReturn.toString()));
	}
	
	private static Text keySetter(Text key){
		return new Text(key.toString().substring(5));
	}
}
