package se.pbt.shufflelab.report;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.analysis.AggregatedDeckAnalysis;
import se.pbt.shufflelab.analysis.displacement.AggregatedDisplacementResult;
import se.pbt.shufflelab.analysis.preservedorder.AggregatedPreservedOrderResult;
import se.pbt.shufflelab.trial.TrialSummary;
import se.pbt.shufflelab.statistics.Statistics;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrialReportFormatterTest {

    private static final Statistics NOVICE_STATISTICS = new Statistics(45.50, 44.00, 30.00, 60.00, 7.25);
    private static final Statistics EXPERT_STATISTICS = new Statistics(12.75, 12.00, 5.00, 20.00, 3.10);

    private static TrialSummary experimentResult(String label, int sampleSize, Statistics statistics) {
        AggregatedDisplacementResult displacement = new AggregatedDisplacementResult(
                sampleSize, 52, statistics, statistics, statistics
        );

        AggregatedPreservedOrderResult preservedOrder = new AggregatedPreservedOrderResult(
                sampleSize, statistics, statistics, statistics, statistics, statistics, statistics
        );

        AggregatedDeckAnalysis analysis = new AggregatedDeckAnalysis(sampleSize, displacement, preservedOrder);

        return new TrialSummary(label, analysis);
    }

    @Nested
    class Formatting {

        @Test
        void shouldIncludeAnOverviewOfAllExperiments() {
            List<TrialSummary> results = List.of(
                    experimentResult("Riffle - NOVICE", 100, NOVICE_STATISTICS),
                    experimentResult("Riffle - EXPERT", 200, EXPERT_STATISTICS)
            );

            String report = TrialReportFormatter.format(results);

            assertTrue(report.contains("Experiment comparison (2 experiments)"));
            assertTrue(report.contains("Riffle - NOVICE"));
            assertTrue(report.contains("Riffle - EXPERT"));
            assertTrue(report.contains("100"));
            assertTrue(report.contains("200"));
        }

        @Test
        void shouldIncludeAllDisplacementFieldSections() {
            List<TrialSummary> results = List.of(experimentResult("Riffle - EXPERT", 100, EXPERT_STATISTICS));

            String report = TrialReportFormatter.format(results);

            assertTrue(report.contains("== Displacement =="));
            assertTrue(report.contains("-- unmovedCards --"));
            assertTrue(report.contains("-- totalDisplacement --"));
            assertTrue(report.contains("-- maximumDisplacement --"));
        }

        @Test
        void shouldIncludeAllPreservedOrderFieldSections() {
            List<TrialSummary> results = List.of(experimentResult("Riffle - EXPERT", 100, EXPERT_STATISTICS));

            String report = TrialReportFormatter.format(results);

            assertTrue(report.contains("== Preserved order =="));
            assertTrue(report.contains("-- preservedPairs --"));
            assertTrue(report.contains("-- preservedSequences --"));
            assertTrue(report.contains("-- cardsInPairs --"));
            assertTrue(report.contains("-- cardsInSequences --"));
            assertTrue(report.contains("-- longestSequence --"));
            assertTrue(report.contains("-- preservedCardPercentage --"));
        }

        @Test
        void shouldIncludeAllFiveStatisticsPerField() {
            List<TrialSummary> results = List.of(experimentResult("Riffle - NOVICE", 100, NOVICE_STATISTICS));

            String report = TrialReportFormatter.format(results);

            assertTrue(report.contains("45.50"));
            assertTrue(report.contains("44.00"));
            assertTrue(report.contains("30.00"));
            assertTrue(report.contains("60.00"));
            assertTrue(report.contains("7.25"));
        }

        @Test
        void shouldKeepEachExperimentsValuesOnItsOwnRow() {
            List<TrialSummary> results = List.of(
                    experimentResult("Riffle - NOVICE", 100, NOVICE_STATISTICS),
                    experimentResult("Riffle - EXPERT", 200, EXPERT_STATISTICS)
            );

            String report = TrialReportFormatter.format(results);

            assertTrue(report.contains("45.50"));
            assertTrue(report.contains("12.75"));
        }
    }

    @Nested
    class Validation {

        @Test
        void shouldRejectNullResults() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> TrialReportFormatter.format(null)
            );

            assertEquals(
                    "results must not be null",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectEmptyResults() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> TrialReportFormatter.format(List.of())
            );

            assertEquals(
                    "at least one experiment result is required",
                    exception.getMessage()
            );
        }
    }
}