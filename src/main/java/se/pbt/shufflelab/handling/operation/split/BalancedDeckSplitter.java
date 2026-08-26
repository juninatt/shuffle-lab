package se.pbt.shufflelab.handling.operation.split;

import se.pbt.shufflelab.deck.Deck;
import se.pbt.shufflelab.deck.card.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

/**
 * Splits a deck into two roughly balanced packets.
 *
 * <p>This models a common real-world split where a player divides the deck
 * near the middle, with a small amount of natural variation.</p>
 */
public class BalancedDeckSplitter implements DeckSplitter {

    private final int maxSplitDeviation;

    /**
     * Creates a splitter that divides a deck into two packets near the middle.
     *
     * @param maxSplitDeviation the maximum allowed deviation from the exact middle
     *                  when selecting the split point
     */
    public BalancedDeckSplitter(int maxSplitDeviation) {
        if (maxSplitDeviation < 0) {
            throw new IllegalArgumentException("tolerance must not be negative");
        }

        this.maxSplitDeviation = maxSplitDeviation;
    }

    /**
     * Splits a deck into two packets near the middle.
     *
     * @param deck the deck to split
     * @param random a source of controlled randomness
     * @return the resulting packets
     */
    @Override
    public List<List<Card>> split(Deck deck, RandomGenerator random) {
        int middle = deck.size() / 2;
        int min = Math.max(1, middle - maxSplitDeviation);
        int max = Math.min(deck.size() - 1, middle + maxSplitDeviation);
        int splitIndex = random.nextInt(min, max + 1);

        List<Card> topPacket = new ArrayList<>(deck.subList(0, splitIndex));
        List<Card> bottomPacket = new ArrayList<>(deck.subList(splitIndex, deck.size()));

        return List.of(topPacket, bottomPacket);
    }
}
