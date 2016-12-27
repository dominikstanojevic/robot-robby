package hr.fer.zemris.projekt.GUI;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JCreateMapDialog extends JDialog {

	private static final long serialVersionUID = 3671122922403916985L;

	private int mapSide;

	public JCreateMapDialog() {

		initGUI();
		setTitle("Pick map size");
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
		
		JButton btnOK = new JButton("OK");
		p.add(btnOK);
		
		btnOK.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				mapSide = slMapSize.getValue();
						
				dispose();
			}
		});
		
	}
	
	public int getMapSide(){
		return mapSide;
	}

}
