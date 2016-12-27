package hr.fer.zemris.projekt.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GenerateMapDialog extends JDialog {
	
	private static final long serialVersionUID = -6527700435432005641L;
	
	private int mapSide;
	private int numOfBottles;

	public GenerateMapDialog(JPanel owner) {
		super();
		
		initGUI();
		setTitle("Pick parameters for the generated map");
		pack();
	}

	private void initGUI() {
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		add(p);
		p.setBorder(BorderFactory.createEmptyBorder(20, 20, 5, 20));
		
		JSlider slMapSize = new JSlider(2, 15, 10);
		slMapSize.createStandardLabels(5);
		slMapSize.setMajorTickSpacing(5);
		slMapSize.setPaintTicks(true);
		slMapSize.setPaintLabels(true);
		
		JLabel lMapSize = new JLabel("Map side size: " + slMapSize.getValue());
		slMapSize.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				lMapSize.setText("Map side size: " + slMapSize.getValue());
				
			}
		});		
		p.add(lMapSize);
		p.add(slMapSize);
		slMapSize.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
		
		JSlider slBottlePercentage = new JSlider(0, 100, 50);
		Hashtable labelTable = new Hashtable();
		labelTable.put(0, new JLabel("0"));
		labelTable.put(50, new JLabel("0.5"));
		labelTable.put(100, new JLabel("1"));
		slBottlePercentage.setLabelTable(labelTable);
		slBottlePercentage.setPaintLabels(true);
		
		JLabel lBottlePercentage = new JLabel("Percentage of bottles: " + slBottlePercentage.getValue() + "%");
		slBottlePercentage.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				lBottlePercentage.setText("Percentage of bottles: " + slBottlePercentage.getValue() + "%");
				
			}
		});
		p.add(lBottlePercentage);
		p.add(slBottlePercentage);
		
		JButton btnOK = new JButton("OK");
		p.add(btnOK);
		
		btnOK.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				mapSide = slMapSize.getValue();
				numOfBottles = (int) Math.round(slBottlePercentage.getValue() * 0.01 * mapSide * mapSide);
						
				dispose();
			}
		});
		
	}
	
	public int getMapSide(){
		return mapSide;
	}
	
	public int getNumberOfBottles(){
		return numOfBottles;
	}
	
	

}
