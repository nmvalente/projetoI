package logic;

import graph.Graph;
import graph.Node;

import java.util.HashMap;

public class AStarNode {

    private Graph graph;
    private double g;
    private double h;
    private Node node;
    private AStarNode parent;
    private Truck truck;

    public AStarNode(Graph graph, Node node, Truck truck) {
        this.graph = new Graph(graph);

        this.truck = new Truck(truck);
        for (Node e : this.graph.getNodes())
            if (e.getId() == node.getId())
                this.node = e;

        this.truck.truckCollect(this.node);
        HashMap<Integer, Node> temp = this.graph.getGraphContainers();
        temp.replace(this.node.getId(), this.node);
        this.graph.setGraphContainers(temp);
    }

    public void setTruck(Truck t) {
        this.truck = t;
    }

    public void setGraph(Graph g) {
        this.graph = g;
    }

    public Graph getGraph() {
        return graph;
    }

    public Node getNode() {
        return this.node;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public int compareTo(AStarNode obj) {
        return Double.compare(this.getG() + this.getH(), obj.getG() + obj.getH());
    }

    public AStarNode getParent() {
        return this.parent;
    }

    public void setParent(AStarNode parent2) {
        this.parent = parent2;

    }

    @Override
    public String toString() {
        String str = this.getNode().getId() + " - G: " + this.getG() + " - H: " + this.getH() + "   - Colected/Total - " + this.getTruck().allWasteSinceStart + "/" + Search.graph.getTotalGarbageByTypeWaste(this.getTruck().getType());
        return str;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        AStarNode aStarNode = (AStarNode) obj;

        if (this.g != aStarNode.g) {
            return false;

        }

        if (this.getTruck().getTotalGarbage() != aStarNode.getTruck().getTotalGarbage())
            return false;

        if (this.getTruck().getTotalGarbageSinceInit() != aStarNode.getTruck().getTotalGarbageSinceInit())
            return false;

        if (this.getGraph().getTotalGarbageByTypeWaste(this.getTruck().getType()) != aStarNode.getGraph().getTotalGarbageByTypeWaste(this.getTruck().getType()))
            return false;

        if(!this.getNode().equals(aStarNode.getNode()))
            return  false;

            /*

			return false;
*/
/*
        System.out.println("G : \n" + this.g + " " + aStarNode.g);
		System.out.println("Total Garbage \n" + this.getTruck().getTotalGarbageSinceInit() + " " + aStarNode.getTruck().getTotalGarbageSinceInit());
		System.out.println("Garbage Since Start : \n" + this.getTruck().getTotalGarbageSinceInit() + " " + aStarNode.getTruck().getTotalGarbageSinceInit());
*/


        return true;
    }

    public boolean hasFinish(String typeofWaste) {
        if (this.getTruck().allWasteSinceStart == Search.graph.getTotalGarbageByTypeWasteWithMinimumLevelInContainers(typeofWaste))
            if (this.getTruck().getTotalGarbage() == 0.0)
                return true;
        return false;
    }

    public Truck getTruck() {
        return truck;
    }
}