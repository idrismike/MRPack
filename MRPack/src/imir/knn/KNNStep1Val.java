package imir.knn;
import java.io.*;

import org.apache.hadoop.io.*;

public class KNNStep1Val implements WritableComparable<KNNStep1Val> {

	private IntWritable first;
	private ArrayWritable second;
	private ByteWritable third;

	public KNNStep1Val() {
		this.first = new IntWritable();
		this.second = new ArrayWritable(FloatWritable.class);
		this.third = new ByteWritable();
	}

	public KNNStep1Val(int first, float[] second, byte third, int dimension) {
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
	
	@Override
	public void write(DataOutput out) throws IOException {
		first.write(out);
		second.write(out);
		third.write(out);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		first.readFields(in);
		second.readFields(in);
		third.readFields(in);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof KNNStep1Val) {
			KNNStep1Val np1v = (KNNStep1Val) o;
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
