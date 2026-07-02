package se.pbt.shufflelab.manipulation.routine;

import se.pbt.shufflelab.deck.card.Card;
import se.pbt.shufflelab.manipulation.shuffle.Shuffle;

import java.util.List;
import java.util.random.RandomGenerator;

/**
 * Represents a complete card-shuffling procedure.
 *
 * <p>Routines combine one or more {@link Shuffle shuffles}, optionally
 * interleaved with individual operations, to produce a complete deck
 * randomization.</p>
 */
@FunctionalInterface
public interface Routine {

    /**
     * Executes this routine on the given deck.
     *
     * @param deck the deck to shuffle
     * @param random a source of controlled randomness
     */
    void execute(List<Card> deck, RandomGenerator random);
}
