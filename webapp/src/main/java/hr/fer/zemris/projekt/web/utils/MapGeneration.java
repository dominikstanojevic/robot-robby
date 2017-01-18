package hr.fer.zemris.projekt.web.utils;

import hr.fer.zemris.projekt.simulator.Simulator;

import java.util.Random;

public final class MapGeneration {

    private static final Random RANDOM = new Random();

    public static void generateGrids(Simulator simulator, SimulatorConfiguration config) {
        if (config.isVariableBottles()) {
            simulator.generateGrids(
                    config.getNumberOfGrids(),
                    config.getGridWidth(),
                    config.getGridHeight(),
                    false,
                    RANDOM
            );
        } else {
            simulator.generateGrids(
                    config.getNumberOfGrids(),
                    config.getNumberOfBottles(),
                    config.getGridWidth(),
                    config.getGridHeight(),
                    false
            );
        }
    }

    private MapGeneration() {}
}
