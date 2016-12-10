package hr.fer.zemris.projekt.algorithms.neural.elman.ga;

import hr.fer.zemris.projekt.simulator.Simulator;

/**
 * Created by Dominik on 9.12.2016..
 */
public class GATest {
    public static void main(String[] args) {
        Simulator simulator = new Simulator(200);
        simulator.generateGrids(10, 50, 10, 10, false);

        GA ga = new GA();
        ga.run(simulator, new GAParameters());
    }
}
