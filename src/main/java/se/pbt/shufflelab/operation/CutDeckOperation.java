package se.pbt.shufflelab.operation;

import se.pbt.shufflelab.card.Card;
import se.pbt.shufflelab.shuffle.Shuffle;
import se.pbt.shufflelab.operation.split.DeckSplitter;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

/**
 * Simulates cutting a deck by moving the top packet below the bottom packet.
 */
public class CutDeckOperation implements Shuffle {

    private final DeckSplitter splitter;

    public CutDeckOperation(DeckSplitter splitter) {
        this.splitter = splitter;
    }

    /**
     * Cuts the deck by moving the top packet below the bottom packet.
     *
     * @param deck the deck to cut
     * @param random a source of controlled randomness
     */
    @Override
    public void apply(List<Card> deck, RandomGenerator random) {
        List<List<Card>> packets = splitter.split(deck, random);

        List<Card> topPacket = packets.get(0);
        List<Card> bottomPacket = packets.get(1);

        List<Card> cutDeck = new ArrayList<>(deck.size());

        cutDeck.addAll(bottomPacket);
        cutDeck.addAll(topPacket);

        deck.clear();
        deck.addAll(cutDeck);
    }
}