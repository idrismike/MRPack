import java.util.ArrayList;
import java.util.List;


public class Partition implements Cloneable{
	public List index_attr;
	public List val_attr;
	double etpy;
	String cLabel;
	Partition()
	{
		 this.index_attr= new ArrayList<Integer>();
		 this.val_attr = new ArrayList<String>();
		
		
	}
	Partition(List index_attr,List val_attr)
	{
		this.index_attr=index_attr;
		this.val_attr=val_attr;
		
	}
	
	void add(Partition obj)
	{
		this.add(obj);
	}
	

}