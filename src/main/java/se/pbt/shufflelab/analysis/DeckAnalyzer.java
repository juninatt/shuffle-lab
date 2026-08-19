package se.pbt.shufflelab.analysis;

import se.pbt.shufflelab.analysis.displacement.DisplacementAnalyzer;
import se.pbt.shufflelab.analysis.preservedorder.PreservedOrderAnalyzer;
import se.pbt.shufflelab.deck.Deck;

import java.util.Objects;

/**
 * Public entry point for analysing how well a deck has been shuffled.
 *
 * <p>This factory compares a shuffled deck against its original order and
 * combines the individual results produced by {@link DisplacementAnalyzer}
 * and {@link PreservedOrderAnalyzer} into a single {@link DeckAnalysis}.
 *
 * <p>As additional metrics are introduced, they can be added here without
 * affecting client code that only depends on this entry point.
 */
public final class DeckAnalyzer {

    private DeckAnalyzer() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Analyzes a shuffled deck by comparing it against its original order.
     *
     * @param original the deck in its original order
     * @param shuffled the deck after shuffling
     * @return the complete analysis of the shuffled deck
     * @throws NullPointerException if either deck is {@code null}
     * @throws IllegalArgumentException if the shuffled deck contains a card
     *                                  not present in the original deck
     */
    public static DeckAnalysis analyze(Deck original, Deck shuffled) {
        Objects.requireNonNull(original, "original must not be null");
        Objects.requireNonNull(shuffled, "shuffled must not be null");

        return new DeckAnalysis(
                DisplacementAnalyzer.analyze(original, shuffled),
                PreservedOrderAnalyzer.analyze(original, shuffled)
        );
    }
}