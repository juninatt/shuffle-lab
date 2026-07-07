package se.pbt.shufflelab.skill;

import se.pbt.shufflelab.manipulation.operation.Operation;
import se.pbt.shufflelab.manipulation.routine.Routine;
import se.pbt.shufflelab.manipulation.shuffle.Shuffle;

/**
 * Represents a named level of skill, describing how precisely a person
 * performs a shuffling technique.
 *
 * <p>A skill level does not affect which {@link Operation operations}
 * or {@link Shuffle shuffles} a {@link Routine routine} consists of,
 * only how precisely each operation is carried out. This keeps routines
 * directly comparable across skill levels, since the same techniques
 * are always used.</p>
 */
public enum SkillLevel {

    /**
     * Loose, imprecise handling with large variation between attempts.
     */
    NOVICE,

    /**
     * Reasonably controlled handling with moderate variation.
     */
    INTERMEDIATE,

    /**
     * Tight, consistent handling with minimal variation.
     */
    EXPERT
}