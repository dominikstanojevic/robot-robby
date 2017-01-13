package hr.fer.zemris.projekt.algorithms.geneticProgramming;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.algorithms.AlgorithmUtils;
import hr.fer.zemris.projekt.algorithms.ObservableAlgorithm;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.algorithms.RobotFormatException;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.FunctionNode;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.Node;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.ParsingException;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.RandomNodeSelector;
import hr.fer.zemris.projekt.parameter.Parameters;
import hr.fer.zemris.projekt.simulator.AbstractSimulator;
import hr.fer.zemris.projekt.simulator.Stats;

/** 
 * An implementation of {@link ObservableAlgorithm} which uses genetic programming to
 * train an {@link Individual}.
 * 
 * @author Dunja Vesinger
 * @version 1.0.0 
 * 
 */
public class GeneticProgramming extends ObservableAlgorithm {
//	public static final int EMPTY_PICKUP_PENALTY = 1;
//	public static final int HITTING_WALL_PENALTY = 5;
//	public static final int PICKUP_PRIZE = 10;

	/**
	 * Tournament size in tournament selection.
	 */
	private static final int TOURNAMENT_SIZE = 2;
	/**
	 * Current population.
	 */
	private List<Individual> population;
	/**
	 * Default parameters of the algorithm.
	 */
	private GeneticProgrammingParameters defaultParameters;
	/**
	 * Best individual produced so far by the algorithm in the latest run.
	 */
	private Individual bestIndividual;
	
	private Individual currBestIndividual;
	
	/**
	 * Selector of random nodes in trees.
	 */
	private RandomNodeSelector selector;
	
	/**
	 * Creates a new instance of GeneticProgramming.
	 */
	public GeneticProgramming() {

		defaultParameters = new GeneticProgrammingParameters();
		selector = new RandomNodeSelector();
	}

	@Override
	public Parameters<? extends Algorithm> getDefaultParameters() {
		return new GeneticProgrammingParameters();
	}
	
	/**
	 * Returns the best individual produced by the algorithm in the last run.
	 * @return best individual or null if the algorithm has not yet been run
	 */
	public Individual getBestIndividual() {
		return bestIndividual;
	}

	@Override
	/**
	 * @throws NullPointerException if either parameters or simulator equals null 
	 */
	public Robot run(AbstractSimulator simulator, Parameters<? extends Algorithm> parameters) {
		
		if(parameters == null || simulator == null) throw new NullPointerException();

		int initialTreeDepth = (int) Math
				.round(parameters.getParameter(GeneticProgrammingParameters.INITIAL_TREE_DEPTH).getValue());
		int populationSize = (int) Math
				.round(parameters.getParameter(GeneticProgrammingParameters.POPULATION_SIZE).getValue());

		int pickupPrize = (int) Math
				.round(parameters.getParameter(GeneticProgrammingParameters.BOTTLE_PICKUP_PRIZE).getValue());
		int hitWallPenalty = (int) Math
				.round(parameters.getParameter(GeneticProgrammingParameters.HITTING_WALL_PENALTY).getValue());
		int emptyPickupPenalty = (int) Math
				.round(parameters.getParameter(GeneticProgrammingParameters.EMPTY_PICKUP_PENALTY).getValue());

		double crossoverRate = parameters.getParameter(GeneticProgrammingParameters.CROSSOVER_RATE).getValue();
		int crossoverMaxDepth = (int) Math
				.round(parameters.getParameter(GeneticProgrammingParameters.CROSSOVER_TREE_DEPTH).getValue());
		
		double mutationRate = parameters.getParameter(GeneticProgrammingParameters.MUTATION_RATE).getValue();
		
		
		population = getRandomPopulation(initialTreeDepth, populationSize);
		bestIndividual = null;

		int generationNum = (int) Math
				.round(parameters.getParameter(GeneticProgrammingParameters.GENERATION_NUM).getValue());

		for (int generation = 0; generation < generationNum; generation++) {

			evaluateFitness(simulator, pickupPrize, hitWallPenalty, emptyPickupPenalty);
			determineBestIndividual();
			
			double average = 0;
			for(Individual i : population) average+=i.standardizedFitness();
			average = average / population.size();
			//System.out.println("Prosjek generacije " + average);
			
			if(generation%20 == 0){
				System.out.println("**************");
				System.out.println("Generacija: " + generation);
				System.out.println("Prosjek generacije: " + average);
				System.out.println("Najbolji do sada: " + bestIndividual.standardizedFitness());
			}
			
//			if(generation>0 && generation%100 == 0){
//				simulator.generateGrids(100, 50, 10, 10, false);
//			}
			
			notifyListeners(currBestIndividual, average, generation);

			population = getNextGeneration(populationSize, mutationRate, crossoverRate, crossoverMaxDepth);
			
			
			
		}
		
		return bestIndividual;

	}

