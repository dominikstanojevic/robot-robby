package hr.fer.zemris.projekt.GUI;

import java.awt.Container;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

public class MainFrame extends JFrame {
	

	private static final long serialVersionUID = -2344383069969205119L;
	
	private LearningPanel learningPanel;
	private SimulationPanel simulationPanel;
	
	public MainFrame() throws HeadlessException {
		super();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Robot Robby");
        initGUI();
        setSize(900, 600);
        setLocationRelativeTo(null);
        setVisible(true);
	}

	private void initGUI() {
		
		JTabbedPane tabbedPane = new JTabbedPane();
		add(tabbedPane);

		
		learningPanel = new LearningPanel(tabbedPane);
		//tabbedPane.addTab("Learning Algorithms", learningPanel);
		
		JScrollPane sp = new JScrollPane(learningPanel);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tabbedPane.addTab("Learning Algorithms", sp);

		simulationPanel = new SimulationPanel();
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

