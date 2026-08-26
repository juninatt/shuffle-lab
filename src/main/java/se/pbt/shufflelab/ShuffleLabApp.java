package se.pbt.shufflelab;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import se.pbt.shufflelab.analysis.AggregatedDeckAnalysis;
import se.pbt.shufflelab.analysis.DeckAnalysis;
import se.pbt.shufflelab.analysis.DeckAnalysisAggregator;
import se.pbt.shufflelab.factory.RoutineCatalog;
import se.pbt.shufflelab.handling.routine.RoutineProtocol;
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
import java.util.concurrent.Callable;
import java.util.random.RandomGenerator;

/**
 * Command-line entry point for running shuffle routines and reporting the
 * results.
 *
 * <p>Which routine(s) and skill level(s) to compare, how many trials to
 * run, and where to write the resulting report are all supplied as
 * command-line options rather than hardcoded, so a comparison can be run
 * without editing and recompiling this class.</p>
 */
@Command(
        name = "shufflelab",
        description = "Run shuffle routines and compare how well they randomise a deck.",
        mixinStandardHelpOptions = true
)
public class ShuffleLabApp implements Callable<Integer> {

    @Option(
            names = {"-r", "--routine"},
            description = "Routine(s) to run. Valid values: ${COMPLETION-CANDIDATES}. Omit to run all.",
            arity = "1..*"
    )
    private RoutineCatalog[] routines;

    @Option(
            names = {"-s", "--skill"},
            description = "Skill level(s) to compare. Valid values: ${COMPLETION-CANDIDATES}. Omit to run all.",
            arity = "1..*"
    )
    private SkillLevel[] skillLevels;

    @Option(
            names = {"-t", "--trials"},
            description = "Number of trials per routine/skill combination (default: ${DEFAULT-VALUE})",
            defaultValue = "1000"
    )
    private int trials;

    @Option(
            names = {"-o", "--out"},
            description = "Output file path (default: ${DEFAULT-VALUE})",
            defaultValue = "shuffle-lab-report.txt"
    )
    private Path outputPath;

    @Override
    public Integer call() {
        RandomGenerator random = new Random();

        RoutineCatalog[] selectedRoutines = routines != null ? routines : RoutineCatalog.values();
        SkillLevel[] selectedSkillLevels = skillLevels != null ? skillLevels : SkillLevel.values();

        List<TrialSummary> summaries = runTrials(selectedRoutines, selectedSkillLevels, random);

        String report = TrialReportFormatter.format(summaries);

        System.out.println(report);

        writeReportToFile(report);

        return 0;
    }

    /**
     * Runs every combination of the given routines and skill levels.
     *
     * @param routines the routines to run
     * @param skillLevels the skill levels to run each routine at
     * @param random a source of randomness, shared across all runs
     * @return one labeled summary per routine/skill-level combination
     */
    private List<TrialSummary> runTrials(RoutineCatalog[] routines, SkillLevel[] skillLevels, RandomGenerator random) {
        List<TrialSummary> summaries = new ArrayList<>();

        for (RoutineCatalog routine : routines) {
            for (SkillLevel skillLevel : skillLevels) {
                RoutineProtocol instance = routine.create(skillLevel);

                List<DeckAnalysis> analyses = TrialRunner.run(instance, trials, random);
                AggregatedDeckAnalysis aggregated = DeckAnalysisAggregator.aggregate(analyses);

                summaries.add(new TrialSummary(instance + " - " + skillLevel, aggregated));
            }
        }

        return summaries;
    }

    /**
     * Writes the formatted report to {@link #outputPath}, printing a
     * message about the outcome either way.
     *
     * @param report the formatted report to write
     */
    private void writeReportToFile(String report) {
        try {
            Files.writeString(outputPath, report);
            System.out.println("Report written to " + outputPath.toAbsolutePath());
        } catch (IOException exception) {
            System.err.println("Failed to write report to file: " + exception.getMessage());
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new ShuffleLabApp()).execute(args);
        System.exit(exitCode);
    }
}