package se.pbt.shufflelab.report;

import se.pbt.shufflelab.statistics.Statistics;
import se.pbt.shufflelab.trial.TrialSummary;

import java.util.List;
import java.util.function.Function;

/**
 * The complete set of measured fields, in report order, together with how to
 * extract each field's {@link Statistics} from a {@link TrialSummary}.
 *
 * <p>Every report formatter iterates this same list, so that adding,
 * removing, or reordering a measured field only requires a change in one
 * place instead of one per formatter.
 */
final class ReportFields {

    /**
     * A single measured field.
     *
     * @param section a human-readable name for the group of fields this one
     *                belongs to, e.g. {@code "Displacement"}
     * @param name the field's own name, e.g. {@code "unmovedCards"}
     * @param statisticsOf extracts this field's {@link Statistics} from a
     *                      {@link TrialSummary}
     */
    record Field(String section, String name, Function<TrialSummary, Statistics> statisticsOf) {
    }

    static final List<Field> ALL = List.of(
            new Field("Displacement", "unmovedCards", result -> result.analysis().displacement().unmovedCards()),
            new Field("Displacement", "totalDisplacement", result -> result.analysis().displacement().totalDisplacement()),
            new Field("Displacement", "maximumDisplacement", result -> result.analysis().displacement().maximumDisplacement()),

            new Field("Preserved order", "preservedPairs", result -> result.analysis().preservedOrder().preservedPairs()),
            new Field("Preserved order", "preservedSequences", result -> result.analysis().preservedOrder().preservedSequences()),
            new Field("Preserved order", "cardsInPairs", result -> result.analysis().preservedOrder().cardsInPairs()),
            new Field("Preserved order", "cardsInSequences", result -> result.analysis().preservedOrder().cardsInSequences()),
            new Field("Preserved order", "longestSequence", result -> result.analysis().preservedOrder().longestSequence()),
            new Field("Preserved order", "preservedCardPercentage", result -> result.analysis().preservedOrder().preservedCardPercentage())
    );

    private ReportFields() {
        throw new UnsupportedOperationException("Utility class");
    }
}
