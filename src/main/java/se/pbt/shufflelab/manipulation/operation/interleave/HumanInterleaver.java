package se.pbt.shufflelab.manipulation.operation.interleave;

import se.pbt.shufflelab.deck.card.Card;
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
    private final int maxInterleavePacketSize;

    private final InterleaveStart interleaveStart;

    /**
     * The maximum allowed size imbalance between the two packets being
     * interleaved, expressed as a ratio of their size difference to the
     * total number of cards.
     */
    private final double maxImbalanceRatio;

    /**
     * Creates a human interleaver with the supplied starting packet,
     * maximum packet size, and maximum packet imbalance.
     *
     * @param start the packet from which interleaving begins
     * @param maxInterleavePacketSize the maximum number of cards that may be
     *                                taken from one packet during a single
     *                                interleaving step
     * @param maxImbalanceRatio the maximum allowed size imbalance between
     *                          the two packets being interleaved; must be
     *                          large enough to tolerate however the packets
     *                          were split
     * @throws NullPointerException if {@code start} is {@code null}
     * @throws IllegalArgumentException if {@code maxInterleavePacketSize}
     *                                  is less than one, or if
     *                                  {@code maxImbalanceRatio} is not
     *                                  greater than zero and at most one
     */
    public HumanInterleaver(
            InterleaveStart start,
            int maxInterleavePacketSize,
            double maxImbalanceRatio) {

        if (maxInterleavePacketSize < 1) {
            throw new IllegalArgumentException(
                    "maxInterleavePacketSize must be at least 1");
        }

        if (maxImbalanceRatio <= 0 || maxImbalanceRatio > 1) {
            throw new IllegalArgumentException(
                    "maxImbalanceRatio must be greater than 0 and at most 1");
        }

        this.interleaveStart = start;
        this.maxInterleavePacketSize = maxInterleavePacketSize;
        this.maxImbalanceRatio = maxImbalanceRatio;
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

        RifflePacketValidator.validate(topPacket, bottomPacket, maxImbalanceRatio);

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

        int numberOfRemainingCards = source.size() - startIndex;

        int packetSize = random.nextInt(
                1,
                Math.min(maxInterleavePacketSize, numberOfRemainingCards) + 1
        );

        for (int i = 0; i < packetSize; i++) {
            target.add(source.get(startIndex + i));
        }

        return startIndex + packetSize;
    }
}