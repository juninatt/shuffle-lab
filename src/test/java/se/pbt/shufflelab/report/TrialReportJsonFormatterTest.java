package se.pbt.shufflelab.report;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.analysis.AggregatedDeckAnalysis;
import se.pbt.shufflelab.analysis.displacement.AggregatedDisplacementResult;
import se.pbt.shufflelab.analysis.preservedorder.AggregatedPreservedOrderResult;
import se.pbt.shufflelab.statistics.Statistics;
import se.pbt.shufflelab.trial.TrialSummary;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrialReportJsonFormatterTest {

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
        void shouldProduceAJsonArray() {
            List<TrialSummary> results = List.of(experimentResult("Riffle - EXPERT", 100, EXPERT_STATISTICS));

            String json = TrialReportJsonFormatter.format(results);

            assertTrue(json.startsWith("["));
            assertTrue(json.endsWith("]"));
        }

        @Test
        void shouldIncludeOneObjectPerExperimentAndField() {
            List<TrialSummary> results = List.of(
                    experimentResult("Riffle - NOVICE", 100, NOVICE_STATISTICS),
                    experimentResult("Riffle - EXPERT", 200, EXPERT_STATISTICS)
            );

            String json = TrialReportJsonFormatter.format(results);

            Matcher matcher = Pattern.compile("\"field\":").matcher(json);
            int objectCount = 0;
            while (matcher.find()) {
                objectCount++;
            }

            assertEquals(2 * ReportFields.ALL.size(), objectCount);
        }

        @Test
        void shouldIncludeEveryFieldNameAndStatistic() {
            List<TrialSummary> results = List.of(experimentResult("Riffle - NOVICE", 100, NOVICE_STATISTICS));

            String json = TrialReportJsonFormatter.format(results);

            assertTrue(json.contains("\"label\":\"Riffle - NOVICE\""));
            assertTrue(json.contains("\"trials\":100"));
            assertTrue(json.contains("\"field\":\"unmovedCards\""));
            assertTrue(json.contains("\"mean\":45.50"));
            assertTrue(json.contains("\"median\":44.00"));
            assertTrue(json.contains("\"min\":30.00"));
            assertTrue(json.contains("\"max\":60.00"));
            assertTrue(json.contains("\"stddev\":7.25"));
        }

        @Test
        void shouldEscapeAQuoteInALabel() {
            List<TrialSummary> results = List.of(experimentResult("Riffle \"human-style\" - NOVICE", 100, NOVICE_STATISTICS));

            String json = TrialReportJsonFormatter.format(results);

            assertTrue(json.contains("\"label\":\"Riffle \\\"human-style\\\" - NOVICE\""));
        }
    }

    @Nested
    class Validation {

        @Test
        void shouldRejectNullResults() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> TrialReportJsonFormatter.format(null)
            );

            assertEquals("results must not be null", exception.getMessage());
        }

        @Test
        void shouldRejectEmptyResults() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> TrialReportJsonFormatter.format(List.of())
            );

            assertEquals("at least one trial summary is required", exception.getMessage());
        }
    }
}
