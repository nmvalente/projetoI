package gui;

import graph.Edge;
import graph.Graph;
import graph.Node;
import logic.ProgramData;
import logic.Search;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import org.jgraph.JGraph;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.List;
import javax.swing.BorderFactory;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.beans.VetoableChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Result extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JFrame frmResult;
	private JLabel statistics;
	private double distanceCovered;
	private int numberOfTrucks;
	private JScrollPane graphResult;
	/*
	 * protected int truckPlastic = 1; protected int truckPaper = 0; protected
	 * int truckGlass = 0; protected int truckCommon = 0; protected int
	 * numberOfStations = 1; protected int truckCapacity = 1000; protected
	 * double minimumLevelContainer = 0.8;
	 */
	protected String current;
	private List<Node> itinerary;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {

			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @param itinerary
	 */
	public Result(Graph graph, List<Node> itinerary, double distanceCovered, int numberOfTrucks) {
		this.distanceCovered = distanceCovered;
		this.numberOfTrucks = numberOfTrucks;
		this.itinerary = itinerary;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frmResult = new JFrame();
		frmResult.setBounds(100, 100, 800, 800);
		frmResult.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmResult.getContentPane().setLayout(null);
		frmResult.setTitle("Result of the algorithm");

		// Graph Display
		GraphModel model = new DefaultGraphModel();
		JGraph graphdisplay = new JGraph(model);
		graphdisplay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				graphdisplay.refresh();
				graphdisplay.revalidate();
				graphdisplay.repaint();
			}
		});

		// Count elements
		int counter = 0;
		for (Node node : ProgramData.graph.getNodes()) {
			counter++;
			counter += node.getOutEdges().size();
		}

		// Insert all three cells in one call, so we need an array to store them
		DefaultGraphCell[] cells = new DefaultGraphCell[counter];

		// Nodes and edges
		int i = 0, x = 20, j = 0, k = 0, y = 20;

		for (Node node : ProgramData.graph.getNodes()) {
			if (this.itinerary.contains(node))
				cells[i] = createVertex(node.getName(), x, y, 150, 40, Color.BLUE, false);
			else
				cells[i] = createVertex(node.getName(), x, y, 150, 40, null, false);

			x += 250;
			if (i % 2.0 == 0.0 && i != 0) {
				y += 150;
				x = 180;
			}
			i++;
		}

		for (Node node : ProgramData.graph.getNodes()) {
			k++;
			for (Edge edgeo : node.getOutEdges()) {
				DefaultEdge edge = new DefaultEdge(edgeo.getDistance());

				edge.setSource(cells[k - 1]);

				j = 0;
				Node target = null;
				for (Node nodeaux : ProgramData.graph.getNodes()) {
					if (nodeaux.equals(edgeo.getDestiny())) {
						edge.setTarget(cells[j]);
						target = nodeaux;
					}
					j++;
				}

				if (this.itinerary.contains(node) && this.itinerary.contains(target))
					GraphConstants.setLineColor(edge.getAttributes(), Color.BLUE);

				GraphConstants.setEndFill(edge.getAttributes(), true);
				cells[i] = edge;
				i++;
			}
		}

		// Insert the cells via the cache, so they get selected
		graphdisplay.getGraphLayoutCache().insert(cells);

		// Show in Frame
		// JFrame frame = new JFrame();
		graphResult = new JScrollPane(graphdisplay);
		graphResult.setBounds(20, 20, frmResult.getWidth() - 60, 600);
		frmResult.getContentPane().add(graphResult);
		graphResult.revalidate();

		// Statistics display

		/* Itinerary */
		statistics = new JLabel();
		statistics.setText("Itenerary : ");
		statistics.setBounds(20, 620, 156, 22);
		frmResult.getContentPane().add(statistics);

		statistics = new JLabel();
		statistics.setForeground(Color.blue);
		statistics.setText(printItinerary());
		statistics.setBounds(42, 635, frmResult.getWidth() - 60, 22);
		frmResult.getContentPane().add(statistics);

		/* Covered Distance */
		statistics = new JLabel();
		statistics.setText("Statistics :");
		statistics.setBounds(20, 655, 156, 22);
		frmResult.getContentPane().add(statistics);

		/* Covered Distance */
		statistics = new JLabel();
		statistics.setForeground(Color.blue);
		statistics.setText("Distance Covered : " + String.valueOf(this.distanceCovered));
		statistics.setBounds(42, 675, 156, 22);
		frmResult.getContentPane().add(statistics);

		/* Number of trucks used */

		statistics = new JLabel();
		statistics.setForeground(Color.blue);
		statistics.setText("Trucks used : " + String.valueOf(this.numberOfTrucks));
		statistics.setBounds(42, 690, 131, 22);
		frmResult.getContentPane().add(statistics);

	}

	public static DefaultGraphCell createVertex(String name, double x, double y, double w, double h, Color bg,
			boolean raised) {

		// Create vertex with the given name
		DefaultGraphCell cell = new DefaultGraphCell(name);

		// Set bounds
		GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double(x, y, w, h));

		// Set fill color
		if (bg != null) {
			GraphConstants.setGradientColor(cell.getAttributes(), bg);
			GraphConstants.setOpaque(cell.getAttributes(), true);
		}

		// Set raised border
		if (raised) {
			GraphConstants.setBorder(cell.getAttributes(), BorderFactory.createRaisedBevelBorder());
		} else // Set black border
		{
			GraphConstants.setBorderColor(cell.getAttributes(), Color.black);
		}
		// Add a Floating Port
		cell.addPort();

		return cell;
	}

	public String printItinerary() {

		String itinerary = "";
		int i = 1;
		for (Node node : this.itinerary) {
			if (i == (this.itinerary.size()))
				itinerary += i + " - " + (node.getName());
			else
				itinerary += i + " - " + (node.getName() + ", ");

			i++;
		}
		return itinerary;

	}
}
