package logic;

import java.util.*;

import graph.Edge;
import graph.Graph;
import graph.Node;
import gui.Result;

public class Search {

    protected HashMap<Integer, Node> containers;
    protected ArrayList<Node> garbageStations;
    public static ArrayList<Node> itinerary = new ArrayList<Node>();
    protected Map<String, Integer> typeTruck;
    protected Node central;
    protected Node station;
    protected Map<String, ArrayList<Truck>> trucks;
    protected static Graph graph;
    private double distanceCovered;
    private Truck truck;

    public Search(Graph graph, HashMap<Integer, Node> containers, ArrayList<Node> garbageStations,
                  Map<String, Integer> typeTruck, Node central, Node station, Map<String, ArrayList<Truck>> trucks,
                  String heuristic) {

        this.containers = containers;
        this.garbageStations = garbageStations;
        this.central = central;
        this.station = station;
        Search.graph = graph;
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

        sendSearchToResult();
    }

    private void computeProgram(String heuristic) {

        truck = this.trucks.get(Utils.PAPER).get(0);

        ArrayList<Object> paperResult = searchAStar(heuristic, Utils.PAPER);
        showresults(paperResult, Utils.PAPER);

        truck = this.trucks.get(Utils.GLASS).get(0);
        ArrayList<Object> glassResult = searchAStar(heuristic, Utils.GLASS);
        showresults(glassResult, Utils.GLASS);

        truck = this.trucks.get(Utils.PLASTIC).get(0);
        ArrayList<Object> plasticResult = searchAStar(heuristic, Utils.PLASTIC);
        showresults(plasticResult, Utils.PLASTIC);

        truck = this.trucks.get(Utils.COMMON).get(0);
        ArrayList<Object> commonResult = searchAStar(heuristic, Utils.COMMON);
        showresults(commonResult, Utils.COMMON);

        showTotalResults(paperResult, glassResult, plasticResult, commonResult);
    }

    public void showresults(ArrayList<Object> result, String typeofWaste) {
        if (result != null) {
            System.out.println("Best solution found for " + typeofWaste + " - Statistics");
            long timePassed = (long) result.get(1);
            System.out.println("Time of execution: " + timePassed + "ms");
            AStarNode finalNode = (AStarNode) result.get(0);

            System.out.println("Number of visited nodes = " + result.get(2));
            System.out.println("Total Cost: " + finalNode.getG() + "km");
            System.out.println("Total Garbage to Collect: " + finalNode.getGraph().getTotalGarbageByTypeWaste(typeofWaste));
            System.out.println("Total Garbage Collected: " + finalNode.getTruck().allWasteSinceStart);
            System.out.println("Initial Garbage to Collect: " + graph.getTotalGarbageByTypeWaste(typeofWaste) + "\n");
            printResult(finalNode);
            System.out.println("==============================================================================================================================================================\n");
        } else {
            System.out.println("No solution found!");
        }
    }

    private void showTotalResults(ArrayList<Object> paperResult, ArrayList<Object> glassResult, ArrayList<Object> plasticResult, ArrayList<Object> commonResult) {

        System.out.println("Total Statistics");
        long timePassed = (long) paperResult.get(1) + (long) glassResult.get(1) + (long) plasticResult.get(1) + (long) commonResult.get(1);
        System.out.println("Time of execution: " + timePassed + "ms");
        AStarNode finalNodePaper = (AStarNode) paperResult.get(0);
        AStarNode finalNodeGlass = (AStarNode) glassResult.get(0);
        AStarNode finalNodePlastic = (AStarNode) plasticResult.get(0);
        AStarNode finalNodeCommon = (AStarNode) commonResult.get(0);
        int totalvisitednodes = (int) paperResult.get(2) + (int) glassResult.get(2) + (int) plasticResult.get(2) + (int) commonResult.get(2);
        System.out.println("Number of visited nodes = " + totalvisitednodes);
        double totalcost = finalNodePaper.getG() + finalNodeCommon.getG() + finalNodeGlass.getG() + finalNodePlastic.getG();
        System.out.println("Total Cost: " + totalcost + "km");
        double totalgarbagetocollect = finalNodePaper.getGraph().getTotalGarbageByTypeWaste(Utils.PAPER) + finalNodeCommon.getGraph().getTotalGarbageByTypeWaste(Utils.COMMON) +
                finalNodeGlass.getGraph().getTotalGarbageByTypeWaste(Utils.GLASS) + finalNodePlastic.getGraph().getTotalGarbageByTypeWaste(Utils.PLASTIC);
        System.out.println("Total Garbage to Collect: " + totalgarbagetocollect);
        double totaltruckcollect = finalNodePaper.getTruck().allWasteSinceStart + finalNodeCommon.getTruck().allWasteSinceStart +
                finalNodeGlass.getTruck().allWasteSinceStart + finalNodePlastic.getTruck().allWasteSinceStart;
        System.out.println("Total Garbage Collected: " + totaltruckcollect);
        double totalInitialGarbage = graph.getTotalGarbageByTypeWaste(Utils.PAPER) +
                graph.getTotalGarbageByTypeWaste(Utils.PLASTIC) +
                graph.getTotalGarbageByTypeWaste(Utils.COMMON) +
                graph.getTotalGarbageByTypeWaste(Utils.GLASS);
        System.out.println("Initial Garbage to Collect: " + totalInitialGarbage + "\n");
        System.out.println("==============================================================================================================================================================\n");


    }

