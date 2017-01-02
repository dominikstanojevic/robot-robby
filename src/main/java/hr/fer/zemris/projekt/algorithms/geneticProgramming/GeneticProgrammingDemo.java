package hr.fer.zemris.projekt.algorithms.geneticProgramming;

import hr.fer.zemris.projekt.parameter.Parameters;
import hr.fer.zemris.projekt.simulator.MultithreadedSimulator;
import hr.fer.zemris.projekt.simulator.Stats;

import java.util.List;

public class GeneticProgrammingDemo {

    public static void main(String[] args) {

        // GeneticProgramming gp = new GeneticProgramming();
        // try {
        // Robot robby = gp.readSolutionFromFile(Paths.get("myRobby.txt"));
        // MultithreadedSimulator simulator = new MultithreadedSimulator();
        //
        // double bottles = 0;
        // double wallsHit = 0;
        // double emptyPickup = 0;
        //
        // simulator.generateGrids(10000, 50, 10, 10, false);
        //
        // List<Stats> s = simulator.playGames(robby);
        // for (Stats stat : s) {
        // bottles += stat.getBottlesCollected();
        // wallsHit += stat.getWallsHit();
        // emptyPickup += stat.getEmptyPickups();
        // }
        //
        // double avg = (bottles*10 + wallsHit*(-5) + emptyPickup*(-1))/10000;
        // System.out.println(new String("Broj boca " + bottles + "; Zabijanja "
        // + wallsHit
        // + "; prazna saginjanja " + emptyPickup + "\n"));
        //
        // System.out.println("Average fitness: " + avg);
        //
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        GeneticProgramming gp = new GeneticProgramming();
        MultithreadedSimulator simulator = new MultithreadedSimulator();
        simulator.generateGrids(100, 50, 10, 10, false);
        Parameters<GeneticProgramming> params = (Parameters<GeneticProgramming>) gp
                .getDefaultParameters();

        gp.run(simulator, params);

        double bottles = 0;
        double wallsHit = 0;
        double emptyPickup = 0;

        simulator.generateGrids(10000, 50, 10, 10, false);

        List<Stats> s = simulator.playGames(gp.getBestIndividual());
        for (Stats stat : s) {
            bottles += stat.getBottlesCollected();
            wallsHit += stat.getWallsHit();
            emptyPickup += stat.getEmptyPickups();
        }

        double avg = (bottles * 10 + wallsHit * (-5) + emptyPickup * (-1)) / 10000;
        System.out.println(new String("Broj boca " + bottles + "; Zabijanja " + wallsHit
                + "; prazna saginjanja " + emptyPickup + "\n"));

        System.out.println("Fitness" + gp.getBestIndividual().getRawFitness());
        System.out.println("Average fitness: " + avg);

        System.out.println(gp.getBestIndividual());

        // String title = new
        // String("Dubina ulaznog stabla, samo grow kreiranje stabala\n");
        // try {
        // Files.write(Paths.get("statistics.txt"), title.getBytes(),
        // StandardOpenOption.APPEND);
        //
        // GeneticProgramming gp = new GeneticProgramming();
        // MultithreadedSimulator simulator = new MultithreadedSimulator();
        // simulator.generateGrids(100, 50, 10, 10, false);
        // Parameters<GeneticProgramming> params =
        // (Parameters<GeneticProgramming>) gp.getDefaultParameters();
        //
        // for (double depth = 0; depth <= 1; depth+= 0.1) {
        //
        // params.setParameter(GeneticProgrammingParameters.CROSSOVER_RATE,
        // depth);
        //
        // double bottles = 0;
        // double wallsHit = 0;
        // double emptyPickup = 0;
        //
        // for (int i = 0; i < 10; i++) {
        //
        // gp.run(simulator, params);
        //
        // List<Stats> s = simulator.playGames(gp.getBestIndividual());
        // for (Stats stat : s) {
        // bottles += stat.getBottlesCollected();
        // wallsHit += stat.getWallsHit();
        // emptyPickup += stat.getEmptyPickups();
        // }
        // }
        //
        // bottles = bottles / 10;
        // wallsHit = wallsHit / 10;
        // emptyPickup /= 10;
        //
        // String param = new String("Max dubina: " +
        // params.getParameter(GeneticProgrammingParameters.CROSSOVER_RATE).getValue()
        // + "\n");
        // Files.write(Paths.get("statistics.txt"),
        // param.getBytes(),StandardOpenOption.APPEND);
        // String statistics = new String("Broj boca " + bottles +
        // "; Zabijanja " + wallsHit
        // + "; prazna saginjanja " + emptyPickup + "\n");
        // Files.write(Paths.get("statistics.txt"),
        // statistics.getBytes(),StandardOpenOption.APPEND);
        //
        // }
        //
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

    }

}
