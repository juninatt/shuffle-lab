package se.pbt.shufflelab.report;

import se.pbt.shufflelab.trial.TrialSummary;
import se.pbt.shufflelab.statistics.Statistics;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;

/**
 * Formats a series of {@link TrialSummary}s into a plain-text comparison
 * report.
 *
 * <p>The report is organized as one narrow table per measured field, with
 * one row per experiment and one column per statistic
 * ({@code mean}/{@code median}/{@code minimum}/{@code maximum}/
 * {@code standardDeviation}), rather than a single very wide table covering
 * every field at once.
 *
 * <p>This is a pure text-formatting step; it does not run any experiments
 * or print or write anything itself.
 */
public final class TrialReportFormatter {

    private TrialReportFormatter() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Formats a series of trial summaries into a plain-text comparison report.
     *
     * @param results the trial summaries to compare; at least one is required
     * @return the formatted report
     * @throws NullPointerException if {@code results} is {@code null}
     * @throws IllegalArgumentException if {@code results} is empty
     */
    public static String format(List<TrialSummary> results) {
        Objects.requireNonNull(results, "results must not be null");

        if (results.isEmpty()) {
            throw new IllegalArgumentException("at least one trial summary is required");
        }

        StringBuilder report = new StringBuilder();

        appendOverview(report, results);

        report.append("== Displacement ==").append(System.lineSeparator()).append(System.lineSeparator());
        appendField(report, "unmovedCards", results, result -> result.analysis().displacement().unmovedCards());
        appendField(report, "totalDisplacement", results, result -> result.analysis().displacement().totalDisplacement());
        appendField(report, "maximumDisplacement", results, result -> result.analysis().displacement().maximumDisplacement());

        report.append("== Preserved order ==").append(System.lineSeparator()).append(System.lineSeparator());
        appendField(report, "preservedPairs", results, result -> result.analysis().preservedOrder().preservedPairs());
        appendField(report, "preservedSequences", results, result -> result.analysis().preservedOrder().preservedSequences());
        appendField(report, "cardsInPairs", results, result -> result.analysis().preservedOrder().cardsInPairs());
        appendField(report, "cardsInSequences", results, result -> result.analysis().preservedOrder().cardsInSequences());
        appendField(report, "longestSequence", results, result -> result.analysis().preservedOrder().longestSequence());
        appendField(report, "preservedCardPercentage", results, result -> result.analysis().preservedOrder().preservedCardPercentage());

        return report.toString();
    }

    /**
     * Appends a short overview listing each experiment's label and sample size.
     *
     * @param report the report being built
     * @param results the experiment results to summarize
     */
    private static void appendOverview(StringBuilder report, List<TrialSummary> results) {
        int labelWidth = labelWidthOf(results);

        report.append("Trial comparison (")
                .append(results.size())
                .append(" trial runs)")
                .append(System.lineSeparator())
                .append(System.lineSeparator());

        report.append(String.format(Locale.ROOT, "%-" + labelWidth + "s %10s%n", "Label", "Trials"));

        for (TrialSummary result : results) {
            report.append(String.format(
                    Locale.ROOT,
                    "%-" + labelWidth + "s %10d%n",
                    result.label(),
                    result.analysis().sampleSize()
            ));
        }

        report.append(System.lineSeparator());
    }

    /**
     * Appends one narrow table comparing a single field across all experiments.
     *
     * @param report the report being built
     * @param fieldName the name of the field being compared
     * @param results the experiment results to compare
     * @param statisticsOf extracts the relevant {@link Statistics} from an
     *                      experiment result for this field
     */
    private static void appendField(
            StringBuilder report,
            String fieldName,
            List<TrialSummary> results,
            Function<TrialSummary, Statistics> statisticsOf) {

        int labelWidth = labelWidthOf(results);

        report.append("-- ").append(fieldName).append(" --").append(System.lineSeparator());
        report.append(String.format(
                Locale.ROOT,
                "%-" + labelWidth + "s %10s %10s %10s %10s %10s%n",
                "Label", "Mean", "Median", "Min", "Max", "StdDev"
        ));

        for (TrialSummary result : results) {
            Statistics statistics = statisticsOf.apply(result);

            report.append(String.format(
                    Locale.ROOT,
                    "%-" + labelWidth + "s %10.2f %10.2f %10.2f %10.2f %10.2f%n",
                    result.label(),
                    statistics.mean(),
                    statistics.median(),
                    statistics.minimum(),
                    statistics.maximum(),
                    statistics.standardDeviation()
            ));
        }

        report.append(System.lineSeparator());
    }

    /**
     * Determines the label column width needed to fit every experiment's label.
     *
     * @param results the experiment results whose labels will be printed
     * @return the width of the longest label, or the width of the header
     *         {@code "Label"}, whichever is greater
     */
    private static int labelWidthOf(List<TrialSummary> results) {
        int longestLabel = results.stream()
                .mapToInt(result -> result.label().length())
                .max()
                .orElse(0);

        return Math.max(longestLabel, "Label".length());
    }
}