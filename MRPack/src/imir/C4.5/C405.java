import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;




public class C405extends Configured implements Tool {

 public static List <Split> splitted=new ArrayList<Split>();;
    
    public static int current_index=0;
    public static Split currentsplit=new Split();
    
   public static void writeRuleToFile(String text) {
	    try {  
	    	BufferedWriter bw = new BufferedWriter(new FileWriter(new File("/home/hduser/C45/rule.txt/"), true));    
	    	bw.write(text);
	            bw.newLine();
	            bw.close();
	    } catch (Exception e) {
	    }
	}
  
 /* public int run(String[] args) throws Exception {
    JobConf conf = new JobConf(getConf(),C45.class);
    conf.setJobName("Id3");

    // the keys are words (strings)
    conf.setOutputKeyClass(Text.class);
    // the values are counts (ints)
    conf.setOutputValueClass(IntWritable.class);

    conf.setMapperClass(MapClass.class);
    conf.setReducerClass(Reduce.class);


	//set your input file path below
    FileInputFormat.setInputPaths(conf, "../../home/hduser/Id3_hds/iris.txt");
    FileOutputFormat.setOutputPath(conf, new Path("../../home/hduser/Id3_hds/1/output"+current_index));

    JobClient.runJob(conf);
    return 0;
  }*/
  
  
  
}
