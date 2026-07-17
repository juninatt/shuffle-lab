package se.pbt.shufflelab.analysis;

/**
 * Represents the result of analysing groups of cards that remain in their original consecutive order after shuffling.
 *
 * <p>A preserved pair consists of exactly two consecutive cards from the
 * original deck that remain consecutive and in the same order after the shuffle.</p>
 *
 * <p>A preserved sequence consists of three or more consecutive cards from
 * the original deck that remain in the same order after the shuffle.
 * Cards belonging to a preserved sequence are not also counted as preserved pairs.</p>
 *
 * <p>The result therefore separates preserved groups of exactly two cards from longer preserved sequences
 * and prevents the same cards from being counted more than once.</p>
 *
 * @param preservedPairs number of preserved groups containing exactly two cards
 * @param preservedSequences number of preserved groups containing at least three cards
 * @param cardsInPairs total number of cards belonging to preserved pairs
 * @param cardsInSequences total number of cards belonging to preserved sequences
 * @param longestSequence number of cards in the longest preserved sequence,
 *                        or {@code 0} if no preserved sequence exists
 * @param preservedCardPercentage percentage of cards belonging to either a
 *                                preserved pair or a preserved sequence
 */
public record PreservedOrderResult(
        int preservedPairs,
        int preservedSequences,
        int cardsInPairs,
        int cardsInSequences,
        int longestSequence,
        double preservedCardPercentage
) {

    /**
     * Creates a result containing potential preserved order patterns
     * identified during analysis of a shuffled deck.
     *
     * @throws IllegalArgumentException if any count is negative, if the number
     *                                  of cards in pairs does not correspond to
     *                                  the number of preserved pairs, if the
     *                                  sequence values are inconsistent, or if
     *                                  the percentage is outside the valid range
     */
    public PreservedOrderResult {

        if (preservedPairs < 0) {
            throw new IllegalArgumentException(
                    "preservedPairs must not be negative"
            );
        }

        if (preservedSequences < 0) {
            throw new IllegalArgumentException(
                    "preservedSequences must not be negative"
            );
        }

        if (cardsInPairs < 0) {
            throw new IllegalArgumentException(
                    "cardsInPairs must not be negative"
            );
        }

        if (cardsInSequences < 0) {
            throw new IllegalArgumentException(
                    "cardsInSequences must not be negative"
            );
        }

        if (longestSequence < 0) {
            throw new IllegalArgumentException(
                    "longestSequence must not be negative"
            );
        }

        if (cardsInPairs != preservedPairs * 2) {
            throw new IllegalArgumentException(
                    "cardsInPairs must equal preservedPairs multiplied by two"
            );
        }

        if (preservedSequences == 0) {
            if (cardsInSequences != 0) {
                throw new IllegalArgumentException(
                        "cardsInSequences must be zero when no preserved sequences exist"
                );
            }

            if (longestSequence != 0) {
                throw new IllegalArgumentException(
                        "longestSequence must be zero when no preserved sequences exist"
                );
            }
        }

        if (preservedSequences > 0) {
            if (cardsInSequences < preservedSequences * 3) {
                throw new IllegalArgumentException(
                        "each preserved sequence must contain at least three cards"
                );
            }

            if (longestSequence < 3) {
                throw new IllegalArgumentException(
                        "longestSequence must contain at least three cards"
                );
            }

            if (longestSequence > cardsInSequences) {
                throw new IllegalArgumentException(
                        "longestSequence must not exceed cardsInSequences"
                );
            }
        }

        if (Double.isNaN(preservedCardPercentage)
                || Double.isInfinite(preservedCardPercentage)
                || preservedCardPercentage < 0.0
                || preservedCardPercentage > 100.0) {
            throw new IllegalArgumentException(
                    "preservedCardPercentage must be between 0.0 and 100.0"
            );
        }
    }
}