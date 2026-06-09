package se.pbt.shufflelab.operation;

import se.pbt.shufflelab.card.Card;
import se.pbt.shufflelab.operation.riffle.RiffleInterleaver;
import se.pbt.shufflelab.split.DeckSplitter;

import java.util.List;
import java.util.random.RandomGenerator;

/**
 * Simulates a riffle shuffle by splitting the deck and interleaving the packets.
 */
public class RiffleShuffleOperation implements ShuffleOperation {

    private final DeckSplitter splitter;
    private final RiffleInterleaver interleaver;

    public RiffleShuffleOperation(
            DeckSplitter splitter,
            RiffleInterleaver interleaver) {

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