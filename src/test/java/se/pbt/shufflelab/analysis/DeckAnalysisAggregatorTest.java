package se.pbt.shufflelab.analysis;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.analysis.displacement.AggregatedDisplacementResult;
import se.pbt.shufflelab.analysis.displacement.DisplacementResult;
import se.pbt.shufflelab.analysis.preservedorder.AggregatedPreservedOrderResult;
import se.pbt.shufflelab.analysis.preservedorder.PreservedOrderResult;
import se.pbt.shufflelab.statistics.Statistics;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeckAnalysisAggregatorTest {

    private static final double DELTA = 0.0001;

    private static DeckAnalysis analysis(int unmovedCards, long totalDisplacement, int maximumDisplacement,
                                         int preservedPairs, int preservedSequences,
                                         int cardsInPairs, int cardsInSequences,
                                         int longestSequence, double preservedCardPercentage) {
        return new DeckAnalysis(
                new DisplacementResult(52, unmovedCards, totalDisplacement, maximumDisplacement),
                new PreservedOrderResult(
                        preservedPairs,
                        preservedSequences,
                        cardsInPairs,
                        cardsInSequences,
                        longestSequence,
                        preservedCardPercentage
                )
        );
    }

    private static void assertStatistics(Statistics actual, double mean, double median, double minimum,
                                         double maximum, double standardDeviation) {
        assertAll(
                () -> assertEquals(mean, actual.mean(), DELTA),
                () -> assertEquals(median, actual.median(), DELTA),
                () -> assertEquals(minimum, actual.minimum(), DELTA),
                () -> assertEquals(maximum, actual.maximum(), DELTA),
                () -> assertEquals(standardDeviation, actual.standardDeviation(), DELTA)
        );
    }

    @Nested
    class Aggregation {

        @Test
        void shouldAggregateDisplacementAndPreservedOrderAcrossMultipleAnalyses() {
            List<DeckAnalysis> analyses = List.of(
                    analysis(10, 100, 20, 1, 0, 2, 0, 0, 10.0),
                    analysis(20, 200, 30, 2, 1, 4, 3, 3, 20.0),
                    analysis(30, 300, 40, 3, 2, 6, 8, 5, 30.0)
            );

            AggregatedDeckAnalysis aggregated = DeckAnalysisAggregator.aggregate(analyses);

            assertEquals(3, aggregated.sampleSize());

            AggregatedDisplacementResult displacement = aggregated.displacement();
            assertEquals(3, displacement.sampleSize());
            assertEquals(52, displacement.totalCards());
            assertStatistics(displacement.unmovedCards(), 20.0, 20.0, 10.0, 30.0, 8.16496580927726);
            assertStatistics(displacement.totalDisplacement(), 200.0, 200.0, 100.0, 300.0, 81.6496580927726);
            assertStatistics(displacement.maximumDisplacement(), 30.0, 30.0, 20.0, 40.0, 8.16496580927726);

            AggregatedPreservedOrderResult preservedOrder = aggregated.preservedOrder();
            assertEquals(3, preservedOrder.sampleSize());
            assertStatistics(preservedOrder.preservedPairs(), 2.0, 2.0, 1.0, 3.0, 0.816496580927726);
            assertStatistics(preservedOrder.preservedSequences(), 1.0, 1.0, 0.0, 2.0, 0.816496580927726);
            assertStatistics(preservedOrder.cardsInPairs(), 4.0, 4.0, 2.0, 6.0, 1.632993161855452);
            assertStatistics(preservedOrder.cardsInSequences(), 3.6666666666666665, 3.0, 0.0, 8.0, 3.299831645537222);
            assertStatistics(preservedOrder.longestSequence(), 2.6666666666666665, 3.0, 0.0, 5.0, 2.0548046676563256);
            assertStatistics(preservedOrder.preservedCardPercentage(), 20.0, 20.0, 10.0, 30.0, 8.16496580927726);
        }

        @Test
        void shouldAggregateASingleAnalysisWithZeroSpread() {
            DeckAnalysis singleAnalysis = analysis(15, 150, 25, 2, 1, 4, 3, 3, 25.0);

            AggregatedDeckAnalysis aggregated = DeckAnalysisAggregator.aggregate(List.of(singleAnalysis));

            assertEquals(1, aggregated.sampleSize());
            assertStatistics(aggregated.displacement().unmovedCards(), 15.0, 15.0, 15.0, 15.0, 0.0);
            assertStatistics(aggregated.preservedOrder().preservedCardPercentage(), 25.0, 25.0, 25.0, 25.0, 0.0);
        }
    }

    @Nested
    class Validation {

        @Test
        void shouldRejectNullList() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> DeckAnalysisAggregator.aggregate(null)
            );

            assertEquals(
                    "analyses must not be null",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectEmptyList() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> DeckAnalysisAggregator.aggregate(List.of())
            );

            assertEquals(
                    "at least one analysis is required",
                    exception.getMessage()
            );
        }
    }
}