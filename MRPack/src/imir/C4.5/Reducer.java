import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;


public  class Reducer extends MapReduceBase
implements Reducer<Text, IntWritable, Text, IntWritable> {
	

public void c45reduce(Text key, Iterator<IntWritable> values,
                   OutputCollector<Text, IntWritable> output,
                   Reporter reporter) throws IOException {
  int sum = 0;
  String line = key.toString();
  StringTokenizer itr = new StringTokenizer(line);
  while (values.hasNext()) {
    sum += values.next().get();
  }
    output.collect(key, new IntWritable(sum));
	  writeToFile(key+" "+sum);
	  int index=Integer.parseInt(itr.nextToken());
	  String value=itr.nextToken();
	  String classLabel=itr.nextToken();
	  int count=sum;
	
 }

public static void writeToFile(String text) {
    try {
    	
    	C405 id=new C405();

    	BufferedWriter bw = new BufferedWriter(new FileWriter(new File("/home/mruser/C405/output/intermediate"+id.current_index+".txt"), true));    
    	bw.write(text);
            bw.newLine();
            bw.close();
    } catch (Exception e) {
    }
}

}