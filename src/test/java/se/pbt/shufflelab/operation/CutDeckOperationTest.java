package se.pbt.shufflelab.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.card.DeckFactory;
import se.pbt.shufflelab.split.BalancedDeckSplitter;

import java.util.HashSet;
import java.util.List;
import java.util.random.RandomGeneratorFactory;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Cut deck operation")
class CutDeckOperationTest {

    @Test
    @DisplayName("A cut should preserve all cards in the deck")
    void shouldPreserveAllCards() {
        var deck = DeckFactory.standardDeck();
        var originalCards = new HashSet<>(deck);

        var operation = new CutDeckOperation(
                new BalancedDeckSplitter(4)
        );

        var random = RandomGeneratorFactory
                .of("L64X128MixRandom")
                .create(42);

        operation.apply(deck, random);

        assertThat(deck).hasSize(52);
        assertThat(new HashSet<>(deck)).isEqualTo(originalCards);
    }

    @Test
    @DisplayName("A cut should change the deck order")
    void shouldChangeDeckOrder() {
        var deck = DeckFactory.standardDeck();
        var original = List.copyOf(deck);

        var operation = new CutDeckOperation(
                new BalancedDeckSplitter(4)
        );

        var random = RandomGeneratorFactory
                .of("L64X128MixRandom")
                .create(42);

        operation.apply(deck, random);

        assertThat(deck).isNotEqualTo(original);
    }

    @Test
    @DisplayName("A cut should move the top packet below the bottom packet")
    void shouldMoveTopPacketBelowBottomPacket() {
        var deck = DeckFactory.standardDeck();
        var original = List.copyOf(deck);

        var operation = new CutDeckOperation(
                new BalancedDeckSplitter(0)
        );

        var random = RandomGeneratorFactory
                .of("L64X128MixRandom")
                .create(42);

        operation.apply(deck, random);

        assertThat(deck.getFirst()).isEqualTo(original.get(26));
        assertThat(deck.get(25)).isEqualTo(original.get(51));
        assertThat(deck.get(26)).isEqualTo(original.getFirst());
        assertThat(deck.get(51)).isEqualTo(original.get(25));
    }

    @Test
    @DisplayName("A cut should preserve the order within each packet")
    void shouldPreserveOrderWithinEachPacket() {
        var deck = DeckFactory.standardDeck();
        var original = List.copyOf(deck);

        var operation = new CutDeckOperation(
                new BalancedDeckSplitter(0)
        );

        var random = RandomGeneratorFactory
                .of("L64X128MixRandom")
                .create(42);

        operation.apply(deck, random);

        assertThat(deck.subList(0, 26))
                .containsExactlyElementsOf(original.subList(26, 52));

        assertThat(deck.subList(26, 52))
                .containsExactlyElementsOf(original.subList(0, 26));
    }

    @Test
    @DisplayName("A cut should support naturally uneven packets")
    void shouldSupportUnevenPackets() {
        var deck = DeckFactory.standardDeck();

        var operation = new CutDeckOperation(
                new BalancedDeckSplitter(4)
        );

        var random = RandomGeneratorFactory
                .of("L64X128MixRandom")
                .create(42);

        operation.apply(deck, random);

        assertThat(deck).hasSize(52);
        assertThat(new HashSet<>(deck)).hasSize(52);
    }
}