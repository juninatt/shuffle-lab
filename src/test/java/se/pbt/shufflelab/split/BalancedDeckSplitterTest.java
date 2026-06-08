package se.pbt.shufflelab.split;

import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.card.DeckFactory;
import se.pbt.shufflelab.shuffle.split.BalancedDeckSplitter;

import java.util.HashSet;
import java.util.List;
import java.util.random.RandomGeneratorFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BalancedDeckSplitterTest {

    @Test
    void shouldSplitDeckIntoTwoPackets() {
        var deck = DeckFactory.standardDeck();
        var splitter = new BalancedDeckSplitter(4);
        var random = RandomGeneratorFactory.of("L64X128MixRandom").create(42);

        var packets = splitter.split(deck, random);

        assertThat(packets).hasSize(2);
    }

    @Test
    void shouldPreserveAllCards() {
        var deck = DeckFactory.standardDeck();
        var splitter = new BalancedDeckSplitter(4);
        var random = RandomGeneratorFactory.of("L64X128MixRandom").create(42);

        var packets = splitter.split(deck, random);

        var combined = packets.stream()
                .flatMap(packet -> packet.stream())
                .toList();

        assertThat(new HashSet<>(combined)).isEqualTo(new HashSet<>(deck));
    }

    @Test
    void shouldCreateReasonablyBalancedPackets() {
        var deck = DeckFactory.standardDeck();
        var splitter = new BalancedDeckSplitter(4);
        var random = RandomGeneratorFactory.of("L64X128MixRandom").create(42);

        var packets = splitter.split(deck, random);

        assertThat(packets.get(0).size()).isBetween(22, 30);
        assertThat(packets.get(1).size()).isBetween(22, 30);
    }

    @Test
    void shouldPreserveTotalCardCount() {
        var deck = DeckFactory.standardDeck();
        var splitter = new BalancedDeckSplitter(4);
        var random = RandomGeneratorFactory.of("L64X128MixRandom").create(42);

        var packets = splitter.split(deck, random);

        int totalSize = packets.stream()
                .mapToInt(List::size)
                .sum();

        assertThat(totalSize).isEqualTo(deck.size());
    }

    @Test
    void shouldKeepOriginalOrderAcrossPackets() {
        var deck = DeckFactory.standardDeck();
        var splitter = new BalancedDeckSplitter(4);
        var random = RandomGeneratorFactory.of("L64X128MixRandom").create(42);

        var packets = splitter.split(deck, random);

        var combined = packets.stream()
                .flatMap(List::stream)
                .toList();

        assertThat(combined).containsExactlyElementsOf(deck);
    }

    @Test
    void shouldNotModifyOriginalDeck() {
        var deck = DeckFactory.standardDeck();
        var original = List.copyOf(deck);
        var splitter = new BalancedDeckSplitter(4);
        var random = RandomGeneratorFactory.of("L64X128MixRandom").create(42);

        splitter.split(deck, random);

        assertThat(deck).containsExactlyElementsOf(original);
    }

    @Test
    void shouldRejectNegativeTolerance() {
        assertThatThrownBy(() -> new BalancedDeckSplitter(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("tolerance must not be negative");
    }
}
