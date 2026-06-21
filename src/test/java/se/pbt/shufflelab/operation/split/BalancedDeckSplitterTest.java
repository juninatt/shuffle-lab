package se.pbt.shufflelab.operation.split;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.TestRandoms;
import se.pbt.shufflelab.card.DeckFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Balanced deck splitter")
class BalancedDeckSplitterTest {

    @Test
    @DisplayName("A balanced split should create two packets")
    void shouldSplitDeckIntoTwoPackets() {
        var deck = DeckFactory.standardDeck();
        var splitter = new BalancedDeckSplitter(4);
        var random = TestRandoms.fixedRandom();

        var packets = splitter.split(deck, random);

        assertThat(packets).hasSize(2);
    }

    @Test
    @DisplayName("A balanced split should keep every card from the original deck")
    void shouldPreserveAllCards() {
        var deck = DeckFactory.standardDeck();
        var splitter = new BalancedDeckSplitter(4);
        var random = TestRandoms.fixedRandom();

        var packets = splitter.split(deck, random);

        var combined = packets.stream()
                .flatMap(Collection::stream)
                .toList();

        assertThat(new HashSet<>(combined)).isEqualTo(new HashSet<>(deck));
    }

    @Test
    @DisplayName("A balanced split should create packets near the middle of the deck")
    void shouldCreateReasonablyBalancedPackets() {
        var deck = DeckFactory.standardDeck();
        var splitter = new BalancedDeckSplitter(4);
        var random = TestRandoms.fixedRandom();

        var packets = splitter.split(deck, random);

        assertThat(packets.get(0).size()).isBetween(22, 30);
        assertThat(packets.get(1).size()).isBetween(22, 30);
    }

    @Test
    @DisplayName("A balanced split should preserve the total number of cards")
    void shouldPreserveTotalCardCount() {
        var deck = DeckFactory.standardDeck();
        var splitter = new BalancedDeckSplitter(4);
        var random = TestRandoms.fixedRandom();

        var packets = splitter.split(deck, random);

        int totalSize = packets.stream()
                .mapToInt(List::size)
                .sum();

        assertThat(totalSize).isEqualTo(deck.size());
    }

    @Test
    @DisplayName("A balanced split should preserve card order across packets")
    void shouldKeepOriginalOrderAcrossPackets() {
        var deck = DeckFactory.standardDeck();
        var splitter = new BalancedDeckSplitter(4);
        var random = TestRandoms.fixedRandom();

        var packets = splitter.split(deck, random);

        var combined = packets.stream()
                .flatMap(List::stream)
                .toList();

        assertThat(combined).containsExactlyElementsOf(deck);
    }

    @Test
    @DisplayName("A balanced split should not modify the original deck")
    void shouldNotModifyOriginalDeck() {
        var deck = DeckFactory.standardDeck();
        var original = List.copyOf(deck);
        var splitter = new BalancedDeckSplitter(4);
        var random = TestRandoms.fixedRandom();

        splitter.split(deck, random);

        assertThat(deck).containsExactlyElementsOf(original);
    }

    @Test
    @DisplayName("A balanced split should reject negative tolerance")
    void shouldRejectNegativeTolerance() {
        assertThatThrownBy(() -> new BalancedDeckSplitter(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("tolerance must not be negative");
    }
}
