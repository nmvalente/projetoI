package gui;


import graph.Graph;
import graph.Node;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;

import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Camera;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import graph.Edge;

public class Result extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Graph graph;
	public JFrame frmResult;
	private JTextField coveredDistance;
	private JTextField numberTrucksUsed;
	private double distanceCovered;
	private int numberOfTrucks;
	/*protected int truckPlastic = 1;
	protected int truckPaper = 0;
	protected int truckGlass = 0;
	protected int truckCommon = 0;
	protected int numberOfStations = 1;
	protected int truckCapacity = 1000;
	protected double minimumLevelContainer = 0.8;
	 */
	protected String current;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Result window = new Result(10,2);
					window.frmResult.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}	

	/**
	 * Create the application.
	 */
	public Result(double distanceCovered, int numberOfTrucks) {
		this.distanceCovered = distanceCovered;
		this.numberOfTrucks = numberOfTrucks;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmResult = new JFrame();
		frmResult.setBounds(100, 100, 626, 427);
		frmResult.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmResult.getContentPane().setLayout(null);
		frmResult.setTitle("Result of the algorithm");

		/* Covered Distance */
		coveredDistance = new JTextField();
		coveredDistance.setEditable(false);
		coveredDistance.setText("Distance Covered");
		coveredDistance.setColumns(10);
		coveredDistance.setBounds(42, 109, 156, 22);
		frmResult.getContentPane().add(coveredDistance);

		/* Number of trucks used */

		numberTrucksUsed = new JTextField();
		numberTrucksUsed.setEditable(false);
		numberTrucksUsed.setText("Trucks used");
		numberTrucksUsed.setColumns(10);
		numberTrucksUsed.setBounds(237, 109, 131, 22);
		frmResult.getContentPane().add(numberTrucksUsed);

		JLabel coveredDistanceResult = new JLabel("");
		coveredDistanceResult.setBounds(42, 144, 156, 16);
		coveredDistanceResult.setText(String.valueOf(this.distanceCovered));
		frmResult.getContentPane().add(coveredDistanceResult);

		JLabel numberTrucksUsedResult = new JLabel("");
		numberTrucksUsedResult.setBounds(237, 144, 131, 16);
		numberTrucksUsedResult.setText(String.valueOf(this.numberOfTrucks));
		frmResult.getContentPane().add(numberTrucksUsedResult);


		JButton btnMapViewer = new JButton("Show map");
		btnMapViewer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				frmResult.dispose();
				//displayGraph(graph, "Show Result");
			}});

		btnMapViewer.setBounds(255, 305, 113, 32);
		frmResult.getContentPane().add(btnMapViewer);
	}

	public static void displayGraph(final Graph trip, final String title) {
		Thread display = new Thread() {
			public void run() {
				MultiGraph graph = new MultiGraph(title);

				ArrayList<Node> tempArr = trip.getNodes();
				for (Node node : tempArr) {
					String id = node.getId() + "";
					graph.addNode(id);
					Node nodeTemp = graph.getNode(id);
					//nodeTemp.setAttribute("x", node.getLatitude());
					//nodeTemp.setAttribute("y", node.getLongitude() * -1);
					//nodeTemp.addAttribute("ui.label", "( " + node.getId() + " )" + " " + node.getDescription());

				}

				for (Node node : tempArr) {
					ArrayList<Edge> Edges = node.getOutEdges();
					for (Edge edge : Edges) {
						String idSource = edge.getSource().getId() + "";
						String idDesteny = edge.getDestiny().getId() + "";

						if (graph.getEdge(idDesteny + idSource) == null && graph.getEdge(idSource + idDesteny) == null) {
							graph.addEdge(idSource + idDesteny, idSource, idDesteny);
						}
					}
				}

				Viewer viewer = graph.display();
				View view = viewer.getDefaultView();
				viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);

				Camera camera = view.getCamera();
				camera.setViewRotation(-90);

				// Let the layout work ...
				viewer.disableAutoLayout();
			}
		};
		display.start();
	}

}
