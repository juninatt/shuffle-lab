package se.pbt.shufflelab.analysis;

import se.pbt.shufflelab.deck.Deck;
import se.pbt.shufflelab.deck.card.Card;

import java.util.HashMap;
import java.util.Map;

/**
 * Builds and queries a lookup of each card's position in a reference deck.
 *
 * <p>Shared by the individual analyzers so that each shuffled deck only needs
 * to be matched against the original deck once per analysis. This assumes a
 * deck of unique cards; matching is done by card value.
 */
public final class CardPositionLookup {

    private CardPositionLookup() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Builds a lookup of each card's position in the given deck.
     *
     * @param deck the deck to index
     * @return a map from card to its position in {@code deck}
     */
    public static Map<Card, Integer> indexByPosition(Deck deck) {
        Map<Card, Integer> positions = new HashMap<>(deck.size());

        for (int position = 0; position < deck.size(); position++) {
            positions.put(deck.get(position), position);
        }

        return positions;
    }

    /**
     * Looks up a card's position in a previously built lookup.
     *
     * @param positions the position lookup to query
     * @param card the card to look up
     * @return the card's position according to {@code positions}
     * @throws IllegalArgumentException if the card is not present in the lookup
     */
    public static int positionOf(Map<Card, Integer> positions, Card card) {
        Integer position = positions.get(card);

        if (position == null) {
            throw new IllegalArgumentException(
                    "shuffled deck contains a card not present in the original deck: " + card
            );
        }

        return position;
    }
}
