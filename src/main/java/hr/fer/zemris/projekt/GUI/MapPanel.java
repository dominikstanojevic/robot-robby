package hr.fer.zemris.projekt.GUI;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import hr.fer.zemris.projekt.grid.IGrid;

public class MapPanel extends JPanel {
	
	private static final long serialVersionUID = 3040933415189290493L;
	private int side;
	private IGrid grid;
	private List<MapField> fields;

	public MapPanel() {
		setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
	}

	private void initGUI() {
		
	}
	
	public void setSide(int side){
		this.side = side;
		
		setLayout(new GridLayout(side, side));
	}
	
	public void setGrid(IGrid grid){
		
		removeAll();
		
		this.grid = grid;
		int width = grid.getWidth();
		int height = grid.getHeight();
		
		setLayout(new GridLayout(width, height));
		fields = new ArrayList<>();
		
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				MapField field = new MapField(grid.getField(i, j));
				fields.add(field);
				add(field);
			}
		}
		
		repaint();
		revalidate();
		
	}

}
