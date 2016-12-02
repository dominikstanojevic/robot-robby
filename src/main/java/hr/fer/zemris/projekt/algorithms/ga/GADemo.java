package hr.fer.zemris.projekt.algorithms.ga;

import hr.fer.zemris.projekt.observer.Observable;
import hr.fer.zemris.projekt.observer.Observer;
import hr.fer.zemris.projekt.observer.observations.TrainingResult;
import hr.fer.zemris.projekt.simulator.AbstractSimulator;
import hr.fer.zemris.projekt.simulator.MultithreadedSimulator;

public final class GADemo {

    private static final int NUMBER_OF_GRIDS = 100;
    private static final int NUMBER_OF_BOTTLES = 50;
    private static final int MAX_MOVES = 200;

    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;
    private static final boolean HAS_WALLS = false;

    private static final int PRINT_FREQUENCY = 10;

    /**
     * Entry point for the program.
     * @param args command line arguments:
     *             <ul>
     *             <li>{@code map generation frequency} - indicates after how many generations
     *             the maps are to be generated again. If this value is left out, the maps
     *             will be static.</li>
     *             </ul>
     */
    public static void main(String[] args) {
        int mapGenerationFrequency;

        if (args.length == 1) {
            mapGenerationFrequency = Integer.parseInt(args[0]);
        } else {
            mapGenerationFrequency = 0;
        }

        AbstractSimulator simulator = new MultithreadedSimulator(MAX_MOVES);
        simulator.generateGrids(NUMBER_OF_GRIDS, NUMBER_OF_BOTTLES, WIDTH, HEIGHT, HAS_WALLS);

        GeneticAlgorithm algorithm = new GeneticAlgorithm();

        algorithm.addObserver(new Observer<TrainingResult>() {

            private int generation = 0;
            private boolean regenMaps = mapGenerationFrequency > 0;

            @Override
            public void observationMade(Observable sender, TrainingResult o) {
                generation++;

                if (generation % PRINT_FREQUENCY == 0) {
                    System.out.printf("%4d. iteration best fitness: %5.2f%n", generation, o.getValue());
                }

                if (regenMaps && generation % mapGenerationFrequency == 0) {
                    simulator.generateGrids(NUMBER_OF_GRIDS, NUMBER_OF_BOTTLES, WIDTH, HEIGHT, HAS_WALLS);
                }
            }
        });

        algorithm.run(simulator);
    }
}
