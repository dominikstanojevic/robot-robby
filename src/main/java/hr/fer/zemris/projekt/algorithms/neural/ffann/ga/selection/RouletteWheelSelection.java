package hr.fer.zemris.projekt.algorithms.neural.ffann.ga.selection;

import hr.fer.zemris.projekt.algorithms.neural.elman.ga.Chromosome;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public class RouletteWheelSelection implements ISelection {
    @Override
    public Chromosome select(List<Chromosome> population) {
        double fitnessSum = 0;
        double worstFitness = population.get(0).getFitness();

        for (Chromosome x : population){
            fitnessSum += x.getFitness();
            if (x.getFitness() < worstFitness){
                worstFitness = x.getFitness();
            }
        }
        fitnessSum -= population.size() * worstFitness;

        double roulette = ThreadLocalRandom.current().nextDouble() * fitnessSum;
        double currentSum = 0;

        for (Chromosome x : population){
            currentSum += (x.getFitness() - worstFitness);

            if (currentSum >= roulette){
                return x;
            }
        }

        return population.get(population.size() - 1);
    }
}
