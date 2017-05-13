package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

import graph.Edge;
import graph.Graph;
import graph.Node;
import gui.Result;

public class Search {

	protected LinkedList<Node> containers;
	protected ArrayList<Node> garbageStations;
	public static List<Node> itinerary;
	protected Map<String, Integer> typeTruck;
	protected Node central;
	protected Node station;
	protected Map<String, ArrayList<Truck>> trucks;
	private Graph graph;
	private double distanceCovered;
	private Truck truck = new Truck(1000,Utils.PAPER); // hard-coded to test

	public Search(Graph graph, LinkedList<Node> containers, ArrayList<Node> garbageStations, LinkedList<Node> itinerary,
			Map<String, Integer> typeTruck, Node central, Node station, Map<String, ArrayList<Truck>> trucks,
			String heuristic) {

		this.containers = containers;
		this.garbageStations = garbageStations;
		this.central = central;
		this.station = station;
		this.graph = graph;
		this.trucks = trucks;

		switch (heuristic) {
		case Utils.HEURISTIC1:
			computeProgram(heuristic);
			break;
		case Utils.HEURISTIC2:
			computeProgram(heuristic);
			break;
		case Utils.HEURISTIC3:
			computeProgram(heuristic);
			break;
		default:
			break;
		}

		// show in gui the result

		//sendSearchToResult();
	}

	private void computeProgram(String heuristic) {
		ArrayList<Object> result = searchAStar(graph, heuristic);
		System.out.println("Best solution found - Statistics\n");
		System.out.println("Time of execution: " + result.get(1) + "ms");
		AStarNode finalNode = (AStarNode)result.get(0);
		System.out.println("Number of visited nodes = " + result.get(2));
		System.out.println("Total Cost: " + finalNode.getG()+ "km\n");
		System.out.println("Total Garbage to Collect: " + finalNode.getGraph().getTotalGarbageByTypeWaste(Utils.PAPER));
		System.out.println("Total Garbage Collected: " + finalNode.getTruck().getTotalGarbageSinceInit());
		printResult(finalNode);

	}

	private void printResult(AStarNode result) {
		Stack<AStarNode> stack = new Stack<AStarNode>();
		AStarNode parent = result.getParent();
		while (parent != null) {
			stack.push(parent);
			parent = parent.getParent();
		}

		while (stack.size() > 0) {
			System.out.println(stack.pop().getNode().getId());
		}

	}

	public ArrayList<Object> searchAStar(Graph g, String heuristic) {
		HashMap<Integer, AStarNode> openSet = new HashMap<Integer, AStarNode>();
		HashMap<Integer, AStarNode> closeSet = new HashMap<Integer, AStarNode>();
		PriorityQueue<AStarNode> queue = PriorityQueue<AStarNode>(this.graph.getNumNodes(), AStarNodeComparator);
		ArrayList<Object> result = new ArrayList<Object>(); // to get the result of search

		// Number of visited nodes
		int visitedNodes = 0;

		AStarNode initial = new AStarNode(graph, central, truck);
		initial.setG(0);
		initial.setH(heuristic_cost_estimate(initial, heuristic));

		openSet.put(initial.getNode().getId(), initial);
		queue.add(initial);
		long startTime = System.currentTimeMillis();
		AStarNode goal = null;

		// Loop the open list as long as it isn't empty
		while (!openSet.isEmpty()) {
			// Increment number of visited nodes
			visitedNodes++;

			AStarNode lowF = queue.poll();
			openSet.remove(lowF.getNode().getId());
			System.out.println(lowF);

			lowF.getTruck().setItinerary(lowF);
			lowF.getTruck().collectWaste();


			//System.out.println(lowF.getGraph().getTotalGarbageByTypeWaste(Utils.PAPER));
			// Check if it is the goal
			if (lowF.hasFinish()) {
				goal = lowF;
				long stopTime = System.currentTimeMillis();
				long elapsedTime = stopTime - startTime;

				result.add(lowF.getTruck().getItinerary());
				result.add(elapsedTime);
				result.add(visitedNodes);
				return result;
			}
			else{

				closeSet.put(lowF.getNode().getId(), lowF);

				ArrayList<AStarNode> neighbors = getAdjacentNodes(lowF);

				// Check each adjacent node not on the closed list
				for (int i = 0; i < neighbors.size(); i++) {
					AStarNode n = openSet.get(neighbors.get(i).getNode().getId());
					if (n == null) {

						neighbors.get(i).setH(heuristic_cost_estimate(neighbors.get(i), heuristic));
						openSet.put(neighbors.get(i).getNode().getId(), neighbors.get(i));
					}
					else{
						if(lowF.getG() < n.getG()){
							n.setParent(lowF);
							n.setG(lowF.getG());
						}
					}
				}
			}
		}
		return null;
	}

	private double heuristic_cost_estimate(AStarNode aStarNode, String heuristic) {
		double h = 0.0;

		// Check the chosen heuristic
		if (heuristic == Utils.HEURISTIC1) {
			// Count every node with minimum level
			// verificar se o no é o inicial para atribuir-lhe um valor ainda mais elevado!
			if(aStarNode.getNode().getType().equals(Utils.TRUE_GARBAGE))
				h = aStarNode.getGraph().getTotalGarbageByTypeWaste(Utils.PAPER) - aStarNode.getNode().getGarbageContainerByType(Utils.PAPER);//getTotalGarbageByTypeWasteWithMinimumLevelInContainers(Utils.PAPER);// - aStarNode.getNode().getGarbageContainerByType(Utils.PAPER);
			else h = aStarNode.getGraph().getTotalGarbageByTypeWaste(Utils.PAPER);//getTotalGarbageByTypeWasteWithMinimumLevelInContainers(Utils.PAPER);
		} else if (heuristic == Utils.HEURISTIC2) {
			return h;
		} else if (heuristic == Utils.HEURISTIC3) {
			return h;
		}
		return h;
	}

	public void sendSearchToResult() {
		try {
			Result window = new Result(this.graph, Search.itinerary, distanceCovered);
			window.frmResult.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static double straightLineDistance(double lat1, double lon1, double lat2, double lon2) {
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double a = Math.pow((Math.sin(dLat / 2.0)), 2.0) + Math.cos(Math.toRadians(lat1))
		* Math.cos(Math.toRadians(lat2)) * Math.pow((Math.sin(dLon / 2.0)), 2.0);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return Utils.EARTH_RADIUS * c;
	}

	private AStarNode lowestF(ArrayList<AStarNode> open) {
		AStarNode temp = null;

		for (int i = 0; i < open.size(); i++) {
			if (temp == null)
				temp = open.get(i);
			else if( (open.get(i).getH() + open.get(i).getG()) < (temp.getH() + temp.getG()))
				temp = open.get(i);
		}

		return temp;
	}

	private ArrayList<AStarNode> getAdjacentNodes(AStarNode curr) {
		ArrayList<AStarNode> adjacents = new ArrayList<AStarNode>();


		for (Edge e : curr.getNode().getOutEdges()) {

			Node neighbor = e.getDestiny();
			double cost = e.getDistance();
			double temp_g_scores = curr.getG() + cost;

			AStarNode aux = new AStarNode(curr.getGraph(), neighbor, curr.getTruck());
			aux.setG(temp_g_scores);
			aux.setParent(curr);

			// Add it
			adjacents.add(aux);
		}
		return adjacents;
	}
}
