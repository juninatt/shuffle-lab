package se.pbt.shufflelab.analysis.displacement;

import se.pbt.shufflelab.analysis.CardPositionLookup;
import se.pbt.shufflelab.deck.Deck;
import se.pbt.shufflelab.deck.card.Card;

import java.util.Map;
import java.util.Objects;

/**
 * Computes a {@link DisplacementResult} by comparing a shuffled deck against
 * its original order.
 *
 * <p>Each card's displacement is the absolute difference between its position
 * in the original deck and its position in the shuffled deck. This analyzer
 * assumes a deck of unique cards; matching is done by card value.
 */
public final class DisplacementAnalyzer {

    private DisplacementAnalyzer() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Analyzes how far each card has moved from its original position.
     *
     * @param original the deck in its original order
     * @param shuffled the deck after shuffling
     * @return the resulting displacement metrics
     * @throws NullPointerException if either deck is {@code null}
     * @throws IllegalArgumentException if the shuffled deck contains a card
     *                                  not present in the original deck
     */
    public static DisplacementResult analyze(Deck original, Deck shuffled) {
        Objects.requireNonNull(original, "original must not be null");
        Objects.requireNonNull(shuffled, "shuffled must not be null");

        Map<Card, Integer> originalPositions = CardPositionLookup.indexByPosition(original);

        int unmovedCards = 0;
        long totalDisplacement = 0;
        int maximumDisplacement = 0;

        for (int shuffledPosition = 0; shuffledPosition < shuffled.size(); shuffledPosition++) {
            Card card = shuffled.get(shuffledPosition);
            int originalPosition = CardPositionLookup.positionOf(originalPositions, card);

            int displacement = Math.abs(originalPosition - shuffledPosition);

            if (displacement == 0) {
                unmovedCards++;
            }

            totalDisplacement += displacement;
            maximumDisplacement = Math.max(maximumDisplacement, displacement);
        }

        return new DisplacementResult(
                shuffled.size(),
                unmovedCards,
                totalDisplacement,
                maximumDisplacement
        );
    }
}