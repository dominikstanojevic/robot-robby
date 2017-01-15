package hr.fer.zemris.projekt.algorithms.neural.ffann.ga;

import hr.fer.zemris.projekt.parameter.Parameter;
import hr.fer.zemris.projekt.parameter.ParameterType;
import hr.fer.zemris.projekt.parameter.Parameters;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public class SASEGASAParameters implements Parameters<SASEGASA> {
    private Map<String, Parameter> parameters;

    public static final String POPULATION_SIZE = "Population size";
    private static final int DEFAULT_POPULATION_SIZE = 100;
    private static final int MIN_POPULATION_SIZE = 10;
    private static final int MAX_POPULATION_SIZE = 250;

    public static final String NUMBER_OF_VILLAGES = "Number of villages";
    private static final int DEFAULT_NUMBER_OF_VILLAGES = 5;
    private static final int MIN_NUMBER_OF_VILLAGES = 1;
    private static final int MAX_NUMBER_OF_VILLAGES = 250;

    public static final String MAX_GENERATIONS = "Max. generations";
    private static final int DEFAULT_MAX_GENERATIONS = 2_500;
    private static final int MIN_MAX_GENERATIONS = 100;
    private static final int MAX_MAX_GENERATIONS = 50_000;

    public static final String TOURNAMENT_SIZE = "Tournament size";
    private static final int DEFAULT_TOURNAMENT_SIZE = 3;
    private static final int MIN_TOURNAMENT_SIZE = 1;
    private static final int MAX_TOURNAMENT_SIZE = 25;

    public static final String SUCCESS_RATIO = "Success ratio";
    private static final double DEFAULT_SUCCESS_RATIO = 0.35;
    private static final double MIN_SUCCESS_RATIO = 0;
    private static final double MAX_SUCCESS_RATIO = 1;

    public static final String MAX_SELECTION_PRESSURE = "Max. selection pressure";
    private static final double DEFAULT_MAX_SELECTION_PRESSURE = 4;
    private static final double MIN_MAX_SELECTION_PRESSURE = 1;
    private static final double MAX_MAX_SELECTION_PRESSURE = 10;

    public static final String MUTATION_RATE = "Mutation rate";
    private static final double DEFAULT_MUTATION_RATE = 0.1;
    private static final double MIN_MUTATION_RATE = 0;
    private static final double MAX_MUTATION_RATE = 1;

    public static final String SIGMA = "Mutation sigma";
    private static final double DEFAULT_SIGMA = 0.5;
    private static final double MIN_SIGMA = 0;
    private static final double MAX_SIGMA = 10;

    public static final String MAX_ITERATION = "Max iterations";
    private static final int DEFAULT_MAX_ITERATION = 100;
    private static final int MIN_MAX_ITERATION = 0;
    private static final int MAX_MAX_ITERATION = 2_500;

    public static final String STARTING_INTERVAL_MIN = "Starting interval min";
    private static final double DEFAULT_STARTING_INTERVAL_MIN = -2;
    private static final double MIN_STARTING_INTERVAL_MIN = -5;
    private static final double MAX_STARTING_INTERVAL_MIN = 5;

    public static final String STARTING_INTERVAL_MAX = "Starting interval max";
    private static final double DEFAULT_STARTING_INTERVAL_MAX = 2;
    private static final double MIN_STARTING_INTERVAL_MAX = -5;
    private static final double MAX_STARTING_INTERVAL_MAX = 5;

    public static final String ALPHA = "Alpha";
    private static final double DEFAULT_ALPHA = 0.5;
    private static final double MIN_ALPHA = 0;
    private static final double MAX_ALPHA = 1;


    public SASEGASAParameters() {
        parameters = new HashMap<>();

        Parameter populationSize = new Parameter(
                POPULATION_SIZE,
                ParameterType.INTEGER,
                MIN_POPULATION_SIZE,
                MAX_POPULATION_SIZE,
                DEFAULT_POPULATION_SIZE
        );
        parameters.put(populationSize.getName(), populationSize);

        Parameter numberOfVillages = new Parameter(
                NUMBER_OF_VILLAGES,
                ParameterType.INTEGER,
                MIN_NUMBER_OF_VILLAGES,
                MAX_NUMBER_OF_VILLAGES,
                DEFAULT_NUMBER_OF_VILLAGES
        );
        parameters.put(numberOfVillages.getName(), numberOfVillages);

        Parameter maxGenerations = new Parameter(
                MAX_GENERATIONS,
                ParameterType.INTEGER,
                MIN_MAX_GENERATIONS,
                MAX_MAX_GENERATIONS,
                DEFAULT_MAX_GENERATIONS
        );
        parameters.put(maxGenerations.getName(), maxGenerations);

        Parameter tournamentSize = new Parameter(
                TOURNAMENT_SIZE,
                ParameterType.INTEGER,
                MIN_TOURNAMENT_SIZE,
                MAX_TOURNAMENT_SIZE,
                DEFAULT_TOURNAMENT_SIZE
        );
        parameters.put(tournamentSize.getName(), tournamentSize);

        Parameter successRatio = new Parameter(
                SUCCESS_RATIO,
                ParameterType.DOUBLE,
                MIN_SUCCESS_RATIO,
                MAX_SUCCESS_RATIO,
                DEFAULT_SUCCESS_RATIO
        );
        parameters.put(successRatio.getName(), successRatio);

        Parameter maxSelectionPressure = new Parameter(
                MAX_SELECTION_PRESSURE,
                ParameterType.DOUBLE,
                MIN_MAX_SELECTION_PRESSURE,
                MAX_MAX_SELECTION_PRESSURE,
                DEFAULT_MAX_SELECTION_PRESSURE
        );
        parameters.put(maxSelectionPressure.getName(), maxSelectionPressure);

        Parameter mutationRate = new Parameter(
                MUTATION_RATE,
                ParameterType.DOUBLE,
                MIN_MUTATION_RATE,
                MAX_MUTATION_RATE,
                DEFAULT_MUTATION_RATE
        );
        parameters.put(mutationRate.getName(), mutationRate);

        Parameter sigma = new Parameter(
                SIGMA,
                ParameterType.DOUBLE,
                MIN_SIGMA,
                MAX_SIGMA,
                DEFAULT_SIGMA
        );
        parameters.put(sigma.getName(), sigma);

        Parameter maxIteration = new Parameter(
                MAX_ITERATION,
                ParameterType.INTEGER,
                MIN_MAX_ITERATION,
                MAX_MAX_ITERATION,
                DEFAULT_MAX_ITERATION
        );
        parameters.put(maxIteration.getName(), maxIteration);

        Parameter startingIntervalMin = new Parameter(
                STARTING_INTERVAL_MIN,
                ParameterType.DOUBLE,
                MIN_STARTING_INTERVAL_MIN,
                MAX_STARTING_INTERVAL_MIN,
                DEFAULT_STARTING_INTERVAL_MIN
        );
        parameters.put(startingIntervalMin.getName(), startingIntervalMin);

        Parameter startingIntervalMax = new Parameter(
                STARTING_INTERVAL_MAX,
                ParameterType.DOUBLE,
                MIN_STARTING_INTERVAL_MAX,
                MAX_STARTING_INTERVAL_MAX,
                DEFAULT_STARTING_INTERVAL_MAX
        );
        parameters.put(startingIntervalMax.getName(), startingIntervalMax);

        Parameter alpha = new Parameter(
                ALPHA,
                ParameterType.DOUBLE,
                MIN_ALPHA,
                MAX_ALPHA,
                DEFAULT_ALPHA
        );
        parameters.put(alpha.getName(), alpha);
    }

    @Override
    public Parameter getParameter(String name) {
        return parameters.get(name);
    }

    @Override
    public void setParameter(String name, double value) {
        Parameter parameter = parameters.get(name);
        if (parameter == null){
            throw new IllegalArgumentException("The given parameter does not exist!");
        }

        parameter.setValue(value);
        parameters.put(name, parameter);
    }

    @Override
    public LinkedHashSet<Parameter> getParameters() {
        return new LinkedHashSet<>(parameters.values());
    }
}
