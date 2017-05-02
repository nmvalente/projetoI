package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

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
	protected Set<Node> explored;
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
			a_star(graph, this.central, this.station);
			buildItinerary();
			printItinerary();
			break;
		case Utils.UNIFORM_COST:
			uniform_cost();
			break;
		default:
			break;
		}

		// show in gui the result

		sendSearchToResult();
	}

	private void uniform_cost() {
		System.out.println("UNIFORM SELECTED");
	}

	private void a_star(Graph graph, Node initial, Node goal) {
		System.out.println("A STAR SELECTED");
		this.copyG = new Graph(graph); // grafo para manipular
		//System.out.println(this.copyG);

	/*	for (Node n : this.copyG.getNodes()) {
			if (!n.equals(central)) {
				n.setGValue(straightLineDistance(central.getLatitude(), central.getLongitude(), n.getLatitude(),
						n.getLongitude()));
				n.setHValue(0);
				n.setFValue(n.getGValue() + n.getHValue());
			}
			// System.out.println(n.getId() + " " + n.getFValue());
		}
*/
		explored = new HashSet<Node>();
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

		// cost from start
		initial.setGValue(0);

		queue.add(initial);
		boolean found = false;

		while ((!queue.isEmpty()) && (!found)) {

			// the node having the lowest f_score value
			Node current = queue.poll();

			explored.add(current);

			// goal found
			if (current.equals(goal)) {
				found = true;
				queue.add(current); // no caso em que deve adicionar o final
			}

			// check every child of current node
			for (Edge e : current.getOutEdges()) {

				Node child = e.getDestiny();

				double cost = straightLineDistance(current.getLatitude(), current.getLongitude(), child.getLatitude(),
						child.getLongitude());
				double temp_g_scores = current.getGValue() + cost;
				double temp_f_scores = temp_g_scores + child.getHValue();
				double h_scores = straightLineDistance(current.getLatitude(), current.getLongitude(), goal.getLatitude(),
						goal.getLongitude());
				current.setHValue(h_scores);
				//current.setFValue();
				// System.out.println(temp_g_scores + " " + temp_f_scores);
				
				/*
				 * if child node has been evaluated and the newer f_score is higher, skip
				 */

				if ((explored.contains(child)) && (temp_f_scores >= child.getFValue())) {
					continue;
				}

				/*
				 * else if child node is not in queue or newer f_score is lower
				 */

				else if ((!queue.contains(child)) || (temp_f_scores < child.getFValue())) {

					child.setParent(current);

					// System.out.println(child.getName() + " has parent " +
					// current.getName());
					child.setGValue(temp_g_scores);
					child.setFValue(temp_f_scores);
					if (queue.contains(child)) {
						queue.remove(child);
					}
					queue.add(child);
				}
			}
		}
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
}
