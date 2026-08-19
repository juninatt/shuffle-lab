package se.pbt.shufflelab.trial;

import se.pbt.shufflelab.analysis.DeckAnalysis;
import se.pbt.shufflelab.analysis.DeckAnalyzer;
import se.pbt.shufflelab.deck.Deck;
import se.pbt.shufflelab.factory.DeckFactory;
import se.pbt.shufflelab.manipulation.routine.Routine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

/**
 * Runs a {@link Routine} repeatedly against fresh standard decks and
 * analyzes the outcome of each run.
 *
 * <p>Each trial starts from an identical, freshly created standard deck, so
 * that results only reflect the routine's own behavior and the randomness
 * supplied to it, not leftover state from a previous trial.
 */
public final class TrialRunner {

    private TrialRunner() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Runs the given routine repeatedly and analyzes each resulting deck.
     *
     * @param routine the routine to run
     * @param trials the number of times to run the routine; at least 1
     * @param random a source of randomness, shared across all trials so that
     *               each trial produces an independent outcome
     * @return one {@link DeckAnalysis} per trial, in the order the trials
     *         were run
     * @throws NullPointerException if {@code routine} or {@code random} is
     *                               {@code null}
     * @throws IllegalArgumentException if {@code trials} is less than 1
     */
    public static List<DeckAnalysis> run(Routine routine, int trials, RandomGenerator random) {
        Objects.requireNonNull(routine, "routine must not be null");
        Objects.requireNonNull(random, "random must not be null");

        if (trials < 1) {
            throw new IllegalArgumentException("trials must be at least 1");
        }

        Deck original = DeckFactory.standardDeck();
        List<DeckAnalysis> results = new ArrayList<>(trials);

        for (int trial = 0; trial < trials; trial++) {
            Deck shuffled = DeckFactory.standardDeck();
            routine.execute(shuffled, random);
            results.add(DeckAnalyzer.analyze(original, shuffled));
        }

        return List.copyOf(results);
    }
}