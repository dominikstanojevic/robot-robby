package hr.fer.zemris.projekt.demo;

import hr.fer.zemris.projekt.algorithms.ObservableAlgorithm;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.algorithms.ga.GeneticAlgorithm;
import hr.fer.zemris.projekt.simulator.AbstractSimulator;
import hr.fer.zemris.projekt.simulator.Simulator;
import hr.fer.zemris.projekt.simulator.Stats;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Demo program for the {@link GeneticAlgorithm}. The algorithm is run
 * with it's default parameters, while the {@link Simulator} is run with
 * 100 10x10 grids, 200 moves, 50 bottles. After the algorithm has run it's
 * course, the resulting {@link Robot}'s actions on a newly generated
 * map are printed onto the standard output.
 *
 * @author Leon Luttenberger
 */
public final class GADemo {

    /** {@link Random} object. */
    private static final Random RANDOM = new Random();

    /** Number of grids to test the robot on. */
    private static final int NUMBER_OF_GRIDS = 100;

    /** Number of moves to test the robot with. */
    private static final int MAX_MOVES = 200;

    /** Width of the grid. */
    private static final int WIDTH = 10;

    /** Height of the grid. */
    private static final int HEIGHT = 10;

    /** Determines if the grid will spawn walls on the inside. */
    private static final boolean HAS_WALLS = false;

    /** Determines after how many generations the demo will print the current best result. */
    private static final int PRINT_FREQUENCY = 10;

    /** Number of maps to test the robot on in the end. */
    private static final int END_NUMBER_OF_MAPS = 10_000;

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
    public static void main(String[] args) throws IOException {
        int mapGenerationFrequency;

        if (args.length == 1) {
            mapGenerationFrequency = Integer.parseInt(args[0]);
        } else {
            mapGenerationFrequency = 0;
        }

        GeneticAlgorithm algorithm = new GeneticAlgorithm();
        AbstractSimulator simulator = new Simulator(MAX_MOVES);
        Robot best = train(algorithm, simulator, mapGenerationFrequency);

        testOnNewMaps(simulator, best, END_NUMBER_OF_MAPS);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Would you like to save robot? y/n");
        String response = scanner.nextLine();

        if (response.toLowerCase().equals("y")) {
            System.out.println("File path: ");
            Path path = Paths.get(scanner.nextLine());

            algorithm.writeSolutionToFile(path, best);
            System.out.println("Robot saved to " + path.toAbsolutePath());
        }
    }

    /**
     * Trains a robot on the specified simulator.
     * @param simulator simulator to train a robot on
     * @param mapGenerationFrequency how often the maps are to be regenerated
     * @return best {@link Robot} from the genetic algorithm
     */
    private static Robot train(ObservableAlgorithm algorithm, AbstractSimulator simulator, int mapGenerationFrequency) {
        simulator.generateGrids(NUMBER_OF_GRIDS, WIDTH, HEIGHT, HAS_WALLS, RANDOM);

        algorithm.addObserver((sender, o) -> {
            int generation = o.getIteration();

            if (generation % PRINT_FREQUENCY == 0) {
                System.out.printf("%4d. iteration best fitness: %5.4f%n", generation, o.getBestResult().standardizedFitness() * 500);
            }

            if (mapGenerationFrequency > 0 && generation % mapGenerationFrequency == 0) {
                simulator.generateGrids(NUMBER_OF_GRIDS, WIDTH, HEIGHT, HAS_WALLS, RANDOM);
            }
        });

        return algorithm.run(simulator);
    }

    private static void testOnNewMaps(AbstractSimulator simulator, Robot robot, int numberOfMaps) {
        simulator.generateGrids(numberOfMaps, WIDTH, HEIGHT, HAS_WALLS, RANDOM);

        List<Stats> stats = simulator.playGames(robot);
        double fitness = GeneticAlgorithm.calculateFitness(stats);

        System.out.println("Fitness: " + fitness);
        System.out.println("Scaled to 500: " + fitness * 500);
    }
}
