package se.pbt.shufflelab.operation;

import se.pbt.shufflelab.card.Card;
import se.pbt.shufflelab.routine.ShuffleRoutine;

import java.util.List;
import java.util.random.RandomGenerator;

/**
 * Represents a single operation commonly performed while shuffling cards.
 *
 * <p>Examples include cutting the deck, performing a riffle merge,
 * transferring packets during an overhand shuffle, or swapping cards.
 * Multiple operations can be combined to form a complete
 * {@link ShuffleRoutine}.</p>
 */
public interface ShuffleOperation {

    /**
     * Applies this operation to the given deck.
     *
     * <p>The operation modifies the deck in place and may use controlled
     * randomness to simulate natural variation in the shuffling process.</p>
     *
     * @param deck the deck to modify
     * @param random a source of controlled randomness
     */
    void apply(List<Card> deck, RandomGenerator random);
}