package se.pbt.shufflelab.trial;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.analysis.AggregatedDeckAnalysis;
import se.pbt.shufflelab.analysis.displacement.AggregatedDisplacementResult;
import se.pbt.shufflelab.analysis.preservedorder.AggregatedPreservedOrderResult;
import se.pbt.shufflelab.statistics.Statistics;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TrialSummaryTest {

    private static AggregatedDeckAnalysis sampleAnalysis() {
        Statistics statistics = new Statistics(20.0, 20.0, 10.0, 30.0, 8.1650);

        return new AggregatedDeckAnalysis(
                3,
                new AggregatedDisplacementResult(3, 52, statistics, statistics, statistics),
                new AggregatedPreservedOrderResult(3, statistics, statistics, statistics, statistics, statistics, statistics)
        );
    }

    @Nested
    class Construction {

        @Test
        void shouldCreateResultWithValidValues() {
            AggregatedDeckAnalysis analysis = sampleAnalysis();

            TrialSummary result = new TrialSummary("Riffle - EXPERT", analysis);

            assertAll(
                    () -> assertEquals("Riffle - EXPERT", result.label()),
                    () -> assertSame(analysis, result.analysis())
            );
        }
    }

    @Nested
    class NullValidation {

        @Test
        void shouldRejectNullLabel() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> new TrialSummary(null, sampleAnalysis())
            );

            assertEquals(
                    "label must not be null",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNullAnalysis() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> new TrialSummary("Riffle - EXPERT", null)
            );

            assertEquals(
                    "analysis must not be null",
                    exception.getMessage()
            );
        }
    }
}