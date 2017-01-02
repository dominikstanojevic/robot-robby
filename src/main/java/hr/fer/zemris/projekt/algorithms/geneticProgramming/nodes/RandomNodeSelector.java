package hr.fer.zemris.projekt.algorithms.geneticProgramming.nodes;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Selector of a random {@link Node} from the given program tree. Each node in
 * the tree has an equal probability of being selected.
 * 
 * @author Dunja Vesinger
 * @version 1.0.0
 */
public class RandomNodeSelector {
    /**
     * Currently selected node.
     */
    private Node currentlySelected;
    /**
     * Number of nodes seen so far.
     */
    private int seenNodes;

    /**
     * Creates a new instance of RandomNodeSelector.
     */
    public RandomNodeSelector() {
    }

    /**
     * Selects a random node from the given program tree. Each node in the tree
     * has an equal probability of being selected.
     * 
     * @param root
     *            root of the program tree
     * @return random node from the program tree
     */
    public Node select(Node root) {
        currentlySelected = null;
        seenNodes = 0;

        selectRandomNode(root);
        return currentlySelected;
    }

    /**
     * Selects a random node from the given program tree and sets it as the
     * currently selected node.
     * 
     * @param root
     *            root of the program tree
     */
    private void selectRandomNode(Node root) {

        if (seenNodes == 0)
            currentlySelected = root;
        else {
            int random = ThreadLocalRandom.current().nextInt(0, seenNodes);
            if (random == (seenNodes - 1))
                currentlySelected = root;
        }

        seenNodes++;

        if (root instanceof FunctionNode) {
            FunctionNode treeRoot = (FunctionNode) root;
            selectRandomNode(treeRoot.getIfTrue());
            selectRandomNode(treeRoot.getIfFalse());
        }

    }

}
