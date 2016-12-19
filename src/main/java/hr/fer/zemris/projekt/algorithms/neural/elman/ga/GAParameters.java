package hr.fer.zemris.projekt.algorithms.neural.elman.ga;

import hr.fer.zemris.projekt.parameter.Parameter;
import hr.fer.zemris.projekt.parameter.ParameterType;
import hr.fer.zemris.projekt.parameter.Parameters;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Dominik on 9.12.2016..
 */
public class GAParameters implements Parameters<GA> {
    private Set<Parameter> parameters = new HashSet<>();

    public static final String POPULATION_SIZE = "populationSize";
    public static final int DEFAULT_POPULATION_SIZE = 50;

    public static final String MAX_GENERATIONS = "maxGenerations";
    public static final int DEFAULT_MAX_GENERATIONS = 2_500;

    public static final String TOURNAMENT_SIZE = "tournamentSize";
    public static final int DEFAULT_TOURNAMENT_SIZE = 3;

    public static final String SELECTION_PROBABILITY = "selectionProbability";
    public static final double DEFAULT_SELECTION_PROBABILITY = 0.9;

    public static final String ALPHA = "alpha";
    public static final double DEFAULT_ALPHA = 0.5;

    public static final String MUTATION_RATE = "mutationRate";
    public static final double DEFAULT_MUTATION_RATE = 0.5;

    public static final String REFRESH_RATE = "refreshRate";
    public static final double DEFAULT_REFRESH_RATE = 0.01;

    public GAParameters() {
        parameters.add(new Parameter(POPULATION_SIZE, ParameterType.INTEGER, 1, 10_000, DEFAULT_POPULATION_SIZE));
        parameters.add(new Parameter(MAX_GENERATIONS, ParameterType.INTEGER, 1, Integer.MAX_VALUE,
                DEFAULT_MAX_GENERATIONS));
        parameters.add(new Parameter(TOURNAMENT_SIZE, ParameterType.INTEGER, 1, DEFAULT_POPULATION_SIZE,
                DEFAULT_TOURNAMENT_SIZE));
        parameters.add(new Parameter(SELECTION_PROBABILITY, ParameterType.DOUBLE, 0, 1, DEFAULT_SELECTION_PROBABILITY));
        parameters.add(new Parameter(ALPHA, ParameterType.DOUBLE, 0, 5, DEFAULT_ALPHA));
        parameters.add(new Parameter(MUTATION_RATE, ParameterType.DOUBLE, 0, 1, DEFAULT_MUTATION_RATE));
        parameters.add(new Parameter(REFRESH_RATE, ParameterType.DOUBLE, 0, 1, DEFAULT_REFRESH_RATE));
    }

    @Override
    public Parameter getParameter(String name) {
        return parameters.stream().filter(p -> p.getName().equals(name)).findFirst().get();
    }

    @Override
    public void setParameter(String name, double value) {
        Parameter parameter = parameters.stream().filter(p -> p.getName().equals(name)).findFirst().get();
        parameter.setValue(value);
    }

    @Override
    public Set<Parameter> getParameters() {
        return new HashSet<>(parameters);
    }

}