	/**
	 * Returns a list of {@link Individual} with random tree depth (which is always less
	 * or equal to maxDepth) and tree contents.
	 * @param maxDepth maximum tree depth
	 * @param populationSize size of the generated population
	 * @return random population
	 */
	private List<Individual> getRandomPopulation(int maxDepth, int populationSize) {

		List<Individual> population = new ArrayList<>();

		for (int i = 0; i < populationSize; i++) {
			
			population.add(Individual.randomIndividual(maxDepth));
			
			//alternative way of generation an initial population: half of the individuals 
			//have full trees instead of random depth trees			
//			if(i%2 == 0) population.add(Individual.randomIndividual(maxDepth));
//			else population.add(Individual.randomFullTreeIndividual(maxDepth));
		}

		return population;
	}

	/**
	 * Evaluates the raw fitness of each {@link Individual} in the current population.
	 * @param simulator simulator to be used for evaluation 
	 * @param pickupPrize prize for picking up a bottle
	 * @param hitWallPenalty penalty for hitting a wall
	 * @param emptyPickupPenalty penalty for empty pickup
	 */
	private void evaluateFitness(AbstractSimulator simulator, int pickupPrize, int hitWallPenalty,
			int emptyPickupPenalty) {
		
		int populationSize = population.size();

		for (int i=0; i<populationSize; i++) {
			List<Stats> statistics = simulator.playGames(population.get(i));
			
			int bottlesLeft = 0;
			int bottlesPickedUp = 0;
			int emptyPickups = 0;
			int wallsHit = 0;
			for (Stats s : statistics) {
				
				bottlesLeft += s.getBottlesLeft();
				bottlesPickedUp += s.getBottlesCollected();
				wallsHit += s.getWallsHit();
				emptyPickups += s.getEmptyPickups();
			}
			
			double fitness = bottlesPickedUp * pickupPrize - emptyPickups * emptyPickupPenalty
					- wallsHit * hitWallPenalty;
			fitness = fitness / statistics.size();
			
//			double standardFitness = bottlesPickedUp * PICKUP_PRIZE - emptyPickups * EMPTY_PICKUP_PENALTY
//					- wallsHit * HITTING_WALL_PENALTY;
//			standardFitness = standardFitness / (bottlesLeft + bottlesPickedUp);
			
			AlgorithmUtils.calculateFitness(statistics);
			
			population.get(i).setRawFitness(fitness);
			population.get(i).setStandardizedFitness(AlgorithmUtils.calculateFitness(statistics));
			
		}
		
	}

	/**
	 * Determines the best {@link Individual} in current population based on calculated fitness.
	 */
	private void determineBestIndividual() {
		currBestIndividual = Collections.max(population);
		
		//System.out.println("Najbolji u generaciji " + currGenerationBest.getRawFitness());
		
		if (bestIndividual == null || currBestIndividual.compareTo(bestIndividual) > 0) {
			bestIndividual = currBestIndividual;
		}

	}

	/**
	 * Creates a new population for the next run of the algorithm based on the current population
	 * using crossover and mutation.
	 * @param populationSize size of the population to be generated
	 * @param mutationRate mutation rate
	 * @param crossoverRate crossover rate
	 * @param crossoverMaxDepth maximum depth of the tree produced by crossover
	 * @return new population
	 */ 
	private List<Individual> getNextGeneration(int populationSize, double mutationRate, double crossoverRate, int crossoverMaxDepth) {

		List<Individual> nextGeneration = new ArrayList<>();
		
		//elitism(1)
		nextGeneration.add(new Individual(currBestIndividual.getTree().copy()));
		
		while(nextGeneration.size() < populationSize){

			double random = ThreadLocalRandom.current().nextDouble();
			List<Individual> newIndividuals = new ArrayList<>();
			
			if (random < crossoverRate) {
				
				Individual firstParent = tournamentSelection(TOURNAMENT_SIZE);
				Individual secondParent = tournamentSelection(TOURNAMENT_SIZE);

				newIndividuals.addAll(performCorssover(firstParent, secondParent, crossoverMaxDepth));

			} else {
				
				newIndividuals.add(tournamentSelection(TOURNAMENT_SIZE));
			}
			
			double mutation = ThreadLocalRandom.current().nextDouble();
			if(mutation < mutationRate){
				newIndividuals.set(0, mutate(newIndividuals.get(0), crossoverMaxDepth));
			}
			
			nextGeneration.addAll(newIndividuals);
		}

		return nextGeneration;
	}
	
	/**
	 * Mutates the given individual by selecting a random node in a tree and replacing its
	 * subtree with a new random subtree. The original individual is altered in the process.
	 * @param individual individual to be mutated
	 * @param maxDepth maximum depth of the mutated individual
	 * @return mutated individual
	 */
	private Individual mutate(Individual individual, int maxDepth) {
		
		Node tree = individual.getTree();
		
		Node mutationPoint = selector.select(tree);
		
		int depth = maxDepth - tree.getDepth() + mutationPoint.getDepth();
		
		Node mutationSubtree = Individual.randomNode(0, depth, false);
		
		insertSubtree(tree, mutationPoint, mutationSubtree);
		
		return new Individual(tree);
		
	}

