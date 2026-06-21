package se.pbt.shufflelab.validation;

import se.pbt.shufflelab.card.Card;

import java.util.List;

/**
 * Validates that two packets are suitable for a riffle shuffle.
 */
public final class RifflePacketValidator {

    public RifflePacketValidator() {
    }

    public static void validate(
            List<Card> left,
            List<Card> right,
            double maxImbalanceRatio) {

        if (left.isEmpty() || right.isEmpty()) {
            throw new IllegalArgumentException(
                    "A riffle shuffle requires two non-empty packets");
        }

        int totalSize = left.size() + right.size();
        int sizeDifference = Math.abs(left.size() - right.size());
        double imbalanceRatio = (double) sizeDifference / totalSize;

        if (imbalanceRatio > maxImbalanceRatio) {
            throw new IllegalArgumentException(
                    "Packet imbalance is too large for this riffle shuffle");
        }
    }
}