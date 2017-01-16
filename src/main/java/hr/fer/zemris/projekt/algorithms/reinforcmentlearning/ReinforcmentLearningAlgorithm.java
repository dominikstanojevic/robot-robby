package hr.fer.zemris.projekt.algorithms.reinforcmentlearning;

import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.algorithms.AlgorithmUtils;
import hr.fer.zemris.projekt.algorithms.ObservableAlgorithm;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.algorithms.RobotFormatException;
import hr.fer.zemris.projekt.algorithms.reinforcmentlearning.functions.QFunction;
import hr.fer.zemris.projekt.algorithms.reinforcmentlearning.functions.RewardFunction;
import hr.fer.zemris.projekt.parameter.Parameters;
import hr.fer.zemris.projekt.simulator.AbstractSimulator;
import hr.fer.zemris.projekt.simulator.Stats;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Class models reinforcment learning algorithm based on model free q learning.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 15.1.2017.
 */
public class ReinforcmentLearningAlgorithm extends ObservableAlgorithm {

    /**
     * Algorithm's q function.
     */
    private QFunction currentQFunction;

    /**
     * Robot solution line comment start.
     */
    private static final String solutionFileCommentStart = "#";

    @Override
    public Robot readSolutionFromFile(Path filePath) throws IOException {
        try {
            String solutionContent = readSolutionFile(filePath);
            ReinforcmentLearningParameters params = new ReinforcmentLearningParameters();
            params.setParameter(ReinforcmentLearningParameters.LEARNING_RATE_NAME, 0);
            currentQFunction = QFunction.fromString(solutionContent, params);
        } catch (IllegalArgumentException ex) {
            throw new RobotFormatException();
        }
        return new Agent(currentQFunction);

    }

    /**
     * Method reads robot solution from given path.
     *
     * @param filePath
     *            path to robot definition
     * @return file content string without comments
     * @throws IOException
     *             if there was a problem while reading file
     */
    private String readSolutionFile(Path filePath) throws IOException {
        StringBuilder solution = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(
                Files.newInputStream(filePath, StandardOpenOption.READ)), StandardCharsets.UTF_8));
        String line = reader.readLine();
        while (line != null) {
            line = line.trim();
            if (!line.isEmpty() && !line.startsWith(solutionFileCommentStart)) {
                solution.append(line);
            }
            line = reader.readLine();
        }
        return solution.toString();
    }

    @Override
    public void writeSolutionToFile(Path filePath, Robot robot) throws IOException {
        FileWriter writer = new FileWriter(new File(filePath.toUri()));
        writer.write(currentQFunction.toString());
        writer.close();
    }

    @Override
    public Parameters<? extends Algorithm> getDefaultParameters() {
        return new ReinforcmentLearningParameters();
    }

    @Override
    public Robot run(AbstractSimulator simulator, Parameters<? extends Algorithm> parameters) {

        // if (currentQFunction == null) {
        currentQFunction = new QFunction((ReinforcmentLearningParameters) parameters);
        // } else {
        // currentQFunction = new QFunction(currentQFunction,
        // (ReinforcmentLearningParameters) parameters);
        // }
        RewardFunction.setRewards((ReinforcmentLearningParameters) parameters);

        for (int i = 0, end = (int) parameters.getParameter(
                ReinforcmentLearningParameters.ITERATIONS_NUMBER_NAME).getValue(); i < end; i++) {

            Agent roby = new Agent(currentQFunction);

            List<Stats> statistics = simulator.playGames(roby);

            roby.setStandardFitness(AlgorithmUtils.calculateFitness(statistics));
            notifyListeners(roby, AlgorithmUtils.calculateFitness(statistics), i);
            if (i == end - 1 || i % 100 == 0) {
                double averageBottlePickUp = statistics.stream()
                        .mapToInt(x -> x.getBottlesCollected()).average().getAsDouble();
                int maxBottlePickUp = statistics.stream().mapToInt(x -> x.getBottlesCollected())
                        .max().getAsInt();
                double averageEmptyPickUp = statistics.stream().mapToInt(x -> x.getEmptyPickups())
                        .average().getAsDouble();

                double averageWallHit = statistics.stream().mapToInt(x -> x.getWallsHit())
                        .average().getAsDouble();

                System.out.println("Iteracija: " + i);
                System.out.println("MAX bottle " + maxBottlePickUp);
                System.out.println("Bottle pickup " + averageBottlePickUp);
                System.out.println("Empty pickup " + averageEmptyPickUp);
                System.out.println("Walls hit " + averageWallHit);
                System.out.println();
            }
        }

        ReinforcmentLearningParameters params = new ReinforcmentLearningParameters();
        params.setParameter(ReinforcmentLearningParameters.LEARNING_RATE_NAME, 0);

        return new Agent(currentQFunction);

    }

    /**
     * Algorithm name.
     */
    private static final String ALGORITHM_NAME = "Reinforcment Learning";

    @Override
    public String toString() {
        return ALGORITHM_NAME;
    }
}
