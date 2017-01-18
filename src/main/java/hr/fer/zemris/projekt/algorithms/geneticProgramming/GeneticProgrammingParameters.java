package hr.fer.zemris.projekt.algorithms.geneticProgramming;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import hr.fer.zemris.projekt.parameter.Parameter;
import hr.fer.zemris.projekt.parameter.ParameterType;
import hr.fer.zemris.projekt.parameter.Parameters;

/**
 * Implementation of {@link Parameters} for {@link GeneticProgramming}
 * algorithm.
 * 
 * @author Dunja Vesinger
 * @version 1.0.0
 */
public class GeneticProgrammingParameters implements Parameters<GeneticProgramming> {

	/**
	 * Name of the parameter.
	 */
	public static final String CROSSOVER_RATE = "Crossover rate";
	/**
	 * Maximum value of the parameter.
	 */
	private static final double MAX_CROSSOVER_RATE = 1;
	/**
	 * Minimum value of the parameter.
	 */
	private static final double MIN_CROSSOVER_RATE = 0;
	/**
	 * Default value of the parameter.
	 */
	private static final double DEFAULT_CROSSOVER_RATE = 0.9;

	/**
	 * Name of the parameter.
	 */
	public static final String MUTATION_RATE = "Mutation rate";
	/**
	 * Maximum value of the parameter.
	 */
	private static final double MAX_MUTATION_RATE = 1;
	/**
	 * Minimum value of the parameter.
	 */
	private static final double MIN_MUTATION_RATE = 0;
	/**
	 * Default value of the parameter.
	 */
	private static final double DEFAULT_MUTATION_RATE = 0.3;

	/**
	 * Name of the parameter.
	 */
	public static final String POPULATION_SIZE = "Population size";
	/**
	 * Maximum value of the parameter.
	 */
	private static final double MAX_POPULATION_SIZE = 1000;
	/**
	 * Minimum value of the parameter.
	 */
	private static final double MIN_POPULATION_SIZE = 20;
	/**
	 * Default value of the parameter.
	 */
	private static final double DEFAULT_POPULATION_SIZE = 100;

	/**
	 * Name of the parameter.
	 */
	public static final String GENERATION_NUM = "Number of generations";
	/**
	 * Maximum value of the parameter.
	 */
	private static final double MAX_GENERATION_NUM = 10000;
	/**
	 * Minimum value of the parameter.
	 */
	private static final double MIN_GENERATION_NUM = 100;
	/**
	 * Default value of the parameter.
	 */
	private static final double DEFAULT_GENERATION_NUM = 3000;

	/**
	 * Name of the parameter.
	 */
	public static final String INITIAL_TREE_DEPTH = "Maximal initial tree depth";
	/**
	 * Maximum value of the parameter.
	 */
	private static final double MAX_INITIAL_TREE_DEPTH = 15;
	/**
	 * Minimum value of the parameter.
	 */
	private static final double MIN_INITIAL_TREE_DEPTH = 2;
	/**
	 * Default value of the parameter.
	 */
	private static final double DEFAULT_INITIAL_TREE_DEPTH = 3;

	/**
	 * Name of the parameter.
	 */
	public static final String CROSSOVER_TREE_DEPTH = "Maximal final tree depth";
	/**
	 * Maximum value of the parameter.
	 */
	private static final double MAX_CROSSOVER_TREE_DEPTH = 20;
	/**
	 * Minimum value of the parameter.
	 */
	private static final double MIN_CROSSOVER_TREE_DEPTH = 5;
	/**
	 * Default value of the parameter.
	 */
	private static final double DEFAULT_CROSSOVER_TREE_DEPTH = 15;

	/**
	 * Name of the parameter.
	 */
	public static final String EMPTY_PICKUP_PENALTY = "Penalty for empty pickup";
	/**
	 * Maximum value of the parameter.
	 */
	private static final double MAX_EMPTY_PICKUP_PENALTY = 10;
	/**
	 * Minimum value of the parameter.
	 */
	private static final double MIN_EMPTY_PICKUP_PENALTY = 0;
	/**
	 * Default value of the parameter.
	 */
	private static final double DEFAULT_EMPTY_PICKUP_PENALTY = 1;

	/**
	 * Name of the parameter.
	 */
	public static final String HITTING_WALL_PENALTY = "Penalty for hitting a wall";
	/**
	 * Maximum value of the parameter.
	 */
	private static final double MAX_HITTING_WALL_PENALTY = 10;
	/**
	 * Minimum value of the parameter.
	 */
	private static final double MIN_HITTING_WALL_PENALTY = 0;
	/**
	 * Default value of the parameter.
	 */
	private static final double DEFAULT_HITTING_WALL_PENALTY = 5;

	/**
	 * Name of the parameter.
	 */
	public static final String BOTTLE_PICKUP_PRIZE = "Prize for picking up a bottle";
	/**
	 * Maximum value of the parameter.
	 */
	private static final double MAX_BOTTLE_PICKUP_PRIZE = 10;
	/**
	 * Minimum value of the parameter.
	 */
	private static final double MIN_BOTTLE_PICKUP_PRIZE = 0;
	/**
	 * Default value of the parameter.
	 */
	private static final double DEFAULT_BOTTLE_PICKUP_PRIZE = 10;

