package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import logic.Utils;

public class Node {

	private double distance;
	private String type;
	private ArrayList<Edge> outEdges;
	private String name;
	private Node parent;
	private Map<String, Double> garbageContainer;
	private static int current_id = 0;
	private int id;

	public Node(Node node) {
		if (node != null) {
			this.type = node.type;
			this.name = node.name;
			this.outEdges = new ArrayList<Edge>();
			this.outEdges = node.getOutEdges();
			if(this.type == Utils.TRUE_GARBAGE){
				this.garbageContainer = new HashMap<String, Double>();
				this.garbageContainer.put("glass", node.getGarbageContainerByType(Utils.GLASS));
				this.garbageContainer.put("paper", node.getGarbageContainerByType(Utils.PAPER));
				this.garbageContainer.put("plastic", node.getGarbageContainerByType(Utils.PLASTIC));
				this.garbageContainer.put("common", node.getGarbageContainerByType(Utils.COMMON));
			}

			this.id = node.id;
		}
	}

	public Node(int id, String type, String nameStreet, double glass, double paper, double plastic, double common) {
		this.name = nameStreet;
		this.outEdges = new ArrayList<Edge>();

		if (type.equals("central"))
			this.type = Utils.CENTRAL;
		else if (type.equals("station"))
			this.type = Utils.STATION;
		else if (type.equals("false"))
			this.type = Utils.FALSE_GARBAGE;
		else{
			this.garbageContainer = new HashMap<String, Double>();
			this.garbageContainer.put("glass", glass);
			this.garbageContainer.put("paper", paper);
			this.garbageContainer.put("plastic", plastic);
			this.garbageContainer.put("common", common);
			this.type = Utils.TRUE_GARBAGE;
		}
		++current_id;
		this.id = current_id;
	}

	// for test class only
	public Node(int id, String name, double distance) {
		this.id = id;
		this.name = name;
		this.setDistance(distance);
		this.outEdges = new ArrayList<Edge>();
	}

	public ArrayList<Edge> cloneList(ArrayList<Edge> list) {
		ArrayList<Edge> clone = new ArrayList<Edge>(list.size());
		for (Edge item : list)
			clone.add(new Edge(item));
		return clone;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setGarbageStation(boolean garbageStation) {
		if (garbageStation)
			this.type = Utils.TRUE_GARBAGE;
		else
			this.type = Utils.FALSE_GARBAGE;
	}

	public ArrayList<Edge> getOutEdges() {
		return this.outEdges;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (this.getClass() != obj.getClass())
			return false;

		Node node = (Node) obj;

		if (this.id == node.id)
			return true;
		else
			return false;
	}

	public boolean removeEdgeTo(Node node) {
		for (int i = 0; i < outEdges.size(); i++) {
			if (outEdges.get(i).getDestiny().equals(node)) {
				outEdges.remove(i);
				return true;
			}
		}
		return false;
	}

	public void addEdge(Node destiny, double distance) {
		Edge edge = new Edge(this, destiny, distance);
		this.outEdges.add(edge);
	}

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

	@Override
	public String toString() {

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

	public Node getParent() {
		return this.parent;
	}

	public void setParent(Node current) {
		this.parent = current;
	}

	public void setGarbageContainer(String typeGarbage, double collected){
		double temp = garbageContainer.get(typeGarbage);
		garbageContainer.replace(typeGarbage, temp - collected);
	}

	public Map<String, Double> getGarbageContainer(){
		return this.garbageContainer;
	}

	public double getGarbageContainerByType(String wasteType){
		return this.garbageContainer.get(wasteType);
	}

	public double getDistance() {
		return this.distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
}