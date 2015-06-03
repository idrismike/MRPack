package imir.knn;
/**
 * 
 */

/**
 * @author idris
 *
 */
public class ListContents {
	private int id;
	private float dist;

	/**
	 * 
	 */
	public ListContents(int dimension, float value, int id) {
		// TODO Auto-generated constructor stub - 3 parameters
		this.dist = value;
		this.id = id;
	}
	
	public ListContents(int dimension, float value) {
		// TODO Auto-generated constructor stub - 2 parameters - constructor overloaded
		this.dist = value;
	}
	
	void setDist(float value){ 
		this.dist = value; 
	}
	
	int getId(){
		return this.id;
	}
	
	float getDist(){
		return this.dist;
	}
	
	
	public String toString(){
		return Integer.toString(this.id) + " " + Float.toString(this.dist);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
