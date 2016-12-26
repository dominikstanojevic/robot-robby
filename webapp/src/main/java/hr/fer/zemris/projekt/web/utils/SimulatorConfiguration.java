package hr.fer.zemris.projekt.web.utils;

import hr.fer.zemris.projekt.simulator.Simulator;

public class SimulatorConfiguration {

    private Simulator simulator;

    private int gridHeight = 10;
    private int gridWidth = 10;
    private int numberOfGrids = 200;

    private boolean variableBottles = true;
    private int numberOfBottles = 50;

    private boolean hasWalls = false;

    public SimulatorConfiguration(Simulator simulator) {
        this.simulator = simulator;
    }

    public Simulator getSimulator() {
        return simulator;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public void setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
    }

    public int getNumberOfGrids() {
        return numberOfGrids;
    }

    public void setNumberOfGrids(int numberOfGrids) {
        this.numberOfGrids = numberOfGrids;
    }

    public boolean isVariableBottles() {
        return variableBottles;
    }

    public void setVariableBottles(boolean variableBottles) {
        this.variableBottles = variableBottles;
    }

    public int getNumberOfBottles() {
        return numberOfBottles;
    }

    public void setNumberOfBottles(int numberOfBottles) {
        this.numberOfBottles = numberOfBottles;
    }
}
