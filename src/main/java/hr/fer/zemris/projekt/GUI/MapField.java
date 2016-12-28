package hr.fer.zemris.projekt.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import hr.fer.zemris.projekt.grid.Field;

public class MapField extends JComponent {
	
	private static final long serialVersionUID = -8621030149139135400L;
	
	private Field field;
	private boolean editingEnabled = false;
	private boolean isCurrent = false;

	public MapField(Field field) {
		
		setSize(new Dimension(20, 20));
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		this.field = field;
		
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(editingEnabled){
					toggleFieldValue();
				}
				
			}
			
		});
	}
	
	public MapField(){
		this(Field.EMPTY);
	}
	
	public void setField(Field field) {
		this.field = field;
		
		repaint();
		revalidate();
	}

	public Field getField() {
		return field;
	}

	public boolean isCurrent() {
		return isCurrent;
	}

	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
		
		repaint();
		revalidate();
	}

	private void toggleFieldValue() {
		
		if(field == Field.BOTTLE) field = Field.EMPTY;
		else if(field == Field.EMPTY) field = Field.BOTTLE;
		
		repaint();
		revalidate();
		
	}

	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		if(isCurrent){
			g.fillOval(getWidth()/2,  getHeight()/2, 20, 20);
		}
		
		if(field == Field.BOTTLE){
			g.drawString("B", getWidth()/2, getHeight()/2);
			
		}else if(field == Field.WALL){
			g.drawString("W", getWidth()/2, getHeight()/2);
		}
		
	}

	public void setEditingEnabled(boolean b) {
		
		this.editingEnabled = b;
		
	}
	
	
	
	
	
	

}
