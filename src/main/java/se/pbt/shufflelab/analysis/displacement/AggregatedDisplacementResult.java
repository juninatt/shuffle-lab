package se.pbt.shufflelab.analysis.displacement;

import se.pbt.shufflelab.statistics.Statistics;

import java.util.Objects;

/**
 * Represents displacement metrics aggregated across multiple shuffles of the
 * same deck size.
 *
 * @param sampleSize the number of shuffles the statistics were computed from
 * @param totalCards the number of cards in each shuffled deck; identical for
 *                    every sample, since deck size does not vary between shuffles
 * @param unmovedCards statistics over how many cards stayed in their original
 *                      position, one sample per shuffle
 * @param totalDisplacement statistics over the summed displacement of all
 *                           cards, one sample per shuffle
 * @param maximumDisplacement statistics over the single largest displacement
 *                             observed, one sample per shuffle
 */
public record AggregatedDisplacementResult(
        int sampleSize,
        int totalCards,
        Statistics unmovedCards,
        Statistics totalDisplacement,
        Statistics maximumDisplacement) {

    public AggregatedDisplacementResult {
        if (sampleSize < 1) {
            throw new IllegalArgumentException("sampleSize must be at least 1");
        }

        if (totalCards < 0) {
            throw new IllegalArgumentException("totalCards must not be negative");
        }

        Objects.requireNonNull(unmovedCards, "unmovedCards must not be null");
        Objects.requireNonNull(totalDisplacement, "totalDisplacement must not be null");
        Objects.requireNonNull(maximumDisplacement, "maximumDisplacement must not be null");
    }
}