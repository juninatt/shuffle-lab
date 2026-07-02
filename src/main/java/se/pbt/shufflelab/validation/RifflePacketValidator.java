package se.pbt.shufflelab.validation;

import se.pbt.shufflelab.deck.card.Card;

import java.util.List;

/**
 * Validates that two packets are suitable for a riffle shuffle.
 */
public final class RifflePacketValidator {

    public RifflePacketValidator() {
    }

    public static void validate(
            List<Card> top,
            List<Card> bottom,
            double maxImbalanceRatio) {

        if (top.isEmpty() || bottom.isEmpty()) {
            throw new IllegalArgumentException(
                    "A riffle shuffle requires two non-empty packets");
        }

        int totalSize = top.size() + bottom.size();
        int sizeDifference = Math.abs(top.size() - bottom.size());
        double imbalanceRatio = (double) sizeDifference / totalSize;

        if (imbalanceRatio > maxImbalanceRatio) {
            throw new IllegalArgumentException(
                    "Packet imbalance is too large for this riffle shuffle");
        }
    }
}