package se.pbt.shufflelab.skill;

import se.pbt.shufflelab.manipulation.shuffle.OverhandShuffle;

import java.util.Objects;

/**
 * Holds the concrete numeric parameters that express how precisely
 * different shuffling operations are carried out.
 *
 * <p>A {@code SkillProfile} is the single place where skill-related numeric
 * parameters are defined. Individual shuffle operations consume the parameters
 * relevant to them but do not decide which values correspond to a particular
 * skill level.</p>
 *
 * <p>Routines remain structurally identical across skill levels. Skill affects
 * only how precisely their underlying shuffles and operations are performed.</p>
 *
 * @param maxInterleavePacketSize the maximum number of cards released from one
 *                                packet during a single interleaving step;
 *                                larger values produce a less controlled
 *                                interleave
 * @param maxOverhandPacketSize the maximum number of cards transferred in one
 *                              packet during an {@link OverhandShuffle};
 *                              larger values preserve larger sections of the
 *                              original deck
 * @param maxSplitDeviation the maximum number of cards by which a split point
 *                          may deviate from the exact middle of the deck;
 *                          larger values produce a less precise split
 */
public record SkillProfile(
        int maxInterleavePacketSize,
        int maxSplitDeviation,
        int maxOverhandPacketSize
) {

    /**
     * Validates the numeric parameters of a skill profile.
     *
     * @throws IllegalArgumentException if either packet-size value is less
     *                                  than one or if
     *                                  {@code maxSplitDeviation} is negative
     */
    public SkillProfile {
        if (maxInterleavePacketSize < 1) {
            throw new IllegalArgumentException(
                    "maxInterleavePacketSize must be at least 1"
            );
        }

        if (maxSplitDeviation < 0) {
            throw new IllegalArgumentException(
                    "maxSplitDeviation must not be negative"
            );
        }

        if (maxOverhandPacketSize < 1) {
            throw new IllegalArgumentException(
                    "maxOverhandPacketSize must be at least 1"
            );
        }
    }

    /**
     * Returns the canonical profile associated with the supplied skill level.
     *
     * <p>The returned profile contains predefined parameters for interleaving,
     * overhand packet transfers, and deck splitting. A caller that requires
     * more precise control may construct a custom {@code SkillProfile}
     * directly.</p>
     *
     * @param level the skill level to look up
     * @return the canonical profile associated with the supplied skill level
     * @throws NullPointerException if {@code level} is {@code null}
     */
    public static SkillProfile withLevel(SkillLevel level) {
        Objects.requireNonNull(level, "level must not be null");

        return switch (level) {
            case NOVICE -> new SkillProfile(8, 6, 8);
            case INTERMEDIATE -> new SkillProfile(4, 3, 4);
            case EXPERT -> new SkillProfile(2, 1, 2);
        };
    }
}