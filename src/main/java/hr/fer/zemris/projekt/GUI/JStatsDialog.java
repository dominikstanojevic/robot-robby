package hr.fer.zemris.projekt.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import hr.fer.zemris.projekt.algorithms.AlgorithmUtils;
import hr.fer.zemris.projekt.simulator.Stats;

public class JStatsDialog extends JDialog {

	private Stats stats;

	public JStatsDialog(Stats stats) {
		this.stats = stats;

		initGUI();
		setTitle("Game Statistics");
		pack();
	}

	private void initGUI() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		add(p);
		p.setBorder(BorderFactory.createEmptyBorder(20, 20, 5, 20));

		p.add(new JLabel("Number of moves taken: " + stats.getMovesNeeded()));
		int bottlesCollected = stats.getBottlesCollected();
		p.add(new JLabel("Number of bottles collected: " + bottlesCollected + "/"
				+ (stats.getBottlesLeft() + bottlesCollected)));
		p.add(new JLabel("Number of walls hit: " + stats.getWallsHit()));
		p.add(new JLabel("Number of empty pickups: " + stats.getEmptyPickups()));
		
		List<Stats> s = new ArrayList<>();
		s.add(stats);
		
		
		p.add(new JLabel(String.format("Robot fitness: %.2f",AlgorithmUtils.calculateFitness(s))));
		
		JButton btnOK = new JButton("OK");
		p.add(btnOK);
		
		btnOK.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {						
				dispose();
			}
		});

	}

}
