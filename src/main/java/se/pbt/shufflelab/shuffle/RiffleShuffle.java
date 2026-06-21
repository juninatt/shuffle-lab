package se.pbt.shufflelab.shuffle;

import se.pbt.shufflelab.card.Card;
import se.pbt.shufflelab.operation.split.DeckSplitter;
import se.pbt.shufflelab.operation.interleave.Interleaver;

import java.util.List;
import java.util.random.RandomGenerator;

/**
 * Simulates a riffle shuffle by splitting the deck and interleaving the packets.
 */
public class RiffleShuffle implements Shuffle {

    private final DeckSplitter splitter;
    private final Interleaver interleaver;

    public RiffleShuffle(
            DeckSplitter splitter,
            Interleaver interleaver) {

        this.splitter = splitter;
        this.interleaver = interleaver;
    }

    @Override
    public void apply(List<Card> deck, RandomGenerator random) {
        List<List<Card>> packets = splitter.split(deck, random);

        List<Card> shuffled = interleaver.interleave(
                packets.get(0),
                packets.get(1),
                random
        );

        deck.clear();
        deck.addAll(shuffled);
    }
}
