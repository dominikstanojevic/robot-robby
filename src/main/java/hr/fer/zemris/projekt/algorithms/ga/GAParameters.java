package hr.fer.zemris.projekt.algorithms.ga;

import hr.fer.zemris.projekt.parameter.Parameter;
import hr.fer.zemris.projekt.parameter.ParameterType;
import hr.fer.zemris.projekt.parameter.Parameters;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    private Map<String, Parameter> parametersMap = new HashMap<>();

    /**
     * Constructs a {@link GAParameters} object with the default parameters.
     */
    GAParameters() {
        maxGenerations = new Parameter(MAX_GEN_ID, ParameterType.INTEGER, 0, 100_000, DEFAULT_MAX_GENERATIONS);
        populationSize = new Parameter(POP_SIZE_ID, ParameterType.INTEGER, 0, 1_000, DEFAULT_POPULATION_SIZE);
        elitismRatio = new Parameter(ELITISM_RATIO_ID, ParameterType.DOUBLE, 0, 0.2, DEFAULT_ELITISM_RATIO);
        tournamentSize = new Parameter(TOURNAMENT_SIZE_ID, ParameterType.INTEGER, 1, 10, DEFAULT_TOURNAMENT_SIZE);

        parametersMap.put(MAX_GEN_ID, maxGenerations);
        parametersMap.put(POP_SIZE_ID, populationSize);
        parametersMap.put(ELITISM_RATIO_ID, elitismRatio);
        parametersMap.put(TOURNAMENT_SIZE_ID, tournamentSize);
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
    public Set<Parameter> getParameters() {
        return new HashSet<>(parametersMap.values());
    }

    /**
     * Returns a copy of the current {@link GAParameters} object.
     * @return a copy of the current {@link GAParameters} object
     */
    public GAParameters copy() {
        GAParameters copy = new GAParameters();

        copy.maxGenerations = copyParameter(this.maxGenerations);
        copy.populationSize = copyParameter(this.populationSize);
        copy.elitismRatio = copyParameter(this.elitismRatio);
        copy.tournamentSize = copyParameter(this.tournamentSize);

        copy.parametersMap = new HashMap<>(parametersMap);

        return copy;
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
