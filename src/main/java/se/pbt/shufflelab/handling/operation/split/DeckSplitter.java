package se.pbt.shufflelab.handling.operation.split;

import se.pbt.shufflelab.deck.Deck;
import se.pbt.shufflelab.deck.card.Card;

import java.util.List;
import java.util.random.RandomGenerator;

/**
 * Splits a deck into multiple packets.
 *
 * <p>Packet sizes may vary depending on the implementation. This abstraction
 * is intended to model the way players naturally divide a deck during
 * shuffling techniques such as cutting, riffle shuffling, and overhand
 * shuffling.</p>
 */
public interface DeckSplitter {

    /**
     * Splits the given deck into one or more packets.
     *
     * @param deck the deck to split
     * @param random a source of controlled randomness
     * @return the resulting packets in their original order
     */
    List<List<Card>> split(Deck deck, RandomGenerator random);
}
