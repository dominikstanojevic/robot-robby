package hr.fer.zemris.projekt.web.utils;

import hr.fer.zemris.projekt.simulator.Simulator;

public class SimulatorConfiguration {

    private transient Simulator simulator;
    private int maxMoves = 200;

    private int gridHeight = 10;
    private int gridWidth = 10;
    private int numberOfGrids = 200;

    private boolean variableBottles = true;
    private int numberOfBottles = 50;

    private int mapRegenFrequency = 40;

    private boolean hasWalls = false;

    public Simulator getSimulator() {
        if (simulator == null) {
            simulator = new Simulator(maxMoves);
        }

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

    public int getMapRegenFrequency() {
        return mapRegenFrequency;
    }

    public void setMapRegenFrequency(int mapRegexFrequency) {
        this.mapRegenFrequency = mapRegexFrequency;
    }

    public int getMaxMoves() {
        return maxMoves;
    }

    public void setMaxMoves(int maxMoves) {
        this.maxMoves = maxMoves;
        this.simulator = null;
    }
}
