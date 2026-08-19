package se.pbt.shufflelab.analysis;

import se.pbt.shufflelab.analysis.displacement.AggregatedDisplacementResult;
import se.pbt.shufflelab.analysis.preservedorder.AggregatedPreservedOrderResult;
import se.pbt.shufflelab.analysis.displacement.DisplacementResult;
import se.pbt.shufflelab.analysis.preservedorder.PreservedOrderResult;
import se.pbt.shufflelab.statistics.Statistics;

import java.util.List;
import java.util.Objects;

/**
 * Aggregates a series of {@link DeckAnalysis} results into summary statistics.
 *
 * <p>This is a pure computation over already-collected analyses; it does not
 * run any shuffles itself.
 */
public final class DeckAnalysisAggregator {

    private DeckAnalysisAggregator() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Aggregates a series of deck analyses into summary statistics.
     *
     * @param analyses the analyses to aggregate; at least one is required
     * @return the aggregated analysis
     * @throws NullPointerException if {@code analyses} is {@code null}
     * @throws IllegalArgumentException if {@code analyses} is empty
     */
    public static AggregatedDeckAnalysis aggregate(List<DeckAnalysis> analyses) {
        Objects.requireNonNull(analyses, "analyses must not be null");

        if (analyses.isEmpty()) {
            throw new IllegalArgumentException("at least one analysis is required");
        }

        int sampleSize = analyses.size();

        return new AggregatedDeckAnalysis(
                sampleSize,
                aggregateDisplacement(analyses, sampleSize),
                aggregatePreservedOrder(analyses, sampleSize)
        );
    }

    /**
     * Aggregates the displacement results of a series of deck analyses.
     *
     * @param analyses the analyses to aggregate
     * @param sampleSize the number of analyses being aggregated
     * @return the aggregated displacement result
     */
    private static AggregatedDisplacementResult aggregateDisplacement(List<DeckAnalysis> analyses, int sampleSize) {
        int totalCards = analyses.getFirst().displacement().totalCards();

        double[] unmovedCards = new double[sampleSize];
        double[] totalDisplacement = new double[sampleSize];
        double[] maximumDisplacement = new double[sampleSize];

        for (int i = 0; i < sampleSize; i++) {
            DisplacementResult displacement = analyses.get(i).displacement();

            unmovedCards[i] = displacement.unmovedCards();
            totalDisplacement[i] = displacement.totalDisplacement();
            maximumDisplacement[i] = displacement.maximumDisplacement();
        }

        return new AggregatedDisplacementResult(
                sampleSize,
                totalCards,
                Statistics.of(unmovedCards),
                Statistics.of(totalDisplacement),
                Statistics.of(maximumDisplacement)
        );
    }

    /**
     * Aggregates the preserved-order results of a series of deck analyses.
     *
     * @param analyses the analyses to aggregate
     * @param sampleSize the number of analyses being aggregated
     * @return the aggregated preserved-order result
     */
    private static AggregatedPreservedOrderResult aggregatePreservedOrder(List<DeckAnalysis> analyses, int sampleSize) {
        double[] preservedPairs = new double[sampleSize];
        double[] preservedSequences = new double[sampleSize];
        double[] cardsInPairs = new double[sampleSize];
        double[] cardsInSequences = new double[sampleSize];
        double[] longestSequence = new double[sampleSize];
        double[] preservedCardPercentage = new double[sampleSize];

        for (int i = 0; i < sampleSize; i++) {
            PreservedOrderResult preservedOrder = analyses.get(i).preservedOrder();

            preservedPairs[i] = preservedOrder.preservedPairs();
            preservedSequences[i] = preservedOrder.preservedSequences();
            cardsInPairs[i] = preservedOrder.cardsInPairs();
            cardsInSequences[i] = preservedOrder.cardsInSequences();
            longestSequence[i] = preservedOrder.longestSequence();
            preservedCardPercentage[i] = preservedOrder.preservedCardPercentage();
        }

        return new AggregatedPreservedOrderResult(
                sampleSize,
                Statistics.of(preservedPairs),
                Statistics.of(preservedSequences),
                Statistics.of(cardsInPairs),
                Statistics.of(cardsInSequences),
                Statistics.of(longestSequence),
                Statistics.of(preservedCardPercentage)
        );
    }
}