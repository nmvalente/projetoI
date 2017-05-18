package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import graph.Graph;
import graph.Node;

public class BuildGraph {

	protected HashMap<Integer, Node> containers;
	protected ArrayList<Node> garbageStations;
	protected Map<String, Integer> typeTruck;
	protected Node central;
	protected Node station;
	protected Map<String, ArrayList<Truck>> trucks;

	public BuildGraph(Graph graph, int truckPlastic, int truckPaper, int truckGlass, int truckCommon,
			int numberOfStations, double truckCapacity, double minimumLevelContainer, String heuristic) {

		setContainersAndStations(graph);
		setMapTrucks(truckPlastic, truckPaper, truckGlass, truckCommon);
		setTrucks(truckCapacity);
		new Search(graph, containers, garbageStations, typeTruck, central, station, trucks, heuristic);


	}

	private Map<String, Integer> setMapTrucks(int truckPlastic, int truckPaper, int truckGlass, int truckCommon) {
		typeTruck = new HashMap<String, Integer>();
		typeTruck.put(Utils.GLASS, truckGlass);
		typeTruck.put(Utils.PAPER, truckPaper);
		typeTruck.put(Utils.PLASTIC, truckPlastic);
		typeTruck.put(Utils.COMMON, truckCommon);
		return typeTruck;
	}

	public ArrayList<Node> getGarbageStation() {
		return this.garbageStations;
	}

	public void setContainersAndStations(Graph graph) {
		this.containers = new HashMap<Integer, Node>();
		this.garbageStations = new ArrayList<Node>();

		for (Node temp : graph.getNodes()) {
			if (temp.getType().equals(Utils.STATION)) {
				station = temp;
				station.setOutEdges(temp.getOutEdges());
				addGarbageStation(temp);
			} else if (temp.getType().equals(Utils.TRUE_GARBAGE)) {
				addGarbageContainer(temp);
			} else if (temp.getType().equals(Utils.CENTRAL)) {
				central = temp;
				central.setOutEdges(temp.getOutEdges());
			}
		}
		graph.setGraphContainers(this.containers);
	}

	public Map<String, ArrayList<Truck>> getTrucks() {
		return this.trucks;
	}

	public void setTrucks(double capacity) {
		Truck truck;
		ArrayList<Truck> truckTemp = new ArrayList<Truck>();
		int i;
		this.trucks = new HashMap<String, ArrayList<Truck>>();

		for (i = 0; i < typeTruck.get(Utils.GLASS); i++) {
			truck = new Truck(capacity, Utils.GLASS);
			truckTemp.add(truck);
		}
		if (truckTemp.size() > 0)
			this.trucks.put(Utils.GLASS, truckTemp);


		truckTemp = new ArrayList<Truck>();
		for (i = 0; i < typeTruck.get(Utils.PLASTIC); i++) {
			truck = new Truck(capacity, Utils.PLASTIC);
			truckTemp.add(truck);
		}
		if (truckTemp.size() > 0)
			this.trucks.put(Utils.PLASTIC, truckTemp);

		truckTemp = new ArrayList<Truck>();
		for (i = 0; i < typeTruck.get(Utils.PAPER); i++) {
			truck = new Truck(capacity, Utils.PAPER);
			truckTemp.add(truck);
		}
		if (truckTemp.size() > 0)
			this.trucks.put(Utils.PAPER, truckTemp);

		truckTemp = new ArrayList<Truck>();
		for (i = 0; i < typeTruck.get(Utils.COMMON); i++) {
			truck = new Truck(capacity, Utils.COMMON);
			truckTemp.add(truck);
		}
		if (truckTemp.size() > 0)
			this.trucks.put(Utils.COMMON, truckTemp);
	}

	public void addGarbageContainer(Node node) {
		this.containers.put(node.getId(), node);
	}

	public void addGarbageStation(Node node) {
		this.garbageStations.add(node);
	}
}
