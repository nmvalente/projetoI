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

	public Truck(Node startingPos, Node destinyPos, double capacity, String type) {
		this.capacity = capacity;
		this.startingPosition = new Node(startingPos);
		this.destinyPosition = new Node(destinyPos);
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

	@Override
	public String toString() {
		String str = this.getType() + " " + this.distanceCovered + " " + this.startingPosition + " "
				+ this.destinyPosition;
		return str;

	}
}