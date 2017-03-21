package graph;

public class Edge {

	private Node source;
	private Node destiny;
	private double distance;
	
	public Edge(Node source, Node destiny, double distance){
		this.setSource(source);
		this.setDestiny(destiny);
		this.distance = distance;
	}

	public Edge(Edge item) {
		this.source = item.getSource();
		this.destiny = item.getDestiny();
		this.distance = item.getDistance();
	}

	public boolean lessDistance(Edge edge){
		if (edge != null)
			return this.distance < edge.distance;
		return false;
	}

	public boolean greaterDistance(Edge edge){
		if (edge != null)
			return this.distance > edge.distance;
		return false;
	}

	public Node getSource(){
		return source;
	}

	public void setSource(Node source){
		this.source = source;
	}

	public Node getDestiny(){
		return destiny;
	}

	public void setDestiny(Node destiny){
		this.destiny = destiny;
	}

	public double getDistance(){
		return distance;
	}

	public void setDistance(double distance){
		this.distance = distance;
	}


	@Override
	public boolean equals(Object obj){

		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (this.getClass() != obj.getClass())
			return false;

		Edge edge = (Edge) obj;

		return this.distance == edge.distance
				&& this.source.equals(edge.source)
				&& this.destiny.equals(edge.destiny);
	}
	
	@Override
	public String toString(){
		String strE = (this.getSource().getName()  + " ---> " + this.getDestiny().getName() + " - " + this.getDistance() + "km");
		return strE;
	}
}