package se.pbt.shufflelab.manipulation.operation.cut;

import se.pbt.shufflelab.deck.Deck;
import se.pbt.shufflelab.deck.card.Card;
import se.pbt.shufflelab.manipulation.operation.split.DeckSplitter;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

/**
 * Cuts a deck by moving the top packet below the bottom packet.
 */
public class DeckCutter {

    private final DeckSplitter deckSplitter;

    /**
     * Creates a cut operation.
     *
     * @param deckSplitter determines where the deck is cut
     */
    public DeckCutter(DeckSplitter deckSplitter) {
        this.deckSplitter = deckSplitter;
    }

    /**
     * Cuts the given deck in place.
     *
     * @param deck the deck to cut
     * @param random a source of controlled randomness
     */
    public void cut(Deck deck, RandomGenerator random) {
        List<List<Card>> packets = deckSplitter.split(deck, random);

        List<Card> cutDeck = moveTopPacketBelowBottomPacket(packets);

        replaceDeckOrder(deck, cutDeck);
    }

    private List<Card> moveTopPacketBelowBottomPacket(List<List<Card>> packets) {
        List<Card> topPacket = packets.get(0);
        List<Card> bottomPacket = packets.get(1);

        List<Card> cutDeck = new ArrayList<>(
                topPacket.size() + bottomPacket.size()
        );

        cutDeck.addAll(bottomPacket);
        cutDeck.addAll(topPacket);

        return cutDeck;
    }

    private void replaceDeckOrder(
            Deck deck,
            List<Card> newOrder) {

        deck.clear();
        deck.addAll(newOrder);
    }
}