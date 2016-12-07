package hr.fer.zemris.projekt.algorithms.ga;

import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.grid.Grid;
import hr.fer.zemris.projekt.observer.Observable;
import hr.fer.zemris.projekt.observer.Observer;
import hr.fer.zemris.projekt.observer.observations.TrainingResult;
import hr.fer.zemris.projekt.simulator.AbstractSimulator;
import hr.fer.zemris.projekt.simulator.MultithreadedSimulator;
import hr.fer.zemris.projekt.simulator.Simulator;
import hr.fer.zemris.projekt.simulator.Stats;

public final class GADemo {

    /** Number of grids to test the robot on. */
    private static final int NUMBER_OF_GRIDS = 100;

    /** Number of moves to test the robot with. */
    private static final int MAX_MOVES = 200;

    /** Number of bottles to spawn on the grid. */
    private static final int NUMBER_OF_BOTTLES = 50;

    /** Width of the grid. */
    private static final int WIDTH = 10;

    /** Height of the grid. */
    private static final int HEIGHT = 10;

    /** Determines if the grid will spawn walls on the inside. */
    private static final boolean HAS_WALLS = false;

    /** Determines after how many generations the demo will print the current best result. */
    private static final int PRINT_FREQUENCY = 10;

    /**
     * Entry point for the program.
     *
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

        Robot best = train(simulator, mapGenerationFrequency);

        simulator = new Simulator(MAX_MOVES);
        simulator.addObserver((sender, observation) -> {
            switch (observation.getMove()) {
                    case UP:
                        System.out.println(
                                "Moved up to: " + observation.getCurrentRow()
                                        + " " + observation.getCurrentColumn()
                        );
                        break;
                    case DOWN:
                        System.out.println(
                                "Moved down to: " + observation.getCurrentRow()
                                        + " " + observation.getCurrentColumn()
                        );
                        break;
                    case LEFT:
                        System.out.println(
                                "Moved left to: " + observation.getCurrentRow()
                                        + " " + observation.getCurrentColumn()
                        );
                        break;
                    case RIGHT:
                        System.out.println(
                                "Moved right to: " + observation.getCurrentRow()
                                        + " " + observation.getCurrentColumn()
                        );
                        break;
                    case RANDOM:
                        System.out.println(
                                "Random move to: " + observation.getCurrentRow()
                                        + " " + observation.getCurrentColumn()
                        );
                        break;
                    case SKIP_TURN:
                        System.out.println("Skipped turn");
                        break;
                    case COLLECT:
                        System.out.println("Collected bottle at: " + observation.getCurrentColumn() + observation.getCurrentRow());
                        break;
            }
        });
        Grid grid = new Grid();
        grid.generate(WIDTH, HEIGHT, NUMBER_OF_BOTTLES, HAS_WALLS);

//        System.out.println(grid.toString());

        simulator.setGrid(grid);
        Stats stats = simulator.playGames(best).get(0);

        System.out.println("Bottles collected: " + stats.getBottlesCollected());
        System.out.println("Empty pickups: " + stats.getEmptyPickups());
        System.out.println("Walls hit: " + stats.getWallsHit());
    }

    /**
     * Trains a robot on the specified simulator.
     * @param simulator simulator to train a robot on
     * @param mapGenerationFrequency how often the maps are to be regenerated
     * @return best {@link Robot} from the genetic algorithm
     */
    private static Robot train(AbstractSimulator simulator, int mapGenerationFrequency) {
        simulator.generateGrids(NUMBER_OF_GRIDS, NUMBER_OF_BOTTLES, WIDTH, HEIGHT, HAS_WALLS);

        GeneticAlgorithm algorithm = new GeneticAlgorithm();

        algorithm.addObserver(new Observer<TrainingResult>() {

            private int generation = 0;
            private boolean regenMaps = mapGenerationFrequency > 0;

            @Override
            public void observationMade(Observable sender, TrainingResult o) {
                generation++;

                if (generation % PRINT_FREQUENCY == 0) {
                    System.out.printf("%4d. iteration best fitness: %5.4f%n", generation, o.getValue());
                }

                if (regenMaps && generation % mapGenerationFrequency == 0) {
                    simulator.generateGrids(NUMBER_OF_GRIDS, NUMBER_OF_BOTTLES, WIDTH, HEIGHT, HAS_WALLS);
                }
            }
        });

        return algorithm.run(simulator);
    }
}
