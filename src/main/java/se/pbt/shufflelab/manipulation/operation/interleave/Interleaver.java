package se.pbt.shufflelab.manipulation.operation.interleave;

import se.pbt.shufflelab.deck.card.Card;

import java.util.List;
import java.util.random.RandomGenerator;

/**
 * Defines how two packets are woven together into a single deck.
 */
public interface Interleaver {


    /**
     * Interleaves two packets into a single deck.
     *
     * @param topPacket the packet originating from the top portion of the deck
     * @param bottomPacket the packet originating from the bottom portion of the deck
     * @param random a source of controlled randomness
     * @return the interleaved cards
     */
    List<Card> interleave(
            List<Card> topPacket,
            List<Card> bottomPacket,
            RandomGenerator random);
}
