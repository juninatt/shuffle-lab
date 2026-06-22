package se.pbt.shufflelab.operation.interleave;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.TestRandoms;
import se.pbt.shufflelab.deck.DeckFactory;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Human interleaver")
class HumanInterleaverTest {

    @Test
    @DisplayName("A human interleave should keep every card from both packets")
    void shouldPreserveAllCards() {
        var deck = DeckFactory.standardDeck();

        var top = deck.subList(0, 26);
        var bottom = deck.subList(26, 52);

        var interleaver = new HumanInterleaver(InterleaveStart.BOTTOM, 3);
        var random = TestRandoms.fixedRandom();

        var result = interleaver.interleave(top, bottom, random);

        assertThat(result).hasSize(52);
        assertThat(new HashSet<>(result)).isEqualTo(new HashSet<>(deck));
    }

    @Test
    @DisplayName("A human interleave should require a positive max drop size")
    void shouldRejectInvalidMaxDropSize() {
        assertThatThrownBy(() -> new HumanInterleaver(InterleaveStart.BOTTOM, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("maxDropSize must be at least 1");
    }

    @Test
    @DisplayName("A human interleave may drop groups of cards instead of alternating perfectly")
    void shouldAllowUnevenCardDrops() {
        var deck = DeckFactory.standardDeck();

        var top = deck.subList(0, 26);
        var bottom = deck.subList(26, 52);

        var interleaver = new HumanInterleaver(InterleaveStart.BOTTOM, 3);
        var random = TestRandoms.fixedRandom();

        var result = interleaver.interleave(top, bottom, random);

        var perfect = new PerfectInterleaver(InterleaveStart.BOTTOM)
                .interleave(top, bottom, random);

        assertThat(result).isNotEqualTo(perfect);
    }

    @Nested
    @DisplayName("Interleave start behavior")
    class InterleaveStartBehaviour {

        @Test
        @DisplayName("A human interleave starting from the top packet should begin with a card from the top packet")
        void shouldStartWithTopPacket() {
            var deck = DeckFactory.standardDeck();

            var top = deck.subList(0, 26);
            var bottom = deck.subList(26, 52);

            var result = new HumanInterleaver(InterleaveStart.TOP, 3)
                    .interleave(top, bottom, TestRandoms.fixedRandom());

            assertThat(result.getFirst()).isEqualTo(top.getFirst());
        }

        @Test
        @DisplayName("A human interleave starting from the bottom packet should begin with a card from the bottom packet")
        void shouldStartWithBottomPacket() {
            var deck = DeckFactory.standardDeck();

            var top = deck.subList(0, 26);
            var bottom = deck.subList(26, 52);

            var result = new HumanInterleaver(InterleaveStart.BOTTOM, 3)
                    .interleave(top, bottom, TestRandoms.fixedRandom());

            assertThat(result.getFirst()).isEqualTo(bottom.getFirst());
        }
    }
}
