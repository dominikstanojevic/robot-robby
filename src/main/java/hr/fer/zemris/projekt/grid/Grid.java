package hr.fer.zemris.projekt.grid;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Kristijan Vulinovic
 * @version 1.1.0
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
        checkArgument(row, -1, height + 1, "row");
        checkArgument(column, -1, width + 1, "column");

        if (row == 0 || column == 0 || row == height - 1 || column == width - 1){
            return Field.WALL;
        }

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
        if (numberOfBottles > width * height){
            throw new IllegalArgumentException("Number of bottles too big!");
        }
        if (numberOfBottles < 0){
            throw new IllegalArgumentException("Number of bottles has to be an positive integer.");
        }
        if (width < 0){
            throw new IllegalArgumentException("Width must be positive!");
        }
        if (height < 0){
            throw new IllegalArgumentException("Height must be positive!");
        }

        grid = new Field[height][width];

        for (int i = 0; i < height; ++i){
            for (int j = 0; j < width; ++j){
                grid[i][j] = Field.EMPTY;
            }
        }

        this.numberOfBottles = numberOfBottles;

        Random rnd = new Random();
        int bottlesPlaced = 0;

        while (bottlesPlaced < numberOfBottles){
            int row = rnd.nextInt(height);
            int column = rnd.nextInt(width);

            if (grid[row][column].equals(Field.EMPTY)){
                grid[row][column] = Field.BOTTLE;
                bottlesPlaced++;
            }
        }

        if (hasWalls){
            throw new UnsupportedOperationException("The given option is not implemented right now :(");
        }
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
    public void readFromFile(Path filePath) throws IOException {
        if (filePath == null){
            throw new IllegalArgumentException("The given file path is null!");
        }
        if (!Files.exists(filePath)){
            throw new IllegalArgumentException("The given file does not exist!");
        }

        List<String> lines = Files.readAllLines(filePath);
        if (lines.size() < 2){
            throw new IllegalArgumentException("The given file is not valid.");
        }

        parseSize(lines.remove(0));
        parseStart(lines.remove(0));

        parseGrid(lines);

        calculateNumberOfBottles();
    }

    /**
     * Parses the given string in order to get the grid width and height.
     *
     * @param str the string defining the width and height
     */
    private void parseSize(String str){
        str = str.trim();
        String[] numbers = str.split(" ");
        if (numbers.length != 2){
            throw new IllegalArgumentException("The size should define two arguments, the height and width!");
        }

        try {
            this.width = Integer.parseInt(numbers[0]);
            this.height = Integer.parseInt(numbers[1]);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Unable to parse the file!", e);
        }
    }

    /**
     * Parses the given string in order to get the starting position.
     *
     * @param str the string containing the starting position.
     */
    private void parseStart(String str){
        str = str.trim();
        String[] numbers = str.split(" ");
        if (numbers.length != 2){
            throw new IllegalArgumentException("The size should define two arguments, the height and width!");
        }

        try {
            this.startX = Integer.parseInt(numbers[0]);
            this.startY = Integer.parseInt(numbers[1]);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Unable to parse the file!", e);
        }
    }

    /**
     * Parses the given list of strings in order to create a grid represented
     * by the list.
     *
     * @param lines the list defining the grid.
     */
    private void parseGrid(List<String> lines){
        if (lines.size() != height){
            throw new IllegalArgumentException("The given file is not valid!");
        }

        grid = new Field[height][width];
        int row = 0;
        for (String line : lines){
            line = line.trim();
            if (line.length() != width){
                throw new IllegalArgumentException("The given file is not valid!");
            }

            int column = 0;
            for (char ch : line.toCharArray()){
                switch (ch){
                    case '*':
                        grid[row][column] = Field.BOTTLE;
                        break;
                    case '.':
                        grid[row][column] = Field.EMPTY;
                        break;
                    case '#':
                        grid[row][column] = Field.WALL;
                        break;
                    default:
                        throw new IllegalArgumentException("The given file format is invalid.");
                }
                column++;
            }

            row++;
        }
    }

    @Override
    public void writeToFile(Path filePath) throws IOException {
        if (filePath == null){
            throw new IllegalArgumentException("The given file path is null!");
        }
        if (!Files.exists(filePath)){
            Files.createFile(filePath);
        }

        List<String> lines = new ArrayList<>();
        lines.add(width + " " + height);
        lines.add(startX + " " + startY);

        for (int i = 0; i < height; ++i){
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < width; ++j){
                switch (grid[i][j]){
                    case BOTTLE:
                        sb.append("*");
                        break;
                    case EMPTY:
                        sb.append(".");
                        break;
                    case WALL:
                        sb.append("#");
                        break;
                }
            }
            lines.add(sb.toString());
        }

        Files.write(filePath, lines);
    }

    @Override
    public IGrid copy() {
        IGrid newGrid = new Grid();

        Field[][] newField = new Field[height][width];

        for (int i = 0; i < height; ++i){
            for (int j = 0; j < width; ++j){
                newField[i][j] = grid[i][j];
            }
        }

        newGrid.setGrid(newField, startY, startX);
        return newGrid;
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
