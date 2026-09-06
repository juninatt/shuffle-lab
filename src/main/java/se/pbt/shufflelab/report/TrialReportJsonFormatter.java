package se.pbt.shufflelab.report;

import se.pbt.shufflelab.statistics.Statistics;
import se.pbt.shufflelab.trial.TrialSummary;

import java.util.List;
import java.util.Objects;

/**
 * Formats a series of {@link TrialSummary}s into a JSON comparison report.
 *
 * <p>The report is a JSON array with one object per experiment and measured
 * field, mirroring the tidy (long) form used by
 * {@link TrialReportCsvFormatter}, rather than one object per experiment
 * nesting every field. This keeps the shape of each array element identical
 * regardless of how many fields are measured, making the output
 * straightforward to consume from another program.
 *
 * <p>This is a pure text-formatting step; it does not run any experiments
 * or print or write anything itself. No JSON library is used, since the
 * data being serialized is a fixed, simple shape.
 */
public final class TrialReportJsonFormatter {

    private TrialReportJsonFormatter() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Formats a series of trial summaries into a JSON comparison report.
     *
     * @param results the trial summaries to compare; at least one is required
     * @return the formatted JSON report, as a single array
     * @throws NullPointerException if {@code results} is {@code null}
     * @throws IllegalArgumentException if {@code results} is empty
     */
    public static String format(List<TrialSummary> results) {
        Objects.requireNonNull(results, "results must not be null");

        if (results.isEmpty()) {
            throw new IllegalArgumentException("at least one trial summary is required");
        }

        StringBuilder json = new StringBuilder();
        json.append('[');

        boolean firstEntry = true;

        for (TrialSummary result : results) {
            for (ReportFields.Field field : ReportFields.ALL) {
                if (!firstEntry) {
                    json.append(',');
                }

                appendEntry(json, result, field);
                firstEntry = false;
            }
        }

        json.append(']');

        return json.toString();
    }

    /**
     * Appends a single object covering one experiment's result for one field.
     *
     * @param json the report being built
     * @param result the experiment result the object describes
     * @param field the measured field the object describes
     */
    private static void appendEntry(StringBuilder json, TrialSummary result, ReportFields.Field field) {
        Statistics statistics = field.statisticsOf().apply(result);

        json.append('{')
                .append("\"label\":").append(JsonSupport.quote(result.label())).append(',')
                .append("\"trials\":").append(result.analysis().sampleSize()).append(',')
                .append("\"field\":").append(JsonSupport.quote(field.name())).append(',')
                .append("\"mean\":").append(JsonSupport.number(statistics.mean())).append(',')
                .append("\"median\":").append(JsonSupport.number(statistics.median())).append(',')
                .append("\"min\":").append(JsonSupport.number(statistics.minimum())).append(',')
                .append("\"max\":").append(JsonSupport.number(statistics.maximum())).append(',')
                .append("\"standardDeviation\":").append(JsonSupport.number(statistics.standardDeviation()))
                .append('}');
    }
}
