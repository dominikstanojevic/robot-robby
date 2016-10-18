package hr.fer.zemris.projekt.grid;

import java.nio.file.Path;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public class Grid implements IGrid {
    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public Field getField(int row, int column) {
        return null;
    }

    @Override
    public void setField(int row, int column, Field newField) {

    }

    @Override
    public int getCurrentRow() {
        return 0;
    }

    @Override
    public int getCurrentColumn() {
        return 0;
    }

    @Override
    public boolean hasBottlesLeft() {
        return false;
    }

    @Override
    public int getNumberOfBottles() {
        return 0;
    }

    @Override
    public void generate(int width, int height, int numberOfBottles, boolean hasWalls) {

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
}
