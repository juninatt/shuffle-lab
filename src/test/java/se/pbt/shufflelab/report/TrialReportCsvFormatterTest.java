package se.pbt.shufflelab.report;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.analysis.AggregatedDeckAnalysis;
import se.pbt.shufflelab.analysis.displacement.AggregatedDisplacementResult;
import se.pbt.shufflelab.analysis.preservedorder.AggregatedPreservedOrderResult;
import se.pbt.shufflelab.skill.SkillLevel;
import se.pbt.shufflelab.statistics.Statistics;
import se.pbt.shufflelab.trial.TrialKind;
import se.pbt.shufflelab.trial.TrialSummary;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrialReportCsvFormatterTest {

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

        return new TrialSummary(label, "Test description.", TrialKind.ROUTINE, SkillLevel.EXPERT, analysis);
    }

    @Nested
    class Formatting {

        @Test
        void shouldStartWithAHeaderRow() {
            List<TrialSummary> results = List.of(experimentResult("Riffle - EXPERT", 100, EXPERT_STATISTICS));

            String csv = TrialReportCsvFormatter.format(results);

            assertTrue(csv.startsWith("label,trials,field,mean,median,min,max,standardDeviation"));
        }

        @Test
        void shouldIncludeOneRowPerExperimentAndField() {
            List<TrialSummary> results = List.of(
                    experimentResult("Riffle - NOVICE", 100, NOVICE_STATISTICS),
                    experimentResult("Riffle - EXPERT", 200, EXPERT_STATISTICS)
            );

            String csv = TrialReportCsvFormatter.format(results);

            long dataRows = csv.lines().count() - 1;

            assertEquals(2L * ReportFields.ALL.size(), dataRows);
        }

        @Test
        void shouldIncludeEveryFieldNameAndStatistic() {
            List<TrialSummary> results = List.of(experimentResult("Riffle - NOVICE", 100, NOVICE_STATISTICS));

            String csv = TrialReportCsvFormatter.format(results);

            assertTrue(csv.contains("Riffle - NOVICE,100,unmovedCards,45.50,44.00,30.00,60.00,7.25"));
            assertTrue(csv.contains("preservedCardPercentage,45.50,44.00,30.00,60.00,7.25"));
        }

        @Test
        void shouldQuoteALabelContainingAComma() {
            List<TrialSummary> results = List.of(experimentResult("Riffle, human-style - NOVICE", 100, NOVICE_STATISTICS));

            String csv = TrialReportCsvFormatter.format(results);

            assertTrue(csv.contains("\"Riffle, human-style - NOVICE\","));
        }
    }

    @Nested
    class Validation {

        @Test
        void shouldRejectNullResults() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> TrialReportCsvFormatter.format(null)
            );

            assertEquals("results must not be null", exception.getMessage());
        }

        @Test
        void shouldRejectEmptyResults() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> TrialReportCsvFormatter.format(List.of())
            );

            assertEquals("at least one trial summary is required", exception.getMessage());
        }
    }
}
