package se.pbt.shufflelab.handling.operation;

import se.pbt.shufflelab.deck.Deck;
import se.pbt.shufflelab.handling.routine.Routine;
import se.pbt.shufflelab.handling.shuffle.Shuffle;

import java.util.random.RandomGenerator;

/**
 * Represents a fundamental operation that changes the order of a deck.
 *
 * <p>Operations are the basic building blocks used to implement
 * {@link Shuffle shuffles}, which can in turn be combined into
 * complete {@link Routine routines}.</p>
 */
@FunctionalInterface
public interface Operation {

    /**
     * Applies this operation to the given deck.
     *
     * @param deck the deck to modify
     * @param random a source of controlled randomness
     */
    void apply(Deck deck, RandomGenerator random);
}