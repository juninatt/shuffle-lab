package se.pbt.shufflelab.manipulation.operation.interleave;

import se.pbt.shufflelab.deck.card.Card;
import se.pbt.shufflelab.validation.RifflePacketValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

/**
 * Interleaves two packets by alternating exactly one card from each packet.
 *
 * <p>This produces a perfectly ordered interleave where cards from both
 * packets are woven together without variation.</p>
 */
public class PerfectInterleaver implements Interleaver {

    /**
     * Determines which packet releases the first card.
     */
    private final InterleaveStart interleaveStart;

    /**
     * Creates a perfect interleaver.
     *
     * @param interleaveStart the packet that releases the first card
     */
    public PerfectInterleaver(InterleaveStart interleaveStart) {
        this.interleaveStart = interleaveStart;
    }

    /**
     * Creates a perfect interleaver that starts with the top packet.
     */
    public PerfectInterleaver() {
        this.interleaveStart = InterleaveStart.TOP;
    }

    /**
     * Interleaves two packets by alternating one card at a time.
     *
     * @param topPacket the top half of the deck
     * @param bottomPacket the bottom half of the deck
     * @param random a source of controlled randomness
     * @return the interleaved cards
     */
    @Override
    public List<Card> interleave(
            List<Card> topPacket,
            List<Card> bottomPacket,
            RandomGenerator random) {

        RifflePacketValidator.validate(topPacket, bottomPacket, 0.05);

        List<Card> interleaved = new ArrayList<>(topPacket.size() + bottomPacket.size());

        int topIndex = 0;
        int bottomIndex = 0;

        boolean takeFromTopPacket = interleaveStart == InterleaveStart.TOP;

        while (topIndex < topPacket.size() || bottomIndex < bottomPacket.size()) {

            if (takeFromTopPacket && topIndex < topPacket.size()) {
                interleaved.add(topPacket.get(topIndex++));
            }

            if (!takeFromTopPacket && bottomIndex < bottomPacket.size()) {
                interleaved.add(bottomPacket.get(bottomIndex++));
            }

            takeFromTopPacket = !takeFromTopPacket;
        }

        return interleaved;
    }
}