    private void printResult(AStarNode result) {
        Stack<AStarNode> stack = new Stack<AStarNode>();
        stack.add(result);

        AStarNode parent = result.getParent();
        while (parent != null) {
            stack.push(parent);
            parent = parent.getParent();
        }
        System.out.println("ID node - Total to Collect / Current Truck Collected\n");
        int count = 1;
        while (stack.size() > 0) {
            itinerary.add(stack.peek().getNode());
            if (count % 6 == 0)
                System.out.println(stack.peek().getNode().getId() + " - " + stack.peek().getTruck().allWasteSinceStart + "/" + stack.peek().getTruck().getTotalGarbage());
            else
                System.out.print(stack.peek().getNode().getId() + " - " + stack.peek().getTruck().allWasteSinceStart + "/" + stack.peek().getTruck().getTotalGarbage() + "  ->  ");

            count++;
            stack.pop();
        }
        System.out.println("");

    }

    public ArrayList<Object> searchAStar(String heuristic, String typeofWaste) {
        ArrayList<Object> result = new ArrayList<Object>(); // to get the result of search
        // Number of visited nodes
        int visitedNodes = 0;

        // Initialize open and closed lists
        ArrayList<AStarNode> open = new ArrayList<AStarNode>();
        ArrayList<AStarNode> closed = new ArrayList<AStarNode>();
        PriorityQueue<AStarNode> queue = new PriorityQueue<AStarNode>(graph.getNumNodes(),new AStarNodeComparator());
        AStarNode initial = new AStarNode(graph, central, truck);
        initial.setG(0);
        initial.setH(heuristic_cost_estimate(initial, heuristic, typeofWaste));

        // Add it to the open list

        open.add(initial);
        queue.add(initial);
        long startTime = System.currentTimeMillis();
        AStarNode lowF = null;

        // Loop the open list as long as it isn't empty
        while (!open.isEmpty()) {
            // Increment number of visited nodes
            visitedNodes++;

            // Get the node with the lowest f value
            //lowF = lowestF(open);
          lowF = queue.poll();
            //System.out.println(lowF);

            // Check if it is the goal
            if (lowF.hasFinish(typeofWaste)) {
                long stopTime = System.currentTimeMillis();
                long elapsedTime = stopTime - startTime;

                result.add(lowF);
                result.add(elapsedTime);
                result.add(visitedNodes);
                return result;
            }

            // Add it to the closed list and remove it from the open list
            closed.add(lowF);
            open.remove(lowF);

            // Get the adjacent nodes
            ArrayList<AStarNode> adj = getAdjacentNodes(lowF);


            // Check each adjacent node not on the closed list
            for (int i = 0; i < adj.size(); i++) {
                if (!closed.contains(adj.get(i))) {
                    // Set this node's f value
                    adj.get(i).setH(heuristic_cost_estimate(adj.get(i), heuristic, typeofWaste));

                    // Check if it is on the open list
                    if (!open.contains(adj.get(i))) {
                        // Add it if it isn't
                        open.add(adj.get(i));
                        queue.add(adj.get(i));
                    } else {
                        // Get the one on the open list
                        AStarNode temp = open.get(open.indexOf(adj.get(i)));

                        // Check which one has the lowest g value
                        if (adj.get(i).getG() < temp.getG()) {

                            temp.setG(adj.get(i).getG());
                            temp.setParent(adj.get(i).getParent());
                        }
                    }
                }
            }
        }
        return null;
    }

    private double heuristic_cost_estimate(AStarNode aStarNode, String heuristic, String typeofWaste) {
        double h = 0.0;
        // Check the chosen heuristic
        if (heuristic == Utils.HEURISTIC1) {
            if (aStarNode.getNode().getType().equals(Utils.TRUE_GARBAGE))
                h = aStarNode.getGraph().getTotalGarbageByTypeWaste(typeofWaste) - aStarNode.getNode().getGarbageContainerByType(typeofWaste);//getTotalGarbageByTypeWasteWithMinimumLevelInContainers(Utils.PAPER);// - aStarNode.getNode().getGarbageContainerByType(Utils.PAPER);
            else
                h = aStarNode.getGraph().getTotalGarbageByTypeWaste(typeofWaste);//getTotalGarbageByTypeWasteWithMinimumLevelInContainers(Utils.PAPER);
        } else if (heuristic == Utils.HEURISTIC2) {
            return h;
        } else if (heuristic == Utils.HEURISTIC3) {
            System.out.println((aStarNode.getGraph().getGraphContainers().size() - aStarNode.getGraph().getNumberOfEmptyContainers(typeofWaste)) * 10);
            if (aStarNode.getNode().getType().equals(Utils.TRUE_GARBAGE))
                h = (aStarNode.getGraph().getGraphContainers().size() - aStarNode.getGraph().getNumberOfEmptyContainers(typeofWaste)) * 10;
            else h = aStarNode.getGraph().getTotalGarbageByTypeWaste(typeofWaste) * 10;
        }
        return h;
    }

    public void sendSearchToResult() {
        try {
            Result window = new Result(Search.graph, Search.itinerary, distanceCovered);
            window.frmResult.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AStarNode lowestF(ArrayList<AStarNode> open) {
        AStarNode temp = null;

        for (int i = 0; i < open.size(); i++) {
            if (temp == null)
                temp = open.get(i);
            else if ((open.get(i).getH() + open.get(i).getG()) < (temp.getH() + temp.getG()))
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

    Graph getGraph() {
        return Search.graph;
    }
}
