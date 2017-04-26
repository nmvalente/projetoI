package tests;

import static org.junit.Assert.*;
import logic.*;
import org.junit.Test;

public class TestApp {

	@Test
	public void testStraightLineDistance() {
		assertEquals(Search.straightLineDistance(38.898556, -77.037852,
				38.897147, -77.043934), 0.549, 0.001);

		assertEquals(Search.straightLineDistance(41.0961655, -8.7248637,
				39.4078969, -0.4315509), 728.321, 0.001);
	}
	
	@Test
	public void testTruckIDs() {
		Truck truck1 = new Truck(null, null, 1000, "paper");
		Truck truck2 = new Truck(null, null, 1000, "common");
		Truck truck3 = new Truck(null, null, 1000, "paper");
		Truck truck4 = new Truck(null, null, 1000, "glass");
		assertEquals(1, truck1.getID());
		assertEquals(2, truck2.getID());
		assertEquals(3, truck3.getID());
		assertEquals(4, truck4.getID());
	}
	
	@Test
	public void testDistanceBetweenNodes() {
		//Node node1 = new Node
	}
	
}
