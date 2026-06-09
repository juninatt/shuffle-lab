package se.pbt.shufflelab.operation.riffle;

import se.pbt.shufflelab.card.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

/**
 * Interleaves two packets by dropping small groups of cards from each packet.
 */
public class HumanRiffleInterleaver implements RiffleInterleaver {

    private final int maxDropSize;

    public HumanRiffleInterleaver(int maxDropSize) {
        if (maxDropSize < 1) {
            throw new IllegalArgumentException("maxDropSize must be at least 1");
        }

        this.maxDropSize = maxDropSize;
    }

    @Override
    public List<Card> interleave(
            List<Card> left,
            List<Card> right,
            RandomGenerator random) {

        RifflePacketValidator.validate(left, right, 0.15);

        List<Card> interleaved = new ArrayList<>(left.size() + right.size());

        int leftIndex = 0;
        int rightIndex = 0;
        boolean takeFromLeft = random.nextBoolean();

        while (leftIndex < left.size() || rightIndex < right.size()) {
            if (takeFromLeft && leftIndex < left.size()) {
                leftIndex = dropCards(left, leftIndex, interleaved, random);
            } else if (rightIndex < right.size()) {
                rightIndex = dropCards(right, rightIndex, interleaved, random);
            }

            takeFromLeft = !takeFromLeft;
        }

        return interleaved;
    }

    private int dropCards(
            List<Card> source,
            int startIndex,
            List<Card> target,
            RandomGenerator random) {

        int remaining = source.size() - startIndex;
        int amount = random.nextInt(1, Math.min(maxDropSize, remaining) + 1);

        for (int i = 0; i < amount; i++) {
            target.add(source.get(startIndex + i));
        }

        return startIndex + amount;
    }
}