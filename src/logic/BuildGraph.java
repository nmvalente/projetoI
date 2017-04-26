package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import graph.Graph;
import graph.Node;

public class BuildGraph {

	protected LinkedList<Node> containers;
	protected ArrayList<Node> garbageStations;
	protected LinkedList<Node> itinerary;
	protected Map<String, Integer> typeTruck;
	protected Node central;
	protected Node station;
	protected Map<String, ArrayList<Truck>> trucks;

	public BuildGraph(Graph graph, 
			int truckPlastic, 
			int truckPaper, 
			int truckGlass, 
			int truckCommon, 
			int numberOfStations, 
			double truckCapacity, 
			double minimumLevelContainer, 
			String heuristic) {

		setContainersAndStations(graph);
		setMapTrucks(truckPlastic, truckPaper, truckGlass, truckCommon);
		setTrucks(truckCapacity);

		new Search(graph, containers, garbageStations, itinerary, typeTruck,central,station,trucks, heuristic);

		//this.itinerary = new LinkedList<Node>();
	}

	private Map<String, Integer> setMapTrucks(int truckPlastic, int truckPaper, int truckGlass, int truckCommon) {
		typeTruck = new HashMap<String, Integer>();
		typeTruck.put("glass",truckGlass);
		typeTruck.put("paper",truckPaper);
		typeTruck.put("plastic",truckPlastic);
		typeTruck.put("common",truckCommon);
		return typeTruck;
	}

	public LinkedList<Node> getItinerary() {
		return itinerary;
	}

	public void setItinerary(LinkedList<Node> itinerary) {
		this.itinerary = itinerary;
	}

	public LinkedList<Node> getContainers() {
		return containers;
	}

	public ArrayList<Node> getGarbageStation() {
		return garbageStations;
	}

	public void setContainersAndStations(Graph graph) {
		this.containers = new LinkedList<Node>();
		this.garbageStations = new ArrayList<Node>();

		for (Node temp : graph.getNodes()) {
			if(temp.getType().equals(Utils.STATION)){
				station = new Node(temp);
				station.setOutEdges(temp.getOutEdges());
				addGarbageStation(temp);
			}
			else if(temp.getType().equals(Utils.TRUE_GARBAGE)){
				addGarbageContainer(temp);
			}
			else if(temp.getType().equals(Utils.CENTRAL)){
				central = new Node(temp);
				central.setOutEdges(temp.getOutEdges()); // falta testar problemas aqui
			}
		}	
	}

	public Map<String, ArrayList<Truck>> getTrucks() {
		return trucks;
	}

	public Map<String, ArrayList<Truck>> setTrucks(double capacity) {
		Truck truck;
		ArrayList<Truck> truckTemp = new ArrayList<Truck>();
		int i;
		this.trucks = new HashMap<String , ArrayList<Truck>>();


		for(i = 0 ; i < typeTruck.get("glass") ; i++){

			truck = new Truck(central,station,capacity, Utils.GLASS);
			truckTemp.add(truck);
		}
		if(truckTemp.size() > 0)
			this.trucks.put("glass", truckTemp);

		truckTemp = new ArrayList<Truck>();
		for(i = 0 ; i < typeTruck.get("plastic") ; i++){
			truck = new Truck(central,station,capacity, Utils.PLASTIC);
			truckTemp.add(truck);
		}
		if(truckTemp.size() > 0)
			this.trucks.put("glass", truckTemp);

		for(i = 0 ; i < typeTruck.get("paper") ; i++){
			truck = new Truck(central,station,capacity, Utils.PAPER);
			truckTemp.add(truck);
		}
		if(truckTemp.size() > 0)
			this.trucks.put("glass", truckTemp);

		for(i = 0 ; i < typeTruck.get("common") ; i++){
			truck = new Truck(central,station,capacity, Utils.COMMON);
			truckTemp.add(truck);
		}
		if(truckTemp.size() > 0)
			this.trucks.put("glass", truckTemp);

		return this.trucks;
	}

	public void addGarbageContainer(Node node) {
		this.containers.add(node);
	}

	public void addGarbageStation(Node node) {
		this.garbageStations.add(node);
	}
}
