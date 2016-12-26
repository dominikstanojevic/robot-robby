package hr.fer.zemris.projekt;

import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.algorithms.ga.GeneticAlgorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class Algorithms {

    private static final Map<String, Supplier<Algorithm>> algorithms = new HashMap<>();

    static {
        algorithms.put("ga", GeneticAlgorithm::new);
    }

    public static Algorithm getAlgorithm(String id) {
        if (!algorithms.containsKey(id)) {
            throw new IllegalArgumentException("No algorithm found with ID: " + id);
        }

        return algorithms.get(id).get();
    }

    private Algorithms() {}
}
