package hr.fer.zemris.projekt.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import hr.fer.zemris.projekt.grid.Field;

public class MapField extends JComponent {
	
	private static final long serialVersionUID = -8621030149139135400L;
	
	private Field field;

	public MapField(Field field) {
		
		setSize(new Dimension(20, 20));
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		this.field = field;
	}
	
	public MapField(){
		this(Field.EMPTY);
	}

	@Override
	protected void paintComponent(Graphics g) {
		
		if(field == Field.BOTTLE){
			g.drawString("B", 10, 10);
			
		}else if(field == Field.WALL){
			g.drawString("W", 10, 10);
		}
		
	}
	
	
	
	

}
