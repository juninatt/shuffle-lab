package se.pbt.shufflelab.operation;

import se.pbt.shufflelab.card.Card;
import se.pbt.shufflelab.routine.Routine;
import se.pbt.shufflelab.shuffle.Shuffle;

import java.util.List;
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
    void apply(List<Card> deck, RandomGenerator random);
}