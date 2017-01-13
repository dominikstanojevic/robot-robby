package hr.fer.zemris.projekt.GUI;

import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.algorithms.RobotFormatException;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.GeneticProgramming;
import hr.fer.zemris.projekt.algorithms.reinforcmentlearning.ReinforcmentLearningAlgorithm;
import hr.fer.zemris.projekt.grid.Grid;
import hr.fer.zemris.projekt.observer.Observable;
import hr.fer.zemris.projekt.observer.Observer;
import hr.fer.zemris.projekt.observer.observations.RobotActionTaken;
import hr.fer.zemris.projekt.simulator.Simulator;

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
import javax.swing.SwingWorker;

public class SimulationPanel extends JPanel {

    private static final long serialVersionUID = -7933105213494475778L;

    private Robot robot;
    private Simulator simulator;
    private MapPanel map = new MapPanel();

    private JButton btnSimulate;
    private JButton btnGenerateMap;
    private JButton btnCreateMap;
    private JButton btnLoadMap;
    private JButton btnSaveMap;
    private JButton btnLoadRobot;

    private JLabel lMapStatus = new JLabel("");
    private JLabel lRobotStatus;

    public SimulationPanel() {
        super();

        setLayout(new BorderLayout());
        initGUI();
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    private void initGUI() {

        JPanel createPanel = new JPanel();
        add(createPanel, BorderLayout.PAGE_START);

        JPanel optionsPanel = new JPanel(new GridLayout(0, 1));
        add(optionsPanel, BorderLayout.LINE_START);
        add(map, BorderLayout.CENTER);

        btnGenerateMap = new JButton("Generate Map");
        optionsPanel.add(btnGenerateMap);

        btnGenerateMap.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                GenerateMapDialog d = new GenerateMapDialog(optionsPanel);
                d.setModal(true);
                d.setVisible(true);

                Grid grid = new Grid();
                grid.generate(d.getColumns(), d.getRows(), d.getNumberOfBottles(), false);
                map.setGrid(grid);
                lMapStatus.setText("Map successfully generated.");

                btnSaveMap.setEnabled(true);
                if (robot != null) {
                    btnSimulate.setEnabled(true);
                }

            }
        });

        btnCreateMap = new JButton("Create Map");
        optionsPanel.add(btnCreateMap);

        btnCreateMap.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JCreateMapDialog d = new JCreateMapDialog();
                d.setModal(true);
                d.setVisible(true);

                map.setSides(d.getRows(), d.getColumns());
                map.enableEditing(true);
                lMapStatus.setText("Creating map.");

                disableButtons();

                createPanel
                .add(new JLabel(
                        "Press on the map field to add bottle. Whan you're done adding bottles, press the 'Done' button to generate map."));
                JButton btnDone = new JButton("Done");
                createPanel.add(btnDone);
                btnDone.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        map.generateGrid();
                        createPanel.removeAll();

                        enableButtons();
                        if (robot == null) {
                            btnSimulate.setEnabled(false);
                        }

                        lMapStatus.setText("Map successfully created.");
                    }
                });
            }
        });

        btnLoadMap = new JButton("Load Map");
        optionsPanel.add(btnLoadMap);

        btnLoadMap.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showDialog(optionsPanel, "Load");

                Grid grid = new Grid();

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();

                    try {
                        grid.readFromFile(Paths.get(file.getPath()));
                        map.setGrid(grid);
                        lMapStatus.setText("Map successfully loaded.");
                        btnSaveMap.setEnabled(true);
                        if (robot != null) {
                            btnSimulate.setEnabled(true);
                        }

                    } catch (Exception e1) {
                        lMapStatus.setText("Unable to load map.");
                    }

                }

            }
        });

        btnSaveMap = new JButton("Save Map");
        btnSaveMap.setEnabled(false);
        optionsPanel.add(btnSaveMap);
        optionsPanel.add(lMapStatus);

        btnSaveMap.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                Grid grid = map.getGrid();

                JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showDialog(optionsPanel, "Save");

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();

                    try {
                        grid.writeToFile((Paths.get(file.getPath())));
                        lMapStatus.setText("Map saved.");
                        btnSaveMap.setEnabled(true);

                    } catch (Exception e1) {
                        lMapStatus.setText("Unable to save map.");
                    }
                }

            }
        });

        optionsPanel.add(new JLabel("Select algorithm for loading robot:"));
        Algorithm[] algorithms = new Algorithm[] { new GeneticProgramming(),
                new ReinforcmentLearningAlgorithm() };
        JComboBox<Algorithm> cbAlgorithm = new JComboBox<>(algorithms);
        cbAlgorithm.setSelectedItem(algorithms[0]);
        optionsPanel.add(cbAlgorithm);

        btnLoadRobot = new JButton("Load Robot");
        optionsPanel.add(btnLoadRobot);
        lRobotStatus = new JLabel("No Robot Selected.");
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
                            if (map.getGrid() != null) {
                                btnSimulate.setEnabled(true);
                            }
                        } else {
                            lRobotStatus
                            .setText("Selected file is not a valid robot for the given algorithm.");
                            robot = null;
                            btnSimulate.setEnabled(false);
                        }

                    } catch (IOException e1) {
                        lRobotStatus.setText("An error occured while loading robot.");
                    } catch (RobotFormatException e2) {
                        lRobotStatus
                        .setText("Selected file is not a valid robot for the given algorithm.");
                    }

                }
            }
        });

        btnSimulate = new JButton("Start Simulation");
        btnSimulate.setEnabled(false);
        optionsPanel.add(btnSimulate);
        JLabel lSimulationStatus = new JLabel("");
        optionsPanel.add(lSimulationStatus);

        btnSimulate.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (robot == null) {
                    lSimulationStatus.setText("Unable to start simulation, no robot was given.");

                } else {
                    Grid grid = map.getGrid();

                    if (grid == null) {
                        lSimulationStatus.setText("Unable to start simulation, no map was given.");
                    } else {

                        disableButtons();

                        simulator = new Simulator(map.getRows() * map.getColumns() * 2);
                        simulator.setGrid(grid);
                        simulator.addObserver(new Observer<RobotActionTaken>() {

                            @Override
                            public void observationMade(Observable sender,
                                    RobotActionTaken observation) {

                                map.simulateAction(observation);
                            }
                        });

                        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                            @Override
                            protected Void doInBackground() throws Exception {

                                map.setGrid(grid);
                                simulator.playGames(robot);

                                return null;
                            }

                            @Override
                            protected void done() {
                                enableButtons();

                            }
                        };

                        worker.execute();

                    }

                }

            }

        });

    }

    /**
     * Enables all the buttons in OptionsPanel.
     */
    private void disableButtons() {

        btnCreateMap.setEnabled(false);
        btnGenerateMap.setEnabled(false);
        btnLoadRobot.setEnabled(false);
        btnLoadMap.setEnabled(false);
        btnSaveMap.setEnabled(false);
        btnSimulate.setEnabled(false);

    }

    /**
     * Enables all the buttons in OptionsPanel.
     */
    private void enableButtons() {

        btnCreateMap.setEnabled(true);
        btnGenerateMap.setEnabled(true);
        btnLoadRobot.setEnabled(true);
        btnLoadMap.setEnabled(true);
        btnSaveMap.setEnabled(true);
        btnSimulate.setEnabled(true);
    }
}
