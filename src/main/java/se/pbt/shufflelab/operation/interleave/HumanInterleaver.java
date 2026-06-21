package se.pbt.shufflelab.operation.interleave;

import se.pbt.shufflelab.card.Card;
import se.pbt.shufflelab.validation.RifflePacketValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

/**
 * Interleaves two packets in a human-like way.
 *
 * <p>Instead of alternating exactly one card at a time, this interleaver
 * releases small groups of cards from each packet, producing a less
 * controlled and more realistic interleave.</p>
 */
public class HumanInterleaver implements Interleaver {

    /**
     * The maximum number of cards that may be released from a packet
     * in a single step of the shuffle.
     */
    private final int maxDropSize;

    private final InterleaveStart interleaveStart;

    /**
     * Creates a human-style riffle interleaver.
     *
     * @param interleaveStart the packet that releases cards first
     * @param maxDropSize the maximum number of cards that may be released
     *                    from a packet at once during the shuffle
     */
    public HumanInterleaver(
            InterleaveStart interleaveStart,
            int maxDropSize) {

        if (maxDropSize < 1) {
            throw new IllegalArgumentException(
                    "maxDropSize must be at least 1");
        }

        this.interleaveStart = interleaveStart;
        this.maxDropSize = maxDropSize;
    }

    /**
     * Interleaves two packets by releasing small groups of cards
     * from each packet.
     *
     * @param topPacket the top packet
     * @param bottomPacket the bottom packet
     * @param random a source of controlled randomness
     * @return the interleaved cards
     */
    @Override
    public List<Card> interleave(
            List<Card> topPacket,
            List<Card> bottomPacket,
            RandomGenerator random) {

        RifflePacketValidator.validate(topPacket, bottomPacket, 0.15);

        List<Card> interleaved = new ArrayList<>(topPacket.size() + bottomPacket.size());

        int topIndex = 0;
        int bottomIndex = 0;

        boolean takeFromTopPacket = interleaveStart == InterleaveStart.TOP;

        while (topIndex < topPacket.size() || bottomIndex < bottomPacket.size()) {

            if (takeFromTopPacket && topIndex < topPacket.size()) {
                topIndex = dropCards(
                        topPacket,
                        topIndex,
                        interleaved,
                        random
                );
            } else if (bottomIndex < bottomPacket.size()) {
                bottomIndex = dropCards(
                        bottomPacket,
                        bottomIndex,
                        interleaved,
                        random
                );
            }

            takeFromTopPacket = !takeFromTopPacket;
        }

        return interleaved;
    }

    /**
     * Releases a small group of cards from a packet into the interleaved result.
     *
     * @param source the packet to release cards from
     * @param startIndex the index of the next card to release
     * @param target the interleaved result being built
     * @param random a source of controlled randomness
     * @return the index of the next unreleased card in the source packet
     */
    private int dropCards(
            List<Card> source,
            int startIndex,
            List<Card> target,
            RandomGenerator random) {

        int remaining = source.size() - startIndex;

        int amount = random.nextInt(
                1,
                Math.min(maxDropSize, remaining) + 1
        );

        for (int i = 0; i < amount; i++) {
            target.add(source.get(startIndex + i));
        }

        return startIndex + amount;
    }
}
