package hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.grid.Field;

/**
 * Defines a single node of a program tree. Each node of the program tree should 
 * be able to do the following:
 * <ul>
 * <li>return a {@link Move} which should be taken according to the strategy
 * described its subtree if each {@link Filed} currently has the given value; </li>
 * <li>return the depth of its subtree;</li>
 * <li>create a deep copy of its subtree.</li>
 * </ul>
 * 
 * @author Dunja Vesinger
 * @version 1.0.0
 */
public interface Node {
	
	/**
	 * Returns the {@link Move} which should be taken in case the values of the {@link Filed} 
	 * given.
	 * @param current contents of the current field
	 * @param left contents of the field on the left
	 * @param right contents of the field on the right
	 * @param up contents of the field above
	 * @param down contents of the field below
	 * @return Move which should be taken
	 */
	public Move evaluate(Field current, Field left, Field right, Field up, Field down);
	
	/**
	 * Returns the depth of its subtree.
	 * @return depth
	 */
	public int getDepth();
	
	/**
	 * Creates a deep copy of the Node and its subtree.
	 * @return copy of the Node
	 */
	public Node copy();

}
