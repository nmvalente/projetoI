package logic;

import graph.Graph;
import graph.Node;

public class AStarNode {

	private Graph graph;
	private double g;
	private double h;
	private Node node;
	private AStarNode parent;
	private Truck truck;

	public AStarNode(Graph graph, Node node, Truck truck) {
		this.graph = new Graph(graph);

		this.truck = new Truck(truck);
		for(Node e : this.graph.getNodes())
			if(e.getId() == node.getId())
				this.node = e;


		this.truck.truckCollect(this.node);
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

	public AStarNode getParent() {
		return this.parent;
	}

	public void setParent(AStarNode parent2) {
		this.parent = parent2;

	}

	@Override
	public String toString() {
		String str = this.getNode().getId() + " - G: " + this.getG() + " - H: " + this.getH();
		return str;
	}

	public boolean hasFinish() {
		//if(this.graph.getTotalGarbageByTypeWaste(Utils.PAPER) == 0.0 && this.truck.getTotalGarbage() == 0.0)
		if(this.getTruck().allWasteSinceStart >= this.graph.getTotalGarbageByTypeWaste(Utils.PAPER))
			return true;
		return false;
	}

	public Truck getTruck() {
		return truck;
	}
}