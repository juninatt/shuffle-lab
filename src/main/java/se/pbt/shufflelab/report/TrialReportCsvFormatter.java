package se.pbt.shufflelab.report;

import se.pbt.shufflelab.statistics.Statistics;
import se.pbt.shufflelab.trial.TrialSummary;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Formats a series of {@link TrialSummary}s into a CSV comparison report.
 *
 * <p>The report is in tidy (long) form: one row per experiment and measured
 * field, rather than one row per experiment with a column per field. This
 * keeps the column count fixed regardless of how many fields are measured,
 * making the output straightforward to load into a spreadsheet or a data
 * analysis tool.
 *
 * <p>This is a pure text-formatting step; it does not run any experiments
 * or print or write anything itself.
 */
public final class TrialReportCsvFormatter {

    private static final String HEADER = "label,trials,field,mean,median,min,max,standardDeviation";

    private TrialReportCsvFormatter() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Formats a series of trial summaries into a CSV comparison report.
     *
     * @param results the trial summaries to compare; at least one is required
     * @return the formatted CSV report, including a header row
     * @throws NullPointerException if {@code results} is {@code null}
     * @throws IllegalArgumentException if {@code results} is empty
     */
    public static String format(List<TrialSummary> results) {
        Objects.requireNonNull(results, "results must not be null");

        if (results.isEmpty()) {
            throw new IllegalArgumentException("at least one trial summary is required");
        }

        StringBuilder csv = new StringBuilder();
        csv.append(HEADER).append(System.lineSeparator());

        for (TrialSummary result : results) {
            for (ReportFields.Field field : ReportFields.ALL) {
                appendRow(csv, result, field);
            }
        }

        return csv.toString();
    }

    /**
     * Appends a single row covering one experiment's result for one field.
     *
     * @param csv the report being built
     * @param result the experiment result the row describes
     * @param field the measured field the row describes
     */
    private static void appendRow(StringBuilder csv, TrialSummary result, ReportFields.Field field) {
        Statistics statistics = field.statisticsOf().apply(result);

        csv.append(escape(result.label())).append(',')
                .append(result.analysis().sampleSize()).append(',')
                .append(escape(field.name())).append(',')
                .append(number(statistics.mean())).append(',')
                .append(number(statistics.median())).append(',')
                .append(number(statistics.minimum())).append(',')
                .append(number(statistics.maximum())).append(',')
                .append(number(statistics.standardDeviation()))
                .append(System.lineSeparator());
    }

    /**
     * Formats a statistic with a fixed number of decimals, independent of
     * the platform's default locale.
     *
     * @param value the value to format
     * @return the formatted value
     */
    private static String number(double value) {
        return String.format(Locale.ROOT, "%.2f", value);
    }

    /**
     * Escapes a value for safe inclusion as a single CSV field.
     *
     * <p>The value is quoted whenever it contains a comma, a double quote,
     * or a line break; any double quote it contains is itself doubled, per
     * the standard CSV quoting rule.
     *
     * @param value the value to escape
     * @return the value, quoted if necessary
     */
    private static String escape(String value) {
        if (value.indexOf(',') < 0 && value.indexOf('"') < 0
                && value.indexOf('\n') < 0 && value.indexOf('\r') < 0) {
            return value;
        }

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
