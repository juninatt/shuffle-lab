package se.pbt.shufflelab.analysis;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.analysis.displacement.AggregatedDisplacementResult;
import se.pbt.shufflelab.analysis.preservedorder.AggregatedPreservedOrderResult;
import se.pbt.shufflelab.statistics.Statistics;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AggregatedDeckAnalysisTest {

    private static AggregatedDisplacementResult sampleDisplacement() {
        return new AggregatedDisplacementResult(
                3,
                52,
                new Statistics(20.0, 20.0, 10.0, 30.0, 8.1650),
                new Statistics(200.0, 200.0, 100.0, 300.0, 81.6497),
                new Statistics(30.0, 30.0, 20.0, 40.0, 8.1650)
        );
    }

    private static AggregatedPreservedOrderResult samplePreservedOrder() {
        return new AggregatedPreservedOrderResult(
                3,
                new Statistics(2.0, 2.0, 1.0, 3.0, 0.8165),
                new Statistics(1.0, 1.0, 0.0, 2.0, 0.8165),
                new Statistics(4.0, 4.0, 2.0, 6.0, 1.6330),
                new Statistics(3.6667, 3.0, 0.0, 8.0, 3.2998),
                new Statistics(2.6667, 3.0, 0.0, 5.0, 2.0548),
                new Statistics(20.0, 20.0, 10.0, 30.0, 8.1650)
        );
    }

    @Nested
    class Construction {

        @Test
        void shouldCreateCompleteAggregatedDeckAnalysis() {
            AggregatedDisplacementResult displacement = sampleDisplacement();
            AggregatedPreservedOrderResult preservedOrder = samplePreservedOrder();

            AggregatedDeckAnalysis analysis = new AggregatedDeckAnalysis(
                    3,
                    displacement,
                    preservedOrder
            );

            assertAll(
                    () -> assertEquals(3, analysis.sampleSize()),
                    () -> assertSame(
                            displacement,
                            analysis.displacement()
                    ),
                    () -> assertSame(
                            preservedOrder,
                            analysis.preservedOrder()
                    )
            );
        }
    }

    @Nested
    class NullValidation {

        @Test
        void shouldRejectNullDisplacement() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> new AggregatedDeckAnalysis(
                            3,
                            null,
                            samplePreservedOrder()
                    )
            );

            assertEquals(
                    "displacement must not be null",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNullPreservedOrder() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> new AggregatedDeckAnalysis(
                            3,
                            sampleDisplacement(),
                            null
                    )
            );

            assertEquals(
                    "preservedOrder must not be null",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectBothResultsBeingNull() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> new AggregatedDeckAnalysis(
                            3,
                            null,
                            null
                    )
            );

            assertEquals(
                    "displacement must not be null",
                    exception.getMessage()
            );
        }
    }

    @Nested
    class Validation {

        @Test
        void shouldRejectSampleSizeBelowOne() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new AggregatedDeckAnalysis(
                            0,
                            sampleDisplacement(),
                            samplePreservedOrder()
                    )
            );

            assertEquals(
                    "sampleSize must be at least 1",
                    exception.getMessage()
            );
        }
    }
}