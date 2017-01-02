package hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes;

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

/**
 * Parser which parses implementations of program trees consisting of instances
 * of {@link Node}.
 * 
 * @author Dunja Vesinger
 * @version 1.0.0
 */
public class ProgramTreeParser {
    /**
     * Index of the character currently being analyzed.
     */
    int currIndex;
    /**
     * Index of the last parsed character.
     */
    int lastParsedIndex;
    /**
     * Text being parsed.
     */
    String tree;

    /**
     * Creates a new ProgramTreeParser for the given tree.
     * 
     * @param tree
     *            program tree to be parsed
     */
    public ProgramTreeParser(String tree) {
        this.tree = tree;
        currIndex = 0;
        lastParsedIndex = 0;
    }

    /**
     * Parses the program tree.
     * 
     * @return program tree
     * @throws ParsingException
     *             if it's not possible to parse a program tree from the given
     *             String
     */
    public Node parse() throws ParsingException {

        while (currIndex < tree.length() && tree.charAt(currIndex) != '('
                && tree.charAt(currIndex) != ')' && tree.charAt(currIndex) != ',') {

            currIndex++;
        }

        String nodeName = tree.substring(lastParsedIndex, currIndex);

        Node node = getNode(nodeName);

        if (node instanceof TerminalNode)
            return node;

        FunctionNode root = (FunctionNode) node;

        if (tree.charAt(currIndex) == '(') {

            currIndex++;
            lastParsedIndex = currIndex;
            root.setIfTrue(parse());

            if (tree.charAt(currIndex) == ',') {

                currIndex++;
                lastParsedIndex = currIndex;
                root.setIfFalse(parse());

                if (tree.charAt(currIndex) == ')') {
                    currIndex++;
                    lastParsedIndex = currIndex;
                    return root;
                }
            }
        }

        throw new ParsingException("Invalid program tree.");
    }

    /**
     * Parses a {@link Node} from the given String.
     * 
     * @param nodeName
     *            name of the node to be parsed
     * @return node
     * @throws ParsingException
     *             if no node with the given name exists
     */
    private Node getNode(String nodeName) throws ParsingException {

        if (nodeName.equalsIgnoreCase(IfCurrentBottle.NAME))
            return new IfCurrentBottle();
        else if (nodeName.equalsIgnoreCase(IfCurrentEmpty.NAME))
            return new IfCurrentEmpty();
        else if (nodeName.equalsIgnoreCase(IfDownBottle.NAME))
            return new IfDownBottle();
        else if (nodeName.equalsIgnoreCase(IfDownEmpty.NAME))
            return new IfDownEmpty();
        else if (nodeName.equalsIgnoreCase(IfDownWall.NAME))
            return new IfDownWall();
        else if (nodeName.equalsIgnoreCase(IfLeftBottle.NAME))
            return new IfLeftBottle();
        else if (nodeName.equalsIgnoreCase(IfLeftEmpty.NAME))
            return new IfLeftEmpty();
        else if (nodeName.equalsIgnoreCase(IfLeftWall.NAME))
            return new IfLeftWall();
        else if (nodeName.equalsIgnoreCase(IfRightBottle.NAME))
            return new IfRightBottle();
        else if (nodeName.equalsIgnoreCase(IfRightEmpty.NAME))
            return new IfRightEmpty();
        else if (nodeName.equalsIgnoreCase(IfRightWall.NAME))
            return new IfRightWall();
        else if (nodeName.equalsIgnoreCase(IfUpBottle.NAME))
            return new IfUpBottle();
        else if (nodeName.equalsIgnoreCase(IfUpEmpty.NAME))
            return new IfUpEmpty();
        else if (nodeName.equalsIgnoreCase(IfUpWall.NAME))
            return new IfUpWall();

        else if (nodeName.equals(Move.COLLECT.toString()))
            return new TerminalNode(Move.COLLECT);
        else if (nodeName.equals(Move.DOWN.toString()))
            return new TerminalNode(Move.DOWN);
        else if (nodeName.equals(Move.LEFT.toString()))
            return new TerminalNode(Move.LEFT);
        else if (nodeName.equals(Move.RANDOM.toString()))
            return new TerminalNode(Move.RANDOM);
        else if (nodeName.equals(Move.RIGHT.toString()))
            return new TerminalNode(Move.RIGHT);
        else if (nodeName.equals(Move.SKIP_TURN.toString()))
            return new TerminalNode(Move.SKIP_TURN);
        else if (nodeName.equals(Move.UP.toString()))
            return new TerminalNode(Move.UP);

        throw new ParsingException("Invalid node name:" + nodeName);

    }

}
