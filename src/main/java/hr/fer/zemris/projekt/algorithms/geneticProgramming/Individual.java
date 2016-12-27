package hr.fer.zemris.projekt.algorithms.geneticProgramming;

import java.util.concurrent.ThreadLocalRandom;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.FunctionNode;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.Node;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.ParsingException;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.ProgramTreeParser;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.TerminalNode;
import hr.fer.zemris.projekt.grid.Field;

/**
 * An implementation of {@link Robot} used in {@link GeneticProgrammin} algorithm.
 * 
 * @author Dunja Vesinger
 * @version 1.0.0
 */
public class Individual implements Robot, Comparable<Individual> {
	/**
	 * Raw fitness of the individual.
	 */
	private double rawFitness;
	
	private double standardFitness;
	/**
	 * Program tree of the individual.
	 */
	private Node tree;

	/**
	 * Creates a new Individual with the given tree.
	 * @param tree program tree
	 */
	public Individual(Node tree){
		this.tree = tree;
	}

	/**
	 * Created a new Individual with a given tree and previously evaluated raw fitness.
	 * @param rawFitness previously evaluated fitness
	 * @param tree program tree
	 */
	public Individual(int rawFitness, Node tree) {
		super();
		this.rawFitness = rawFitness;
		this.tree = tree;
	}


	/**
	 * Returns the raw fitness of the individual. The fitness is 0 if the individual has
	 * not yet been evaluated.
	 * @return raw fitness
	 */
	public double getRawFitness() {
		return rawFitness;
	}

	/**
	 * Sets raw fitness of the individual on the given value.
	 * @param rawFitness raw fitness to be set
	 */
	public void setRawFitness(double rawFitness) {
		this.rawFitness = rawFitness;
	}
	
	


	public double getStandardFitness() {
		return standardFitness;
	}

	public void setStandardFitness(double standardFitness) {
		this.standardFitness = standardFitness;
	}

	/**
	 * Returns the program tree.
	 * @return program tree
	 */
	public Node getTree() {
		return tree;
	}

	/**
	 * Sets the program tree of the individual to a given value.
	 * @param tree program tree to be set
	 */
	public void setTree(Node tree) {
		this.tree = tree;
	}


	@Override
	public Move nextMove(Field current, Field left, Field right, Field up, Field down) {
		return tree.evaluate(current, left, right, up, down);
	}
	
	public static Individual parseIndividual(String individual) throws ParsingException{
		
		ProgramTreeParser parser = new ProgramTreeParser(individual);
		
		Node tree = parser.parse();
		
		return new Individual(tree);	
		
	}
	
	/**
	 * Returns new Individual with a random full tree of given depth.
	 * @param depth depth of the tree
	 * @return new Individual with a random full tree
	 * @throws IllegalArgumentException if depth is negative or 0
	 */
	public static Individual randomFullTreeIndividual(int depth){		
		return new Individual(randomNode(0, depth, true));
	}

	/**
	 * Returns new Individual with a random tree which has maximum depth greater or equal to given
	 * maximal depth.
	 * @param maxDepth maximal depth of the tree
	 * @return new Individual with a random tree
	 * @throws IllegalArgumentException if depth is negative or 0
	 */
	public static Individual randomIndividual(int maxDepth) {
		return new Individual(randomNode(0, maxDepth, false));
	}

	/**
	 * Recursively produces a random program tree.
	 * @param currDepth current depth of the tree
	 * @param maxDepth maximum depth of the tree
	 * @param full whether tree should be full or not
	 * @return random program tree
	 */
	public static Node randomNode(int currDepth, int maxDepth, boolean full) {
		
		currDepth++;
		
		if(currDepth >= maxDepth) return TerminalNode.randomTerminalNode();
		
		if(currDepth == 1 || full){
			
			//for creating random Individuals with minimal function set
			//FunctionNode root = FunctionNode.reducedRandomFunctionNode();
			
			FunctionNode root = FunctionNode.randomFunctionNode();
			root.setIfTrue(randomNode(currDepth, maxDepth, full));
			root.setIfFalse(randomNode(currDepth, maxDepth, full));
			return root;
		}		
		
		int random = ThreadLocalRandom.current().nextInt(0, 2);
		
		if(random > 0) {
			FunctionNode root = FunctionNode.randomFunctionNode();
			root.setIfTrue(randomNode(currDepth, maxDepth, full));
			root.setIfFalse(randomNode(currDepth, maxDepth, full));
			return root;
		}
		return TerminalNode.randomTerminalNode();
		
	}

	@Override
	public String toString(){
		return tree.toString();
	}

	@Override
	public int compareTo(Individual ind) {
		if(this.rawFitness < ind.rawFitness) return -1;
		else if(this.rawFitness > ind.rawFitness) return 1;
		return 0;
	}

	

	

}
