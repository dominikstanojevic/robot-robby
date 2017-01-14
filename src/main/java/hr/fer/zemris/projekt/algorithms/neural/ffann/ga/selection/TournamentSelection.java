package hr.fer.zemris.projekt.algorithms.neural.ffann.ga.selection;

import hr.fer.zemris.projekt.algorithms.neural.elman.ga.Chromosome;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public class TournamentSelection implements ISelection {
    private final int tournamentSize;

    public TournamentSelection(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }

    @Override
    public Chromosome select(List<Chromosome> population) {
        List<Chromosome> selected = new ArrayList<>();

        int n = population.size();
        for (int i = 0; i < tournamentSize; ++i){
            int index = ThreadLocalRandom.current().nextInt(n);
            selected.add(population.get(index));
        }
        selected.sort(Comparator.reverseOrder());

        return selected.get(0);
    }
}
