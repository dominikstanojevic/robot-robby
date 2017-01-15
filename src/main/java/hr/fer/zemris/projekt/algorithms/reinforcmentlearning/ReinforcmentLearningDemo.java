package hr.fer.zemris.projekt.algorithms.reinforcmentlearning;

import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.simulator.Simulator;
import hr.fer.zemris.projekt.simulator.Stats;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * Method demonstrates function of reinforcment learning algorithm.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 15.1.2017.
 */
public class ReinforcmentLearningDemo {

    public static void main(String[] args) throws IOException {

        final int numberOfGrids = 3000;
        final int numberOfBottles = 50;
        final int width = 10;
        final int height = 10;
        final boolean hasWalls = false;

        Simulator sim = new Simulator();
        sim.generateGrids(numberOfGrids, numberOfBottles, width, height, hasWalls);
        ReinforcmentLearningAlgorithm algorithm = new ReinforcmentLearningAlgorithm();

        algorithm.run(sim);
        algorithm.writeSolutionToFile(Paths.get("robots/reinforcment_learning/b.txt"), null);

        Robot roby = algorithm
                .readSolutionFromFile(Paths.get("robots/reinforcment_learning/b.txt"));

        for (int i = 0; i < 10; i++) {
            sim.generateGrids(100, 50, 10, 10, false);
            List<Stats> statistics = sim.playGames(roby);

            double averageBottlePickUp = statistics.stream().mapToInt(x -> x.getBottlesCollected())
                    .average().getAsDouble();
            int maxBottlePickUp = statistics.stream().mapToInt(x -> x.getBottlesCollected()).max()
                    .getAsInt();
            double averageEmptyPickUp = statistics.stream().mapToInt(x -> x.getEmptyPickups())
                    .average().getAsDouble();

            double averageWallHit = statistics.stream().mapToInt(x -> x.getWallsHit()).average()
                    .getAsDouble();

            System.out.println("MAX bottle " + maxBottlePickUp);
            System.out.println("Bottle pickup " + averageBottlePickUp);
            System.out.println("Empty pickup " + averageEmptyPickUp);
            System.out.println("Walls hit " + averageWallHit);
            System.out.println();
        }
    }
}
