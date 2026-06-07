package se.pbt.shufflelab.shuffle;

import se.pbt.shufflelab.card.Card;

import java.util.List;
import java.util.random.RandomGenerator;

/**
 * Represents a complete card shuffling strategy.
 *
 * <p>A strategy defines the full sequence of actions used to randomize
 * a deck. Examples include a Fisher-Yates shuffle, repeated riffle
 * shuffles, or other combinations of shuffle operations.</p>
 */
public interface ShuffleStrategy {

    /**
     * Applies this shuffling strategy to the given deck.
     *
     * <p>The deck is shuffled in place and may be modified by one or
     * more underlying shuffle operations.</p>
     *
     * @param deck the deck to shuffle
     * @param random a source of controlled randomness used by the strategy
     */
    void shuffle(List<Card> deck, RandomGenerator random);

    /**
     * Returns a human-readable name for the strategy.
     *
     * @return the strategy name
     */
    String name();
}