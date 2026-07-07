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

        SkillProfile profile = SkillProfile.forLevel(skillLevel);

        return new RiffleShuffle(
                new BalancedDeckSplitter(profile.splitTolerance()),
                new HumanInterleaver(
                        InterleaveStart.BOTTOM,
                        profile.maxDropSize()
                )
        );
    }
}