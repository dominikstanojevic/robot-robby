package hr.fer.zemris.projekt.algorithms.ga;

import hr.fer.zemris.projekt.parameter.Parameter;
import hr.fer.zemris.projekt.parameter.ParameterType;
import hr.fer.zemris.projekt.parameter.Parameters;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * <p>Implementation of the {@link Parameters} interface for use with the
 * {@link GeneticAlgorithm}.</p>
 * <p>The available parameters are the following:
 * <ul>
 *     <li>Max generations - the number of generation for that the {@code GA}
 *     should run for</li>
 *     <li>Population size - the number of individuals in the population</li>
 *     <li>Elitism ratio - the percentage of the best individuals from the previous
 *     generation which should remain in the next generation</li>
 *     <li>Tournament size - the size of the tournament, which is used for selecting
 *     which parents should be used in the crossover operation</li>
 *     <li>Stop threshold - indicated the stop condition for the algorithm. When the
 *     population's best fitness exceeds the specified value, the algorithm stops</li>
 * </ul></p>
 *
 * @author Leon Luttenberger
 */
public class GAParameters implements Parameters<GeneticAlgorithm> {

    /**
     * ID of the {@code maximum generations} parameter.
     */
    public static final String MAX_GEN_ID = "Max generations";

    /**
     * ID of the {@code population size} parameter.
     */
    public static final String POP_SIZE_ID = "Population size";

    /**
     * ID of the {@code elitism ratio} parameter.
     */
    public static final String ELITISM_RATIO_ID = "Elitism ratio";

    /**
     * ID of the {@code tournament size} parameter.
     */
    public static final String TOURNAMENT_SIZE_ID = "Tournament size";

    /**
     * ID of the ${code stop threshold} parameter.
     */
    public static final String STOP_THRESHOLD_ID = "Stop threshold";

    /**
     * Default value of the {@code maximum generations} parameter.
     */
    public static final int DEFAULT_MAX_GENERATIONS = 1_500;

    /**
     * Default value of the {@code population size} parameter.
     */
    public static final int DEFAULT_POPULATION_SIZE = 200;

    /**
     * Default value of the {@code elitism ratio} parameter.
     */
    public static final double DEFAULT_ELITISM_RATIO = 0.01;

    /**
     * Default value of the {@code tournament size} parameter.
     */
    public static final int DEFAULT_TOURNAMENT_SIZE = 3;

    /**
     * Default value of the ${code stop threshold} parameter.
     */
    public static final int DEFAULT_THRESHOLD = 1;

    /**
     * {@code Maximum generations} parameter.
     */
    Parameter maxGenerations;

    /**
     * {@code Population size} parameter.
     */
    Parameter populationSize;

    /**
     * {@code Elitism ratio} parameter.
     */
    Parameter elitismRatio;

    /**
     * {@code Tournament size} parameter.
     */
    Parameter tournamentSize;

    /**
     * ${code Stop threshold} parameter.
     */
    Parameter stopThreshold;

    private Map<String, Parameter> parametersMap = new HashMap<>();

    /**
     * Constructs a {@link GAParameters} object with the default parameters.
     */
    GAParameters() {
        Parameter maxGenerations = new Parameter(MAX_GEN_ID, ParameterType.INTEGER, 0, 100_000, DEFAULT_MAX_GENERATIONS);
        Parameter populationSize = new Parameter(POP_SIZE_ID, ParameterType.INTEGER, 0, 1_000, DEFAULT_POPULATION_SIZE);
        Parameter elitismRatio = new Parameter(ELITISM_RATIO_ID, ParameterType.DOUBLE, 0, 0.2, DEFAULT_ELITISM_RATIO);
        Parameter tournamentSize = new Parameter(TOURNAMENT_SIZE_ID, ParameterType.INTEGER, 1, 10, DEFAULT_TOURNAMENT_SIZE);
        Parameter stopThreshold = new Parameter(STOP_THRESHOLD_ID, ParameterType.DOUBLE, 0, 1, DEFAULT_THRESHOLD);

        init(maxGenerations, populationSize, elitismRatio, tournamentSize, stopThreshold);
    }

    private GAParameters(Parameter maxGenerations, Parameter populationSize,
                         Parameter elitismRatio, Parameter tournamentSize, Parameter stopThreshold) {

        init(maxGenerations, populationSize, elitismRatio, tournamentSize, stopThreshold);
    }
	
	private void init(Parameter maxGenerations, Parameter populationSize,
                      Parameter elitismRatio, Parameter tournamentSize, Parameter stopThreshold) {

		this.maxGenerations = maxGenerations;
		this.populationSize = populationSize;
		this.elitismRatio = elitismRatio;
		this.tournamentSize = tournamentSize;
		this.stopThreshold = stopThreshold;
		
		parametersMap.put(MAX_GEN_ID, maxGenerations);
        parametersMap.put(POP_SIZE_ID, populationSize);
        parametersMap.put(ELITISM_RATIO_ID, elitismRatio);
        parametersMap.put(TOURNAMENT_SIZE_ID, tournamentSize);
        parametersMap.put(STOP_THRESHOLD_ID, stopThreshold);
	}

    @Override
    public Parameter getParameter(String name) {
        if (!parametersMap.containsKey(name)) {
            throw new IllegalArgumentException("Unrecognized parameter: " + name);
        }

        return parametersMap.get(name);
    }

    @Override
    public void setParameter(String name, double value) {
        if (!parametersMap.containsKey(name)) {
            throw new IllegalArgumentException("Unrecognized parameter: " + name);
        }

        parametersMap.get(name).setValue(value);
    }

    @Override
    public LinkedHashSet<Parameter> getParameters() {
        return new LinkedHashSet<>(parametersMap.values());
    }

    /**
     * Returns a copy of the current {@link GAParameters} object.
     * @return a copy of the current {@link GAParameters} object
     */
    public GAParameters copy() {
        return new GAParameters(
                copyParameter(maxGenerations),
                copyParameter(populationSize),
                copyParameter(elitismRatio),
                copyParameter(tournamentSize),
                copyParameter(stopThreshold)
        );
    }

    /**
     * Copies the specified parameter and returns the copy.
     * @param p parameter to copy
     * @return copy of the parameter
     */
    private static Parameter copyParameter(Parameter p) {
        return new Parameter(
                p.getName(),
                p.getType(),
                p.getMinValue(),
                p.getMaxValue(),
                p.getValue()
        );
    }
}
