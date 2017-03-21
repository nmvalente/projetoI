package logic;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

import graph.Edge;
import graph.Graph;
import graph.Node;
import gui.Result;

public class Search {

	public static enum SEARCH_MODE {
		UNIFORM_COST("Uniform-cost search"), 
		A_STAR_BASIC("A* search with basic heuristic function"), 
		A_STAR_ADVANCED("A* search with advanced heuristic function");

		private final String outputString;

		private SEARCH_MODE(String value) {
			outputString = value;
		}

		@Override
		public String toString() {
			return outputString;
		}
	}


	protected Truck truck;
	protected ResultGraph result;
	protected SEARCH_MODE searchMode;
	protected double priceRatio;
	protected double distRatio;
	private int generatedNodesCounter;

	public Search(Truck truck, SEARCH_MODE searchMode, double distRatio, double priceRatio){
		this.generatedNodesCounter = 0;
		this.setTruck(truck);
		//this.setResult(new ResultGraph());
		this.searchMode = searchMode;
		this.distRatio = distRatio;
		this.priceRatio = priceRatio;
	}

	////////////////////////////////////////////////////////////////////

	protected LinkedList<Node> containers;
	protected ArrayList<Node> garbageStations;
	protected LinkedList<Node> itinerary;
	protected Map<String, Integer> typeTruck;
	protected Node central;
	protected Node station;
	protected Map<String, ArrayList<Truck>> trucks;

	public Search(Graph graph,
			LinkedList<Node> containers,
			ArrayList<Node> garbageStations, 
			LinkedList<Node> itinerary,
			Map<String, Integer> typeTruck,
			Node central,
			Node station,
			Map<String, ArrayList<Truck>> trucks){

		
		sendSearchToResult();
	}

	public static double straightLineDistance(double lat1, double lon1,
			double lat2, double lon2) {
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double a = Math.pow((Math.sin(dLat / 2.0)), 2.0)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2))
				* Math.pow((Math.sin(dLon / 2.0)), 2.0);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return Utils.EARTH_RADIUS * c;
	}

	public boolean buildResult(StateNode finalNode) {
		//this.result = new ResultGraph();
		this.truck = finalNode.getTruck();
		StateNode lastNode = null;
		StateNode nodEval = finalNode;

		do {
			Node destNode = nodEval.getNode();

			Node sourcNode = new Node(destNode);
			if (lastNode != null) {
				Edge edge = lastNode.getEdge();
				if (edge != null) {
					sourcNode.addEdge(lastNode.getNode(), edge.getDistance());
				}
			}
			//this.result.addNode(sourcNode);

			if (nodEval.isSleepNeeded()) {
				// System.out.println("sleep needed: "
				// + nodEval.getNode().getDescription());
				this.result.addGarbageContainer(nodEval.getNode());
			}
			if (nodEval.isGasNeeded()) {
				this.result.addGarbageStation(nodEval.getNode());
			}

			lastNode = nodEval;
			nodEval = nodEval.getParent();
		} while (nodEval != null);

		//this.result.setTruck(truck);
		return true;
	}

	public boolean run() {
		Comparator<StateNode> compareNodes = new Comparator<StateNode>() {
			public int compare(StateNode node1, StateNode node2) {
				return node1.compareEvaluation(node2);
			}
		};

		PriorityQueue<StateNode> open = new PriorityQueue<StateNode>(20,
				compareNodes);
		HashMap<Integer, StateNode> closed = new HashMap<Integer, StateNode>();

		StateNode startNode = new StateNode(this.truck.getStartingPosition(), null, null,
				new Truck(truck), this.searchMode, this.distRatio, this.priceRatio);
		open.add(startNode);

		StateNode currentNode;
		StateNode finalNode = new StateNode(this.truck.getDestinyPosition(), null, null,
				new Truck(truck), this.searchMode, this.distRatio, this.priceRatio);

		while (!open.isEmpty()) {
			currentNode = open.poll();
			closed.put(currentNode.getNode().getId(), currentNode);

			if (currentNode.getNode().getId() == finalNode.getNode().getId()) {
				return buildResult(currentNode);
			}
			for (Edge edge : currentNode.getNode().getOutEdges()) {
				StateNode neighbour = new StateNode(edge.getDestiny(),
						new StateNode(currentNode), edge, new Truck(
								currentNode.getTruck()), this.searchMode,
						this.distRatio, this.priceRatio);

				generatedNodesCounter++;

				if (closed.containsKey(neighbour.getNode().getId())) {
					continue;
				}

				// Replace node if already exists one with higher cost
				/*if (neighbour.isValid() && !open.contains(neighbour)) {
					open.add(neighbour);
				}*/
			}
		}
		return false;
	}

	public Truck getTruck() {
		return truck;
	}

	public void setTruck(Truck truck) {
		this.truck = truck;
	}

	public ResultGraph getResult() {
		return result;
	}

	protected void setResult(ResultGraph result) {
		this.result = result;
	}

	public int getGeneratedNodesCounter() {
		return generatedNodesCounter;
	}

	public void sendSearchToResult() {
		try {
			Result window = new Result(10,2);
			window.frmResult.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
