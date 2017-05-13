package graph;

import java.util.ArrayList;

import logic.Utils;

public class Graph {
	protected ArrayList<Node> nodes;
	protected double wastePaper=0.0, wastePlastic=0.0, wasteGlass=0.0, wasteCommon=0.0;

	public Graph() {
		this.nodes = new ArrayList<Node>();
		for (int i = 0; i < nodes.size(); i++) {
			if (this.nodes.get(i).getType().equals(Utils.TRUE_GARBAGE)) {
				wastePaper += nodes.get(i).getGarbageContainerByType(Utils.PAPER);
				wastePlastic += nodes.get(i).getGarbageContainerByType(Utils.PLASTIC);
				wasteGlass += nodes.get(i).getGarbageContainerByType(Utils.GLASS);
				wasteCommon += nodes.get(i).getGarbageContainerByType(Utils.COMMON);
			}
		}
	}

	public Graph(Graph newG) {
		this.nodes = newG.getNodes();
		for (int i = 0; i < nodes.size(); i++) {
			if (this.nodes.get(i).getType().equals(Utils.TRUE_GARBAGE)) {
				wastePaper += nodes.get(i).getGarbageContainerByType(Utils.PAPER);
				wastePlastic += nodes.get(i).getGarbageContainerByType(Utils.PLASTIC);
				wasteGlass += nodes.get(i).getGarbageContainerByType(Utils.GLASS);
				wasteCommon += nodes.get(i).getGarbageContainerByType(Utils.COMMON);
			}
		}
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

	public void setWasteByType(String wasteType, double collected){
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).getType().equals(Utils.TRUE_GARBAGE)) {
				if(nodes.get(i).getGarbageContainerByType(wasteType) >= collected)
					nodes.get(i).setGarbageContainer(wasteType, collected);
			}
		}
	}

	public double getTotalWaste(){return wastePaper+wastePlastic+wasteGlass+wasteCommon;}

	public double getTotalGarbageByTypeWaste(String wasteType){
		double total=0.0;
		for(Node n : nodes){
			if(n.getType() == Utils.TRUE_GARBAGE)
				total += n.getGarbageContainerByType(wasteType);
		}
		return total;
	}

	public double getTotalGarbageByTypeWasteWithMinimumLevelInContainers(String wasteType){
		double total=0.0;
		for(Node n : nodes){
			if(n.getType() == Utils.TRUE_GARBAGE)
				if(n.getGarbageContainerByType(wasteType) > Utils.MinimumGarbageCapacity)
					total += n.getGarbageContainerByType(wasteType);
		}
		return total;
	}

	public boolean addNode(Node node) {
		if (nodes.contains(node))
			return false;
		nodes.add(node);
		return true;
	}

	public boolean removeNode(Node node) {
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

	public boolean removeEdge(Node source, Node destiny) {
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

	public boolean findEdge(Node a, Node b) {
		for (int i = 0; i < nodes.size(); i++) {
			for(int j = 0 ; j < nodes.get(i).getOutEdges().size() ; j++){
				if((nodes.get(i).getOutEdges().get(j).getSource().getName() == a.getName()) && (nodes.get(i).getOutEdges().get(j).getDestiny().getName() == b.getName()))
					return true;
			}
		}
		return false;
	}

	public double calcDistance(Node a, Node b) {
		for (int i = 0; i < nodes.size(); i++) {
			for(int j = 0 ; j < nodes.get(i).getOutEdges().size() ; j++){
				if((nodes.get(i).getOutEdges().get(j).getSource().getName() == a.getName()) && (nodes.get(i).getOutEdges().get(j).getDestiny().getName() == b.getName()))
					return nodes.get(i).getOutEdges().get(j).getDistance();
			}
		}
		return 0.0;
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
