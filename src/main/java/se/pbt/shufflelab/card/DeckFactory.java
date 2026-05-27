package se.pbt.shufflelab.card;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating standard playing-card decks.
 */
public final class DeckFactory {

    private DeckFactory() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Creates a standard 52-card deck.
     *
     * @return a mutable list containing 52 unique cards
     */
    public static List<Card> standardDeck() {
        List<Card> deck = new ArrayList<>(52);

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(suit, rank));
            }
        }

        return deck;
    }
}