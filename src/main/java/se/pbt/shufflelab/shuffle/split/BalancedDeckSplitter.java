package se.pbt.shufflelab.shuffle.split;

import se.pbt.shufflelab.card.Card;

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

    private final int tolerance;

    /**
     * Creates a splitter that divides a deck into two packets near the middle.
     *
     * @param tolerance the maximum allowed deviation from the exact middle
     *                  when selecting the split point
     */
    public BalancedDeckSplitter(int tolerance) {
        if (tolerance < 0) {
            throw new IllegalArgumentException("tolerance must not be negative");
        }

        this.tolerance = tolerance;
    }

    /**
     * Splits a deck into two packets near the middle to simulate a natural cut.
     *
     * @param deck the deck to split
     * @param random a source of controlled randomness
     * @return the resulting packets
     */
    @Override
    public List<List<Card>> split(List<Card> deck, RandomGenerator random) {
        int middle = deck.size() / 2;
        int min = Math.max(1, middle - tolerance);
        int max = Math.min(deck.size() - 1, middle + tolerance);
        int splitIndex = random.nextInt(min, max + 1);

        List<Card> firstPacket = new ArrayList<>(deck.subList(0, splitIndex));
        List<Card> secondPacket = new ArrayList<>(deck.subList(splitIndex, deck.size()));

        return List.of(firstPacket, secondPacket);
    }
}