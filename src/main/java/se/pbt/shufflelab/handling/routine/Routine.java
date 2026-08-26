package se.pbt.shufflelab.handling.routine;

import se.pbt.shufflelab.deck.Deck;
import se.pbt.shufflelab.handling.shuffle.Shuffle;

import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

/**
 * A complete shuffling procedure composed of one or more {@link Shuffle} operations.
 *
 * <p>A {@code Routine} combines a sequence of shuffles, executing
 * them in order against the same deck, to model a complete real-world
 * shuffling procedure — for example, riffle shuffling a deck several times
 * before a single final cut. Individual shuffle techniques model a single
 * pass; a {@code Routine} models what a person actually does at the table.</p>
 *
 */
public class Routine implements RoutineProtocol {

    private final String name;
    private final List<Shuffle> operations;

    /**
     * Creates a new routine consisting of the given operations.
     *
     * @param name a human-readable name identifying this routine, e.g. the
     *             technique it models
     * @param operations the operations that make up the routine, applied in
     *                    order; the list is copied, so later changes to the
     *                    supplied list do not affect this routine
     * @throws NullPointerException if {@code name} or {@code operations} is
     *                               {@code null}
     */
    public Routine(String name, List<Shuffle> operations) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(operations, "operations must not be null");

        this.name = name;
        this.operations = List.copyOf(operations);
    }

    /**
     * Returns this routine's human-readable name.
     *
     * @return the name identifying this routine
     */
    public String name() {
        return name;
    }

    /**
     * Applies each operation in sequence to the given deck.
     *
     * @param deck the deck to shuffle
     * @param random a source of controlled randomness
     */
    @Override
    public void execute(Deck deck, RandomGenerator random) {
        for (Shuffle operation : operations) {
            operation.shuffle(deck, random);
        }
    }

    /**
     * Returns this routine's name.
     *
     * @return the name identifying this routine
     */
    @Override
    public String toString() {
        return name;
    }
}