package graph;

import java.util.ArrayList;
import java.util.HashMap;

import logic.Utils;

public class Graph {
	private ArrayList<Node> nodes;
	private double wastePaper=0.0,
			wastePlastic=0.0,
			wasteGlass=0.0,
			wasteCommon=0.0;
	private boolean original;
	private HashMap<Integer, Node> containers;

	public Graph() {
		this.original = true;
		this.nodes = new ArrayList<Node>();
		for (int i = 0; i < this.nodes.size(); i++) {
			if (this.nodes.get(i).getType().equals(Utils.TRUE_GARBAGE)) {
				this.wastePaper += this.nodes.get(i).getGarbageContainerByType(Utils.PAPER);
				this.wastePlastic += this.nodes.get(i).getGarbageContainerByType(Utils.PLASTIC);
				this.wasteGlass += this.nodes.get(i).getGarbageContainerByType(Utils.GLASS);
				this.wasteCommon += this.nodes.get(i).getGarbageContainerByType(Utils.COMMON);
			}
		}
		this.containers = new HashMap<Integer, Node>();
	}

	public Graph(Graph newG) {
		this.original = false;
		this.nodes = new ArrayList<Node>(newG.getNumNodes());
		for(Node n : newG.getNodes())
		this.nodes.add(new Node(n));

		for (int i = 0; i < newG.getNodes().size(); i++) {
			if (newG.getNodes().get(i).getType().equals(Utils.TRUE_GARBAGE)) {
				this.wastePaper += newG.getNodes().get(i).getGarbageContainerByType(Utils.PAPER);
				this.wastePlastic += newG.getNodes().get(i).getGarbageContainerByType(Utils.PLASTIC);
				this.wasteGlass += newG.getNodes().get(i).getGarbageContainerByType(Utils.GLASS);
				this.wasteCommon += newG.getNodes().get(i).getGarbageContainerByType(Utils.COMMON);
			}
		}

		this.containers = newG.getGraphContainers();
	}

	public ArrayList<Node> getNodes() {
		return this.nodes;
	}

	public Node findNode(int nodeId) {
		for (int i = 0; i < this.nodes.size(); i++) {
			if (this.nodes.get(i).getId() == nodeId) {
				return this.nodes.get(i);
			}
		}
		return null;
	}

	public void setWasteByType(String wasteType, double collected){
		for (int i = 0; i < this.nodes.size(); i++) {
			if (this.nodes.get(i).getType().equals(Utils.TRUE_GARBAGE)) {
				if(this.nodes.get(i).getGarbageContainerByType(wasteType) >= collected)
					this.nodes.get(i).setGarbageContainer(wasteType, collected);
			}
		}
	}

	public double getTotalWaste(){return wastePaper+wastePlastic+wasteGlass+wasteCommon;}

	public double getTotalGarbageByTypeWaste(String wasteType){
		double total=0.0;
		for(Node n : this.nodes){
			if(n.getType() == Utils.TRUE_GARBAGE)
				total += n.getGarbageContainerByType(wasteType);
		}
		return total;
	}

	public double getTotalGarbageByTypeWasteWithMinimumLevelInContainers(String wasteType){
		double total=0.0;
		for(Node n : this.nodes){
			if(n.getType() == Utils.TRUE_GARBAGE)
				if(n.getGarbageContainerByType(wasteType) > Utils.MinimumGarbageCapacity)
					total += n.getGarbageContainerByType(wasteType);
		}
		return total;
	}

	public boolean addNode(Node node) {
		if (this.nodes.contains(node))
			return false;
		this.nodes.add(node);
		return true;
	}

	public boolean removeNode(Node node) {
		if (this.nodes.remove(node)) {
			for (int i = 0; i < this.nodes.size(); i++) {
				this.nodes.get(i).removeEdgeTo(node);
			}
			return true;
		}
		return false;
	}

	public boolean addEdge(Node source, Node destiny, double distance) {
		if (this.nodes.contains(source) && this.nodes.contains(destiny)) {
			source.addEdge(destiny, distance);
			return true;
		}
		return false;
	}

	public boolean removeEdge(Node source, Node destiny) {
		if (this.nodes.contains(source) && this.nodes.contains(destiny)) {
			return source.removeEdgeTo(destiny);
		}
		return false;
	}

	public int getNumNodes() {
		return this.nodes.size();
	}

	public int getNumEdges() {
		int count = 0;
		for (int i = 0; i < this.nodes.size(); i++) {
			count += this.nodes.get(i).getOutEdges().size();
		}
		return count;
	}

	public boolean findEdge(Node a, Node b) {
		for (int i = 0; i < this.nodes.size(); i++) {
			for(int j = 0 ; j < this.nodes.get(i).getOutEdges().size() ; j++){
				if((this.nodes.get(i).getOutEdges().get(j).getSource().getName() == a.getName()) && (nodes.get(i).getOutEdges().get(j).getDestiny().getName() == b.getName()))
					return true;
			}
		}
		return false;
	}

	public double calcDistance(Node a, Node b) {
		for (int i = 0; i < this.nodes.size(); i++) {
			for(int j = 0 ; j < this.nodes.get(i).getOutEdges().size() ; j++){
				if((this.nodes.get(i).getOutEdges().get(j).getSource().getName() == a.getName()) && (this.nodes.get(i).getOutEdges().get(j).getDestiny().getName() == b.getName()))
					return this.nodes.get(i).getOutEdges().get(j).getDistance();
			}
		}
		return 0.0;
	}

	@Override
	public String toString() {
		StringBuilder strb = new StringBuilder();
		for (int i = 0; i < this.nodes.size(); i++) {
			strb.append(this.nodes.get(i).toString() + "\n");
		}
		String str = strb.toString();
		return str;
	}

	public void setGraphContainers(HashMap<Integer, Node> containers){
		this.containers = containers;
	}

	public HashMap<Integer, Node> getGraphContainers(){
		return this.containers;
	}

	public double getTotalInContainers(String typeofWaste){
		double waste = 0.0;
		for (HashMap.Entry<Integer, Node> entry : this.containers.entrySet()) {
			Node node = entry.getValue();
			waste += node.getGarbageContainerByType(typeofWaste);
		}
		return waste;
	}

	public int getNumberOfEmptyContainers(String typeofWaste){
		int nr = 0;
		for (HashMap.Entry<Integer, Node> entry : this.containers.entrySet()) {
			Node node = entry.getValue();

			if(node.getGarbageContainerByType(typeofWaste) == 0.0)
				nr++;
		}
		return nr;
	}
/*
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (this.getClass() != obj.getClass())
			return false;

		Graph graph = (Graph) obj;

		for(int i = 0 ; i < this.getNumNodes() ; i++){
			if(this.getNodes().get(i).getType().equals(Utils.TRUE_GARBAGE))
				if (this.getNodes().get(i).getGarbageContainerByType(Utils.PAPER) != graph.getNodes().get(i).getGarbageContainerByType(Utils.PAPER)){
					return false;
				}
		}
		return true;
	}*/
}
