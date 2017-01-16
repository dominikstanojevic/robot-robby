package hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.functionNodes;

import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.FunctionNode;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.Node;
import hr.fer.zemris.projekt.grid.Field;

/**
 * An implementation of {@link FunctionNode} with the condition checking whether 
 * the {@link Field} left contains a bottle.
 * 
 * @author Dunja Vesinger
 * @version 1.0.0
 */
public class IfLeftBottle extends FunctionNode{
	
	public static final String NAME = "ifLeftBottle";
	
	

	public IfLeftBottle() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IfLeftBottle(Node ifTrue, Node ifFalse) {
		super(ifTrue, ifFalse);
	}

	@Override
	protected boolean evaluateCondition(Field current, Field left, Field right, Field up, Field down) {
		if(left == Field.BOTTLE) return true;
		return false;
	}
	
	@Override
	public String toString(){
		return NAME + super.toString();
	}
	
	@Override
	protected Node copyThisNode(Node ifTrue, Node ifFalse) {
		return new IfLeftBottle(ifTrue, ifFalse);
	}

	

}
