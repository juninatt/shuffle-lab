package se.pbt.shufflelab.operation.riffle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.card.DeckFactory;

import java.util.HashSet;
import java.util.random.RandomGeneratorFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Human riffle interleaver")
class HumanRiffleInterleaverTest {

    @Test
    @DisplayName("A human riffle should keep every card from both packets")
    void shouldPreserveAllCards() {
        var deck = DeckFactory.standardDeck();

        var left = deck.subList(0, 26);
        var right = deck.subList(26, 52);

        var interleaver = new HumanRiffleInterleaver(3);
        var random = RandomGeneratorFactory.of("L64X128MixRandom").create(42);

        var result = interleaver.interleave(left, right, random);

        assertThat(result).hasSize(52);
        assertThat(new HashSet<>(result)).isEqualTo(new HashSet<>(deck));
    }

    @Test
    @DisplayName("A human riffle should require a positive max drop size")
    void shouldRejectInvalidMaxDropSize() {
        assertThatThrownBy(() -> new HumanRiffleInterleaver(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("maxDropSize must be at least 1");
    }

    @Test
    @DisplayName("A human riffle may drop groups of cards instead of alternating perfectly")
    void shouldAllowUnevenCardDrops() {
        var deck = DeckFactory.standardDeck();

        var left = deck.subList(0, 26);
        var right = deck.subList(26, 52);

        var interleaver = new HumanRiffleInterleaver(3);
        var random = RandomGeneratorFactory.of("L64X128MixRandom").create(42);

        var result = interleaver.interleave(left, right, random);

        var perfect = new PerfectRiffleInterleaver()
                .interleave(left, right, random);

        assertThat(result).isNotEqualTo(perfect);
    }
}