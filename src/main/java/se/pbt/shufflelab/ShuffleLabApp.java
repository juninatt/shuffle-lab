package se.pbt.shufflelab;

import se.pbt.shufflelab.analysis.AggregatedDeckAnalysis;
import se.pbt.shufflelab.analysis.DeckAnalysis;
import se.pbt.shufflelab.analysis.DeckAnalysisAggregator;
import se.pbt.shufflelab.manipulation.routine.Routine;
import se.pbt.shufflelab.factory.RoutineFactory;
import se.pbt.shufflelab.report.TrialReportFormatter;
import se.pbt.shufflelab.skill.SkillLevel;
import se.pbt.shufflelab.trial.TrialRunner;
import se.pbt.shufflelab.trial.TrialSummary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

/**
 * Entry point for running a fixed set of shuffle trials and reporting the
 * results.
 *
 * <p>Which routines, skill levels, and trial count are compared is currently
 * hardcoded below. As {@link RoutineFactory} grows to expose more named
 * routines, more trial runs can be added here to include them.
 */
public class ShuffleLabApp {

    private static final int TRIALS = 1000;
    private static final Path REPORT_PATH = Path.of("shuffle-lab-report.txt");

    public static void main(String[] args) {
        RandomGenerator random = new Random();

        List<TrialSummary> summaries = runTrials(random);

        String report = TrialReportFormatter.format(summaries);

        System.out.println(report);

        writeReportToFile(report);
    }

    /**
     * Runs one trial series per skill level, using the simple riffle shuffle
     * routine.
     *
     * @param random a source of randomness, shared across all trial runs
     * @return one labeled summary per skill level
     */
    private static List<TrialSummary> runTrials(RandomGenerator random) {
        List<TrialSummary> summaries = new ArrayList<>();

        for (SkillLevel skillLevel : SkillLevel.values()) {
            Routine routine = RoutineFactory.simpleRiffleShuffle(skillLevel);

            List<DeckAnalysis> analyses = TrialRunner.run(routine, TRIALS, random);
            AggregatedDeckAnalysis aggregated = DeckAnalysisAggregator.aggregate(analyses);

            summaries.add(new TrialSummary("Riffle - " + skillLevel, aggregated));
        }

        return summaries;
    }

    /**
     * Writes the formatted report to {@link #REPORT_PATH}, printing a
     * message about the outcome either way.
     *
     * @param report the formatted report to write
     */
    private static void writeReportToFile(String report) {
        try {
            Files.writeString(REPORT_PATH, report);
            System.out.println("Report written to " + REPORT_PATH.toAbsolutePath());
        } catch (IOException exception) {
            System.err.println("Failed to write report to file: " + exception.getMessage());
        }
    }
}