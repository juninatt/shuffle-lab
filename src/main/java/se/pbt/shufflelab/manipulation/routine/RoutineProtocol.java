package se.pbt.shufflelab.manipulation.routine;

import se.pbt.shufflelab.deck.Deck;

import java.util.random.RandomGenerator;

/**
 * Represents something that can be run as a single trial.
 *
 * <p>A {@code RoutineProtocol} performs its procedure exactly once against a deck,
 * given a source of randomness. It is the contract a trial runner depends on
 * to run something repeatedly and measure the outcome of each run — the
 * point in the library's flow where a defined shuffling procedure becomes
 * something that can actually be executed and analyzed:</p>
 *
 * <pre>Operation → Shuffle → Routine → Trial → Report</pre>
 */
@FunctionalInterface
public interface RoutineProtocol {

    /**
     * Executes this procedure once on the given deck.
     *
     * @param deck the deck to shuffle
     * @param random a source of controlled randomness
     */
    void execute(Deck deck, RandomGenerator random);
}