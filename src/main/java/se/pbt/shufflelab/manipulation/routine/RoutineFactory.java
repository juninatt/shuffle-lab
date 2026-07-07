package se.pbt.shufflelab.manipulation.routine;

import se.pbt.shufflelab.manipulation.operation.cut.DeckCutter;
import se.pbt.shufflelab.manipulation.operation.split.BalancedDeckSplitter;
import se.pbt.shufflelab.manipulation.shuffle.Shuffle;
import se.pbt.shufflelab.manipulation.shuffle.ShuffleFactory;
import se.pbt.shufflelab.skill.SkillLevel;
import se.pbt.shufflelab.skill.SkillProfile;

import java.util.List;
import java.util.Objects;

/**
 * Public entry point for constructing reusable shuffling routines.
 *
 * <p>A routine represents one or more shuffles combined with additional deck
 * manipulations to model a complete real-world shuffling procedure.
 *
 * <p>This factory exposes a collection of predefined routines that hide the
 * underlying implementation details from client code. As the library evolves,
 * additional routines can be introduced without affecting the public API.
 */
public final class RoutineFactory {

    private RoutineFactory() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Creates a simple riffle shuffle routine.
     *
     * <p>The routine consists of one human-style riffle shuffle followed by one
     * deck cut. The riffle shuffle and the cut both use the {@link SkillProfile}
     * associated with the supplied {@link SkillLevel}.</p>
     *
     * <p>This represents the simplest complete riffle-based routine currently
     * exposed by the library.</p>
     *
     * @param skillLevel the simulated performer skill level
     * @return a simple riffle shuffle routine
     * @throws NullPointerException if {@code skillLevel} is {@code null}
     */
    public static Routine simpleRiffleShuffle(SkillLevel skillLevel) {
        Objects.requireNonNull(skillLevel, "skillLevel must not be null");

        SkillProfile profile = SkillProfile.forLevel(skillLevel);

        Shuffle riffleShuffle = ShuffleFactory.riffle(skillLevel);

        DeckCutter deckCutter = new DeckCutter(
                new BalancedDeckSplitter(profile.splitTolerance())
        );

        Shuffle cut = deckCutter::cut;

        return new SequentialRoutine(
                List.of(
                        riffleShuffle,
                        cut
                )
        );
    }
}