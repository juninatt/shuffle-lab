package se.pbt.shufflelab.skill;

import se.pbt.shufflelab.manipulation.operation.interleave.Interleaver;
import se.pbt.shufflelab.manipulation.operation.split.DeckSplitter;

import java.util.Objects;

/**
 * Holds the concrete numeric parameters that express how precisely a
 * shuffling technique is carried out.
 *
 * <p>A {@code SkillProfile} is the single place where skill-related
 * numbers live. {@link DeckSplitter} and {@link Interleaver}
 * implementations consume these values but do not decide them, and
 * routines never vary based on skill, only the precision of the
 * operations they are built from.</p>
 *
 * @param maxDropSize the maximum number of cards released from a
 *                     packet in a single step during interleaving;
 *                     larger values produce a less controlled interleave
 * @param splitTolerance the maximum allowed deviation from an exact
 *                        split point; larger values produce a less
 *                        precise split
 */
public record SkillProfile(int maxDropSize, int splitTolerance) {

    public SkillProfile {
        if (maxDropSize < 1) {
            throw new IllegalArgumentException("maxDropSize must be at least 1");
        }

        if (splitTolerance < 0) {
            throw new IllegalArgumentException("splitTolerance must not be negative");
        }
    }

    /**
     * Returns the canonical profile for a given skill level.
     *
     * @param level the skill level to look up
     * @return the profile associated with the given skill level
     */
    public static SkillProfile forLevel(SkillLevel level) {
        Objects.requireNonNull(level, "level must not be null");

        return switch (level) {
            case NOVICE -> new SkillProfile(8, 6);
            case INTERMEDIATE -> new SkillProfile(4, 3);
            case EXPERT -> new SkillProfile(2, 1);
        };
    }
}