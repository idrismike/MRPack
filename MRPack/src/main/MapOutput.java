package main;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

import imir.kmeans.Instance;
import imir.knn.KNNStep1Val;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.ByteWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

public class MapOutput implements Writable,WritableComparable<KNNStep1Val> {

	String instanceId;	
	double [] data;
	double weight; 
	/*  KNN-OutPut  */
	private IntWritable first;
	private ArrayWritable second;
	private ByteWritable third;
	public IntWritable one = new IntWritable(1);
	public Text filename; //= new Text();
	public boolean iiflag = false;
	public boolean wcflag = false;

	/*public MapOutput(){
	}*/
	public MapOutput(IntWritable one){
		this.one = one;
	}
	
	public MapOutput(Text filename){
		this.filename = filename;
	}
	
	public void setFileName(String file){
		
		this.filename = new Text(file);
	}
	
	public void setIIflag(boolean flag){
		iiflag = flag;
	}
	
	public boolean getIIflag(){
		return iiflag;
	} 
	public void setWCflag(boolean flag){
		wcflag = flag;
	}
	
	public boolean getWCflag(){
		return wcflag;
	} 
	
	public Text getFileName(){
		return this.filename;
	}

	public MapOutput(String s){
		String [] ss = s.split(",");
		instanceId = ss[0];
		weight = Double.parseDouble(ss[1]);
		data = new double[Integer.parseInt(ss[2])];
		for (int i = 0; i < data.length; i++){
			data[i] = Double.parseDouble(ss[i+3
]);
		}
	}

	public MapOutput(MapOutput instance){
		instanceId = instance.instanceId;
		data = Arrays.copyOf(instance.data, instance.data.length);
		weight = instance.weight;	
	}

	public void combine(MapOutput instance){
		if (data.length == instance.data.length){
			double newweight = weight + instance.weight; 
			for (int i = 0; i < data.length; i++){
				data[i] = (data[i] * weight + instance.data[i] * instance.weight)/newweight;
			}
			weight = newweight;
		}
		else{
			System.err.println("Combine instance with different dimensionality!");
		}
	}

	public void write(DataOutput out) throws IOException {
		if (data != null){
			out.writeUTF(instanceId);
			out.writeDouble(weight);		
			out.writeInt(data.length);
			for (int i = 0; i < data.length; i++){
				out.writeDouble(data[i]);
			}
		}
		else{
			first.write(out);
			second.write(out);
			third.write(out);
		}
	}
	
	public void readFields(DataInput in) throws IOException{
		instanceId = in.readUTF();		
		weight = in.readDouble(); 		
		data = new double[in.readInt()];
		for (int i = 0; i < data.length; i++){
			data[i] = in.readDouble();				
		}
		
		first.readFields(in);
		second.readFields(in);
		third.readFields(in);
	}
	
	
	/*  KNN-OutPut  */
	

	public MapOutput() {
		this.first = new IntWritable();
		this.second = new ArrayWritable(FloatWritable.class);
		this.third = new ByteWritable();
	}

	public MapOutput(int first, float[] second, byte third, int dimension) {
		set(new IntWritable(first), second, new ByteWritable(third), 
				dimension);
	}

	public void set(IntWritable first, float[] second, 
			ByteWritable third, int dimension) {
		this.first = first;
		this.third = third;

		FloatWritable[] floatArray = new FloatWritable[dimension];
		for (int i = 0; i < dimension; i++)
			floatArray[i] = new FloatWritable(second[i]);
		this.second = new ArrayWritable(FloatWritable.class, floatArray);
	}

	public IntWritable getFirst() {
		return first;
	}

	public ArrayWritable getSecond() {
		return second;
	}

	public ByteWritable getThird() {
		return third;
	}
	
	/*@Override
	public void write(DataOutput out) throws IOException {
		first.write(out);
		second.write(out);
		third.write(out);
	}*/

	/*@Override
	public void readFields(DataInput in) throws IOException {
		first.readFields(in);
		second.readFields(in);
		third.readFields(in);
	}*/

	@Override
	public boolean equals(Object o) {
		if (o instanceof MapOutput) {
			MapOutput np1v = (MapOutput) o;
			return first.equals(np1v.first) && second.equals(np1v.second)
				&& third.equals(np1v.third);
		}
		return false;
	}

	@Override
	public String toString() {
		int dimension = 2;
		String result;
		result = first.toString() + " " ;

		String[] parts = second.toStrings();
		for (int i = 0; i < dimension; i++)
			result = result + parts[i] + " ";

		return result + third.toString();	
	}

	public String toString(int dimension) {
		String result;
		result = first.toString() + " " ;

		String[] parts = second.toStrings();
		for (int i = 0; i < dimension; i++)
			result = result + parts[i] + " ";

		return result + third.toString();	
	}

	@Override
	public int compareTo(KNNStep1Val np1v) {
		return 1;
	}

}
