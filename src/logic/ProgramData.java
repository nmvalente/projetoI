package logic;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import graph.Graph;
import graph.Node;
import logic.Utils;

public class ProgramData {

	protected Graph graph;
	protected int truckPlastic;
	protected int truckPaper;
	protected int truckGlass;
	protected int truckCommon;
	protected int numberOfStations;
	protected int truckCapacity;
	protected double minimumLevelContainer;
	protected String heuristic;

	public ProgramData(int truckCapacity,
			int numberOfStations,
			double minimumLevelContainer,
			int truckPlastic,
			int truckPaper,
			int truckGlass,
			int truckCommon, 
			String heuristic){

		this.truckPlastic = truckPlastic;
		this.truckPaper = truckPaper;
		this.truckGlass = truckGlass;
		this.truckCommon = truckCommon;
		this.numberOfStations = numberOfStations;
		this.truckCapacity = truckCapacity;
		this.minimumLevelContainer = minimumLevelContainer;
		this.heuristic = heuristic;

		//displayInformation();
		try {
			this.graph = loadMap();
		}
		catch (IOException e) {System.out.println("Unable to load csv file"); e.printStackTrace(); }
		//printGraph();

		new BuildGraph(this.graph,
				this.truckPlastic,
				this.truckPaper,
				this.truckGlass,
				this.truckCommon,
				this.numberOfStations,
				this.truckCapacity,
				this.minimumLevelContainer,
				this.heuristic);
	}
	public void displayInformation(){
		System.out.println(this);
	}

	public String toString(){
		int totalTrucks = this.truckCommon + this.truckGlass + this.truckPaper + this.truckPlastic;
		String str = "Number of plastic trucks: " + this.truckPlastic + "\n" +
				"Number of glass trucks: " + this.truckGlass + "\n" +
				"Number of paper trucks: " + this.truckPaper + "\n" +
				"Number of common trucks: " + this.truckCommon + "\n" +
				"Number of total trucks: " + totalTrucks + "\n" +
				"Minimum Level of each container: " + this.minimumLevelContainer + "\n" +
				"Truck capacity: " + this.truckCapacity + "\n" + 
				"Heuristic selected: " + this.heuristic + "\n";
		return str;
	}

	public Graph loadMap() throws IOException {

		Graph graph = new Graph();
		FileInputStream fstream = new FileInputStream(Utils.graphFile);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String readMode = Utils.UNDEFINED;
		String fileLine;

		while ((fileLine = br.readLine()) != null) {

			// Check non empty line
			if (fileLine.length() > 0) {


				// Checks if is a node or edge line
				if (fileLine.contains(Utils.NODES)) {
					readMode = Utils.NODES;
					continue;
				} else if (fileLine.contains(Utils.EDGES)) {
					readMode = Utils.EDGES;
					continue;
				}

				switch (readMode) {

				case Utils.NODES:
					String[] nodeValues = fileLine.split(Utils.SPLITTER);
					if (nodeValues.length != 5) {
						break;
					}

					Node node = new Node(Integer.parseInt(nodeValues[0]), 	//id
							Double.parseDouble(nodeValues[1]), 	//lat
							Double.parseDouble(nodeValues[2]), 	//lon
							nodeValues[3],						//type
							nodeValues[4]); 					//name of street
					graph.addNode(node);

					break;

				case Utils.EDGES:

					String[] edgeValues = fileLine.split(Utils.SPLITTER);

					if (edgeValues.length != 3) {
						break;
					}

					Node source = graph.findNode(Integer.parseInt(edgeValues[0]));
					Node destiny = graph.findNode(Integer.parseInt(edgeValues[1]));

					if (source == null || destiny == null) {
						throw new IOException();						
					}

					double distance = Double.parseDouble(edgeValues[2]);

					// double side because the file has only one - undirected graph

					source.addEdge(destiny, distance);
					destiny.addEdge(source, distance);

					break;
				default:
					readMode = Utils.UNDEFINED;
					break;
				}
			}
		}
		// Close the input stream
		br.close();
		return graph;
	}

	public void printGraph(){
		System.out.println(this.graph);
	}
}

