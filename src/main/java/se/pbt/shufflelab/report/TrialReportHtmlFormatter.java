package se.pbt.shufflelab.report;

import se.pbt.shufflelab.statistics.Statistics;
import se.pbt.shufflelab.trial.TrialSummary;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * Formats a series of {@link TrialSummary}s into a self-contained, interactive
 * HTML comparison report.
 *
 * <p>The report embeds one row per experiment and measured field — the same
 * tidy (long) shape {@link TrialReportJsonFormatter} produces, plus each
 * experiment's {@link TrialSummary#kind()}, {@link TrialSummary#skillLevel()}
 * and {@link TrialSummary#description()} — into a bundled HTML template, so
 * the resulting file can be opened directly in a browser: results can be
 * filtered by shuffle/routine and skill level, a measure can be picked from a
 * dropdown, each bar's full statistics and description are available on
 * hover, and a routine named "Ideal random shuffle" is highlighted as the
 * reference every other technique is measured against, if one is present.
 *
 * <p>This is a pure text-formatting step; it does not run any experiments or
 * print or write anything itself. No JSON library is used, since the data
 * being serialized is a fixed, simple shape.
 */
public final class TrialReportHtmlFormatter {

    private static final String TEMPLATE_PATH = "/templates/trial-report.html";
    private static final String ROWS_PLACEHOLDER = "/*__ROWS_JSON__*/[]";

    private TrialReportHtmlFormatter() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Formats a series of trial summaries into a self-contained HTML report.
     *
     * @param results the trial summaries to compare; at least one is required
     * @return the formatted HTML report, ready to be written to a {@code .html} file
     * @throws NullPointerException if {@code results} is {@code null}
     * @throws IllegalArgumentException if {@code results} is empty
     */
    public static String format(List<TrialSummary> results) {
        Objects.requireNonNull(results, "results must not be null");

        if (results.isEmpty()) {
            throw new IllegalArgumentException("at least one trial summary is required");
        }

        return loadTemplate().replace(ROWS_PLACEHOLDER, buildRowsJson(results));
    }

    /**
     * Builds the JSON array embedded into the template: one object per
     * experiment and measured field, carrying every piece of metadata the
     * template's filters and tooltip need.
     *
     * @param results the trial summaries to serialize
     * @return the JSON array, as text
     */
    private static String buildRowsJson(List<TrialSummary> results) {
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
                .append("\"kind\":").append(JsonSupport.quote(result.kind().name())).append(',')
                .append("\"skillLevel\":").append(JsonSupport.quote(result.skillLevel().name())).append(',')
                .append("\"description\":").append(JsonSupport.quote(result.description())).append(',')
                .append("\"trials\":").append(result.analysis().sampleSize()).append(',')
                .append("\"field\":").append(JsonSupport.quote(field.name())).append(',')
                .append("\"mean\":").append(JsonSupport.number(statistics.mean())).append(',')
                .append("\"median\":").append(JsonSupport.number(statistics.median())).append(',')
                .append("\"min\":").append(JsonSupport.number(statistics.minimum())).append(',')
                .append("\"max\":").append(JsonSupport.number(statistics.maximum())).append(',')
                .append("\"standardDeviation\":").append(JsonSupport.number(statistics.standardDeviation()))
                .append('}');
    }

    /**
     * Loads the bundled HTML template from the classpath.
     *
     * @return the template's contents
     * @throws IllegalStateException if the template resource cannot be found or read
     */
    private static String loadTemplate() {
        try (InputStream stream = TrialReportHtmlFormatter.class.getResourceAsStream(TEMPLATE_PATH)) {
            if (stream == null) {
                throw new IllegalStateException("Template resource not found: " + TEMPLATE_PATH);
            }

            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to read template resource: " + TEMPLATE_PATH, exception);
        }
    }
}
