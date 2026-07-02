package se.pbt.shufflelab.manipulation.operation.interleave;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.TestRandoms;
import se.pbt.shufflelab.deck.DeckFactory;
import se.pbt.shufflelab.manipulation.operation.interleave.InterleaveStart;
import se.pbt.shufflelab.manipulation.operation.interleave.PerfectInterleaver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Perfect interleaver")
class PerfectInterleaverTest {

    @Test
    @DisplayName("A perfect interleave should alternate cards from both packets")
    void shouldAlternateCardsFromBothPackets() {
        var deck = DeckFactory.standardDeck();

        var top = deck.subList(0, 26);
        var bottom = deck.subList(26, 52);

        var interleaver = new PerfectInterleaver(InterleaveStart.TOP);

        var result = interleaver.interleave(
                top,
                bottom,
                TestRandoms.fixedRandom()
        );

        assertThat(result.get(0)).isEqualTo(top.get(0));
        assertThat(result.get(1)).isEqualTo(bottom.get(0));
        assertThat(result.get(2)).isEqualTo(top.get(1));
        assertThat(result.get(3)).isEqualTo(bottom.get(1));
    }

    @Test
    @DisplayName("A perfect interleave should keep every card from both packets")
    void shouldPreserveAllCards() {
        var deck = DeckFactory.standardDeck();

        var top = deck.subList(0, 26);
        var bottom = deck.subList(26, 52);

        var interleaver = new PerfectInterleaver(InterleaveStart.BOTTOM);

        var result = interleaver.interleave(
                top,
                bottom,
                TestRandoms.fixedRandom()
        );

        assertThat(result).containsExactlyInAnyOrderElementsOf(deck);
    }

    @Test
    @DisplayName("A perfect interleave should preserve the order within each packet")
    void shouldPreserveOrderWithinPackets() {
        var deck = DeckFactory.standardDeck();

        var top = deck.subList(0, 26);
        var bottom = deck.subList(26, 52);

        var interleaver = new PerfectInterleaver(InterleaveStart.BOTTOM);

        var result = interleaver.interleave(
                top,
                bottom,
                TestRandoms.fixedRandom()
        );

        assertThat(result.indexOf(top.get(0)))
                .isLessThan(result.indexOf(top.get(1)));

        assertThat(result.indexOf(top.get(1)))
                .isLessThan(result.indexOf(top.get(2)));

        assertThat(result.indexOf(bottom.get(0)))
                .isLessThan(result.indexOf(bottom.get(1)));
    }

    @Test
    @DisplayName("A perfect interleave should produce the same result regardless of random generator")
    void shouldProduceDeterministicResult() {
        var deck = DeckFactory.standardDeck();

        var top = deck.subList(0, 26);
        var bottom = deck.subList(26, 52);

        var interleaver = new PerfectInterleaver(InterleaveStart.BOTTOM);

        var first = interleaver.interleave(
                top,
                bottom,
                TestRandoms.fixedRandom()
        );

        var second = interleaver.interleave(
                top,
                bottom,
                TestRandoms.seededRandom(999)
        );

        assertThat(first).isEqualTo(second);
    }

    @Test
    @DisplayName("A perfect interleave should reject packets with too much imbalance")
    void shouldRejectPacketsWithTooMuchImbalance() {
        var deck = DeckFactory.standardDeck();

        var top = deck.subList(0, 3);
        var bottom = deck.subList(3, 8);

        var interleaver = new PerfectInterleaver(InterleaveStart.BOTTOM);

        assertThatThrownBy(() -> interleaver.interleave(
                top,
                bottom,
                TestRandoms.fixedRandom()
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Packet imbalance is too large for this riffle shuffle");
    }

    @Nested
    @DisplayName("Interleave start behavior")
    class FaroTypeBehavior {

        @Test
        @DisplayName("An interleave starting from the top packet should begin with a card from the top packet")
        void shouldStartWithTopPacket() {
            var deck = DeckFactory.standardDeck();

            var top = deck.subList(0, 26);
            var bottom = deck.subList(26, 52);

            var result = new PerfectInterleaver()
                    .interleave(top, bottom, TestRandoms.fixedRandom());

            assertThat(result.getFirst()).isEqualTo(top.getFirst());
        }

        @Test
        @DisplayName("An interleave starting from the bottom packet should begin with a card from the bottom packet")
        void shouldStartWithBottomPacket() {
            var deck = DeckFactory.standardDeck();

            var top = deck.subList(0, 26);
            var bottom = deck.subList(26, 52);

            var result = new PerfectInterleaver(InterleaveStart.BOTTOM)
                    .interleave(top, bottom, TestRandoms.fixedRandom());

            assertThat(result.getFirst()).isEqualTo(bottom.getFirst());
        }

        @Test
        @DisplayName("An interleave starting from the top packet should end with a card from the bottom packet")
        void shouldEndWithBottomPacket() {
            var deck = DeckFactory.standardDeck();

            var topPacket = deck.subList(0, 26);
            var bottomPacket = deck.subList(26, 52);

            var result = new PerfectInterleaver()
                    .interleave(topPacket, bottomPacket, TestRandoms.fixedRandom());

            assertThat(result.getLast()).isEqualTo(bottomPacket.getLast());
        }

        @Test
        @DisplayName("An interleave starting from the bottom packet should end with a card from the top packet")
        void shouldEndWithTopPacket() {
            var deck = DeckFactory.standardDeck();

            var topPacket = deck.subList(0, 26);
            var bottomPacket = deck.subList(26, 52);

            var result = new PerfectInterleaver(InterleaveStart.BOTTOM)
                    .interleave(topPacket, bottomPacket, TestRandoms.fixedRandom());

            assertThat(result.getLast()).isEqualTo(topPacket.getLast());
        }
    }
}
