package se.pbt.shufflelab.analysis;

import se.pbt.shufflelab.analysis.displacement.AggregatedDisplacementResult;
import se.pbt.shufflelab.analysis.preservedorder.AggregatedPreservedOrderResult;
import se.pbt.shufflelab.analysis.displacement.DisplacementResult;
import se.pbt.shufflelab.analysis.preservedorder.PreservedOrderResult;

import java.util.Objects;

/**
 * Represents the complete analysis of a deck, aggregated across multiple
 * shuffles.
 *
 * <p>Combines the individual results produced by aggregating many
 * {@link DisplacementResult}s and {@link PreservedOrderResult}s, mirroring
 * how {@link DeckAnalysis} combines a single shuffle's results.
 *
 * @param sampleSize the number of shuffles the statistics were computed from
 * @param displacement the aggregated displacement metrics
 * @param preservedOrder the aggregated preserved-order metrics
 */
public record AggregatedDeckAnalysis(
        int sampleSize,
        AggregatedDisplacementResult displacement,
        AggregatedPreservedOrderResult preservedOrder) {

    public AggregatedDeckAnalysis {
        if (sampleSize < 1) {
            throw new IllegalArgumentException("sampleSize must be at least 1");
        }

        Objects.requireNonNull(displacement, "displacement must not be null");
        Objects.requireNonNull(preservedOrder, "preservedOrder must not be null");
    }
}