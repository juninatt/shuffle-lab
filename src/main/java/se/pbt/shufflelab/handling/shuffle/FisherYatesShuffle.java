package se.pbt.shufflelab.handling.shuffle;

import se.pbt.shufflelab.deck.Deck;
import se.pbt.shufflelab.deck.card.Card;

import java.util.Objects;
import java.util.random.RandomGenerator;

/**
 * Shuffles a deck into a mathematically uniform random permutation using the
 * Fisher–Yates algorithm.
 *
 * <p>This does not model any real-world shuffling technique. It exists as a
 * reference point: since every possible card order is equally likely, it
 * represents the ideal that a real shuffling routine is being compared
 * against, letting how close a technique gets to true randomness be measured
 * directly rather than only ranked against other techniques.</p>
 *
 * <p>Unlike other shuffles, this one is not affected by simulated performer
 * skill — there is no way to perform it "better" or "worse".</p>
 */
public final class FisherYatesShuffle implements Shuffle {

    /**
     * Shuffles the given deck into a uniform random permutation.
     *
     * @param deck the deck to shuffle
     * @param random a source of controlled randomness
     * @throws NullPointerException if {@code deck} or {@code random} is
     *                              {@code null}
     */
    @Override
    public void shuffle(Deck deck, RandomGenerator random) {
        Objects.requireNonNull(deck, "deck must not be null");
        Objects.requireNonNull(random, "random must not be null");

        for (int index = deck.size() - 1; index > 0; index--) {
            int swapIndex = random.nextInt(index + 1);

            Card card = deck.get(index);
            deck.set(index, deck.get(swapIndex));
            deck.set(swapIndex, card);
        }
    }
}
