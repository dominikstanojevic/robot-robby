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
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.GeneticProgramming;
import hr.fer.zemris.projekt.grid.Grid;
import hr.fer.zemris.projekt.simulator.Simulator;

public class SimulationPanel extends JPanel {

	private static final long serialVersionUID = -7933105213494475778L;
	
	private Robot robot;
	private Simulator simulator = new Simulator();
	private MapPanel map = new MapPanel();

	public SimulationPanel() {
		super();
		
		setLayout(new BorderLayout());
		initGUI();
	}

	private void initGUI() {
		
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
				
			}
		});
		
		JButton btnCreateMap = new JButton("Create Map");
		optionsPanel.add(btnCreateMap);
		
		JButton btnLoadMap = new JButton("Load Map");
		optionsPanel.add(btnLoadMap);
		
		JButton btnSaveMap = new JButton("Save Map");
		btnSaveMap.setEnabled(false);
		optionsPanel.add(btnSaveMap);	
		
		optionsPanel.add(new JLabel("Select algorithm for loading robot:"));
		Algorithm[] algorithms = new Algorithm[]{new GeneticProgramming()};
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
				
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					
					try {
						robot = a.readSolutionFromFile(Paths.get(file.getPath()));
						if(robot != null){
							lRobotStatus.setText("Robot successfully loaded.");
						}else{
							lRobotStatus.setText("Selected file is not a valid robot for the given algorithm.");
						}
						
					} catch (IOException e1) {
						lRobotStatus.setText("An error occured while loading robot.");
					}
					
					
				}
			}
		});
		
		
	}
	
}
