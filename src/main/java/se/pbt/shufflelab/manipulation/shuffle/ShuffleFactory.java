package se.pbt.shufflelab.manipulation.shuffle;

import se.pbt.shufflelab.manipulation.operation.interleave.HumanInterleaver;
import se.pbt.shufflelab.manipulation.operation.interleave.InterleaveStart;
import se.pbt.shufflelab.manipulation.operation.split.BalancedDeckSplitter;
import se.pbt.shufflelab.skill.SkillLevel;
import se.pbt.shufflelab.skill.SkillProfile;

import java.util.Objects;

/**
 * Factory responsible for constructing individual {@link Shuffle} implementations.
 *
 * <p>The factory translates a {@link SkillLevel} into the corresponding
 * {@link SkillProfile} and configures the underlying components required by a
 * shuffle, such as deck splitters and packet interleavers.
 *
 * <p>This centralizes shuffle construction and prevents client code from
 * depending on low-level implementation details. New shuffle techniques should
 * be exposed through this factory rather than instantiated directly.
 */
public final class ShuffleFactory {

    private ShuffleFactory() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Creates a human-style riffle shuffle configured for the supplied skill level.
     *
     * <p>The supplied {@link SkillLevel} controls how accurately the deck is split
     * and how many cards may be released from a packet during interleaving.</p>
     *
     * @param skillLevel the simulated performer skill level
     * @return a configured human-style riffle shuffle
     * @throws NullPointerException if {@code skillLevel} is {@code null}
     */
    public static Shuffle riffle(SkillLevel skillLevel) {
        Objects.requireNonNull(skillLevel, "skillLevel must not be null");

        SkillProfile profile = SkillProfile.withLevel(skillLevel);

        return new RiffleShuffle(
                new BalancedDeckSplitter(profile.maxSplitDeviation()),
                new HumanInterleaver(
                        InterleaveStart.BOTTOM,
                        profile.maxInterleavePacketSize()
                )
        );
    }

    /**
     * Creates an overhand shuffle for a simulated performer with the supplied
     * skill level.
     *
     * <p>The supplied {@link SkillLevel} is converted to a
     * {@link SkillProfile}, whose overhand-specific packet-size limit determines
     * the maximum number of cards that may be transferred in one step.</p>
     *
     * @param skillLevel the simulated performer's skill level
     * @return an overhand shuffle configured for the supplied skill level
     * @throws NullPointerException if {@code skillLevel} is {@code null}
     */
    public static Shuffle overhand(SkillLevel skillLevel) {
        Objects.requireNonNull(skillLevel, "skillLevel must not be null");

        SkillProfile profile = SkillProfile.withLevel(skillLevel);

        return new OverhandShuffle(
                profile.maxOverhandPacketSize()
        );
    }
}