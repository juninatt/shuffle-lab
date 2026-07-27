package se.pbt.shufflelab.analysis.displacement;

/**
 * Represents the result of analysing how far cards have moved from their
 * original positions after shuffling.
 *
 * <p>The result contains displacement metrics describing the overall movement
 * of the deck. Cards that remain in their original positions are recorded
 * directly, while derived values such as moved cards and percentages can be
 * obtained through the provided convenience methods.</p>
 *
 * @param totalCards total number of cards included in the analysis
 * @param unmovedCards number of cards that remain in their original positions
 * @param totalDisplacement sum of the positional displacement of all cards
 * @param maximumDisplacement greatest positional displacement observed for any
 *                            single card
 */
public record DisplacementResult(
        int totalCards,
        int unmovedCards,
        long totalDisplacement,
        int maximumDisplacement
) {

    /**
     * Creates a result containing displacement metrics identified during
     * analysis of a shuffled deck.
     *
     * @throws IllegalArgumentException if any value is negative, if the number
     *                                  of unmoved cards exceeds the total
     *                                  number of cards, or if the maximum
     *                                  displacement exceeds the maximum
     *                                  possible displacement for the analysed
     *                                  deck
     */
    public DisplacementResult {

        if (totalCards < 0) {
            throw new IllegalArgumentException(
                    "totalCards must not be negative"
            );
        }

        if (unmovedCards < 0) {
            throw new IllegalArgumentException(
                    "unmovedCards must not be negative"
            );
        }

        if (unmovedCards > totalCards) {
            throw new IllegalArgumentException(
                    "unmovedCards must not exceed totalCards"
            );
        }

        if (totalDisplacement < 0) {
            throw new IllegalArgumentException(
                    "totalDisplacement must not be negative"
            );
        }

        if (maximumDisplacement < 0) {
            throw new IllegalArgumentException(
                    "maximumDisplacement must not be negative"
            );
        }

        if (totalCards == 0 && maximumDisplacement != 0) {
            throw new IllegalArgumentException(
                    "maximumDisplacement must be zero when totalCards is zero"
            );
        }

        if (totalCards > 0 && maximumDisplacement > totalCards - 1) {
            throw new IllegalArgumentException(
                    "maximumDisplacement exceeds the maximum possible displacement"
            );
        }
    }

    /**
     * Returns the number of cards that moved from their original positions.
     *
     * @return number of moved cards
     */
    public int movedCards() {
        return totalCards - unmovedCards;
    }

    /**
     * Returns the percentage of cards that remained in their original
     * positions.
     *
     * @return percentage of unmoved cards
     */
    public double unmovedCardPercentage() {
        return totalCards == 0
                ? 0.0
                : unmovedCards * 100.0 / totalCards;
    }

    /**
     * Returns the percentage of cards that moved from their original
     * positions.
     *
     * @return percentage of moved cards
     */
    public double movedCardPercentage() {
        return totalCards == 0
                ? 0.0
                : movedCards() * 100.0 / totalCards;
    }

    /**
     * Returns the average positional displacement per card.
     *
     * @return average displacement
     */
    public double averageDisplacement() {
        return totalCards == 0
                ? 0.0
                : (double) totalDisplacement / totalCards;
    }
}