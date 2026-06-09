package se.pbt.shufflelab.operation.riffle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.card.DeckFactory;

import java.util.random.RandomGeneratorFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Perfect riffle interleaver")
class PerfectRiffleInterleaverTest {

    @Test
    @DisplayName("A perfect riffle should alternate cards from both packets")
    void shouldAlternateCardsFromBothPackets() {
        var deck = DeckFactory.standardDeck();

        var left = deck.subList(0, 26);
        var right = deck.subList(26, 52);

        var interleaver = new PerfectRiffleInterleaver();

        var result = interleaver.interleave(
                left,
                right,
                RandomGeneratorFactory.of("L64X128MixRandom").create(42)
        );

        assertThat(result.get(0)).isEqualTo(left.get(0));
        assertThat(result.get(1)).isEqualTo(right.get(0));
        assertThat(result.get(2)).isEqualTo(left.get(1));
        assertThat(result.get(3)).isEqualTo(right.get(1));
    }

    @Test
    @DisplayName("A perfect riffle should keep every card from both packets")
    void shouldPreserveAllCards() {
        var deck = DeckFactory.standardDeck();

        var left = deck.subList(0, 26);
        var right = deck.subList(26, 52);

        var interleaver = new PerfectRiffleInterleaver();

        var result = interleaver.interleave(
                left,
                right,
                RandomGeneratorFactory.of("L64X128MixRandom").create(42)
        );

        assertThat(result).containsExactlyInAnyOrderElementsOf(deck);
    }

    @Test
    @DisplayName("A perfect riffle should preserve the order within each packet")
    void shouldPreserveOrderWithinPackets() {
        var deck = DeckFactory.standardDeck();

        var left = deck.subList(0, 26);
        var right = deck.subList(26, 52);

        var interleaver = new PerfectRiffleInterleaver();

        var result = interleaver.interleave(
                left,
                right,
                RandomGeneratorFactory.of("L64X128MixRandom").create(42)
        );

        assertThat(result.indexOf(left.get(0)))
                .isLessThan(result.indexOf(left.get(1)));

        assertThat(result.indexOf(left.get(1)))
                .isLessThan(result.indexOf(left.get(2)));

        assertThat(result.indexOf(right.get(0)))
                .isLessThan(result.indexOf(right.get(1)));
    }

    @Test
    @DisplayName("A perfect riffle should produce the same result regardless of random generator")
    void shouldProduceDeterministicResult() {
        var deck = DeckFactory.standardDeck();

        var left = deck.subList(0, 26);
        var right = deck.subList(26, 52);

        var interleaver = new PerfectRiffleInterleaver();

        var first = interleaver.interleave(
                left,
                right,
                RandomGeneratorFactory.of("L64X128MixRandom").create(42)
        );

        var second = interleaver.interleave(
                left,
                right,
                RandomGeneratorFactory.of("L64X128MixRandom").create(999)
        );

        assertThat(first).isEqualTo(second);
    }

    @Test
    @DisplayName("A perfect riffle should reject packets with too much imbalance")
    void shouldRejectPacketsWithTooMuchImbalance() {
        var deck = DeckFactory.standardDeck();

        var left = deck.subList(0, 3);
        var right = deck.subList(3, 8);

        var interleaver = new PerfectRiffleInterleaver();

        assertThatThrownBy(() -> interleaver.interleave(
                left,
                right,
                RandomGeneratorFactory.of("L64X128MixRandom").create(42)
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Packet imbalance is too large for this riffle shuffle");
    }
}