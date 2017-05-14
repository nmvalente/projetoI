package logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import graph.Graph;
import graph.Node;

public class ProgramData {

	public static Graph graph;
	protected int truckPlastic;
	protected int truckPaper;
	protected int truckGlass;
	protected int truckCommon;
	protected int numberOfStations;
	protected int truckCapacity;
	protected double minimumLevelContainer;
	protected String heuristic;

	public ProgramData(int truckCapacity, int numberOfStations, double minimumLevelContainer, int truckPlastic,
			int truckPaper, int truckGlass, int truckCommon, String heuristic, File file) {

		this.truckPlastic = truckPlastic;
		this.truckPaper = truckPaper;
		this.truckGlass = truckGlass;
		this.truckCommon = truckCommon;
		this.numberOfStations = numberOfStations;
		this.truckCapacity = truckCapacity;
		this.minimumLevelContainer = minimumLevelContainer;
		this.heuristic = heuristic;

		// displayInformation();
		try {
			ProgramData.graph = loadMap(file);
		} catch (IOException e) {
			System.out.println("Unable to load csv file");
			e.printStackTrace();
		}
		// printGraph();

		new BuildGraph(ProgramData.graph, this.truckPlastic, this.truckPaper, this.truckGlass, this.truckCommon,
				this.numberOfStations, this.truckCapacity, this.minimumLevelContainer, this.heuristic);
	}

	public void displayInformation() {
		System.out.println(this);
	}

	@Override
	public String toString() {
		int totalTrucks = this.truckCommon + this.truckGlass + this.truckPaper + this.truckPlastic;
		String str = "Number of plastic trucks: " + this.truckPlastic + "\n" + "Number of glass trucks: "
				+ this.truckGlass + "\n" + "Number of paper trucks: " + this.truckPaper + "\n"
				+ "Number of common trucks: " + this.truckCommon + "\n" + "Number of total trucks: " + totalTrucks
				+ "\n" + "Minimum Level of each container: " + this.minimumLevelContainer + "\n" + "Truck capacity: "
				+ this.truckCapacity + "\n" + "Heuristic selected: " + this.heuristic + "\n";
		return str;
	}

	public Graph loadMap(File file) throws IOException {

		Graph graph = new Graph();
		FileInputStream fstream = new FileInputStream(file);
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
					if (nodeValues.length != 7) {
						break;
					}

					Node node = new Node(Integer.parseInt(nodeValues[0]), // id
							nodeValues[1], // type
							nodeValues[2], // name of street
							Double.parseDouble(nodeValues[3]), // glass garbage
							Double.parseDouble(nodeValues[4]), // paper garbage
							Double.parseDouble(nodeValues[5]), // plastic garbage
							Double.parseDouble(nodeValues[6]) // common garbage
							);
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
						System.out.println(source);
						System.out.println(destiny);
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

	public void printGraph() {
		System.out.println(ProgramData.graph);
	}
}
