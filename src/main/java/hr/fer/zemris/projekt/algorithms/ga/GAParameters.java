package hr.fer.zemris.projekt.algorithms.ga;

import hr.fer.zemris.projekt.parameter.Parameter;
import hr.fer.zemris.projekt.parameter.ParameterType;
import hr.fer.zemris.projekt.parameter.Parameters;

import java.util.Arrays;
import java.util.HashSet;
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
    public static final int DEFAULT_MAX_GENERATIONS = 1_000;

    /**
     * Default value of the {@code population size} parameter.
     */
    public static final int DEFAULT_POPULATION_SIZE = 200;

    /**
     * Default value of the {@code elitism ratio} parameter.
     */
    public static final double DEFAULT_ELITISM__RATIO = 0.01;

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

    /**
     * Constructs a {@link GAParameters} object with the default parameters.
     */
    GAParameters() {
        maxGenerations = new Parameter(MAX_GEN_ID, ParameterType.INTEGER, 0, 100_000, DEFAULT_MAX_GENERATIONS);
        populationSize = new Parameter(POP_SIZE_ID, ParameterType.INTEGER, 0, 1_000, DEFAULT_POPULATION_SIZE);
        elitismRatio = new Parameter(ELITISM_RATIO_ID, ParameterType.DOUBLE, 0, 1, DEFAULT_ELITISM__RATIO);
        tournamentSize = new Parameter(TOURNAMENT_SIZE_ID, ParameterType.INTEGER, 1, 10, DEFAULT_TOURNAMENT_SIZE);
    }

    @Override
    public Parameter getParameter(String name) {
        switch (name) {
            case MAX_GEN_ID:
                return maxGenerations;
            case POP_SIZE_ID:
                return populationSize;
            case ELITISM_RATIO_ID:
                return elitismRatio;
            case TOURNAMENT_SIZE_ID:
                return tournamentSize;
            default:
             throw new IllegalArgumentException("Unrecognized parameter: " + name);
        }
    }

    @Override
    public void setParameter(String name, double value) {
        switch (name) {
            case MAX_GEN_ID:
                maxGenerations.setValue(value);
                break;
            case POP_SIZE_ID:
                populationSize.setValue(value);
                break;
            case ELITISM_RATIO_ID:
                elitismRatio.setValue(value);
                break;
            case TOURNAMENT_SIZE_ID:
                tournamentSize.setValue(value);
            default:
                throw new IllegalArgumentException("Unrecognized parameter: " + name);
        }
    }

    @Override
    public Set<Parameter> getParameters() {
        return new HashSet<>(Arrays.asList(maxGenerations, populationSize, elitismRatio, tournamentSize));
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
