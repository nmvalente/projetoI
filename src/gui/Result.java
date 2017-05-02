package gui;

import graph.Edge;
import graph.Graph;
import graph.Node;
import logic.ProgramData;
import java.awt.Toolkit;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JScrollBar;

public class Result extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JFrame frmResult;
	private JLabel statistics;

	protected String current;
	private int screenWidth;
	private int screenHeight;
	private double paper_distanceCovered;
	private double plastic_distanceCovered;
	private double glass_distanceCovered;
	private double common_distanceCovered;
	private List<Node> itineraryPaper;
	private List<Node> itineraryPlastic;
	private List<Node> itineraryGlass;
	private List<Node> itineraryCommon;
	private JScrollPane graphResultPaper;
	private JScrollPane graphResultGlass;
	private JScrollPane graphResultPlastic;
	private JScrollPane graphResultCommon;

	/**
	 * Create the application.
	 * 
	 * @param itinerary
	 * @wbp.parser.constructor
	 */
	public Result(Graph graph, List<Node> itinerary, double distanceCovered) {

		this.paper_distanceCovered = distanceCovered;
		this.plastic_distanceCovered = distanceCovered;
		this.glass_distanceCovered = distanceCovered;
		this.common_distanceCovered = distanceCovered;

		this.itineraryPaper = itinerary;
		this.itineraryPlastic = itinerary;
		this.itineraryGlass = itinerary;
		this.itineraryCommon = itinerary; 
		
		this.screenWidth = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
		this.screenHeight = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight());

		initialize();
	}

	public Result(Graph graph, List<Node> itineraryPaper, double paper_distanceCovered, List<Node> itineraryPlastic, double plastic_distanceCovered, List<Node> itineraryGlass, double glass_distanceCovered, List<Node> itineraryCommon, double common_distanceCovered) {

		this.paper_distanceCovered = paper_distanceCovered;
		this.plastic_distanceCovered = plastic_distanceCovered;
		this.glass_distanceCovered = glass_distanceCovered;
		this.common_distanceCovered = common_distanceCovered;

		this.itineraryPaper = itineraryPaper;
		this.itineraryPlastic = itineraryPlastic;
		this.itineraryGlass = itineraryGlass;
		this.itineraryCommon = itineraryCommon; 

		this.screenWidth = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
		this.screenHeight = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight());

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		/* ALL WINDOWS FRAME */
		frmResult = new JFrame();
		frmResult.setBounds(this.screenWidth/64, this.screenHeight/64, (int)(this.screenWidth* 0.9), (int)(this.screenHeight* 0.9));
		frmResult.setResizable(false);
		frmResult.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmResult.getContentPane().setLayout(null);
		frmResult.setTitle("Result of the algorithm");

		/* TAB */
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(null);
		tabbedPane.setBounds(0, 0, (int)(this.screenWidth* 0.895), (int)(this.screenHeight* 0.87));
		tabbedPane.setFont(new Font("Consolas", Font.PLAIN, 16));

		/* TAB PAPER*/
		JPanel panelPaper = new JPanel(null);
		tabbedPane.addTab("PAPER", null, panelPaper, null);

		// Graph Display
		GraphModel modelPaper = new DefaultGraphModel();

		DefaultGraphCell[] cellsPaper = Graph(this.itineraryPaper);

		JGraph graphPaper = new JGraph(modelPaper);
		graphPaper.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				graphPaper.refresh();
				graphPaper.revalidate();
				graphPaper.repaint();
			}
		});

		// Insert the cells via the cache, so they get selected
		graphPaper.getGraphLayoutCache().insert(cellsPaper);

		graphResultPaper = new JScrollPane(graphPaper);
		graphResultPaper.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		graphResultPaper.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);	        
		graphResultPaper.setBounds(0, 0, (int)(this.screenWidth* 0.89), (int)(this.screenHeight* 0.71));
		panelPaper.add(graphResultPaper);

		statistics = new JLabel();
		statistics.setFont(new Font("Consolas", Font.PLAIN, 14));
		statistics.setText("<html>Paper trash itenerary : <br>" + printItinerary(this.itineraryPaper) + "<br><br>Statistics :" + "<br>Distance Covered - " + String.valueOf(this.paper_distanceCovered) + "</html>");
		statistics.setBounds((int)(this.screenWidth* 0.01), (int)(this.screenHeight* 0.7), (int)(this.screenWidth* 0.5), (int)(this.screenHeight* 0.15));
		panelPaper.add(statistics);

		/* TAB PLASTIC*/
		JPanel panelPlastic = new JPanel(null);
		tabbedPane.addTab("PLASTIC", null, panelPlastic, null);

		// Graph Display
		GraphModel modelPlastic = new DefaultGraphModel();

		DefaultGraphCell[] cellsPlastic = Graph(this.itineraryPlastic);

		JGraph graphPlastic = new JGraph(modelPlastic);
		graphPlastic.addMouseListener(new MouseAdapter() { 
			@Override
			public void mouseReleased(MouseEvent arg0) {
				graphPlastic.refresh();
				graphPlastic.revalidate();
				graphPlastic.repaint();
			}
		});

		// Insert the cells via the cache, so they get selected
		graphPlastic.getGraphLayoutCache().insert(cellsPlastic);

		graphResultPlastic = new JScrollPane(graphPlastic);
		graphResultPlastic.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		graphResultPlastic.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);	        
		graphResultPlastic.setBounds(0, 0, (int)(this.screenWidth* 0.89), (int)(this.screenHeight* 0.71));
		panelPlastic.add(graphResultPlastic);

		statistics = new JLabel();
		statistics.setFont(new Font("Consolas", Font.PLAIN, 14));
		statistics.setText("<html>Plastic trash itenerary : <br>" + printItinerary(this.itineraryPlastic) + "<br><br>Statistics :" + "<br>Distance Covered - " + String.valueOf(this.plastic_distanceCovered) + "</html>");
		statistics.setBounds((int)(this.screenWidth* 0.01), (int)(this.screenHeight* 0.7), (int)(this.screenWidth* 0.5), (int)(this.screenHeight* 0.15));
		panelPlastic.add(statistics);

		/* TAB GLASS*/
		JPanel panelGlass = new JPanel(null);
		tabbedPane.addTab("GLASS", null, panelGlass, null);

		// Graph Display
		GraphModel modelGlass = new DefaultGraphModel();

		DefaultGraphCell[] cellsGlass = Graph(this.itineraryGlass);

		JGraph graphGlass = new JGraph(modelGlass);
		graphGlass.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				graphGlass.refresh();
				graphGlass.revalidate();
				graphGlass.repaint();
			}
		});

		// Insert the cells via the cache, so they get selected
		graphGlass.getGraphLayoutCache().insert(cellsGlass);

		graphResultGlass = new JScrollPane(graphGlass);
		graphResultGlass.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		graphResultGlass.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);	        
		graphResultGlass.setBounds(0, 0, (int)(this.screenWidth* 0.89), (int)(this.screenHeight* 0.71));
		panelGlass.add(graphResultGlass);

		statistics = new JLabel();
		statistics.setFont(new Font("Consolas", Font.PLAIN, 14));
		statistics.setText("<html>Glass trash itenerary : <br>" + printItinerary(this.itineraryGlass) + "<br><br>Statistics :" + "<br>Distance Covered - " + String.valueOf(this.glass_distanceCovered) + "</html>");
		statistics.setBounds((int)(this.screenWidth* 0.01), (int)(this.screenHeight* 0.7), (int)(this.screenWidth* 0.5), (int)(this.screenHeight* 0.15));
		panelGlass.add(statistics);

		/* TAB COMMON*/
		JPanel panelCommon = new JPanel(null);
		tabbedPane.addTab("COMMON", null, panelCommon, null);

		// Graph Display
		GraphModel modelCommon = new DefaultGraphModel();

		DefaultGraphCell[] cellsCommon = Graph(this.itineraryCommon);

		JGraph graphCommon = new JGraph(modelCommon);
		graphCommon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				graphCommon.refresh();
				graphCommon.revalidate();
				graphCommon.repaint();
			}
		});

		// Insert the cells via the cache, so they get selected
		graphCommon.getGraphLayoutCache().insert(cellsCommon);

		graphResultCommon = new JScrollPane(graphCommon);
		graphResultCommon.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		graphResultCommon.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);	        
		graphResultCommon.setBounds(0, 0, (int)(this.screenWidth* 0.89), (int)(this.screenHeight* 0.71));
		panelCommon.add(graphResultCommon);

		statistics = new JLabel();
		statistics.setFont(new Font("Consolas", Font.PLAIN, 14));
		statistics.setText("<html>Common trash itenerary : <br>" + printItinerary(this.itineraryCommon) + "<br><br>Statistics :" + "<br>Distance Covered - " + String.valueOf(this.common_distanceCovered) + "</html>");
		statistics.setBounds((int)(this.screenWidth* 0.01), (int)(this.screenHeight* 0.7), (int)(this.screenWidth* 0.5), (int)(this.screenHeight* 0.15));
		panelCommon.add(statistics);
		frmResult.getContentPane().add(tabbedPane);

	}

	private DefaultGraphCell[] Graph(List<Node> itinerary) {
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
			if (itinerary.contains(node))
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

				if (itinerary.contains(node) && itinerary.contains(target))
					GraphConstants.setLineColor(edge.getAttributes(), Color.BLUE);

				GraphConstants.setEndFill(edge.getAttributes(), true);
				cells[i] = edge;
				i++;
			}
		}
		return cells;
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

	public String printItinerary(List<Node> itinerary) {

		String string = "";
		int i = 1;
		for (Node node : itinerary) {
			if (i == (itinerary.size()))
				string += i + " - " + (node.getName());
			else
				string += i + " - " + (node.getName() + ", ");

			i++;
		}
		return string;

	}
}
