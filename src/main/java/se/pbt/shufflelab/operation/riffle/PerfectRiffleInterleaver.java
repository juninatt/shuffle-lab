package se.pbt.shufflelab.operation.riffle;

import se.pbt.shufflelab.card.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

/**
 * Interleaves two packets by alternating one card from each packet.
 */
public class PerfectRiffleInterleaver implements RiffleInterleaver {

    @Override
    public List<Card> interleave(
            List<Card> left,
            List<Card> right,
            RandomGenerator random) {

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
