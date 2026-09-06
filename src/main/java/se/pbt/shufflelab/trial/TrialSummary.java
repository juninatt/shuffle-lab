package se.pbt.shufflelab.trial;

import se.pbt.shufflelab.analysis.AggregatedDeckAnalysis;
import se.pbt.shufflelab.skill.SkillLevel;

import java.util.Objects;

/**
 * Associates a human-readable label with an {@link AggregatedDeckAnalysis},
 * so that the results of multiple experiments can be identified when
 * compared side by side.
 *
 * @param label a human-readable name identifying what was run, e.g. the
 *              technique and skill level used
 * @param description a short, human-readable explanation of what the
 *                     technique or routine actually does
 * @param kind whether this experiment ran a single {@link TrialKind#SHUFFLE}
 *             or a complete {@link TrialKind#ROUTINE}
 * @param skillLevel the simulated performer skill level used for this
 *                    experiment
 * @param analysis the aggregated analysis produced by the experiment
 */
public record TrialSummary(
        String label,
        String description,
        TrialKind kind,
        SkillLevel skillLevel,
        AggregatedDeckAnalysis analysis
) {

    public TrialSummary {
        Objects.requireNonNull(label, "label must not be null");
        Objects.requireNonNull(description, "description must not be null");
        Objects.requireNonNull(kind, "kind must not be null");
        Objects.requireNonNull(skillLevel, "skillLevel must not be null");
        Objects.requireNonNull(analysis, "analysis must not be null");
    }
}