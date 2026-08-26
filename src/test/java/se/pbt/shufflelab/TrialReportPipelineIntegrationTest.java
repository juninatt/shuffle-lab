package se.pbt.shufflelab;

import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.analysis.AggregatedDeckAnalysis;
import se.pbt.shufflelab.analysis.DeckAnalysis;
import se.pbt.shufflelab.analysis.DeckAnalysisAggregator;
import se.pbt.shufflelab.factory.RoutineFactory;
import se.pbt.shufflelab.handling.routine.RoutineProtocol;
import se.pbt.shufflelab.report.TrialReportFormatter;
import se.pbt.shufflelab.skill.SkillLevel;
import se.pbt.shufflelab.trial.TrialRunner;
import se.pbt.shufflelab.trial.TrialSummary;

import java.util.List;
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Exercises the full pipeline from running a routine through to a formatted
 * report, using a real {@link RoutineProtocol} and real randomness.
 *
 * <p>Unit tests elsewhere in the project cover each step in isolation with
 * hand-built data. This test instead verifies that the steps actually
 * produce a coherent result when wired together, without depending on
 * {@code ShuffleLabApp} or touching the console or the filesystem.
 */
class TrialReportPipelineIntegrationTest {

    @Test
    void shouldProduceACompleteReportFromARealRoutine() {
        RandomGenerator random = TestRandoms.fixedRandom();
        RoutineProtocol routine = RoutineFactory.simpleRiffleShuffle(SkillLevel.EXPERT);

        List<DeckAnalysis> analyses = TrialRunner.run(routine, 50, random);
        AggregatedDeckAnalysis aggregated = DeckAnalysisAggregator.aggregate(analyses);
        TrialSummary summary = new TrialSummary("Riffle - EXPERT", aggregated);

        String report = TrialReportFormatter.format(List.of(summary));

        assertFalse(report.isBlank(), "a real run should produce a non-empty report");
        assertTrue(report.contains("Riffle - EXPERT"));
        assertTrue(report.contains("Trial comparison (1 trial runs)"));
        assertTrue(report.contains("== Displacement =="));
        assertTrue(report.contains("== Preserved order =="));

        // Sanity-checks the real numbers, not just the report's structure.
        assertTrue(aggregated.displacement().totalDisplacement().mean() > 0,
                "a shuffled deck should show some displacement");
        assertTrue(aggregated.preservedOrder().preservedCardPercentage().mean() >= 0.0
                        && aggregated.preservedOrder().preservedCardPercentage().mean() <= 100.0,
                "preserved-card percentage should be a valid percentage");
    }

    @Test
    void shouldProduceAComparisonReportAcrossAllSkillLevels() {
        RandomGenerator random = TestRandoms.fixedRandom();

        List<TrialSummary> summaries = List.of(
                summaryFor(SkillLevel.NOVICE, random),
                summaryFor(SkillLevel.INTERMEDIATE, random),
                summaryFor(SkillLevel.EXPERT, random)
        );

        String report = TrialReportFormatter.format(summaries);

        assertTrue(report.contains("Trial comparison (3 trial runs)"));
        assertTrue(report.contains("Riffle - NOVICE"));
        assertTrue(report.contains("Riffle - INTERMEDIATE"));
        assertTrue(report.contains("Riffle - EXPERT"));
    }

    @Test
    void shouldProduceIdenticalAggregatedResultsForTheSameSeed() {
        AggregatedDeckAnalysis firstRun =
                summaryFor(SkillLevel.INTERMEDIATE, TestRandoms.seededRandom(123)).analysis();
        AggregatedDeckAnalysis secondRun =
                summaryFor(SkillLevel.INTERMEDIATE, TestRandoms.seededRandom(123)).analysis();

        assertEquals(firstRun, secondRun,
                "the same seed should produce identical aggregated results end-to-end, "
                        + "proving the whole pipeline is deterministic given its randomness source");
    }

    @Test
    void shouldProduceDifferentAggregatedResultsAcrossSkillLevels() {
        RandomGenerator random = TestRandoms.seededRandom(7);

        AggregatedDeckAnalysis novice = summaryFor(SkillLevel.NOVICE, random).analysis();
        AggregatedDeckAnalysis expert = summaryFor(SkillLevel.EXPERT, random).analysis();

        assertNotEquals(novice, expert,
                "different skill levels should propagate all the way through SkillProfile, "
                        + "TrialRunner, and DeckAnalysisAggregator into measurably different results");
    }

    private static TrialSummary summaryFor(SkillLevel skillLevel, RandomGenerator random) {
        RoutineProtocol routine = RoutineFactory.simpleRiffleShuffle(skillLevel);
        List<DeckAnalysis> analyses = TrialRunner.run(routine, 20, random);
        AggregatedDeckAnalysis aggregated = DeckAnalysisAggregator.aggregate(analyses);

        return new TrialSummary("Riffle - " + skillLevel, aggregated);
    }
}