	/**
	 * Selects an individual from the current population using tournament selection and
	 * returns a deep copy of the individual.
	 * @param tournamentSize size of the tournament
	 * @return deep copy of the selected individual
	 */
	private Individual tournamentSelection(int tournamentSize){
		int populationSize = population.size();
		
		List<Individual> tournament = new ArrayList<>();
		for(int i=0; i<tournamentSize; i++){
			int index = ThreadLocalRandom.current().nextInt(0, populationSize - 1);
			tournament.add(population.get(index));
		}
		
		Collections.sort(tournament);
		return new Individual(tournament.get(tournamentSize - 1).getTree().copy());
		
	}
//
//	private Individual rouletteSelection() {
//		
//		int populationSize = population.size();
//		double[] cumulativeFitnesses = new double[populationSize];
//		cumulativeFitnesses[0] = population.get(0).getFitness();
//
//		for (int i = 1; i < populationSize; i++) {
//
//			cumulativeFitnesses[i] = cumulativeFitnesses[i - 1] + population.get(i).getFitness();
//		}
//
//		double random = ThreadLocalRandom.current().nextDouble() * cumulativeFitnesses[cumulativeFitnesses.length - 1];
//		int index = Arrays.binarySearch(cumulativeFitnesses, random);
//		index = index < 0 ? (-index + 1) : index;
//		index = index > (populationSize - 1) ? (populationSize - 1) : index;
//
//		Node tree = population.get(index).getTree();
//		
//		return new Individual(tree.copy());
//
//	}

	/**
	 * Performs crossover between two given individuals and return two children produced by the crossover.
	 * If the child would exceed the maximum tree depth, it returns the copy of a parent instead. It may
	 * alter the given individuals in the process.
	 * @param firstParent first parent
	 * @param secondParent second parent
	 * @param crossoverMaxDepth maximum depth of the resulting tree
	 * @return two children produced by crossover
	 */
	private List<Individual> performCorssover(Individual firstParent, Individual secondParent, int crossoverMaxDepth) {

		Node firstParentTree = firstParent.getTree();
		Node secondParentTree = secondParent.getTree();

		Node firstPoint = selector.select(firstParentTree);
		Node secondPoint = selector.select(secondParentTree);

		if ((firstParentTree.getDepth() - firstPoint.getDepth() + secondPoint.getDepth()) <= crossoverMaxDepth) {
			if (firstPoint == firstParentTree) {
				firstParentTree = secondPoint;

			} else {
				insertSubtree(firstParentTree, firstPoint, secondPoint);
			}
		}

		if ((secondParentTree.getDepth() - secondPoint.getDepth() + firstPoint.getDepth()) <= crossoverMaxDepth) {
			if (secondPoint == secondParentTree) {
				secondParentTree = firstPoint;
			} else {
				insertSubtree(secondParentTree, secondPoint, firstPoint);
			}
		}

		List<Individual> newIndividuals = new ArrayList<>();
		newIndividuals.add(new Individual(firstParentTree));
		newIndividuals.add(new Individual(secondParentTree));

		return newIndividuals;
	}

	/**
	 * Replaces a subtree at the insertion point with the given insert.
	 * @param root root of the tree
	 * @param insertionPoint root of the subtree to be replaced
	 * @param insert replacement subtree
	 * @return true if the subtree has been replaced, false if the insertion point does not belong to the given tree
	 */
	private boolean insertSubtree(Node root, Node insertionPoint, Node insert) {

		if (!(root instanceof FunctionNode))
			return false;
		
		FunctionNode treeRoot = (FunctionNode) root;

		if (treeRoot.getIfTrue() == insertionPoint) {
			treeRoot.setIfTrue(insert);
			return true;
			
		} else if (treeRoot.getIfFalse() == insertionPoint) {
			treeRoot.setIfFalse(insert);
			return true;
			
		}else{
			return insertSubtree(treeRoot.getIfTrue(), insertionPoint, insert)
					|| insertSubtree(treeRoot.getIfFalse(), insertionPoint, insert);
		}

		

	}
	

	@Override
	public String toString() {
		return "Genetic Programming";
	}

	@Override
	public Robot readSolutionFromFile(Path filePath) throws IOException {
		byte[] encoded = Files.readAllBytes(filePath);
		String individual = new String(encoded);

		try {
			return Individual.parseIndividual(individual);
		} catch (ParsingException e) {
			throw new RobotFormatException("Unable to parse Robot from file.");
		}
	}

	@Override
	public void writeSolutionToFile(Path filePath, Robot robot) throws IOException {

		Files.write(filePath, robot.toString().getBytes());

	}

}
