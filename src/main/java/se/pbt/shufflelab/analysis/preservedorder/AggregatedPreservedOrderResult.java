package se.pbt.shufflelab.analysis.preservedorder;

import se.pbt.shufflelab.statistics.Statistics;

import java.util.Objects;

/**
 * Represents preserved-order metrics aggregated across multiple shuffles.
 *
 * @param sampleSize the number of shuffles the statistics were computed from
 * @param preservedPairs statistics over how many preserved pairs occurred,
 *                        one sample per shuffle
 * @param preservedSequences statistics over how many preserved sequences of
 *                            three or more cards occurred, one sample per shuffle
 * @param cardsInPairs statistics over how many cards belonged to a preserved
 *                      pair, one sample per shuffle
 * @param cardsInSequences statistics over how many cards belonged to a
 *                          preserved sequence, one sample per shuffle
 * @param longestSequence statistics over the length of the longest preserved
 *                         sequence, one sample per shuffle
 * @param preservedCardPercentage statistics over the percentage of cards
 *                                 belonging to any preserved pair or sequence,
 *                                 one sample per shuffle
 */
public record AggregatedPreservedOrderResult(
        int sampleSize,
        Statistics preservedPairs,
        Statistics preservedSequences,
        Statistics cardsInPairs,
        Statistics cardsInSequences,
        Statistics longestSequence,
        Statistics preservedCardPercentage) {

    public AggregatedPreservedOrderResult {
        if (sampleSize < 1) {
            throw new IllegalArgumentException("sampleSize must be at least 1");
        }

        Objects.requireNonNull(preservedPairs, "preservedPairs must not be null");
        Objects.requireNonNull(preservedSequences, "preservedSequences must not be null");
        Objects.requireNonNull(cardsInPairs, "cardsInPairs must not be null");
        Objects.requireNonNull(cardsInSequences, "cardsInSequences must not be null");
        Objects.requireNonNull(longestSequence, "longestSequence must not be null");
        Objects.requireNonNull(preservedCardPercentage, "preservedCardPercentage must not be null");
    }
}