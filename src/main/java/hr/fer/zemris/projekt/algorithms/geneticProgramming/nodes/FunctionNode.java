package hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes;

import java.util.concurrent.ThreadLocalRandom;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.functionNodes.IfCurrentBottle;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.functionNodes.IfCurrentEmpty;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.functionNodes.IfDownBottle;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.functionNodes.IfDownEmpty;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.functionNodes.IfDownWall;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.functionNodes.IfLeftBottle;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.functionNodes.IfLeftEmpty;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.functionNodes.IfLeftWall;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.functionNodes.IfRightBottle;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.functionNodes.IfRightEmpty;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.functionNodes.IfRightWall;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.functionNodes.IfUpBottle;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.functionNodes.IfUpEmpty;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.functionNodes.IfUpWall;
import hr.fer.zemris.projekt.grid.Field;

/**
 * <p>
 * An implementation of the {@link Node} interface which represents a
 * non-terminal node of the program tree.
 * </p>
 * <p>
 * Each function node contains two children. If the condition of the function
 * node if true, the ifTrue child is evaluated and the result of its tree is
 * returned, otherwise the ifFalse child is evaluated and its result returned.
 * </p>
 * <p>
 * The method evaluate works correctly only if both children are trees
 * terminated by {@link Terminal} nodes.
 * </p>
 * 
 * @author Dunja Vesinger
 * @version 1.0.0
 */
public abstract class FunctionNode implements Node {
	/**
	 * Tree which is evaluated if the condition of the function node is true.
	 */
	private Node ifTrue;
	/**
	 * Tree which is evaluated if the condition of the function node is false.
	 */
	private Node ifFalse;
	/**
	 * Depth of the tree.
	 */
	private int depth;

	/**
	 * Initializes both children to null.
	 */
	public FunctionNode() {
		calclulateDepth();
	}

	/**
	 * Initializes both children nodes to the given values.
	 * 
	 * @param ifTrue
	 *            tree evaluated if condition is true
	 * @param ifFalse
	 *            tree evaluated if condition is false
	 */
	public FunctionNode(Node ifTrue, Node ifFalse) {

		this.ifTrue = ifTrue;
		this.ifFalse = ifFalse;

		calclulateDepth();
	}

	/**
	 * Calculates depth of the current tree.
	 */
	private void calclulateDepth() {
		if (ifTrue == null && ifFalse == null)
			depth = 0;
		else if (ifTrue != null && ifFalse == null)
			depth = ifTrue.getDepth() + 1;
		else if (ifFalse != null && ifTrue == null)
			depth = ifFalse.getDepth() + 1;
		else
			depth = 1 + Integer.max(ifTrue.getDepth(), ifFalse.getDepth());

	}

	/**
	 * Sets ifTrue child node to the given value.
	 * 
	 * @param ifTrue
	 *            tree evaluated if condition is true
	 */
	public void setIfTrue(Node ifTrue) {
		if (ifTrue.getClass() == this.getClass()) {
			FunctionNode duplicate = (FunctionNode) ifTrue;
			this.setIfTrue(duplicate.getIfTrue());
		} else {
			this.ifTrue = ifTrue;
			calclulateDepth();
		}

	}

	/**
	 * Sets ifFalse child node to the given value.
	 * 
	 * @param ifFalse
	 *            tree evaluated if condition is false
	 */
	public void setIfFalse(Node ifFalse) {
		if (ifFalse.getClass() == this.getClass()) {
			FunctionNode duplicate = (FunctionNode) ifFalse;
			this.setIfFalse(duplicate.getIfFalse());
		} else {
			this.ifFalse = ifFalse;
			calclulateDepth();
		}
	}

	/**
	 * Returns the ifTrue child node.
	 * 
	 * @return tree evaluated if condition is true
	 */
	public Node getIfTrue() {
		return ifTrue;
	}

	/**
	 * Returns the ifFalse child node.
	 * 
	 * @return tree evaluated if condition is false
	 */
	public Node getIfFalse() {
		return ifFalse;
	}

	@Override
	public int getDepth() {

		return depth;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(ifTrue.toString());
		sb.append(",");
		sb.append(ifFalse.toString());
		sb.append(")");

		return sb.toString();
	}

	@Override
	/**
	 * @throws NullPointerException
	 *             if any of the children in the subtree which is evaluated
	 *             equals to null.
	 */
	public Move evaluate(Field current, Field left, Field right, Field up, Field down) {

		if (evaluateCondition(current, left, right, up, down)) {
			return ifTrue.evaluate(current, left, right, up, down);
		}
		return ifFalse.evaluate(current, left, right, up, down);
	}

	/**
	 * Returns a random non-terminal node with both of its children set to null.
	 * 
	 * @return random non-terminal node
	 */
	public static FunctionNode randomFunctionNode() {

		int random = ThreadLocalRandom.current().nextInt(0, 14);

		switch (random) {
		case 0:
			return new IfCurrentBottle();
		case 1:
			return new IfCurrentEmpty();
		case 2:
			return new IfDownBottle();
		case 3:
			return new IfDownEmpty();
		case 4:
			return new IfDownWall();
		case 5:
			return new IfLeftBottle();
		case 6:
			return new IfLeftEmpty();
		case 7:
			return new IfLeftWall();
		case 8:
			return new IfRightBottle();
		case 9:
			return new IfRightEmpty();
		case 10:
			return new IfRightWall();
		case 11:
			return new IfUpBottle();
		case 12:
			return new IfUpEmpty();
		default:
			return new IfUpWall();

		}
	}

	/**
	 * Returns a random non-terminal node from the minimal set of non-terminal
	 * nodes required for the algorithm to work correctly with both of its
	 * children set to null.
	 * 
	 * @return random non-terminal node
	 */
	public static FunctionNode reducedRandomFunctionNode() {

		int random = ThreadLocalRandom.current().nextInt(0, 9);

		switch (random) {
		case 0:
			return new IfCurrentBottle();
		case 1:
			return new IfDownBottle();
		case 2:
			return new IfDownWall();
		case 3:
			return new IfLeftBottle();
		case 4:
			return new IfLeftWall();
		case 5:
			return new IfRightBottle();
		case 6:
			return new IfRightWall();
		case 7:
			return new IfUpBottle();
		default:
			return new IfUpWall();

		}
	}

	@Override
	public Node copy() {
		Node ifTrue = null;
		Node ifFalse = null;
		if (this.ifTrue != null) {
			ifTrue = this.ifTrue.copy();
		}
		if (this.ifFalse != null) {
			ifFalse = this.ifFalse.copy();
		}

		return copyThisNode(ifTrue, ifFalse);
	}

	/**
	 * Creates a deep copy only of this node (not its children) and sets its
	 * children to ifTrue and ifFalse.
	 * 
	 * @param ifTrue
	 *            tree to be set as ifTrue child
	 * @param ifFalse
	 *            tree to be set as ifFalse child
	 * @return copy of this node
	 */
	protected abstract Node copyThisNode(Node ifTrue, Node ifFalse);

	/**
	 * Evaluates the condition of this node depending on the given values of
	 * {@link Filed} elements.
	 * 
	 * @param current
	 *            contents of the current field
	 * @param left
	 *            contents of the field on the left
	 * @param right
	 *            contents of the field on the right
	 * @param up
	 *            contents of the field above
	 * @param down
	 *            contents of the field below
	 * @return true if the condition is true, false otherwise
	 */
	protected abstract boolean evaluateCondition(Field current, Field left, Field right, Field up, Field down);

}
