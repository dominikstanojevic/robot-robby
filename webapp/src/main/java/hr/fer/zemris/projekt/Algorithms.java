package hr.fer.zemris.projekt;

import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.algorithms.ObservableAlgorithm;
import hr.fer.zemris.projekt.algorithms.ga.GeneticAlgorithm;
import hr.fer.zemris.projekt.parameter.Parameters;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class Algorithms {

    private static final Map<String, Supplier<ObservableAlgorithm>> ALGORITHM_SUPPLIERS = new HashMap<>();
    private static final Map<String, Algorithm> ALGORITHM_MAP = new HashMap<>();

    static {
        ALGORITHM_SUPPLIERS.put("ga", GeneticAlgorithm::new);

        ALGORITHM_MAP.put("ga", new GeneticAlgorithm());
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

    private Algorithms() {}
}