	/**
	 * Name of the parameter.
	 */
	public static final String STOP_CONDITION = "Stop condition";
	/**
	 * Maximum value of the parameter.
	 */
	private static final double MAX_STOP_CONDITION = 1;
	/**
	 * Minimum value of the parameter.
	 */
	private static final double MIN_STOP_CONDITION = 0;
	/**
	 * Default value of the parameter.
	 */
	private static final double DEFAULT_STOP_CONDITION = 0.98;

	/**
	 * Name of the parameter.
	 */
	public static final String TOURNAMENT_SIZE = "Tournament size";
	/**
	 * Maximum value of the parameter.
	 */
	private static final int MAX_TOURNAMENT_SIZE = 7;
	/**
	 * Minimum value of the parameter.
	 */
	private static final int MIN_TOURNAMENT_SIZE = 1;
	/**
	 * Default value of the parameter.
	 */
	private static final int DEFAULT_TOURNAMENT_SIZE = 2;

	/**
	 * Map of parameter names and values.
	 */
	private Map<String, Parameter> parameters;

	/**
	 * Creates a new instance of GeneticProgrammingParameters and sets all
	 * parameters to default values.
	 */
	public GeneticProgrammingParameters() {

		parameters = new HashMap<>();

		parameters.put(CROSSOVER_RATE, new Parameter(CROSSOVER_RATE, ParameterType.DOUBLE, MIN_CROSSOVER_RATE,
				MAX_CROSSOVER_RATE, DEFAULT_CROSSOVER_RATE));
		parameters.put(MUTATION_RATE, new Parameter(MUTATION_RATE, ParameterType.DOUBLE, MIN_MUTATION_RATE,
				MAX_MUTATION_RATE, DEFAULT_MUTATION_RATE));
		parameters.put(POPULATION_SIZE, new Parameter(POPULATION_SIZE, ParameterType.INTEGER, MIN_POPULATION_SIZE,
				MAX_POPULATION_SIZE, DEFAULT_POPULATION_SIZE));
		parameters.put(GENERATION_NUM, new Parameter(GENERATION_NUM, ParameterType.INTEGER, MIN_GENERATION_NUM,
				MAX_GENERATION_NUM, DEFAULT_GENERATION_NUM));
		parameters.put(INITIAL_TREE_DEPTH, new Parameter(INITIAL_TREE_DEPTH, ParameterType.INTEGER,
				MIN_INITIAL_TREE_DEPTH, MAX_INITIAL_TREE_DEPTH, DEFAULT_INITIAL_TREE_DEPTH));
		parameters.put(CROSSOVER_TREE_DEPTH, new Parameter(CROSSOVER_TREE_DEPTH, ParameterType.INTEGER,
				MIN_CROSSOVER_TREE_DEPTH, MAX_CROSSOVER_TREE_DEPTH, DEFAULT_CROSSOVER_TREE_DEPTH));
		parameters.put(EMPTY_PICKUP_PENALTY, new Parameter(EMPTY_PICKUP_PENALTY, ParameterType.INTEGER,
				MIN_EMPTY_PICKUP_PENALTY, MAX_EMPTY_PICKUP_PENALTY, DEFAULT_EMPTY_PICKUP_PENALTY));
		parameters.put(HITTING_WALL_PENALTY, new Parameter(HITTING_WALL_PENALTY, ParameterType.INTEGER,
				MIN_HITTING_WALL_PENALTY, MAX_HITTING_WALL_PENALTY, DEFAULT_HITTING_WALL_PENALTY));
		parameters.put(BOTTLE_PICKUP_PRIZE, new Parameter(BOTTLE_PICKUP_PRIZE, ParameterType.INTEGER,
				MIN_BOTTLE_PICKUP_PRIZE, MAX_BOTTLE_PICKUP_PRIZE, DEFAULT_BOTTLE_PICKUP_PRIZE));
		parameters.put(STOP_CONDITION, new Parameter(STOP_CONDITION, ParameterType.DOUBLE,
				MIN_STOP_CONDITION, MAX_STOP_CONDITION, DEFAULT_STOP_CONDITION));
		parameters.put(TOURNAMENT_SIZE, new Parameter(TOURNAMENT_SIZE, ParameterType.INTEGER,
				MIN_TOURNAMENT_SIZE, MAX_TOURNAMENT_SIZE, DEFAULT_TOURNAMENT_SIZE));
	}

	@Override
	public Parameter getParameter(String name) {
		return parameters.get(name);
	}

	@Override
	public void setParameter(String name, double value) {
		parameters.get(name).setValue(value);

	}

	@Override
	public LinkedHashSet<Parameter> getParameters() {
//		Set<Parameter> paramSet = new HashSet<>();
//		Collection<Parameter> params = parameters.values();
//		for (Parameter p : params) {
//			paramSet.add(new Parameter(p.getName(), p.getType(), p.getMinValue(), p.getMaxValue(), p.getValue()));
//		}
//		return paramSet;
		return new LinkedHashSet<>(parameters.values());
	}

}
