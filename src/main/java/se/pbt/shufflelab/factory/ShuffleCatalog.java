package se.pbt.shufflelab.factory;

import se.pbt.shufflelab.handling.routine.RoutineProtocol;
import se.pbt.shufflelab.handling.shuffle.FaroShuffle;
import se.pbt.shufflelab.handling.shuffle.FaroType;
import se.pbt.shufflelab.handling.shuffle.FisherYatesShuffle;
import se.pbt.shufflelab.handling.shuffle.MongeanShuffle;
import se.pbt.shufflelab.handling.shuffle.MongeanStart;
import se.pbt.shufflelab.handling.shuffle.PileShuffle;
import se.pbt.shufflelab.handling.shuffle.Shuffle;
import se.pbt.shufflelab.skill.SkillLevel;

import java.util.Objects;
import java.util.function.Function;

/**
 * Enumerates every single-technique {@link Shuffle} that can be run and
 * measured on its own, so that all available shuffles can be discovered and
 * iterated over programmatically — mirroring what {@link RoutineCatalog}
 * does for complete, multi-step routines.
 *
 * <p>A single shuffle models one pass of a technique in isolation, with no
 * follow-up cut or repetition. Comparing these alongside {@link
 * RoutineCatalog} entries shows how much a real-world routine's additional
 * steps actually contribute over the bare technique.</p>
 */
public enum ShuffleCatalog {

    RIFFLE(
            "Riffle shuffle",
            "A single riffle shuffle pass: the deck is split in two and the halves are interleaved.",
            skillLevel -> ShuffleFactory.riffle(skillLevel)::shuffle
    ),

    OVERHAND(
            "Overhand shuffle",
            "A single overhand shuffle pass: small packets are transferred from one hand to the other.",
            skillLevel -> ShuffleFactory.overhand(skillLevel)::shuffle
    ),

    PILE(
            "Pile shuffle",
            "Deals the deck into 4 piles and collects them in reverse order. Deterministic — the simulated skill level has no effect.",
            skillLevel -> new PileShuffle(4)::shuffle
    ),

    MONGEAN_TOP(
            "Mongean shuffle (top start)",
            "Deals cards alternately onto the top and bottom of a new pile, starting with top. Deterministic — the simulated skill level has no effect.",
            skillLevel -> new MongeanShuffle(MongeanStart.TOP)::shuffle
    ),

    MONGEAN_BOTTOM(
            "Mongean shuffle (bottom start)",
            "Deals cards alternately onto the top and bottom of a new pile, starting with bottom. Deterministic — the simulated skill level has no effect.",
            skillLevel -> new MongeanShuffle(MongeanStart.BOTTOM)::shuffle
    ),

    FARO_OUT(
            "Out-Faro shuffle",
            "A perfect Faro shuffle that keeps the original top and bottom cards in place. Deterministic — the simulated skill level has no effect.",
            skillLevel -> new FaroShuffle(FaroType.OUT)::shuffle
    ),

    FARO_IN(
            "In-Faro shuffle",
            "A perfect Faro shuffle that moves the original top and bottom cards inward. Deterministic — the simulated skill level has no effect.",
            skillLevel -> new FaroShuffle(FaroType.IN)::shuffle
    ),

    FISHER_YATES(
            "Fisher–Yates shuffle",
            "A mathematically uniform random permutation — the ideal every other shuffle is measured against. Not affected by the simulated skill level.",
            skillLevel -> new FisherYatesShuffle()::shuffle
    );

    private final String displayName;
    private final String description;
    private final Function<SkillLevel, RoutineProtocol> factory;

    ShuffleCatalog(String displayName, String description, Function<SkillLevel, RoutineProtocol> factory) {
        this.displayName = displayName;
        this.description = description;
        this.factory = factory;
    }

    /**
     * Builds this catalog entry's shuffle for the supplied skill level,
     * adapted to run as a single-pass {@link RoutineProtocol}.
     *
     * @param skillLevel the simulated performer skill level
     * @return the shuffle, configured for the supplied skill level
     * @throws NullPointerException if {@code skillLevel} is {@code null}
     */
    public RoutineProtocol create(SkillLevel skillLevel) {
        Objects.requireNonNull(skillLevel, "skillLevel must not be null");

        return factory.apply(skillLevel);
    }

    /**
     * Returns this shuffle's human-readable display name.
     *
     * @return this shuffle's display name
     */
    public String displayName() {
        return displayName;
    }

    /**
     * Returns a short, human-readable explanation of what this shuffle does.
     *
     * @return this shuffle's description
     */
    public String description() {
        return description;
    }
}
