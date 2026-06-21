package se.pbt.shufflelab.operation.interleave;

import se.pbt.shufflelab.card.Card;
import se.pbt.shufflelab.validation.RifflePacketValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

/**
 * Interleaves two packets by alternating one card from each packet.
 */
public class PerfectInterleaver implements Interleaver {

    @Override
    public List<Card> interleave(
            List<Card> left,
            List<Card> right,
            RandomGenerator random) {

        RifflePacketValidator.validate(left, right, 0.05);

        List<Card> interleaved = new ArrayList<>(left.size() + right.size());

        int leftIndex = 0;
        int rightIndex = 0;

        while (leftIndex < left.size() || rightIndex < right.size()) {
            if (leftIndex < left.size()) {
                interleaved.add(left.get(leftIndex++));
            }

            if (rightIndex < right.size()) {
                interleaved.add(right.get(rightIndex++));
            }
        }

        return interleaved;
    }
}
