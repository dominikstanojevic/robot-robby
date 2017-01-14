package hr.fer.zemris.projekt.GUI;

import hr.fer.zemris.projekt.algorithms.ObservableAlgorithm;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.GeneticProgramming;
import hr.fer.zemris.projekt.observer.Observable;
import hr.fer.zemris.projekt.observer.Observer;
import hr.fer.zemris.projekt.observer.observations.TrainingResult;
import hr.fer.zemris.projekt.simulator.Simulator;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LearningPanel extends JPanel {

    private static final long serialVersionUID = 6104960870300948842L;

    private JTabbedPane parent;

    private ObservableAlgorithm algorithm;
    private Robot robot;
    private Simulator simulator;

    private JButton btnStart = new JButton("Start Algorithm Training");
    private JButton btnPause = new JButton("Pause");
    private JButton btnResume = new JButton("Resume");
    private JButton btnCancel = new JButton("Cancel");
    private JButton btnExportRobot = new JButton("Save Robot to File");
    private JButton btnRunSimulation = new JButton("Simulate Robot");

    private SwingWorker<Void, Integer> worker;

    public LearningPanel(JTabbedPane parent) {
        super();

        this.parent = parent;

        initGUI();

    }

    private void initGUI() {
        setLayout(new BorderLayout());

        JPanel algorithmOptions = new JPanel(new FlowLayout());

        ParametersPanel parameters = new ParametersPanel();
        algorithmOptions.add(parameters, BorderLayout.CENTER);

        // ADD ALL ALGORITHMS
        ObservableAlgorithm[] algoritms = new ObservableAlgorithm[] { new GeneticProgramming() };
        JComboBox<ObservableAlgorithm> cbAlgoritms = new JComboBox<>(algoritms);
        add(cbAlgoritms, BorderLayout.PAGE_START);

        cbAlgoritms.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {

                algorithm = (ObservableAlgorithm) cbAlgoritms.getSelectedItem();
                parameters.setParameters(algorithm.getDefaultParameters());
                btnStart.setEnabled(true);
                btnExportRobot.setEnabled(false);
                btnRunSimulation.setEnabled(false);
            }
        });
        add(algorithmOptions, BorderLayout.LINE_START);

        FitnessChartPanel graphicalPanel = new FitnessChartPanel();
        add(graphicalPanel, BorderLayout.CENTER);

        JPanel mapEditor = new JPanel();
        mapEditor.setLayout(new BoxLayout(mapEditor, BoxLayout.PAGE_AXIS));
        add(mapEditor, BorderLayout.LINE_END);

        JSlider slMapNumber = new JSlider(10, 100, 50);
        slMapNumber.createStandardLabels(50);
        slMapNumber.setMajorTickSpacing(20);
        slMapNumber.setPaintTicks(true);
        slMapNumber.setPaintLabels(true);

        JLabel lMapNumber = new JLabel("Number of maps: " + slMapNumber.getValue());
        slMapNumber.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                lMapNumber.setText("Number of maps: " + slMapNumber.getValue());

            }
        });

        mapEditor.add(lMapNumber);
        mapEditor.add(slMapNumber);

        JSlider slMapRows = new JSlider(2, 15, 10);
        slMapRows.createStandardLabels(5);
        slMapRows.setMajorTickSpacing(5);
        slMapRows.setPaintTicks(true);
        slMapRows.setPaintLabels(true);

        JLabel lMapRows = new JLabel("Number of rows: " + slMapRows.getValue());
        slMapRows.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                lMapRows.setText("Number of rows: " + slMapRows.getValue());

            }
        });
        mapEditor.add(lMapRows);
        mapEditor.add(slMapRows);

        JSlider slMapColumns = new JSlider(2, 15, 10);
        slMapColumns.createStandardLabels(5);
        slMapColumns.setMajorTickSpacing(5);
        slMapColumns.setPaintTicks(true);
        slMapColumns.setPaintLabels(true);

        JLabel lMapColumns = new JLabel("Number of columns: " + slMapColumns.getValue());
        slMapColumns.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                lMapColumns.setText("Number of columns: " + slMapColumns.getValue());

            }
        });
        mapEditor.add(lMapColumns);
        mapEditor.add(slMapColumns);

        JSlider slBottlePercentage = new JSlider(0, 100, 50);
        Hashtable labelTable = new Hashtable();
        labelTable.put(0, new JLabel("0"));
        labelTable.put(50, new JLabel("0.5"));
        labelTable.put(100, new JLabel("1"));
        slBottlePercentage.setLabelTable(labelTable);
        slBottlePercentage.setPaintLabels(true);

        JLabel lBottlePercentage = new JLabel("Percentage of bottles: "
                + slBottlePercentage.getValue() + "%");
        slBottlePercentage.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                lBottlePercentage.setText("Percentage of bottles: " + slBottlePercentage.getValue()
                        + "%");

            }
        });
        mapEditor.add(lBottlePercentage);
        mapEditor.add(slBottlePercentage);

        btnExportRobot.setEnabled(false);
        JLabel lExportResult = new JLabel("");
        btnExportRobot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showDialog(mapEditor, "Save");

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();

                    try {
                        algorithm.writeSolutionToFile(Paths.get(file.getPath()), robot);
                        lExportResult.setText("Robot saved.");
                    } catch (IOException e1) {
                        lExportResult.setText("Failed to save robot.");
                    }
                }
            }
        });
        mapEditor.add(btnExportRobot);
        mapEditor.add(lExportResult);

        btnRunSimulation.setEnabled(false);
        btnRunSimulation.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                parent.setSelectedIndex(1);
                ((SimulationPanel) parent.getSelectedComponent()).setRobot(robot);
            }
        });
        mapEditor.add(btnRunSimulation);

        btnStart.setEnabled(false);
        mapEditor.add(btnStart);

        btnStart.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                btnExportRobot.setEnabled(false);
                btnRunSimulation.setEnabled(false);
                btnPause.setEnabled(true);
                btnCancel.setEnabled(true);
                btnResume.setEnabled(false);
                btnStart.setEnabled(false);
                graphicalPanel.clearGraph();

                int mapNum = slMapNumber.getValue();
                int mapRows = slMapRows.getValue();
                int mapCols = slMapColumns.getValue();
                int numOfBottles = (int) Math.round(slBottlePercentage.getValue() * 0.01 * mapRows
                        * mapCols);

                worker = new SwingWorker<Void, Integer>() {

                    @Override
                    protected Void doInBackground() throws Exception {

                        simulator = new Simulator(2 * mapCols * mapRows);
                        simulator.generateGrids(mapNum, numOfBottles, mapCols, mapRows, false);

                        algorithm.addObserver(new Observer<TrainingResult>() {

                            @Override
                            public void observationMade(Observable sender,
                                    TrainingResult observation) {
                                robot = observation.getBestResult();
                                SwingUtilities.invokeLater(() -> graphicalPanel.addValue(
                                        robot.standardizedFitness(),
                                        observation.getAverageFitness()));
                            }
                        });

                        robot = algorithm.run(simulator, parameters.getParameters());

                        return null;
                    }

                    @Override
                    protected void done() {

                        btnExportRobot.setEnabled(true);
                        btnRunSimulation.setEnabled(true);
                        btnStart.setEnabled(true);
                        btnPause.setEnabled(false);
                        btnResume.setEnabled(false);
                        btnCancel.setEnabled(false);
                    }
                };

                worker.execute();

            }
        });

        btnPause.setEnabled(false);
        mapEditor.add(btnPause);
        btnPause.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                simulator.suspend();
                btnPause.setEnabled(false);
                btnResume.setEnabled(true);

                btnExportRobot.setEnabled(true);
                btnRunSimulation.setEnabled(true);

            }
        });

        btnResume.setEnabled(false);
        mapEditor.add(btnResume);
        btnResume.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                simulator.resume();
                btnResume.setEnabled(false);
                btnPause.setEnabled(true);

                btnExportRobot.setEnabled(false);
                btnRunSimulation.setEnabled(false);

            }
        });

        btnCancel.setEnabled(false);
        mapEditor.add(btnCancel);
        btnCancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                simulator.suspend();
                btnPause.setEnabled(false);
                btnResume.setEnabled(false);
                btnCancel.setEnabled(false);
                btnStart.setEnabled(true);

                btnExportRobot.setEnabled(true);
                btnRunSimulation.setEnabled(true);

            }
        });

    }

}