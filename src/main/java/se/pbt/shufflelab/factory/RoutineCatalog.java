package se.pbt.shufflelab.factory;

import se.pbt.shufflelab.handling.routine.RoutineProtocol;
import se.pbt.shufflelab.skill.SkillLevel;

import java.util.function.Function;

/**
 * Enumerates every routine {@link RoutineFactory} can build, so that all
 * available routines can be discovered and iterated over programmatically,
 * instead of only being visible as individual factory methods scattered
 * through that class.
 *
 * <p>Each constant pairs a descriptive name with the {@link RoutineFactory}
 * method that constructs it. Adding a new predefined routine means adding
 * both a factory method there and a corresponding constant here — the
 * constant is what makes the routine visible to code that compares many
 * routines at once, such as {@link se.pbt.shufflelab.ShuffleLabApp}.</p>
 */
public enum RoutineCatalog {

    /**
     * @see RoutineFactory#simpleRiffleShuffle(SkillLevel)
     */
    SIMPLE_RIFFLE_SHUFFLE(RoutineFactory::simpleRiffleShuffle),

    /**
     * @see RoutineFactory#repeatedOverhandShuffle(SkillLevel)
     */
    REPEATED_OVERHAND_SHUFFLE(RoutineFactory::repeatedOverhandShuffle),

    /**
     * @see RoutineFactory#standardRiffleShuffle(SkillLevel)
     */
    STANDARD_RIFFLE_SHUFFLE(RoutineFactory::standardRiffleShuffle),

    /**
     * @see RoutineFactory#casualShuffleSequence(SkillLevel)
     */
    CASUAL_SHUFFLE_SEQUENCE(RoutineFactory::casualShuffleSequence),

    /**
     * @see RoutineFactory#pileShuffleThenRiffle(SkillLevel)
     */
    PILE_SHUFFLE_THEN_RIFFLE(RoutineFactory::pileShuffleThenRiffle),

    /**
     * @see RoutineFactory#idealRandomShuffle(SkillLevel)
     */
    IDEAL_RANDOM_SHUFFLE(RoutineFactory::idealRandomShuffle);

    private final Function<SkillLevel, RoutineProtocol> factory;

    RoutineCatalog(Function<SkillLevel, RoutineProtocol> factory) {
        this.factory = factory;
    }

    /**
     * Builds this catalog entry's routine for the supplied skill level.
     *
     * @param skillLevel the simulated performer skill level
     * @return the routine, configured for the supplied skill level
     * @throws NullPointerException if {@code skillLevel} is {@code null}
     */
    public RoutineProtocol create(SkillLevel skillLevel) {
        return factory.apply(skillLevel);
    }
}