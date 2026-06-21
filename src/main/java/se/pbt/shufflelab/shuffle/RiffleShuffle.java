package se.pbt.shufflelab.shuffle;

import se.pbt.shufflelab.card.Card;
import se.pbt.shufflelab.operation.split.DeckSplitter;
import se.pbt.shufflelab.operation.interleave.Interleaver;

import java.util.List;
import java.util.random.RandomGenerator;

/**
 * Simulates a riffle shuffle.
 *
 * <p>A riffle shuffle is performed by splitting a deck into two packets
 * and interleaving cards from both packets into a single deck. It is one
 * of the most common and effective card shuffling techniques.</p>
 *
 * <p>The exact behavior depends on the supplied {@link DeckSplitter} and
 * {@link Interleaver} implementations. For example, the shuffle may be
 * performed as a perfect riffle or a more realistic human riffle.</p>
 */
public class RiffleShuffle implements Shuffle {

    private final DeckSplitter deckSplitter;
    private final Interleaver interleaver;

    public RiffleShuffle(
            DeckSplitter deckSplitter,
            Interleaver interleaver) {

        this.deckSplitter = deckSplitter;
        this.interleaver = interleaver;
    }

    /**
     * Applies a riffle shuffle to the given deck.
     *
     * @param deck the deck to shuffle
     * @param random a source of controlled randomness
     */
    @Override
    public void apply(List<Card> deck, RandomGenerator random) {
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
