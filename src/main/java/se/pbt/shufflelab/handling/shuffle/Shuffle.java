package se.pbt.shufflelab.handling.shuffle;

import se.pbt.shufflelab.deck.Deck;
import se.pbt.shufflelab.handling.operation.Operation;
import se.pbt.shufflelab.handling.routine.Routine;

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
    void shuffle(Deck deck, RandomGenerator random);
}
