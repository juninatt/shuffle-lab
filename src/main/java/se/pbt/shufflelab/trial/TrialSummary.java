package se.pbt.shufflelab.trial;

import se.pbt.shufflelab.analysis.AggregatedDeckAnalysis;

import java.util.Objects;

/**
 * Associates a human-readable label with an {@link AggregatedDeckAnalysis},
 * so that the results of multiple experiments can be identified when
 * compared side by side.
 *
 * @param label a human-readable name identifying what was run, e.g. the
 *              technique and skill level used
 * @param analysis the aggregated analysis produced by the experiment
 */
public record TrialSummary(String label, AggregatedDeckAnalysis analysis) {

    public TrialSummary {
        Objects.requireNonNull(label, "label must not be null");
        Objects.requireNonNull(analysis, "analysis must not be null");
    }
}