package gui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import logic.ProgramData;
import logic.Utils;

import javax.swing.JSpinner;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JSeparator;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class InformationsRequest {

	private JFrame frame;
	private JTextField txtHowManyTrucks;
	private JTextField truckPlastic1;
	private JTextField truckGlass1;
	private JTextField truckCommon1;
	private JTextField stationsNumber;
	private JTextField garbageTruckCapacity;
	private JTextField containerMinimum;
	private SpinnerModel spinnerModel;
	private String heuristic;

	protected int truckPlastic = 1;
	protected int truckPaper = 0;
	protected int truckGlass = 0;
	protected int truckCommon = 0;
	protected int numberOfStations = 1;
	protected int truckCapacity = 1000;
	protected double minimumLevelContainer = 0.8;

	protected String current;



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InformationsRequest window = new InformationsRequest();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public InformationsRequest() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 626, 427);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Waste collection");

		/* Paper */
		txtHowManyTrucks = new JTextField();
		txtHowManyTrucks.setEditable(false);
		txtHowManyTrucks.setText("Truck paper?");
		txtHowManyTrucks.setBounds(403, 197, 116, 22);
		frame.getContentPane().add(txtHowManyTrucks);
		txtHowManyTrucks.setColumns(10);

		spinnerModel = new SpinnerNumberModel(truckPaper, //initial value
				0, //min
				10, //max
				1);//step
		JSpinner truckPaperSpinner = new JSpinner(spinnerModel);

		truckPaperSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				String current = ((JSpinner) e.getSource()).getValue().toString();
				truckPaper = Integer.parseInt(current);
			}
		});
		truckPaperSpinner.setBounds(403, 222, 116, 22);
		frame.getContentPane().add(truckPaperSpinner);


		/* Glass */
		truckGlass1 = new JTextField();
		truckGlass1.setEditable(false);
		truckGlass1.setText("Truck glass?");
		truckGlass1.setColumns(10);
		truckGlass1.setBounds(403, 119, 116, 22);
		frame.getContentPane().add(truckGlass1);

		spinnerModel = new SpinnerNumberModel(truckGlass, //initial value
				0, //min
				10, //max
				1);//step
		JSpinner truckGlassSpinner = new JSpinner(spinnerModel);
		truckGlassSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				String current = ((JSpinner) e.getSource()).getValue().toString();
				truckGlass = Integer.parseInt(current);
			}
		});
		truckGlassSpinner.setBounds(403, 147, 116, 22);
		frame.getContentPane().add(truckGlassSpinner);

		/* Common */
		truckCommon1 = new JTextField();
		truckCommon1.setEditable(false);
		truckCommon1.setText("Truck common?");
		truckCommon1.setColumns(10);
		truckCommon1.setBounds(106, 197, 116, 22);
		frame.getContentPane().add(truckCommon1);

		spinnerModel = new SpinnerNumberModel(truckCommon, //initial value
				0, //min
				10, //max
				1);//step
		JSpinner truckCommonSpinner = new JSpinner(spinnerModel);
		truckCommonSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				String current = ((JSpinner) e.getSource()).getValue().toString();
				truckCommon = Integer.parseInt(current);
			}
		});
		truckCommonSpinner.setBounds(106, 222, 116, 22);
		frame.getContentPane().add(truckCommonSpinner);

		/* Plastic */
		truckPlastic1 = new JTextField();
		truckPlastic1.setEditable(false);
		truckPlastic1.setText("Truck plastic?");
		truckPlastic1.setColumns(10);
		truckPlastic1.setBounds(106, 119, 116, 22);
		frame.getContentPane().add(truckPlastic1);

		spinnerModel = new SpinnerNumberModel(truckPlastic, //initial value
				0, //min
				10, //max
				1);//step
		JSpinner truckPlasticSpinner = new JSpinner(spinnerModel);
		truckPlasticSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				String current = ((JSpinner) e.getSource()).getValue().toString();
				truckPlastic = Integer.parseInt(current);

			}
		});
		truckPlasticSpinner.setBounds(106, 147, 116, 22);
		frame.getContentPane().add(truckPlasticSpinner);

		/* Number of stations */
		stationsNumber = new JTextField();
		stationsNumber.setEditable(false);
		stationsNumber.setText("How many stations?");
		stationsNumber.setColumns(10);
		stationsNumber.setBounds(419, 13, 137, 22);
		frame.getContentPane().add(stationsNumber);

		spinnerModel = new SpinnerNumberModel(numberOfStations, //initial value
				1, //min
				3, //max
				1);//step
		JSpinner stationsNumberSpinnner = new JSpinner(spinnerModel);
		stationsNumberSpinnner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				String current = ((JSpinner) e.getSource()).getValue().toString();
				numberOfStations = Integer.parseInt(current);
			}
		});
		stationsNumberSpinnner.setBounds(419, 36, 137, 22);
		frame.getContentPane().add(stationsNumberSpinnner);

		/* Truck capacity */
		garbageTruckCapacity = new JTextField();
		garbageTruckCapacity.setEditable(false);
		garbageTruckCapacity.setText("Garbage truck capacity?");
		garbageTruckCapacity.setColumns(10);
		garbageTruckCapacity.setBounds(44, 13, 156, 22);
		frame.getContentPane().add(garbageTruckCapacity);

		JSpinner garbageTruckCapacitySpinner = new JSpinner();
		spinnerModel = new SpinnerNumberModel(truckCapacity, //initial value
				0, //min
				3000, //max
				100);//step
		garbageTruckCapacitySpinner = new JSpinner(spinnerModel);
		garbageTruckCapacitySpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				String current = ((JSpinner) e.getSource()).getValue().toString();
				truckCapacity = Integer.parseInt(current);
			}
		});
		garbageTruckCapacitySpinner.setBounds(44, 36, 156, 22);
		frame.getContentPane().add(garbageTruckCapacitySpinner);

		/* Container minimum level */

		containerMinimum = new JTextField();
		containerMinimum.setEditable(false);
		containerMinimum.setText("Container minimum?");
		containerMinimum.setColumns(10);
		containerMinimum.setBounds(239, 13, 131, 22);
		frame.getContentPane().add(containerMinimum);

		spinnerModel = new SpinnerNumberModel(minimumLevelContainer, //initial value
				0, //min
				1, //max
				0.1);//step
		JSpinner containerMinimumSpinner = new JSpinner(spinnerModel);
		containerMinimumSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				String current = ((JSpinner) e.getSource()).getValue().toString();
				minimumLevelContainer = Double.parseDouble(current);
			}
		});
		containerMinimumSpinner.setBounds(239, 36, 131, 22);
		frame.getContentPane().add(containerMinimumSpinner);

		JSeparator separator = new JSeparator();
		separator.setBounds(12, 87, 584, 2);
		frame.getContentPane().add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(12, 280, 584, 2);
		frame.getContentPane().add(separator_1);

		DefaultListModel<String> listModel = new DefaultListModel<String>();	
		listModel.addElement(Utils.A_STAR);
		listModel.addElement(Utils.UNIFORM_COST);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(178, 295, 280, 47);
		frame.getContentPane().add(scrollPane);

		JList<String> list = new JList<String>(listModel);
		scrollPane.setViewportView(list);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		heuristic = list.getSelectedValue();
		list.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				heuristic = list.getSelectedValue();
			}
		});
		list.setVisibleRowCount(2);


		/* Button submit */
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				frame.dispose();
				new ProgramData(truckCapacity, numberOfStations, minimumLevelContainer, truckPlastic, truckPaper, truckGlass, truckCommon, heuristic);
			}
		});
		btnSubmit.setBounds(257, 355, 97, 25);
		frame.getContentPane().add(btnSubmit);
	}
}
