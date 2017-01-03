package hr.fer.zemris.projekt.algorithms.neural.ffann;

import hr.fer.zemris.projekt.algorithms.neural.ffann.ga.SASEGASA;
import hr.fer.zemris.projekt.algorithms.neural.ffann.ga.SASEGASAParameters;
import hr.fer.zemris.projekt.simulator.AbstractSimulator;
import hr.fer.zemris.projekt.simulator.MultithreadedSimulator;
import hr.fer.zemris.projekt.simulator.Simulator;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public class FFANNTest {
    public static void main(String[] args) {
        AbstractSimulator simulator = new Simulator(200);
        simulator.generateGrids(10, 50, 10, 10, false);

        SASEGASA ga = new SASEGASA();
        ga.run(simulator, new SASEGASAParameters());
    }
}
