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
    SIMPLE_RIFFLE_SHUFFLE(
            RoutineFactory::simpleRiffleShuffle,
            "One human-style riffle shuffle followed by a single cut."
    ),

    /**
     * @see RoutineFactory#repeatedOverhandShuffle(SkillLevel)
     */
    REPEATED_OVERHAND_SHUFFLE(
            RoutineFactory::repeatedOverhandShuffle,
            "Three overhand shuffles in a row, modelling how a performer typically repeats the technique."
    ),

    /**
     * @see RoutineFactory#standardRiffleShuffle(SkillLevel)
     */
    STANDARD_RIFFLE_SHUFFLE(
            RoutineFactory::standardRiffleShuffle,
            "Three riffle shuffles followed by a cut — the sequence commonly used at a casino table."
    ),

    /**
     * @see RoutineFactory#casualShuffleSequence(SkillLevel)
     */
    CASUAL_SHUFFLE_SEQUENCE(
            RoutineFactory::casualShuffleSequence,
            "An overhand shuffle to loosen the deck, followed by two riffle shuffles and a cut."
    ),

    /**
     * @see RoutineFactory#pileShuffleThenRiffle(SkillLevel)
     */
    PILE_SHUFFLE_THEN_RIFFLE(
            RoutineFactory::pileShuffleThenRiffle,
            "A 4-pile shuffle followed by one riffle shuffle and a cut, since a pile shuffle alone does not randomise a deck."
    ),

    /**
     * @see RoutineFactory#idealRandomShuffle(SkillLevel)
     */
    IDEAL_RANDOM_SHUFFLE(
            RoutineFactory::idealRandomShuffle,
            "A single Fisher–Yates shuffle — the mathematically ideal random baseline every other routine is measured against."
    ),

    /**
     * @see RoutineFactory#faroShuffleThenRiffle(SkillLevel)
     */
    FARO_SHUFFLE_THEN_RIFFLE(
            RoutineFactory::faroShuffleThenRiffle,
            "An out-Faro shuffle followed by one riffle shuffle and a cut, since a Faro shuffle alone is highly structured."
    ),

    /**
     * @see RoutineFactory#mongeanShuffleThenRiffle(SkillLevel)
     */
    MONGEAN_SHUFFLE_THEN_RIFFLE(
            RoutineFactory::mongeanShuffleThenRiffle,
            "A Mongean shuffle followed by one riffle shuffle and a cut, since a Mongean shuffle alone is highly structured."
    );

    private final Function<SkillLevel, RoutineProtocol> factory;
    private final String description;

    RoutineCatalog(Function<SkillLevel, RoutineProtocol> factory, String description) {
        this.factory = factory;
        this.description = description;
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

    /**
     * Returns a short, human-readable explanation of what this routine does.
     *
     * @return this routine's description
     */
    public String description() {
        return description;
    }
}