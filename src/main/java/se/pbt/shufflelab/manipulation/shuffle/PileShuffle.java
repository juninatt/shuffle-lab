package se.pbt.shufflelab.manipulation.shuffle;

import se.pbt.shufflelab.deck.Deck;
import se.pbt.shufflelab.deck.card.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

/**
 * Shuffles a deck by dealing its cards into multiple piles and then
 * collecting those piles in reverse order.
 *
 * <p>Cards are dealt one at a time from the top of the deck, moving between
 * the piles in sequence. Once all cards have been dealt, the piles are
 * collected from the last pile to the first. The order of cards within each
 * pile is preserved.</p>
 *
 * <p>A pile shuffle is deterministic for a given deck and pile count. It
 * rearranges the deck into a visibly different order but does not introduce
 * randomness by itself.</p>
 */
public final class PileShuffle implements Shuffle {

    private final int pileCount;

    /**
     * Creates a pile shuffle with the specified number of piles.
     *
     * @param pileCount the number of piles into which the deck is dealt
     * @throws IllegalArgumentException if {@code pileCount} is less than two
     */
    public PileShuffle(int pileCount) {
        if (pileCount < 2) {
            throw new IllegalArgumentException(
                    "pileCount must be at least 2"
            );
        }

        this.pileCount = pileCount;
    }

    /**
     * Performs a pile shuffle on the given deck.
     *
     * <p>The supplied random generator is not used because this implementation
     * follows a fixed dealing and collection order. It remains part of the
     * method signature to satisfy the common {@link Shuffle} contract.</p>
     *
     * @param deck   the deck to shuffle
     * @param random a source of controlled randomness
     * @throws NullPointerException if {@code deck} or {@code random} is
     *                              {@code null}
     */
    @Override
    public void shuffle(Deck deck, RandomGenerator random) {
        Objects.requireNonNull(deck, "deck must not be null");
        Objects.requireNonNull(random, "random must not be null");

        List<List<Card>> piles = createEmptyPiles();

        for (int cardIndex = 0; cardIndex < deck.size(); cardIndex++) {
            int pileIndex = cardIndex % pileCount;
            piles.get(pileIndex).add(deck.get(cardIndex));
        }

        deck.clear();

        for (int pileIndex = piles.size() - 1; pileIndex >= 0; pileIndex--) {
            deck.addAll(piles.get(pileIndex));
        }
    }

    /**
     * Creates the empty receiving piles for this shuffle.
     *
     * @return the initialized piles
     */
    private List<List<Card>> createEmptyPiles() {
        List<List<Card>> piles = new ArrayList<>(pileCount);

        for (int pileIndex = 0; pileIndex < pileCount; pileIndex++) {
            piles.add(new ArrayList<>());
        }

        return piles;
    }
}