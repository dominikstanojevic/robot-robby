package hr.fer.zemris.projekt.grid;

import java.nio.file.Path;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public class Grid implements IGrid {
    private Field[][] grid;
    private int width;
    private int height;

    private int startX;
    private int startY;

    private int numberOfBottles;

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Field getField(int row, int column) {
        checkArgument(row, 0, height, "row");
        checkArgument(column, 0, width, "column");

        return grid[row][column];
    }

    @Override
    public void setField(int row, int column, Field newField) {
        checkArgument(row, 0, height, "row");
        checkArgument(column, 0, width, "column");

        if (newField == null){
            throw new IllegalArgumentException("The field is not allowed to be null!");
        }

        if (grid[row][column].equals(Field.BOTTLE) && !newField.equals(Field.BOTTLE)){
            numberOfBottles--;
        } else if(!grid[row][column].equals(Field.BOTTLE) && newField.equals(Field.BOTTLE)){
            numberOfBottles++;
        }
        grid[row][column] = newField;
    }

    @Override
    public int getCurrentRow() {
        return startY;
    }

    @Override
    public int getCurrentColumn() {
        return startX;
    }

    @Override
    public boolean hasBottlesLeft() {
        return getNumberOfBottles() != 0;
    }

    @Override
    public int getNumberOfBottles() {
        return numberOfBottles;
    }

    @Override
    public void generate(int width, int height, int numberOfBottles, boolean hasWalls) {
        grid = new Field[height][width];


    }

    @Override
    public void setGrid(Field[][] gridField, int startRow, int startColumn) {
        if (gridField == null){
            throw new IllegalArgumentException("The grid should not be null!");
        }

        this.grid = gridField;
        this.height = gridField.length;
        this.width = gridField[0].length;

        checkArgument(startRow, 0, height, "starting row");
        checkArgument(startColumn, 0, width, "starting column");

        this.startX = startColumn;
        this.startY = startRow;

        calculateNumberOfBottles();
    }

    @Override
    public void readFromFile(Path filePath) {

    }

    @Override
    public void writeToFile(Path filePath) {

    }

    @Override
    public IGrid copy() {
        return null;
    }

    /**
     * A helper method to check if the given argument is inside the valid range.
     *
     * @param arg the value of the argument to be checked.
     * @param min the minimal value of the argument.
     * @param max the maximal value of the argument.
     * @param name the name of the argument.
     */
    private void checkArgument(int arg, int min, int max, String name){
        if (arg < min || arg >= max){
            throw new IllegalArgumentException("The given " + name + " index is invalid.");
        }
    }

    /**
     * Helper method to calculate the total number of bottles on the grid.
     */
    private void calculateNumberOfBottles(){
        int bottleCount = 0;

        for (int i = 0; i < height; ++i){
            for (int j = 0; j < width; ++j){
                if (grid[i][j].equals(Field.BOTTLE)){
                    bottleCount++;
                }
            }
        }

        this.numberOfBottles = bottleCount;
    }
}
