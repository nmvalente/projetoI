package logic;


import graph.Node;

public class Truck {

	private final double capacity; // Kilograms
	private Node startingPosition; // Starting node
	private Node destinyPosition; // Goal node
	private final String type; // paper, plastic, glass, common
	private double distanceCovered; // Kilometers
	private double totalGarbage; // garbage collected
	protected static int current_id = 0;
	protected int id;
	protected AStarNode itinerary = null;
	protected double allWasteSinceStart = 0.0;

	public Truck(double capacity, String type) {
		this.capacity = capacity;
		this.type = new String(type);
		this.distanceCovered = 0.0;
		this.totalGarbage = 0.0;

		++current_id;
		this.id = current_id;
	}

	public Truck(Truck truck) {
		this.capacity = truck.capacity;
		this.startingPosition = truck.startingPosition;
		this.destinyPosition = truck.destinyPosition;
		this.type = truck.type;
		this.distanceCovered = truck.distanceCovered;
		this.totalGarbage = truck.totalGarbage;
		this.id = truck.id;
		this.allWasteSinceStart = truck.allWasteSinceStart;

	}

	public Node getStartingPosition() {
		return startingPosition;
	}

	public double getDistanceCovered() {
		return distanceCovered;
	}

	public void setStartingPosition(Node startPos) {
		this.startingPosition = startPos;
	}

	public Node getDestinyPosition() {
		return destinyPosition;
	}

	public void setDestinyPosition(Node destinyPos) {
		this.destinyPosition = destinyPos;
	}

	public void addToDistanceCovered(double distance) {
		this.distanceCovered += distance;
	}

	public void resetDistanceCovered() {
		this.distanceCovered = 0.0;
	}

	public double getCapacity() {
		return capacity;
	}

	public boolean isFull() {
		return this.totalGarbage > this.capacity;
	}

	public String getType() {
		return type;
	}

	public int getID() {
		return id;
	}

	public void setTotalGarbage(double garbage) {
		this.totalGarbage += garbage;
	}

	public void resetTotalGarbage() {
		this.totalGarbage = 0.0;
	}

	public double getTotalGarbage() {
		return totalGarbage;
	}

	public AStarNode getItinerary(){
		return this.itinerary;
	}

	public void setItinerary(AStarNode node){
		this.itinerary = node;
	}

	@Override
	public String toString() {
		String str = this.getID() + "-" + this.getType() + ": " + this.distanceCovered;// + " " + this.startingPosition.getName() + " -> " + this.destinyPosition.getName();
		return str;
	}

	public void collectWaste(){
		truckCollect(itinerary.getNode());
		AStarNode parent = itinerary.getParent();

		while (parent != null) {
			truckCollect(parent.getNode());
			parent = parent.getParent();
		}
	}

	public double truckCollect(Node node) {
		if(node.getType().equals(Utils.TRUE_GARBAGE)){ // se for contentor de lixo
			double actualPaperToCollected = node.getGarbageContainerByType(this.type);
			if(actualPaperToCollected > 0.0){ // se houver papel por apanhar
				if(this.getType().equals(this.type) && (this.getTotalGarbage()+actualPaperToCollected) <= this.getCapacity()){
					this.setTotalGarbage(actualPaperToCollected);
					this.allWasteSinceStart += actualPaperToCollected;
					node.setGarbageContainer(this.type, actualPaperToCollected); // apanha o papel
					return actualPaperToCollected;
				}
				else if(this.getType().equals(this.type) && (this.getTotalGarbage()+actualPaperToCollected) > this.getCapacity()){
					double currentLoadGarbage = this.getCapacity() - this.getTotalGarbage();
					this.setTotalGarbage(currentLoadGarbage);
					this.allWasteSinceStart += currentLoadGarbage;
					node.setGarbageContainer(this.type, currentLoadGarbage); // apanha o papel
					return currentLoadGarbage;
				}
			}
		}

		else if(node.getType().equals(Utils.STATION) && this.getTotalGarbage() > 0.0){ // se for estacao de tratamento, esvazia
			this.resetTotalGarbage();
		}
		return 0.0;
	}

	public void printItinerary(){
			System.out.println(itinerary);
	}

	public double getTotalGarbageSinceInit() {
		return this.allWasteSinceStart;
	}


}