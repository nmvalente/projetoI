package graph;

import java.util.ArrayList;

public class Graph {
	protected ArrayList<Node> nodes;

	public Graph() {
		this.nodes = new ArrayList<Node>();
	}

	public Graph(Graph newG) {
		this.nodes = newG.getNodes();
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public Node findNode(int nodeId) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).getId() == nodeId) {
				return nodes.get(i);
			}
		}
		return null;
	}

	public boolean addNode(Node node) {
		if (nodes.contains(node))
			return false;
		nodes.add(node);
		return true;
	}

	/*
	 * public boolean addNodeBegin(MyNode node) { if (nodes.contains(node))
	 * return false; nodes.add(0, node); return true; }
	 */

	public boolean removeNode(Node node) {// need to send the same node memory
		// position as the one sent to
		// because in removeEdgeTo is going
		// to decrement the indegree value
		// (in this case is stupid because
		// we are going to remove the
		// destiny node from the graph)
		if (nodes.remove(node)) {
			for (int i = 0; i < nodes.size(); i++) {
				nodes.get(i).removeEdgeTo(node);
			}
			return true;
		}
		return false;
	}

	public boolean addEdge(Node source, Node destiny, double distance) {
		if (nodes.contains(source) && nodes.contains(destiny)) {
			source.addEdge(destiny, distance);
			return true;
		}
		return false;
	}

	public boolean removeEdge(Node source, Node destiny) {// Important to
		// send
		// the destiny
		// memory
		// position
		// of the graph
		// to
		// correctly
		// decrement the
		// indegree
		// value
		// from the node
		if (nodes.contains(source) && nodes.contains(destiny)) {
			return source.removeEdgeTo(destiny);
		}
		return false;
	}

	public int getNumNodes() {
		return nodes.size();
	}

	public int getNumEdges() {
		int count = 0;

		for (int i = 0; i < nodes.size(); i++) {
			count += nodes.get(i).getOutEdges().size();
		}

		return count;
	}

	public double getTotalDistance(boolean directed) {
		int distance = 0;

		for (int i = 0; i < nodes.size(); i++) {
			ArrayList<Edge> edges = nodes.get(i).getOutEdges();

			for (int z = 0; z < edges.size(); z++) {
				distance += edges.get(z).getDistance();
			}
		}

		if (directed)
			return distance / 2.0;
		else
			return distance;
	}

	/*
	 * public double getTotalGarbage(boolean directed) { int totalGarbage = 0;
	 * 
	 * for (int i = 0; i < nodes.size(); i++) { ArrayList<Edge> edges =
	 * nodes.get(i).getOutEdges(); for (int z = 0; z < edges.size(); z++) {
	 * totalGarbage += edges.get(z).getMinutes(); } }
	 * 
	 * if (directed) return time / 2.0; else return time; }
	 */
	public int compareTotalDistance(Graph obj) {
		double d1 = this.getTotalDistance(false);
		double d2 = obj.getTotalDistance(false);
		if (d1 > d2) {
			return 1;
		} else {
			if (d1 < d2) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder strb = new StringBuilder();
		for (int i = 0; i < nodes.size(); i++) {
			strb.append(nodes.get(i).toString() + "\n");
		}
		String str = strb.toString();
		return str;
	}

}
