package se.pbt.shufflelab.manipulation.shuffle;

import se.pbt.shufflelab.deck.Deck;
import se.pbt.shufflelab.manipulation.operation.interleave.InterleaveStart;
import se.pbt.shufflelab.manipulation.operation.interleave.PerfectInterleaver;
import se.pbt.shufflelab.manipulation.operation.split.BalancedDeckSplitter;

import java.util.Objects;
import java.util.random.RandomGenerator;

/**
 * Shuffles a deck using the Faro shuffle technique.
 *
 * <p>A Faro shuffle divides the deck into two equal packets before
 * perfectly interleaving them. The {@link FaroType} determines whether
 * the interleave begins with the top packet (out Faro) or the bottom
 * packet (in Faro).</p>
 *
 * <p>A Faro shuffle is deterministic and therefore produces the same
 * result whenever it is applied to the same deck order.</p>
 */
public final class FaroShuffle implements Shuffle {

    private final Shuffle delegate;

    /**
     * Creates a Faro shuffle of the specified type.
     *
     * @param type the Faro shuffle variant
     * @throws NullPointerException if {@code type} is {@code null}
     */
    public FaroShuffle(FaroType type) {
        Objects.requireNonNull(type, "type must not be null");

        delegate = new RiffleShuffle(
                new BalancedDeckSplitter(0),
                new PerfectInterleaver(
                        switch (type) {
                            case FaroType.OUT -> InterleaveStart.TOP;
                            case FaroType.IN -> InterleaveStart.BOTTOM;
                        }
                )
        );
    }

    /**
     * Performs a Faro shuffle on the given deck.
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

        delegate.shuffle(deck, random);
    }
}