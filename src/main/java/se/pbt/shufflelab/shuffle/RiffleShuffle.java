package se.pbt.shufflelab.shuffle;

import se.pbt.shufflelab.card.Card;
import se.pbt.shufflelab.operation.split.DeckSplitter;
import se.pbt.shufflelab.operation.interleave.Interleaver;

import java.util.List;
import java.util.random.RandomGenerator;

/**
 * Shuffles a deck using the riffle shuffle technique.
 *
 * <p>A riffle shuffle is performed by splitting the deck into two packets
 * and interleaving them into a single deck. The exact behavior depends
 * on the supplied {@link DeckSplitter} and {@link Interleaver},
 * allowing the shuffle to model anything from a perfect riffle to a
 * more natural human shuffle.</p>
 */
public class RiffleShuffle implements Shuffle {

    private final DeckSplitter deckSplitter;
    private final Interleaver interleaver;

    /**
     * Creates a riffle shuffle.
     *
     * @param deckSplitter determines how the deck is divided into packets
     * @param interleaver determines how the packets are interleaved
     */
    public RiffleShuffle(
            DeckSplitter deckSplitter,
            Interleaver interleaver) {

        this.deckSplitter = deckSplitter;
        this.interleaver = interleaver;
    }

    /**
     * Shuffles the given deck using a riffle shuffle.
     *
     * @param deck the deck to shuffle
     * @param random a source of controlled randomness
     */
    @Override
    public void shuffle(List<Card> deck, RandomGenerator random) {
        List<List<Card>> packets = deckSplitter.split(deck, random);

        List<Card> shuffledDeck = interleaver.interleave(
                packets.get(0),
                packets.get(1),
                random
        );

        deck.clear();
        deck.addAll(shuffledDeck);
    }
}