package hr.fer.zemris.projekt.algorithms.neural.elman.ga;

import hr.fer.zemris.projekt.observer.Observable;
import hr.fer.zemris.projekt.observer.Observer;
import hr.fer.zemris.projekt.observer.observations.TrainingResult;
import hr.fer.zemris.projekt.simulator.Simulator;

/**
 * Created by Dominik on 9.12.2016..
 */
public class GATest {
    public static void main(String[] args) {
        Simulator simulator = new Simulator(200);
        simulator.generateGrids(10, 50, 10, 10, false);

        GA ga = new GA();
        ga.addObserver(new Result());
        ga.run(simulator, new GAParameters());
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
