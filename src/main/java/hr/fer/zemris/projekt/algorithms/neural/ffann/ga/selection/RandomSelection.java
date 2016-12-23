package hr.fer.zemris.projekt.algorithms.neural.ffann.ga.selection;

import hr.fer.zemris.projekt.algorithms.neural.Utils;
import hr.fer.zemris.projekt.algorithms.neural.elman.ga.Chromosome;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public class RandomSelection implements ISelection {
    @Override
    public Chromosome select(List<Chromosome> population) {
        List<Chromosome> populationList = new ArrayList<>(population);
        int n = population.size();

        int index = Utils.RANDOM.nextInt(n);
        return populationList.get(index);
    }
}
