package tests;

import graph.Graph;
import graph.Node;
import logic.Truck;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestApp {

	/*@Test
	public void testStraightLineDistance() {
		assertEquals(Search.straightLineDistance(38.898556, -77.037852, 38.897147, -77.043934), 0.549, 0.001);

		assertEquals(Search.straightLineDistance(41.0961655, -8.7248637, 39.4078969, -0.4315509), 728.321, 0.001);
	}*/

	@Test
	public void testTruckIDs() {
		Truck truck1 = new Truck(1000, "paper");
		Truck truck2 = new Truck(1000, "common");
		Truck truck3 = new Truck(1000, "paper");
		Truck truck4 = new Truck(1000, "glass");
		assertEquals(1, truck1.getID());
		assertEquals(2, truck2.getID());
		assertEquals(3, truck3.getID());
		assertEquals(4, truck4.getID());
	}

	@Test
	public void testDistanceBetweenNodes() {
		// Node node1 = new Node
	}

	@Test // go from n1 to n13
	public void testAlgotithmAstar() {

		Node n1 = new Node(1, "Arad", 366);
		Node n2 = new Node(2, "Zerind", 374);
		Node n3 = new Node(3, "Oradea", 380);
		Node n4 = new Node(4, "Sibiu", 253);
		Node n5 = new Node(5, "Fagaras", 178);
		Node n6 = new Node(6, "Rimnicu Vilcea", 193);
		Node n7 = new Node(7, "Pitesti", 98);
		Node n8 = new Node(8, "Timisoara", 329);
		Node n9 = new Node(9, "Lugoj", 244);
		Node n10 = new Node(10, "Mehadia", 241);
		Node n11 = new Node(11, "Drobeta", 242);
		Node n12 = new Node(12, "Craiova", 160);
		Node n13 = new Node(13, "Bucharest", 0);
		Node n14 = new Node(14, "Giurgiu", 77);

		// Arad
		n1.addEdge(n2, 75);
		n1.addEdge(n4, 140);
		n1.addEdge(n8, 118);

		// Zerind
		n2.addEdge(n1, 75);
		n2.addEdge(n3, 71);

		// Oradea
		n3.addEdge(n2, 71);
		n3.addEdge(n4, 151);

		// Sibiu
		n4.addEdge(n1, 140);
		n4.addEdge(n5, 99);
		n4.addEdge(n3, 151);
		n4.addEdge(n6, 80);

		// Fagaras
		n5.addEdge(n4, 99);
		n5.addEdge(n13, 211);

		// Rimnicu Vilcea
		n6.addEdge(n4, 80);
		n6.addEdge(n7, 97);
		n6.addEdge(n12, 146);

		// Pitesti
		n7.addEdge(n6, 97);
		n7.addEdge(n13, 101);
		n7.addEdge(n12, 138);

		// Timisoara
		n8.addEdge(n1, 118);
		n8.addEdge(n9, 111);

		// Lugoj
		n9.addEdge(n8, 111);
		n9.addEdge(n10, 70);

		// Mehadia
		n10.addEdge(n9, 70);
		n10.addEdge(n11, 75);

		// Drobeta
		n11.addEdge(n10, 75);
		n11.addEdge(n12, 120);

		// Craiova
		n12.addEdge(n11, 120);
		n12.addEdge(n6, 146);
		n12.addEdge(n7, 138);

		// Bucharest
		n13.addEdge(n7, 101);
		n13.addEdge(n14, 90);
		n13.addEdge(n5, 211);

		// Giurgiu
		n14.addEdge(n13, 90);

		assertArrayEquals(new int[] { 3, 2 }, new int[] { n1.getOutEdges().size(), n2.getOutEdges().size() });

		Graph graph = new Graph();
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);
		graph.addNode(n4);
		graph.addNode(n5);
		graph.addNode(n6);
		graph.addNode(n7);
		graph.addNode(n8);
		graph.addNode(n9);
		graph.addNode(n10);
		graph.addNode(n11);
		graph.addNode(n12);
		graph.addNode(n13);
		graph.addNode(n14);

		assertEquals(14, graph.getNodes().size());




	}
};