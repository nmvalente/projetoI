package logic;

import java.util.ArrayList;

import graph.Edge;
import graph.Graph;
import graph.Node;

public class AStarNode {

	private Graph graph;
	private double g;
	private double h;
	private Node node;
	private AStarNode parent;
	//private ArrayList<Edge> adjacents = new ArrayList<Edge>();
	private Truck truck;

	public AStarNode(Graph graph, Node node, Truck truck) {
		this.graph = graph;
		this.node = node;
		this.truck = truck;

		/*for(int i = 0 ; i < this.node.getOutEdges().size() ; i++){
			Edge temp = this.node.getOutEdges().get(i);
			adjacents.add(temp);
		}
		 */
		//this.truck.truckCollect(this.getNode());
		//System.out.println(this.getNode().getId() + " - " + this.graph.getTotalGarbageByTypeWaste(Utils.PAPER));
		//this.truck.itinerary = this;
	}

	public void setTruck(Truck t){
		this.truck = t;
	}

	public void setGraph(Graph g) {
		this.graph = g;
	}

	public Graph getGraph() {
		return graph;
	}

	public Node getNode() {
		return this.node;
	}

	public double getG() {
		return g;
	}

	public void setG(double g) {
		this.g = g;
	}

	public double getH() {
		return h;
	}

	public void setH(double h) {
		this.h = h;
	}

	public int compareTo(AStarNode obj) {
		return Double.compare(this.getG()+this.getH(),obj.getG()+obj.getH());
	}

	/*public ArrayList<Edge> getAdjacentNodes() {
		return this.adjacents;
	}

	public void addAdjacentAStarNodes(Edge edge) {
		this.adjacents.add(edge);
	}
	 */
	public AStarNode getParent() {
		return this.parent;
	}

	/*public void addEdge(Node destiny, double distance) {
		Edge edge = new Edge(this.node, destiny, distance);
		this.adjacents.add(edge);
	}
	 */
	public void setParent(AStarNode parent2) {
		this.parent = parent2;

	}

	@Override

	public String toString() {
		String str = this.getNode().getId() + " - G: " + this.getG() + " - H: " + this.getH();
		return str;
	}

	public boolean hasFinish() {

		if(this.graph.getTotalGarbageByTypeWaste(Utils.PAPER) == 0.0 && this.truck.getTotalGarbage() == 0.0)
			return true;
		return false;
	}

	public Truck getTruck() {
		return truck;
	}

	

}