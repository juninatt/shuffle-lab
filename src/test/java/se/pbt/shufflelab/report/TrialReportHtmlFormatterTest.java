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

class TrialReportHtmlFormatterTest {

    private static final Statistics STATISTICS = new Statistics(45.50, 44.00, 30.00, 60.00, 7.25);

    private static TrialSummary experimentResult(
            String label, String description, TrialKind kind, SkillLevel skillLevel, int sampleSize) {

        AggregatedDisplacementResult displacement = new AggregatedDisplacementResult(
                sampleSize, 52, STATISTICS, STATISTICS, STATISTICS
        );

        AggregatedPreservedOrderResult preservedOrder = new AggregatedPreservedOrderResult(
                sampleSize, STATISTICS, STATISTICS, STATISTICS, STATISTICS, STATISTICS, STATISTICS
        );

        AggregatedDeckAnalysis analysis = new AggregatedDeckAnalysis(sampleSize, displacement, preservedOrder);

        return new TrialSummary(label, description, kind, skillLevel, analysis);
    }

    @Nested
    class Formatting {

        @Test
        void shouldProduceACompleteHtmlDocument() {
            List<TrialSummary> results = List.of(
                    experimentResult("Riffle - EXPERT", "A riffle shuffle.", TrialKind.SHUFFLE, SkillLevel.EXPERT, 100)
            );

            String html = TrialReportHtmlFormatter.format(results);

            assertTrue(html.startsWith("<!DOCTYPE html>"));
            assertTrue(html.contains("<title>Shuffle Ledger</title>"));
            assertTrue(html.trim().endsWith("</html>"));
        }

        @Test
        void shouldEmbedEveryExperimentsMetadataAlongsideItsStatistics() {
            List<TrialSummary> results = List.of(
                    experimentResult(
                            "Riffle - NOVICE", "A single riffle shuffle pass.",
                            TrialKind.SHUFFLE, SkillLevel.NOVICE, 100
                    ),
                    experimentResult(
                            "Ideal random shuffle - NOVICE", "The mathematically ideal random baseline.",
                            TrialKind.ROUTINE, SkillLevel.NOVICE, 100
                    )
            );

            String html = TrialReportHtmlFormatter.format(results);

            assertTrue(html.contains("\"label\":\"Riffle - NOVICE\""));
            assertTrue(html.contains("\"kind\":\"SHUFFLE\""));
            assertTrue(html.contains("\"skillLevel\":\"NOVICE\""));
            assertTrue(html.contains("\"description\":\"A single riffle shuffle pass.\""));
            assertTrue(html.contains("\"kind\":\"ROUTINE\""));
            assertTrue(html.contains("\"description\":\"The mathematically ideal random baseline.\""));
            assertTrue(html.contains("\"mean\":45.50"));
        }

        @Test
        void shouldIncludeOneRowPerExperimentAndMeasuredField() {
            List<TrialSummary> results = List.of(
                    experimentResult("Riffle - EXPERT", "desc", TrialKind.SHUFFLE, SkillLevel.EXPERT, 100)
            );

            String html = TrialReportHtmlFormatter.format(results);

            long fieldCount = ReportFields.ALL.size();
            long occurrences = html.split("\"field\":", -1).length - 1;

            assertEquals(fieldCount, occurrences);
        }
    }

    @Nested
    class Validation {

        @Test
        void shouldRejectNullResults() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> TrialReportHtmlFormatter.format(null)
            );

            assertEquals("results must not be null", exception.getMessage());
        }

        @Test
        void shouldRejectEmptyResults() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> TrialReportHtmlFormatter.format(List.of())
            );

            assertEquals("at least one trial summary is required", exception.getMessage());
        }
    }
}
