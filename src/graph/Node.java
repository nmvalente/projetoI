package graph;

import java.util.ArrayList;
import logic.Utils;

public class Node{

	private int id;
	private double latitude;
	private double longitude;
	private String type;
	private ArrayList<Edge> outEdges;
	private boolean processing;
	private boolean visited;
	private int indegree; // incident edges
	protected String name;
	protected Node parent;
	protected double g,h,f=0;

	/*public Node(int id, double latitude, double longitude, String type){
		this.setId(id);
		this.latitude = latitude;
		this.longitude = longitude;
		this.processing = false;
		this.indegree = 0;
		this.outEdges = new ArrayList<Edge>();
	}
	 */
	// clones node without edges
	public Node(Node node){
		if (node != null) {
			this.setId(node.id);
			this.latitude = node.latitude;
			this.longitude = node.longitude;
			this.type = node.type;
			this.processing = node.processing;
			this.indegree = 0;
			this.name = node.name;
			this.outEdges = new ArrayList<Edge>();
			
		}
	}

	public Node(int id, double latitude, double longitude, String type, String nameStreet) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = nameStreet;
		this.outEdges = new ArrayList<Edge>();

		if(type.equals("central"))
			this.type = Utils.CENTRAL;
		else if(type.equals("station"))
			this.type = Utils.STATION;
		else if(type.equals("false"))
			this.type = Utils.FALSE_GARBAGE;
		else this.type = Utils.TRUE_GARBAGE;
	}

	public ArrayList<Edge> cloneList(ArrayList<Edge> list) {
		ArrayList<Edge> clone = new ArrayList<Edge>(list.size());
		for (Edge item : list)
			clone.add(new Edge(item));
		return clone;
	}

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id = id;
	}

	public double getLatitude(){
		return latitude;
	}

	public double getLongitude(){
		return longitude;
	}

	public int getIndegree() {
		return indegree;
	}

	public boolean hasGasStation(){
		if(this.type.equals(Utils.TRUE_GARBAGE))
			return false;
		else return true;
	}

	public void setGarbageStation(boolean garbageStation){
		if(garbageStation)
			this.type = Utils.TRUE_GARBAGE;
		else this.type = Utils.FALSE_GARBAGE;
	}

	public ArrayList<Edge> getOutEdges(){
		return this.outEdges;
	}

	public void setLatitude(double latitude){
		this.latitude = latitude;
	}

	public void setLongitude(double longitude){
		this.longitude = longitude;
	}

	public boolean getVisited(){
		return visited;
	}

	public void setVisited(boolean visited){
		this.visited = visited;
	}

	@Override
	public boolean equals(Object obj){
		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (this.getClass() != obj.getClass())
			return false;

		Node node = (Node) obj;

		return this.id == node.id;
	}

	// we need to decrement the destiny indegree value outside (if we don't send
	// the same node memory position)
	public boolean removeEdgeTo(Node node){
		for (int i = 0; i < outEdges.size(); i++) {
			if (outEdges.get(i).getDestiny().equals(node)) {
				node.indegree--;
				outEdges.remove(i);
				return true;
			}
		}
		return false;
	}

	public void addEdge(Node destiny, double distance){
		//destiny.indegree++;
		Edge edge = new Edge(this, destiny, distance);
		this.outEdges.add(edge);
	}

	public String getName() {
		return this.name;
	}

	public String getType(){
		return this.type;
	}
	
	@Override
	public String toString(){	
		
		StringBuilder strN = new StringBuilder();
		strN.append(this.getId() + " " + this.getName() + "\n");
		
		for (Edge temp : this.outEdges) {
			strN.append("\t" + temp.toString() + "\n");
		}
		 
		return strN.toString();
	}

	public void setOutEdges(ArrayList<Edge> outEdges2) {
		this.outEdges = outEdges2;
	}
	
	public double getGValue(){return this.g;}
	public double getHValue(){return this.h;}
	public double getFValue(){return this.f;}
	public void setGValue(double gValue){this.g = gValue;}
	public void setHValue(double hValue){this.h = hValue;}
	public void setFValue(double fValue){this.f = fValue;}
	
	public Node getParent(){return this.parent;}

	public void setParent(Node current) {
		this.parent = current;		
	}
}