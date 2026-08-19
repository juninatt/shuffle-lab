package se.pbt.shufflelab;

import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.analysis.AggregatedDeckAnalysis;
import se.pbt.shufflelab.analysis.DeckAnalysis;
import se.pbt.shufflelab.analysis.DeckAnalysisAggregator;
import se.pbt.shufflelab.factory.RoutineFactory;
import se.pbt.shufflelab.manipulation.routine.Routine;
import se.pbt.shufflelab.report.TrialReportFormatter;
import se.pbt.shufflelab.skill.SkillLevel;
import se.pbt.shufflelab.trial.TrialRunner;
import se.pbt.shufflelab.trial.TrialSummary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Writes a complete, real trial report to disk so its format and content can
 * be reviewed by eye, without needing to run {@link ShuffleLabApp} manually.
 *
 * <p>This is not a correctness test in the usual sense — {@link
 * TrialReportPipelineIntegrationTest} already covers that with assertions on
 * the report's content. This test instead exists purely to produce a
 * human-readable artifact under {@code target/}, refreshed every time the
 * test suite runs, so the report can be inspected without a manual run.
 */
class TrialReportSampleTest {

    private static final Path SAMPLE_REPORT_PATH =
            Path.of("target", "sample-reports", "trial-report-sample.txt");

    @Test
    void shouldWriteASampleReportForManualReview() throws IOException {
        RandomGenerator random = TestRandoms.seededRandom(42);

        List<TrialSummary> summaries = new ArrayList<>();

        for (SkillLevel skillLevel : SkillLevel.values()) {
            Routine routine = RoutineFactory.simpleRiffleShuffle(skillLevel);

            List<DeckAnalysis> analyses = TrialRunner.run(routine, 200, random);
            AggregatedDeckAnalysis aggregated = DeckAnalysisAggregator.aggregate(analyses);

            summaries.add(new TrialSummary("Riffle - " + skillLevel, aggregated));
        }

        String report = TrialReportFormatter.format(summaries);

        Files.createDirectories(SAMPLE_REPORT_PATH.getParent());
        Files.writeString(SAMPLE_REPORT_PATH, report);

        System.out.println("Sample report written to " + SAMPLE_REPORT_PATH.toAbsolutePath());
        System.out.println(report);

        assertFalse(report.isBlank(), "the generated report should not be empty");
        assertTrue(Files.exists(SAMPLE_REPORT_PATH), "the sample report file should have been written");
        assertEquals(report, Files.readString(SAMPLE_REPORT_PATH), "the file should contain exactly what was formatted");
    }
}