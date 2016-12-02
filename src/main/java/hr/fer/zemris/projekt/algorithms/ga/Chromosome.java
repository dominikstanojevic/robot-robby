package hr.fer.zemris.projekt.algorithms.ga;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.grid.Field;

import java.util.Comparator;
import java.util.Random;

public class Chromosome implements Robot {

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
     * {@link Comparator} of the {@link Chromosome} by fitness.
     */
    public static final Comparator<Chromosome> BY_FITNESS = Comparator.comparingDouble(c -> c.fitness);

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
            chromosome.moves[i] = MOVES[random.nextInt(MOVES.length)];
        }

        return chromosome;
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
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public Chromosome mutate(Random random) {
        int index = random.nextInt(moves.length);
        moves[index] = MOVES[random.nextInt(MOVES.length)];

        return this;
    }

    public Chromosome[] mate(Chromosome other, Random random) {
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
