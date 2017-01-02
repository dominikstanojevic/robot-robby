package hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.functionNodes;

import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.FunctionNode;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes.Node;
import hr.fer.zemris.projekt.grid.Field;

/**
 * An implementation of {@link FunctionNode} with the condition checking whether
 * the {@link Field} left is empty.
 * 
 * @author Dunja Vesinger
 * @version 1.0.0
 */
public class IfLeftEmpty extends FunctionNode {

    public static final String NAME = "ifLeftEmpty";

    public IfLeftEmpty() {
        super();
        // TODO Auto-generated constructor stub
    }

    public IfLeftEmpty(Node ifTrue, Node ifFalse) {
        super(ifTrue, ifFalse);
    }

    @Override
    protected boolean evaluateCondition(Field current, Field left, Field right, Field up, Field down) {
        if (left == Field.EMPTY)
            return true;
        return false;
    }

    @Override
    public String toString() {
        return NAME + super.toString();
    }

    @Override
    protected Node copyThisNode(Node ifTrue, Node ifFalse) {
        return new IfLeftEmpty(ifTrue, ifFalse);
    }

}
