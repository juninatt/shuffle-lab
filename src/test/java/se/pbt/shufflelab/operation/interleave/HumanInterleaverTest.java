package se.pbt.shufflelab.operation.interleave;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.TestRandoms;
import se.pbt.shufflelab.card.DeckFactory;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Human interleaver")
class HumanInterleaverTest {

    @Test
    @DisplayName("A human interleave should keep every card from both packets")
    void shouldPreserveAllCards() {
        var deck = DeckFactory.standardDeck();

        var left = deck.subList(0, 26);
        var right = deck.subList(26, 52);

        var interleaver = new HumanInterleaver(3);
        var random = TestRandoms.fixedRandom();

        var result = interleaver.interleave(left, right, random);

        assertThat(result).hasSize(52);
        assertThat(new HashSet<>(result)).isEqualTo(new HashSet<>(deck));
    }

    @Test
    @DisplayName("A human interleave should require a positive max drop size")
    void shouldRejectInvalidMaxDropSize() {
        assertThatThrownBy(() -> new HumanInterleaver(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("maxDropSize must be at least 1");
    }

    @Test
    @DisplayName("A human interleave may drop groups of cards instead of alternating perfectly")
    void shouldAllowUnevenCardDrops() {
        var deck = DeckFactory.standardDeck();

        var left = deck.subList(0, 26);
        var right = deck.subList(26, 52);

        var interleaver = new HumanInterleaver(3);
        var random = TestRandoms.fixedRandom();

        var result = interleaver.interleave(left, right, random);

        var perfect = new PerfectInterleaver()
                .interleave(left, right, random);

        assertThat(result).isNotEqualTo(perfect);
    }
}