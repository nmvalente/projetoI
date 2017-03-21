package logic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

import graph.Graph;
import graph.Node;

public class Itinerary extends Search {

	private Graph map;
	private LinkedList<Node> unvisitedPOI;
	private final Node destiny;
	private int generatedNodesCounter;

	protected LinkedList<Node> gasStations;
	protected LinkedList<Node> hotels;

	public Itinerary(Graph map, Truck truck, LinkedList<Node> poiList, SEARCH_MODE heuristics, double distRatio, double priceRatio) {
		super(truck, heuristics, distRatio, priceRatio);
		setMap(map);
		this.generatedNodesCounter = 0;
		//this.result = new ResultGraph();
		this.unvisitedPOI = new LinkedList<Node>(poiList);
		unvisitedPOI.removeFirst();
		destiny = unvisitedPOI.peekLast();
		unvisitedPOI.removeLast();
	}

	@Override
	public boolean run() {
		Comparator<ResultGraph> compareGraphs = new Comparator<ResultGraph>() {
			public int compare(ResultGraph g1, ResultGraph g2) {
				return -1;//g1.compareTotalDistance(g2);
			}
		};

		PriorityQueue<ResultGraph> open = new PriorityQueue<ResultGraph>(20, compareGraphs);
		HashMap<Integer, ResultGraph> closed = new HashMap<Integer, ResultGraph>();
		truck.resetDistanceCovered();

		ArrayList<MyThread> threads = new ArrayList<MyThread>();

		for (int i = 0; i < this.unvisitedPOI.size(); i++) {
			truck.setDestinyPosition(unvisitedPOI.get(i));

			MyThread th = new MyThread(new Search(new Truck(truck), searchMode, distRatio, priceRatio));
			th.start();
			threads.add(th);
		}
		for (MyThread thread : threads) {
			Search search;
			try {
				search = thread.waitStop();
				this.generatedNodesCounter += thread.search.getGeneratedNodesCounter();
				open.add(search.getResult());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		threads.clear();

		while (!open.isEmpty() && unvisitedPOI.size() > 0) {
			ResultGraph subItinerary = open.poll();
			/*if (subItinerary.getTruck() == null) {
				return false;
			}*/
			//truck = new Truck(subItinerary.getTruck());
			//truck.setStartingPosition(map.findNode(subItinerary.getNodes().get(0).getId()));
			closed.put(truck.getStartingPosition().getId(), subItinerary);

		//	this.result.mergeGraph(subItinerary);

			open.clear();

			for (Node poi : unvisitedPOI) {
				if (closed.containsKey(poi.getId())) {
					continue;
				}

				truck.setDestinyPosition(poi);
				MyThread th = new MyThread(new Search(new Truck(truck), searchMode, distRatio, priceRatio));
				th.start();
				threads.add(th);
			}

			for (MyThread thread : threads) {
				Search search;
				try {
					search = thread.waitStop();
					this.generatedNodesCounter += thread.search.getGeneratedNodesCounter();
					open.add(search.getResult());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			threads.clear();
		}

		// go to the final destiny
		if (!truck.getStartingPosition().equals(destiny)) {
			truck.setDestinyPosition(destiny);
			Search search = new Search(new Truck(truck), this.searchMode, this.distRatio, this.priceRatio);
			search.run();
			this.generatedNodesCounter += search.getGeneratedNodesCounter();
			//this.result.mergeGraph(search.getResult());

			/*if (search.getResult().getNodes().size() > 0) {
				truck.setStartingPosition(map.findNode(search.getResult().getNodes().get(0).getId()));
				return true;
			} else {
				return false;
			}*/
		}
		return false;
	}

	public int getGeneratedNodesCounter() {
		return generatedNodesCounter;
	}

	public Graph getMap() {
		return map;
	}

	public void setMap(Graph map) {
		this.map = map;
	}
}
