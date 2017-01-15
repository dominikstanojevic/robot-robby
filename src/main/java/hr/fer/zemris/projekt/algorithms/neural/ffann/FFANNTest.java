package hr.fer.zemris.projekt.algorithms.neural.ffann;

import hr.fer.zemris.projekt.algorithms.neural.ffann.ga.FFANGAParameters;
import hr.fer.zemris.projekt.algorithms.neural.ffann.ga.FFANNGA;
import hr.fer.zemris.projekt.observer.Observable;
import hr.fer.zemris.projekt.observer.Observer;
import hr.fer.zemris.projekt.observer.observations.TrainingResult;
import hr.fer.zemris.projekt.simulator.AbstractSimulator;
import hr.fer.zemris.projekt.simulator.Simulator;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public class FFANNTest {
    public static void main(String[] args) {
        AbstractSimulator simulator = new Simulator(200);
        simulator.generateGrids(50, 50, 10, 10, false);

        FFANNGA ga = new FFANNGA();
        ga.addObserver(new Result());
        ga.run(simulator, new FFANGAParameters());
    }

    private static class Result implements Observer<TrainingResult> {

        @Override
        public void observationMade(
                Observable sender, TrainingResult observation) {
            if(observation.getIteration() % 10 == 0) {
                System.out.println("Iteration: " + observation.getIteration() + ", best: " + observation
                        .getBestResult().standardizedFitness() + ", average: " + observation.getAverageFitness());
            }
        }
    }
}
