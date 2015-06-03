package imir.knn;
import java.util.Comparator;

class KNNComparator implements Comparator<ListContents>
{
	public int compare(ListContents o1, ListContents o2) 
	{
		int ret = 0;

		float dist = o1.getDist() - o2.getDist();
		if (Math.abs(dist) < 1E-6) {
			//ret = 0;
			ret = o1.getId() - o2.getId();	
		} else if (dist > 0)
			ret = 1;
		else if (dist < 0)
			ret = -1;

		return -ret;  //Descending order
	}
}