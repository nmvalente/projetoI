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
		String str = this.getNode().getId() + " - G: " + this.getG() + " - H: " + this.getH() + "   - Colected/Total - " + this.getTruck().allWasteSinceStart + "/" + Search.graph.getTotalGarbageByTypeWaste(this.getTruck().getType());
		return str;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (this.getClass() != obj.getClass())
			return false;

		AStarNode aStarNode = (AStarNode) obj;

		if (this.g != aStarNode.g)
			return false;

		if(this.getNode().getId() != aStarNode.getNode().getId())
			return false;

		return true;
	}

	public boolean hasFinish(String typeofWaste) {
		if(this.getTruck().allWasteSinceStart == Search.graph.getTotalGarbageByTypeWaste(typeofWaste))
			if(this.getTruck().getTotalGarbage() == 0.0)
				return true;
		return false;
	}

	public Truck getTruck() {
		return truck;
	}
}