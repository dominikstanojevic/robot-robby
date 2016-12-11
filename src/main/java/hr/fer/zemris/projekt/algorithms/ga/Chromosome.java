package hr.fer.zemris.projekt.algorithms.ga;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.grid.Field;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Random;

/**
 * <p>Implementation of the {@link Robot} class, specialized for training in a
 * {@link GeneticAlgorithm genetic algorithm}. In this implementation, each action
 * is coded directly for a specified environment.</p>
 *
 * <p>Due to it's use in a genetic algorithm, this class contains methods for mutating
 * and crossing over. The crossover operator is a
 * <a href="https://en.wikipedia.org/wiki/Crossover_(genetic_algorithm)#Single-point_crossover">
 * single-point crossover</a> operator, while the mutation operator chooses one {@link Move}
 * in the chromosome at random, and randomizes it's value.</p>
 *
 * @author Leon Luttenberger
 */
public class Chromosome implements Robot, Serializable {

    /**
     * Unique identifier.
     */
    private static final long serialVersionUID = 1274476794769719180L;

    /**
     * {@link Comparator} of the {@link Chromosome} by fitness.
     */
    public static final Comparator<Chromosome> BY_FITNESS = Comparator.comparingDouble(c -> c.fitness);

    /**
     * Number of possible situations that a robot can find itself in.
     */
    private static final int NUMBER_OF_SITUATIONS = 2 * 3 * 3 * 3 * 3;

    /**
     * Array of all the possible {@link Move} objects.
     */
    private static final Move[] MOVES = Move.values();

    /**
     * Array of all the possible {@link Field} objects.
     */
    private static final Field[] FIELDS = Field.values();

    /**
     * The genome of the chromosome, represents the moves that the robot will make in any
     * given situation.
     */
    private Move[] moves = new Move[NUMBER_OF_SITUATIONS];

    /**
     * Fitness of the chromosome.
     */
    private double fitness;

    /**
     * Constructs a {@link Chromosome} with the specified genome.
     * @param moves gene of the {@code Chromosome}
     */
    public Chromosome(Move[] moves) {
        this.moves = moves;
    }

    /**
     * Generates a {@link Chromosome} with a random genome.
     * @param random {@link Random} object to use
     * @return {@code Chromosome} with random genome
     */
    public static Chromosome generateRandom(Random random) {
        Chromosome chromosome = new Chromosome(new Move[NUMBER_OF_SITUATIONS]);

        for (int i = 0; i < chromosome.moves.length; i++) {
            chromosome.moves[i] = RANDOM_MOVE(random);
        }

        return chromosome;
    }

    /**
     * Returns a random {@link Move} object.
     * @param random {@link Random} object
     * @return a random {@link Move} object.
     */
    private static Move RANDOM_MOVE(Random random) {
        return MOVES[random.nextInt(MOVES.length)];
    }

    /**
     * Returns the fitness of the chromosome.
     * @return the fitness of the chromosome
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * Sets the fitness of the chromosome to the specified value.
     * @param fitness value to set the fitness to
     */
    void setFitness(double fitness) {
        this.fitness = fitness;
    }

    /**
     * Mutates this {@code Chromosome}. The mutation is performed on a single
     * random move/action, which is then changed into another move/action.
     * @param random {@link Random} object
     * @return {@code this}
     */
    Chromosome mutate(Random random) {
        int index = random.nextInt(moves.length);
        moves[index] = MOVES[random.nextInt(MOVES.length)];

        return this;
    }

    /**
     * <p>Crosses this {@link Chromosome} with the specified one, and returns their two children
     * {@code Chromosomes}.</p>
     * <p>This method uses the
     * <a href="https://en.wikipedia.org/wiki/Crossover_(genetic_algorithm)#Single-point_crossover">
     * single-point crossover</a>, meaning it chooses one random point from the genome, and combines
     * each chosen part of the genome with the other part of the second genome.</p>
     *
     * @param other  {@code Chromosome} to crossover with
     * @param random {@link Random} object to use
     * @return children from the crossover
     */
    Chromosome[] crossover(Chromosome other, Random random) {
        int crossoverPoint = random.nextInt(NUMBER_OF_SITUATIONS);

        Move[] moves1 = new Move[NUMBER_OF_SITUATIONS];
        Move[] moves2 = new Move[NUMBER_OF_SITUATIONS];

        System.arraycopy(this.moves, 0, moves1, 0, crossoverPoint);
        System.arraycopy(other.moves, crossoverPoint, moves1, crossoverPoint, moves2.length - crossoverPoint);

        System.arraycopy(other.moves, 0, moves2, 0, crossoverPoint);
        System.arraycopy(this.moves, crossoverPoint, moves2, crossoverPoint, moves2.length - crossoverPoint);

        return new Chromosome[] {
                new Chromosome(moves1),
                new Chromosome(moves2)
        };
    }

    @Override
    public Move nextMove(Field current, Field left, Field right, Field up, Field down) {
        int index = indexOf(current, left, right, up, down);
        return moves[index];
    }

    /**
     * Calculates the index of this environment in the {@code moves} array.
     *
     * @param current the {@link Field} that this robot is standing on
     * @param left the {@code Field} to the left of the robot
     * @param right the {@code Field} to the right of the robot
     * @param up the {@code Field} above the robot
     * @param down the {@code Field} below the robot
     * @return index of this environment in the {@code moves} array
     */
    private int indexOf(Field current, Field left, Field right, Field up, Field down) {
        if (current == Field.WALL) {
            throw new IllegalArgumentException("Robot cannot be on a wall.");
        }

        int result = indexOf(current);
        result *= FIELDS.length;

        result += indexOf(left);
        result *= FIELDS.length;

        result += indexOf(right);
        result *= FIELDS.length;

        result += indexOf(up);
        result *= FIELDS.length;

        result += indexOf(down);

        return result;
    }

    /**
     * Calculates the index of the specified {@link Field}.
     * @param field field to retrieve the index of
     * @return index of the specified {@code Field}
     */
    private int indexOf(Field field) {
        switch (field) {
            case EMPTY:
                return 0;
            case BOTTLE:
                return 1;
            case WALL:
                return 2;
            default:
                throw new IllegalArgumentException("Unknown move type: " + field);
        }
    }
}
