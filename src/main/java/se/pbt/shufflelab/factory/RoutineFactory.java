package se.pbt.shufflelab.factory;

import se.pbt.shufflelab.handling.operation.cut.DeckCutter;
import se.pbt.shufflelab.handling.operation.split.BalancedDeckSplitter;
import se.pbt.shufflelab.handling.routine.Routine;
import se.pbt.shufflelab.handling.routine.RoutineProtocol;
import se.pbt.shufflelab.handling.shuffle.FaroShuffle;
import se.pbt.shufflelab.handling.shuffle.FaroType;
import se.pbt.shufflelab.handling.shuffle.FisherYatesShuffle;
import se.pbt.shufflelab.handling.shuffle.MongeanShuffle;
import se.pbt.shufflelab.handling.shuffle.PileShuffle;
import se.pbt.shufflelab.handling.shuffle.Shuffle;
import se.pbt.shufflelab.skill.SkillLevel;
import se.pbt.shufflelab.skill.SkillProfile;

import java.util.ArrayList;
import java.util.Collections;
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

    /**
     * The number of times a single overhand shuffle is repeated by
     * {@link #repeatedOverhandShuffle(SkillLevel)}.
     */
    private static final int OVERHAND_REPETITIONS = 3;

    /**
     * The number of riffle shuffles performed before the final cut in
     * {@link #standardRiffleShuffle(SkillLevel)}.
     */
    private static final int STANDARD_RIFFLE_REPETITIONS = 3;

    /**
     * The number of riffle shuffles performed after the overhand shuffle in
     * {@link #casualShuffleSequence(SkillLevel)}.
     */
    private static final int CASUAL_RIFFLE_REPETITIONS = 2;

    /**
     * The number of piles used by the pile shuffle in
     * {@link #pileShuffleThenRiffle(SkillLevel)}.
     */
    private static final int PILE_SHUFFLE_PILE_COUNT = 4;

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
    public static RoutineProtocol simpleRiffleShuffle(SkillLevel skillLevel) {
        Objects.requireNonNull(skillLevel, "skillLevel must not be null");

        SkillProfile profile = SkillProfile.withLevel(skillLevel);

        Shuffle riffleShuffle = ShuffleFactory.riffle(skillLevel);

        DeckCutter deckCutter = new DeckCutter(
                new BalancedDeckSplitter(profile.maxSplitDeviation())
        );

        Shuffle cut = deckCutter::cut;

        return new Routine(
                "Simple riffle shuffle",
                List.of(
                        riffleShuffle,
                        cut
                )
        );
    }

    /**
     * Creates a repeated overhand shuffle routine.
     *
     * <p>A single overhand shuffle only transfers a few packets and leaves
     * most of the original card order intact, so this routine repeats the
     * same overhand shuffle {@value #OVERHAND_REPETITIONS} times in
     * sequence, modelling how a performer typically overhand-shuffles a
     * deck several times in a row rather than just once. Every repetition
     * uses the {@link SkillProfile} associated with the supplied
     * {@link SkillLevel}.</p>
     *
     * @param skillLevel the simulated performer skill level
     * @return a repeated overhand shuffle routine
     * @throws NullPointerException if {@code skillLevel} is {@code null}
     */
    public static RoutineProtocol repeatedOverhandShuffle(SkillLevel skillLevel) {
        Objects.requireNonNull(skillLevel, "skillLevel must not be null");

        Shuffle overhandShuffle = ShuffleFactory.overhand(skillLevel);

        return new Routine(
                "Repeated overhand shuffle",
                Collections.nCopies(OVERHAND_REPETITIONS, overhandShuffle)
        );
    }

    /**
     * Creates a standard riffle shuffle routine.
     *
     * <p>A single riffle shuffle does not sufficiently randomise a deck on
     * its own, so this routine performs {@value #STANDARD_RIFFLE_REPETITIONS}
     * human-style riffle shuffles before finishing with a single deck cut,
     * modelling the shuffling sequence commonly used at a casino table
     * before a new round is dealt. Every riffle and the final cut use the
     * {@link SkillProfile} associated with the supplied {@link SkillLevel}.</p>
     *
     * @param skillLevel the simulated performer skill level
     * @return a standard riffle shuffle routine
     * @throws NullPointerException if {@code skillLevel} is {@code null}
     */
    public static RoutineProtocol standardRiffleShuffle(SkillLevel skillLevel) {
        Objects.requireNonNull(skillLevel, "skillLevel must not be null");

        SkillProfile profile = SkillProfile.withLevel(skillLevel);

        List<Shuffle> operations = new ArrayList<>(
                Collections.nCopies(STANDARD_RIFFLE_REPETITIONS, ShuffleFactory.riffle(skillLevel))
        );

        DeckCutter deckCutter = new DeckCutter(
                new BalancedDeckSplitter(profile.maxSplitDeviation())
        );

        operations.add(deckCutter::cut);

        return new Routine("Standard riffle shuffle", operations);
    }

    /**
     * Creates a casual shuffle sequence.
     *
     * <p>Models how many players shuffle informally: a single overhand
     * shuffle to loosen the deck, followed by {@value #CASUAL_RIFFLE_REPETITIONS}
     * human-style riffle shuffles to actually randomise it, finished with a
     * deck cut. Every step uses the {@link SkillProfile} associated with the
     * supplied {@link SkillLevel}.</p>
     *
     * @param skillLevel the simulated performer skill level
     * @return a casual shuffle sequence
     * @throws NullPointerException if {@code skillLevel} is {@code null}
     */
    public static RoutineProtocol casualShuffleSequence(SkillLevel skillLevel) {
        Objects.requireNonNull(skillLevel, "skillLevel must not be null");

        SkillProfile profile = SkillProfile.withLevel(skillLevel);

        List<Shuffle> operations = new ArrayList<>();
        operations.add(ShuffleFactory.overhand(skillLevel));
        operations.addAll(Collections.nCopies(CASUAL_RIFFLE_REPETITIONS, ShuffleFactory.riffle(skillLevel)));

        DeckCutter deckCutter = new DeckCutter(
                new BalancedDeckSplitter(profile.maxSplitDeviation())
        );

        operations.add(deckCutter::cut);

        return new Routine("Casual shuffle sequence", operations);
    }

    /**
     * Creates a pile shuffle followed by a riffle shuffle.
     *
     * <p>A pile shuffle alone rearranges a deck into a visibly different but
     * highly structured order — it does not introduce genuine randomness by
     * itself. This routine deals the deck into {@value #PILE_SHUFFLE_PILE_COUNT}
     * piles, then follows up with one human-style riffle shuffle and a final
     * cut to actually randomise the result. The riffle and the cut use the
     * {@link SkillProfile} associated with the supplied {@link SkillLevel};
     * the pile shuffle itself is deterministic and unaffected by skill.</p>
     *
     * @param skillLevel the simulated performer skill level
     * @return a pile shuffle followed by a riffle shuffle
     * @throws NullPointerException if {@code skillLevel} is {@code null}
     */
    public static RoutineProtocol pileShuffleThenRiffle(SkillLevel skillLevel) {
        Objects.requireNonNull(skillLevel, "skillLevel must not be null");

        SkillProfile profile = SkillProfile.withLevel(skillLevel);

        List<Shuffle> operations = new ArrayList<>();
        operations.add(new PileShuffle(PILE_SHUFFLE_PILE_COUNT));
        operations.add(ShuffleFactory.riffle(skillLevel));

        DeckCutter deckCutter = new DeckCutter(
                new BalancedDeckSplitter(profile.maxSplitDeviation())
        );

        operations.add(deckCutter::cut);

        return new Routine("Pile shuffle then riffle", operations);
    }

    /**
     * Creates the ideal random shuffle baseline.
     *
     * <p>Produces a mathematically uniform random permutation of the deck
     * using the Fisher–Yates algorithm, rather than modelling any real-world
     * shuffling technique. This gives every other routine a fixed reference
     * point to be measured against, instead of only being ranked against
     * each other.</p>
     *
     * @param skillLevel accepted for signature parity with every other
     *                    catalog entry, but otherwise ignored — this routine
     *                    is not affected by simulated performer skill
     * @return the ideal random shuffle baseline
     * @throws NullPointerException if {@code skillLevel} is {@code null}
     */
    public static RoutineProtocol idealRandomShuffle(SkillLevel skillLevel) {
        Objects.requireNonNull(skillLevel, "skillLevel must not be null");

        return new Routine(
                "Ideal random shuffle (baseline)",
                List.of(new FisherYatesShuffle())
        );
    }

    /**
     * Creates a Faro shuffle followed by a riffle shuffle.
     *
     * <p>A Faro shuffle alone rearranges a deck into a highly structured
     * order — repeated often enough, it even cycles back to the original
     * order — so it does not introduce genuine randomness by itself. This
     * routine performs one out-Faro shuffle, then follows up with one
     * human-style riffle shuffle and a final cut to actually randomise the
     * result. The riffle and the cut use the {@link SkillProfile} associated
     * with the supplied {@link SkillLevel}; the Faro shuffle itself is
     * deterministic and unaffected by skill.</p>
     *
     * @param skillLevel the simulated performer skill level
     * @return a Faro shuffle followed by a riffle shuffle
     * @throws NullPointerException if {@code skillLevel} is {@code null}
     */
    public static RoutineProtocol faroShuffleThenRiffle(SkillLevel skillLevel) {
        Objects.requireNonNull(skillLevel, "skillLevel must not be null");

        SkillProfile profile = SkillProfile.withLevel(skillLevel);

        List<Shuffle> operations = new ArrayList<>();
        operations.add(new FaroShuffle(FaroType.OUT));
        operations.add(ShuffleFactory.riffle(skillLevel));

        DeckCutter deckCutter = new DeckCutter(
                new BalancedDeckSplitter(profile.maxSplitDeviation())
        );

        operations.add(deckCutter::cut);

        return new Routine("Faro shuffle then riffle", operations);
    }

    /**
     * Creates a Mongean shuffle followed by a riffle shuffle.
     *
     * <p>A Mongean shuffle alone rearranges a deck into a highly structured,
     * fully reversed-and-interleaved order — it does not introduce genuine
     * randomness by itself. This routine performs one Mongean shuffle, then
     * follows up with one human-style riffle shuffle and a final cut to
     * actually randomise the result. The riffle and the cut use the
     * {@link SkillProfile} associated with the supplied {@link SkillLevel};
     * the Mongean shuffle itself is deterministic and unaffected by skill.</p>
     *
     * @param skillLevel the simulated performer skill level
     * @return a Mongean shuffle followed by a riffle shuffle
     * @throws NullPointerException if {@code skillLevel} is {@code null}
     */
    public static RoutineProtocol mongeanShuffleThenRiffle(SkillLevel skillLevel) {
        Objects.requireNonNull(skillLevel, "skillLevel must not be null");

        SkillProfile profile = SkillProfile.withLevel(skillLevel);

        List<Shuffle> operations = new ArrayList<>();
        operations.add(new MongeanShuffle());
        operations.add(ShuffleFactory.riffle(skillLevel));

        DeckCutter deckCutter = new DeckCutter(
                new BalancedDeckSplitter(profile.maxSplitDeviation())
        );

        operations.add(deckCutter::cut);

        return new Routine("Mongean shuffle then riffle", operations);
    }
}