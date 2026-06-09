package se.pbt.shufflelab.operation.riffle;

import se.pbt.shufflelab.card.Card;

import java.util.List;
import java.util.random.RandomGenerator;

/**
 * Defines how two card packets are interleaved during a riffle shuffle.
 */
public interface RiffleInterleaver {

    /**
     * Interleaves two packets into a single deck.
     *
     * @param left the first packet
     * @param right the second packet
     * @param random a source of controlled randomness
     * @return the interleaved cards
     */
    List<Card> interleave(
            List<Card> left,
            List<Card> right,
            RandomGenerator random);
}