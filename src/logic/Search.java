package logic;

import java.nio.channels.ClosedSelectorException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import graph.Edge;
import graph.Graph;
import graph.Node;
import gui.Result;

public class Search {

	protected Graph copyG;
	protected LinkedList<Node> containers;
	protected ArrayList<Node> garbageStations;
	public static List<Node> itinerary;
	protected Map<String, Integer> typeTruck;
	protected Node central;
	protected Node station;
	protected Map<String, ArrayList<Truck>> trucks;
	protected PriorityQueue<Node> queue;
	protected HashMap<Integer, Node> explored;
	protected HashMap<Integer, Node> closeSet;
	private Graph graph;
	private double distanceCovered;

	public Search(Graph graph, LinkedList<Node> containers, ArrayList<Node> garbageStations, LinkedList<Node> itinerary,
			Map<String, Integer> typeTruck, Node central, Node station, Map<String, ArrayList<Truck>> trucks,
			String heuristic) {

		this.containers = containers;
		this.central = new Node(central);
		this.station = new Node(station);
		this.graph = graph;
		this.trucks = trucks;

		switch (heuristic) {
		case Utils.A_STAR:
			computeProgram();
			//buildItinerary();
			//printItinerary();
			break;
		case Utils.UNIFORM_COST:
			uniform_cost();
			break;
		default:
			break;
		}

		// show in gui the result

		//sendSearchToResult();
	}

	private void computeProgram() {


		ArrayList<Node> edges = a_star(graph, this.central);
		System.out.println(edges);
	}

	private void uniform_cost() {
		System.out.println("UNIFORM SELECTED");
	}

	private ArrayList<Node> a_star(Graph graph, Node initial) {
		System.out.println("A STAR SELECTED");
		this.copyG = new Graph(graph); // grafo para manipular

		explored = new HashMap<Integer, Node>();
		closeSet = new HashMap<Integer, Node>();
		queue = new PriorityQueue<Node>(this.copyG.getNodes().size(), new Comparator<Node>() {
			// override compare method
			@Override
			public int compare(Node i, Node j) {
				if (i.getFValue() > j.getFValue()) {
					return 1;
				} else if (i.getFValue() < j.getFValue()) {
					return -1;
				} else {
					return 0;
				}
			}
		});

		double allPaper = this.copyG.getTotalGarbageByTypeWaste(Utils.PAPER);
		double allPlastic = this.copyG.getTotalGarbageByTypeWaste(Utils.PLASTIC);
		double allCommon = this.copyG.getTotalGarbageByTypeWaste(Utils.COMMON);
		double allGlass = this.copyG.getTotalGarbageByTypeWaste(Utils.GLASS);
		double allWaste = allPaper + allPlastic + allCommon + allGlass;

		// cost from start
		initial.setGValue(0);

		queue.add(initial);
		explored.put(initial.getId(), initial);
		Node goal = null; // quando tiver apanhado todo o lixo
		double collected = 0.0;
		while (explored.size() > 0 ) {

			// the node having the lowest f_score value
			Node current = queue.poll();
			explored.remove(current);

			if(current != null && current.getType() == Utils.TRUE_GARBAGE) // se for contentor de lixo
				if(current.getGarbageContainerByType("paper") > 0){ // se houver papel por apanhar
					collected += current.getGarbageContainerByType("paper"); // apanha o papel todo
					current.setGarbageContainer("paper", 0.0); // coloca contentor vazio
				}			

			// goal found
			if (allPaper == collected || current == null)  {
				//goal = current;
				System.out.println(allPaper);
				System.out.println(collected);
				break;
			}
			else{
				System.out.println("Chegou");
				goal = current; 		// just for test
				closeSet.put(current.getId(), current);
				ArrayList<Edge> neighbors = current.getOutEdges();
				// check every child of current node
				for (Edge neighborEdge : neighbors) {

					Node neighbor = neighborEdge.getDestiny();

					double cost = straightLineDistance(current.getLatitude(), current.getLongitude(), neighbor.getLatitude(),
							neighbor.getLongitude());
					double temp_g_scores = current.getGValue() + cost;
					double temp_f_scores = temp_g_scores;// + neighbor.getHValue();
					/*double h_scores = straightLineDistance(current.getLatitude(), current.getLongitude(), goal.getLatitude(),
							goal.getLongitude());
					current.setHValue(h_scores);
					 */
					Node n = explored.get(neighbor.getId());

					// se ainda n foi explorado
					if (n == null) {
						n = new Node(neighbor);
						n.setParent(current);
						// calculos faltam...
						n.setFValue(temp_g_scores);
						explored.put(neighbor.getId(), n);
						queue.add(n);
					}

					else{ // se ja foi explorado
						if (temp_g_scores < n.getGValue()) {
							n.setParent(current);
							n.setFValue(temp_g_scores);
						}
					}
				}
			}
		}
		if(goal != null){

			Stack<Node> stack = new Stack<Node>();
			ArrayList<Node> list = new ArrayList<Node>();
			Queue<Edge> edges = new LinkedList<Edge>();
			stack.push(goal);
			Node parent = goal.getParent();
			while (parent != null) {
				stack.push(parent);
				parent = parent.getParent();
			}

			while (stack.size() > 0) {
				list.add(stack.pop());
			}
			//Node finalNode = list.get(list.size() - 1);
			
			/*while (list.size() > 1) {
				Node n = list.get(0);
				for (Edge e : n.getOutEdges()) {
					if (e.getDestiny().equals(list.get(1))) {
						edges.add(e);
						list.remove(0);
						break;
					}
				}
			}*/
			/*if (finalNode.getPathToDump() != null)
					for (int i = 0; i < finalNode.getPathToDump().size(); i++) {
						edges.add(finalNode.getPathToDump().get(i));
					}
			 */
			return list;
			//return edges;
		}
		return null;
	}




	public List<Node> buildItinerary() {
		Search.itinerary = new ArrayList<Node>();

		for (Node node = queue.poll(); node != null; node = node.getParent()) {
			Search.itinerary.add(node);
		}

		Collections.reverse(Search.itinerary);

		for (int i = 0; i < Search.itinerary.size() - 1; i++) {
			for (Edge edgeo : Search.itinerary.get(i).getOutEdges()) {
				if (edgeo.getSource().equals(Search.itinerary.get(i))
						&& edgeo.getDestiny().equals(Search.itinerary.get(i + 1)))
					distanceCovered += edgeo.getDistance();
			}
		}

		return Search.itinerary;
	}

	public void printItinerary() {

		System.out.println("Itinerary :");

		System.out.print("[");
		for (Node node : Search.itinerary) {
			if (node.getType() == this.station.getType())
				System.out.print(node.getName());
			else
				System.out.print(node.getName() + ", ");
		}
		System.out.println("]");

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

	public double calculateHValue(Node actual, Node goal){


		return 0;


	}
}
