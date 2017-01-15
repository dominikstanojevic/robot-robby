package hr.fer.zemris.projekt;

import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.algorithms.ObservableAlgorithm;
import hr.fer.zemris.projekt.algorithms.ga.GeneticAlgorithm;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.GeneticProgramming;
import hr.fer.zemris.projekt.algorithms.neural.elman.ga.GA;
import hr.fer.zemris.projekt.algorithms.neural.ffann.ga.FFANNGA;
import hr.fer.zemris.projekt.algorithms.reinforcmentlearning.ReinforcmentLearningAlgorithm;
import hr.fer.zemris.projekt.parameter.Parameters;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class Algorithms {

    private static final Map<String, Supplier<ObservableAlgorithm>> ALGORITHM_SUPPLIERS = new HashMap<>();
    private static final Map<String, Algorithm> ALGORITHM_MAP = new HashMap<>();

    private static final String GA_KEY = "ga";
    private static final String ELMAN_KEY = "elman";
    private static final String FFANN_KEY = "nn";
    private static final String GP_KEY = "gp";
    private static final String REINFORCEMENT_KEY = "reinforcement";

    static {
        ALGORITHM_SUPPLIERS.put(GA_KEY, GeneticAlgorithm::new);
        ALGORITHM_SUPPLIERS.put(ELMAN_KEY, GA::new);
        ALGORITHM_SUPPLIERS.put(FFANN_KEY, FFANNGA::new);
        ALGORITHM_SUPPLIERS.put(GP_KEY, GeneticProgramming::new);
        ALGORITHM_SUPPLIERS.put(REINFORCEMENT_KEY, ReinforcmentLearningAlgorithm::new);

        ALGORITHM_MAP.put(GA_KEY, new GeneticAlgorithm());
        ALGORITHM_MAP.put(ELMAN_KEY, new GA());
        ALGORITHM_MAP.put(FFANN_KEY, new FFANNGA());
        ALGORITHM_MAP.put(GA_KEY, new GeneticProgramming());
        ALGORITHM_MAP.put(REINFORCEMENT_KEY, new ReinforcmentLearningAlgorithm());
    }

    public static ObservableAlgorithm getAlgorithm(String id) {
        if (!ALGORITHM_SUPPLIERS.containsKey(id)) {
            throw new IllegalArgumentException("No algorithm found with ID: " + id);
        }

        return ALGORITHM_SUPPLIERS.get(id).get();
    }

    public static Parameters<? extends Algorithm> getDefaultParameters(String id) {
        if (!ALGORITHM_MAP.containsKey(id)) {
            throw new IllegalArgumentException("No algorithm found with ID: " + id);
        }

        return ALGORITHM_MAP.get(id).getDefaultParameters();
    }

    public static Map<String, String> getAvailableAlgorithms(){
        Map<String, String> algorithms = new HashMap<>();

        ALGORITHM_MAP.forEach((key, value) -> algorithms.put(key, value.toString()));

        return algorithms;
    }

    private Algorithms() {}
}
