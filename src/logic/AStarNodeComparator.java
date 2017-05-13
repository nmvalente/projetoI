package logic;

import java.util.Comparator;

public class AStarNodeComparator implements Comparator<AStarNode> {

	public int compare(AStarNode arg0, AStarNode arg1) {
			return arg0.compareTo(arg1);
		
	}
}
