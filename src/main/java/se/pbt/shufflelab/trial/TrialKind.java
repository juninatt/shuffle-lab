package se.pbt.shufflelab.trial;

/**
 * Distinguishes a single-technique {@link se.pbt.shufflelab.handling.shuffle.Shuffle}
 * from a complete, multi-step {@link se.pbt.shufflelab.handling.routine.Routine},
 * so reports can separate the two instead of listing them side by side
 * undifferentiated.
 */
public enum TrialKind {

    /**
     * A single shuffling technique, run on its own.
     */
    SHUFFLE,

    /**
     * A complete real-world shuffling procedure composed of one or more
     * shuffles and operations.
     */
    ROUTINE
}
