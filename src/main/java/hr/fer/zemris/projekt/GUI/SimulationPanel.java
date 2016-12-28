package hr.fer.zemris.projekt.GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.GeneticProgramming;
import hr.fer.zemris.projekt.grid.Grid;
import hr.fer.zemris.projekt.grid.IGrid;
import hr.fer.zemris.projekt.observer.Observable;
import hr.fer.zemris.projekt.observer.Observer;
import hr.fer.zemris.projekt.observer.observations.RobotActionTaken;
import hr.fer.zemris.projekt.simulator.Simulator;

public class SimulationPanel extends JPanel {

	private static final long serialVersionUID = -7933105213494475778L;

	private Robot robot;
	private Simulator simulator = new Simulator();
	private MapPanel map = new MapPanel();
	
	private JButton btnSaveMap;

	public SimulationPanel() {
		super();

		setLayout(new BorderLayout());
		initGUI();
	}

	private void initGUI() {

		JPanel createPanel = new JPanel();
		add(createPanel, BorderLayout.PAGE_START);

		JPanel optionsPanel = new JPanel(new GridLayout(0, 1));
		add(optionsPanel, BorderLayout.LINE_START);
		add(map, BorderLayout.CENTER);

		JButton btnGenerateMap = new JButton("Generate Map");
		optionsPanel.add(btnGenerateMap);

		btnGenerateMap.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				GenerateMapDialog d = new GenerateMapDialog(optionsPanel);
				d.setModal(true);
				d.setVisible(true);

				Grid grid = new Grid();
				grid.generate(d.getMapSide(), d.getMapSide(), d.getNumberOfBottles(), false);
				map.setGrid(grid);

				btnSaveMap.setEnabled(true);
			
			}
		});

		JButton btnCreateMap = new JButton("Create Map");
		optionsPanel.add(btnCreateMap);

		btnCreateMap.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JCreateMapDialog d = new JCreateMapDialog();
				d.setModal(true);
				d.setVisible(true);

				map.setSide(d.getMapSide());
				map.enableEditing(true);
				
				createPanel.add(new JLabel(
						"Press on the map field to add bottle. Whan you're done adding bottles, press the 'Done' button to generate map."));
				JButton btnDone = new JButton("Done");
				createPanel.add(btnDone);
				btnDone.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						
						 map.generateGrid();
						 createPanel.removeAll();
						 btnSaveMap.setEnabled(true);
					}
				});

			}
		});

		JButton btnLoadMap = new JButton("Load Map");
		optionsPanel.add(btnLoadMap);

		btnSaveMap = new JButton("Save Map");
		btnSaveMap.setEnabled(false);
		optionsPanel.add(btnSaveMap);

		optionsPanel.add(new JLabel("Select algorithm for loading robot:"));
		Algorithm[] algorithms = new Algorithm[] { new GeneticProgramming() };
		JComboBox<Algorithm> cbAlgorithm = new JComboBox<>(algorithms);
		cbAlgorithm.setSelectedItem(algorithms[0]);
		optionsPanel.add(cbAlgorithm);

		JButton btnLoadRobot = new JButton("Load Robot");
		optionsPanel.add(btnLoadRobot);
		JLabel lRobotStatus = new JLabel("No Robot Selected.");
		optionsPanel.add(lRobotStatus);

		btnLoadRobot.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Algorithm a = (Algorithm) cbAlgorithm.getSelectedItem();

				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showDialog(optionsPanel, "Load");

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();

					try {
						robot = a.readSolutionFromFile(Paths.get(file.getPath()));
						if (robot != null) {
							lRobotStatus.setText("Robot successfully loaded.");
						} else {
							lRobotStatus.setText("Selected file is not a valid robot for the given algorithm.");
						}

					} catch (IOException e1) {
						lRobotStatus.setText("An error occured while loading robot.");
					}

				}
			}
		});

		JButton btnSimulate = new JButton("Start Simulation");
		optionsPanel.add(btnSimulate);
		JLabel lSimulationStatus = new JLabel("");

		btnSimulate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (robot == null) {
					lSimulationStatus.setText("Unable to start simulation, no robot was given.");

				} else {
					IGrid grid = map.getGrid();

					if (grid == null) {
						lSimulationStatus.setText("Unable to start simulation, no map was given.");
					} else {
						simulator.setGrid(grid);
						simulator.addObserver(new Observer<RobotActionTaken>() {
							
							@Override
							public void observationMade(Observable sender, RobotActionTaken observation) {
								
								map.simulateAction(observation);
								
								map.repaint();
								map.revalidate();
								
							}
						});
						
						SwingWorker<Void, Void> w = new SwingWorker<Void, Void>(){

							@Override
							protected Void doInBackground() throws Exception {
								simulator.playGames(robot);
								return null;
							}
							
						};
						
						w.execute();
						

					}

				}

			}
		});

	}
	

}
