package se.pbt.shufflelab.handling.shuffle;

import se.pbt.shufflelab.deck.Deck;
import se.pbt.shufflelab.deck.card.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

/**
 * Shuffles a deck using the overhand shuffle technique.
 *
 * <p>An overhand shuffle repeatedly removes small packets from the top of
 * the remaining deck and places each packet on top of the previously
 * transferred cards. This reverses the order of the transferred packets
 * while preserving the order of cards within each packet.</p>
 *
 * <p>The maximum packet size controls how many cards may be transferred
 * in a single step. Smaller packet sizes result in finer transfers, while
 * larger packet sizes preserve larger sections of the original deck.</p>
 */
public final class OverhandShuffle implements Shuffle {

    private final int maxPacketSize;

    /**
     * Creates an overhand shuffle with the specified maximum packet size.
     *
     * @param maxPacketSize the maximum number of cards that may be transferred
     *                      in a single packet
     * @throws IllegalArgumentException if {@code maxPacketSize} is less than one
     */
    public OverhandShuffle(int maxPacketSize) {
        if (maxPacketSize < 1) {
            throw new IllegalArgumentException(
                    "maxPacketSize must be at least 1"
            );
        }

        this.maxPacketSize = maxPacketSize;
    }

    /**
     * Performs an overhand shuffle on the given deck.
     *
     * <p>Cards are repeatedly removed from the top of a temporary source deck
     * in randomly sized packets. Each packet is placed on top of the receiving
     * pile until all cards have been transferred.</p>
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

        List<Card> source = new ArrayList<>(deck);
        List<Card> shuffledDeck = new ArrayList<>(deck.size());

        while (!source.isEmpty()) {
            int packetSize = random.nextInt(
                    1,
                    Math.min(maxPacketSize, source.size()) + 1
            );

            List<Card> packet = new ArrayList<>(
                    source.subList(0, packetSize)
            );

            source.subList(0, packetSize).clear();
            shuffledDeck.addAll(0, packet);
        }

        deck.clear();
        deck.addAll(shuffledDeck);
    }
}
