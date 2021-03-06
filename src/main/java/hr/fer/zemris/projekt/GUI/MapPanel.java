package hr.fer.zemris.projekt.GUI;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.grid.Field;
import hr.fer.zemris.projekt.grid.Grid;
import hr.fer.zemris.projekt.grid.IGrid;
import hr.fer.zemris.projekt.observer.observations.RobotActionTaken;

public class MapPanel extends JPanel {
	
	private static final long serialVersionUID = 3040933415189290493L;
	private int columns;
	private int rows;
	private Grid grid;
	private MapField[][] fields;

	public MapPanel() {
		setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
	}
	
	public void setSides(int rows, int columns){
		this.columns = columns;
		this.rows = rows;
		this.grid = null;
		
		removeAll();
		
		setLayout(new GridLayout(rows, columns));
		
		fields = new MapField[rows][columns];
		for(int row=0; row<rows; row++){
			for(int col=0; col<columns; col++){
				
				MapField field = new MapField();
				fields[row][col] = field;
				add(field);
			}
		}
		
		repaint();
		revalidate();
	}
	
	public int getColumns() {
		return columns;
	}

	public int getRows() {
		return rows;
	}

	public void setGrid(Grid grid){
		
		removeAll();
		
		this.grid = grid;
		this.columns = grid.getWidth();
		this.rows = grid.getHeight();
		
		setLayout(new GridLayout(rows, columns));
		fields = new MapField[rows][columns];
		
		for(int row=0; row<rows; row++){
			for(int col=0; col<columns; col++){
				
				MapField field = new MapField(grid.getField(row, col));
				fields[row][col] = field;
				add(field);
			}
		}
		
		repaint();
		revalidate();
		
	}

	public void enableEditing(boolean b) {
		if(fields != null){

			for(int row=0; row<rows; row++){
				for(int col=0; col<columns; col++){
					
					fields[row][col].setEditingEnabled(b);
				}
			}
		}
		
		
	}

	public void generateGrid() {
		Field[][] gridField = new Field[rows][columns];
		
		for(int row=0; row<rows; row++){
			for(int col=0; col<columns; col++){
				
				gridField[row][col] = fields[row][col].getField();
			}
		}
		
		grid = new Grid();
		grid.setGrid(gridField, 0, 0);
	}

	public Grid getGrid() {
		return grid;
	}

	public void simulateAction(RobotActionTaken observation) {
		
		Move move = observation.getMove();
		
		int previousRow = observation.getPreviousRow();
		int previousColumn = observation.getPreviousColumn();
		
		if(move == Move.COLLECT){
			
			fields[previousRow][previousColumn].setPickup(true);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				//Ignore
			}
			if(fields[previousRow][previousColumn].getField() == Field.BOTTLE){
				fields[previousRow][previousColumn].setField(Field.EMPTY);
			}
			fields[previousRow][previousColumn].setPickup(false);
		}		
		
		int currentRow = observation.getCurrentRow();
		int currentColumn = observation.getCurrentColumn();
		
		if(currentRow >= 0 && currentRow < rows && currentColumn >=0 && currentColumn < columns){
			
			fields[previousRow][previousColumn].setCurrent(false);			
			fields[currentRow][currentColumn].setCurrent(true);
			
		}else{
			fields[previousRow][previousColumn].setWallHit(true);
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				//Ignore
			}
			
			fields[previousRow][previousColumn].setWallHit(false);
			
		}
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			//Ignore
		}
		
		
	}
	
	

}
