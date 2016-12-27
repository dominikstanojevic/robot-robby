package hr.fer.zemris.projekt.GUI;

import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

public class MainFrame extends JFrame {
	
	public MainFrame() throws HeadlessException {
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Robot Robby");
        initGUI();
        setSize(900, 600);
        setLocationRelativeTo(null);
        setVisible(true);
	}

	private void initGUI() {
		
		JTabbedPane tabbedPane = new JTabbedPane();
		add(tabbedPane);
		
		JPanel learningPanel = new LearningPanel();
		tabbedPane.addTab("Learning Algorithms", learningPanel);
		
		JPanel simulationPanel = new SimulationPanel();
		tabbedPane.addTab("Simulation", simulationPanel);
		
	}

	public static void main(String[] args){
		 
        try {
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException ignorable) {
        }
        
		SwingUtilities.invokeLater(MainFrame::new);
		
	}

}

