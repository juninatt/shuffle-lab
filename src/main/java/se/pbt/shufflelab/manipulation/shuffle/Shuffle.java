package se.pbt.shufflelab.manipulation.shuffle;

import se.pbt.shufflelab.deck.card.Card;
import se.pbt.shufflelab.manipulation.operation.Operation;
import se.pbt.shufflelab.manipulation.routine.Routine;

import java.util.List;
import java.util.random.RandomGenerator;

/**
 * Represents a card-shuffling technique.
 *
 * <p>Shuffles combine one or more {@link Operation operations} to mix
 * the order of a deck. Multiple shuffles can in turn be combined into
 * complete {@link Routine routines}.</p>
 */
@FunctionalInterface
public interface Shuffle {

    /**
     * Shuffles the given deck.
     *
     * @param deck the deck to shuffle
     * @param random a source of controlled randomness
     */
    void shuffle(List<Card> deck, RandomGenerator random);
}
