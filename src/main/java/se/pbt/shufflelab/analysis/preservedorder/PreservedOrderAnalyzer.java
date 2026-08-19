package se.pbt.shufflelab.analysis.preservedorder;

import se.pbt.shufflelab.analysis.CardPositionLookup;
import se.pbt.shufflelab.deck.Deck;
import se.pbt.shufflelab.deck.card.Card;

import java.util.Map;
import java.util.Objects;

/**
 * Computes a {@link PreservedOrderResult} by locating rising sequences in a
 * shuffled deck.
 *
 * <p>A rising sequence is a run of consecutive positions in the shuffled deck
 * whose cards were also consecutive, in the same order, in the original deck.
 * Runs of exactly two cards are counted as preserved pairs, and runs of three
 * or more cards are counted as preserved sequences. This analyzer assumes a
 * deck of unique cards; matching is done by card value.
 */
public final class PreservedOrderAnalyzer {

    private PreservedOrderAnalyzer() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Analyzes which groups of cards remain in their original consecutive
     * order after shuffling.
     *
     * @param original the deck in its original order
     * @param shuffled the deck after shuffling
     * @return the resulting preserved-order metrics
     * @throws NullPointerException if either deck is {@code null}
     * @throws IllegalArgumentException if the shuffled deck contains a card
     *                                  not present in the original deck
     */
    public static PreservedOrderResult analyze(Deck original, Deck shuffled) {
        Objects.requireNonNull(original, "original must not be null");
        Objects.requireNonNull(shuffled, "shuffled must not be null");

        int totalCards = shuffled.size();

        if (totalCards == 0) {
            return new PreservedOrderResult(0, 0, 0, 0, 0, 0.0);
        }

        int[] originalIndices = originalIndicesOf(original, shuffled);

        int preservedPairs = 0;
        int preservedSequences = 0;
        int cardsInPairs = 0;
        int cardsInSequences = 0;
        int longestSequence = 0;

        int runLength = 1;

        for (int position = 1; position < totalCards; position++) {
            boolean continuesRisingRun = originalIndices[position] == originalIndices[position - 1] + 1;

            if (continuesRisingRun) {
                runLength++;
                continue;
            }

            if (runLength == 2) {
                preservedPairs++;
                cardsInPairs += 2;
            } else if (runLength >= 3) {
                preservedSequences++;
                cardsInSequences += runLength;
                longestSequence = Math.max(longestSequence, runLength);
            }

            runLength = 1;
        }

        if (runLength == 2) {
            preservedPairs++;
            cardsInPairs += 2;
        } else if (runLength >= 3) {
            preservedSequences++;
            cardsInSequences += runLength;
            longestSequence = Math.max(longestSequence, runLength);
        }

        double preservedCardPercentage = (cardsInPairs + cardsInSequences) * 100.0 / totalCards;

        return new PreservedOrderResult(
                preservedPairs,
                preservedSequences,
                cardsInPairs,
                cardsInSequences,
                longestSequence,
                preservedCardPercentage
        );
    }

    /**
     * Maps each card in the shuffled deck to its position in the original deck.
     *
     * @param original the deck in its original order
     * @param shuffled the deck after shuffling
     * @return an array where each element is the original position of the
     *         card at the corresponding position in {@code shuffled}
     */
    private static int[] originalIndicesOf(Deck original, Deck shuffled) {
        Map<Card, Integer> originalPositions = CardPositionLookup.indexByPosition(original);

        int[] originalIndices = new int[shuffled.size()];

        for (int position = 0; position < shuffled.size(); position++) {
            originalIndices[position] = CardPositionLookup.positionOf(
                    originalPositions,
                    shuffled.get(position)
            );
        }

        return originalIndices;
    }
}