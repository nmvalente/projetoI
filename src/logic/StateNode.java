package logic;

import graph.Edge;
import graph.Node;

public class StateNode {
	private StateNode parent;
	private Edge edge;
	private Node node;
	private Truck truck;
	private boolean gasNeeded;
	private boolean sleepNeeded;
	private double g, h;

	public StateNode(Node node, StateNode parent, Edge edge, Truck truck,
			String useHeuristics, double distRatio, double priceRatio) {
		this.parent = parent;
		this.node = node;
		this.edge = edge;
		this.gasNeeded = false;
		this.sleepNeeded = false;
		this.truck = truck;

		if (this.parent != null) {
			if (toppingUpFuelTank()) {
				// calc before reset... to use in the cost
				/*fuelLiters = this.parent.truck.getTankFuel()
						- ((this.parent.truck.getDrivingRange() * this.parent.truck
								.getTankFuel()) / this.truck.getMaxDrivingRange());
				this.parent.gasNeeded = true;
				this.parent.truck.resetDrivingRange();*/
			}

			/*if (sleepOver()) {
				if (this.parent.getNode().getHotelPrice() > 0.0) {
					this.parent.sleepNeeded = true;
					this.parent.truck.resetDrivenMinutesOnDay();
				}
			}*/

			if (this.edge != null) {/*
				this.truck = new Truck(this.parent.gettruck());
				this.truck.subtractDrivingRange(this.edge.getDistance());
				this.truck.addTotalDrivenMinutes(this.edge.getMinutes());*/
			}
		}

		double dist = 0.0;
		double price = 0.0;
		if (this.edge != null) {
			dist = edge.getDistance();
			//price = edge.getPrice();
		}

		if (this.parent == null) {
			this.g = 0.0;
		} else {
			this.g = this.parent.getPathCost()
					+ ((dist * distRatio) + (price * priceRatio));
			if (this.parent.sleepNeeded) {
				//this.g += (this.parent.getNode().getHotelPrice() * priceRatio);
			}

			if (this.parent.gasNeeded) {
				// System.out.println("litters needed: " + fuelLiters + " XD ");
				//this.g += ((fuelLiters * this.parent.truck.getFuelPrice()) * priceRatio);
			}
		}

		Node goalNode = truck.getDestinyPosition();

		double distanceToGoal;
		double drivingRangeNeeded;
		double currentTankFuelLiters;
		double neededLitersToGoal;

		switch (useHeuristics) {
		case Utils.A_STAR:
			distanceToGoal = Search.straightLineDistance(node.getLatitude(),
					node.getLongitude(), goalNode.getLatitude(),
					goalNode.getLongitude());
			drivingRangeNeeded = distanceToGoal - this.truck.getDistanceCovered();
			currentTankFuelLiters = this.truck.getDistanceCovered();
			if (drivingRangeNeeded < 0) {
				//neededLitersToGoal = 0;
			}
			//this.h = distanceToGoal * distRatio + neededLitersToGoal * priceRatio;
			break;
		case Utils.UNIFORM_COST:
			break;
		default:
			break;
		}
	}

	public StateNode(StateNode stateNode) {
		this.parent = stateNode.parent;
		this.edge = stateNode.edge;
		this.node = stateNode.node;
		this.truck = new Truck(stateNode.getTruck());
		this.gasNeeded = stateNode.gasNeeded;
		this.sleepNeeded = stateNode.sleepNeeded;
		this.g = stateNode.g;
		this.h = stateNode.h;
	}

	public boolean toppingUpFuelTank() {
		if (this.edge == null)
			return false;
		double newDrivingRange = this.truck.getDistanceCovered() - this.edge.getDistance();
		return (newDrivingRange <= 0);
	}

	public StateNode getParent() {
		return parent;
	}

	public void setParent(StateNode parent) {
		this.parent = parent;
	}

	public Node getNode() {
		return this.node;
	}
// (nr camioes + distancia percorrida) / quantidade de lixo
// camiao deve voltar a central?

	public Edge getEdge() {
		return edge;
	}

	public double getEvaluation() {
		return (this.g + this.h);
	}

	public double getPathCost() {
		return this.g;
	}

	public double getCostToGoal() {
		return this.h;
	}

	public int compareEvaluation(StateNode obj) {
		double eval1 = this.getEvaluation();
		double eval2 = obj.getEvaluation();
		if (eval1 > eval2) {
			return 1;
		} else {
			if (eval1 < eval2) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	public int compareCostToReach(StateNode obj) {
		double eval1 = this.getPathCost();
		double eval2 = obj.getPathCost();

		if (eval1 > eval2) {
			return 1;
		} else if (eval1 < eval2) {
			return -1;
		} else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this == obj) {
			return true;
		}

		if (this.getClass() != obj.getClass())
			return false;

		StateNode pqEvalNode = (StateNode) obj;

		boolean match = false;

		// dirty trick
		// public boolean contains(Object o)
		// Returns true if this queue contains the specified element.
		// More formally, returns true if and only if this queue contains
		// at least one element e such that o.equals(e).
		if (this.node.getId() == pqEvalNode.getNode().getId()) {
			if (pqEvalNode.getPathCost() > this.getPathCost()) {
				pqEvalNode.parent = this.parent;
				pqEvalNode.edge = this.edge;
				pqEvalNode.g = this.g;
				pqEvalNode.h = this.h;
				pqEvalNode.node = this.node;
				pqEvalNode.truck = this.truck;
			}
			match = true;
		}

		return match;
	}

	@Override
	public int hashCode() {
		int hash = this.node.getId() * 31;
		return hash;
	}

	@Override
	public String toString() {
		return "id: "
				+ this.node.getId()
				+ "\nparent = "
				+ (this.parent == null ? "null" : (this.parent.getNode()
						.getId() + "")) + "\ng = " + this.g + "\nh = " + this.h;
	}

	/*public boolean isValid() {
		return truck.isDistanceDrivable(edge.getDistance())
				&& truck.isTimeDrivable(edge.getMinutes());
	}
	 */
	public Truck getTruck() {
		return truck;
	}

	public boolean isGasNeeded() {
		return gasNeeded;
	}

	public boolean isSleepNeeded() {
		return sleepNeeded;
	}
}
