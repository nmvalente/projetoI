package logic;

import java.io.File;

public class Utils {

	public static final String UNDEFINED = "UNDEFINED";
	public static final String NODES = "[nodes]";
	public static final String EDGES = "[edges]";
	public static final String CENTRAL = "central";
	public static final String STATION = "station";
	public static final String TRUE_GARBAGE = "true";
	public static final String FALSE_GARBAGE = "false";
	public static final String GLASS = "glass";
	public static final String PLASTIC = "plastic";
	public static final String PAPER = "paper";
	public static final String COMMON = "common";
	public static final String SPLITTER = ";";
	public static final File graphFile = new File("./resources/graphs");
	public static final String HEURISTIC1 = "heuristic1";
	public static final String HEURISTIC2 = "uniform cost";
	public static final String HEURISTIC3 = "heuristic3";
	public static int MinimumGarbageCapacity;
	public static final int garbageCapacity = 100;
	public static final String defaultFileGraph = "./resources/graphs/medium_graph.csv";
}
