import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import java.io.IOException;
import java.util.Stringtknizer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;


public class Mapper extends MapReduceBase
implements Mapper<LongWritable, Text, Text, IntWritable> {

private final static IntWritable one = new IntWritable(1);
private Text valAtt = new Text();
private Text cLabel = new Text();
private int i;
private String tkn;
public static int no_Attr;
private int flag=0;



public void C405Map(Text value,OutputCollector<Text, IntWritable> output) throws IOException {

  C405 id=new C405();
  Split split=null;
  int size_split=0;
  split=id.currentsplit;
  
  String line = value.toString();      //changing input instance value to string
  Stringtknizer itr = new Stringtknizer(line);
  int index=0;
  String attr_value=null;
  no_Attr=itr.counttkns()-1;
  String attr[]=new String[no_Attr];
  boolean match=true;
  for(i =0;i<no_Attr;i++)
  {
	  attr[i]=itr.nexttkn();		//Finding the values of different attributes
  }
  String classLabel=itr.nexttkn();
  size_split=split.attr_index.size();
  for(int count=0;count<size_split;count++)
  {
	  index=(Integer) split.attr_index.get(count);
	  attr_value=(String)split.attr_value.get(count);
	 if(attr[index].equals(attr_value))   //may also use attr[index][z][1].contentEquals(attr_value)
	 {
		 //System.out.println("EQUALS IN MAP  nodes  "+attr[index]+"   inline  "+attr_value);
	 }
	 else
	 {
		// System.out.println("NOT EQUAL IN MAP  nodes  "+attr[index]+"   inline  "+attr_value);
		 match=false;
		 break;
	 }
	  
  }
  
  
  

  if(match)
  {
	  for(int l=0;l<no_Attr;l++)
	  {  
		  if(split.attr_index.contains(l))
		  {
			  
		  }
		  else
		  {
			  tkn=l+" "+attr[l]+" "+classLabel;
			  valAtt.set(tkn);
			  output.collect(valAtt, one);
		  }
	 
  	}
	  if(size_split==no_Attr)
	  {
		  tkn=no_Attr+" "+"null"+" "+classLabel;
		  valAtt.set(tkn);
		  output.collect(valAtt, one);
	  	}
   }
 }
  
}