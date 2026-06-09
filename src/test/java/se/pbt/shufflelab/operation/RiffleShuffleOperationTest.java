package se.pbt.shufflelab.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.card.DeckFactory;
import se.pbt.shufflelab.operation.riffle.PerfectRiffleInterleaver;
import se.pbt.shufflelab.split.BalancedDeckSplitter;

import java.util.HashSet;
import java.util.List;
import java.util.random.RandomGeneratorFactory;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Riffle shuffle operation")
class RiffleShuffleOperationTest {

    @Test
    @DisplayName("A riffle shuffle should keep every card in the deck")
    void shouldPreserveAllCards() {
        var deck = DeckFactory.standardDeck();
        var originalCards = new HashSet<>(deck);

        var operation = new RiffleShuffleOperation(
                new BalancedDeckSplitter(4),
                new PerfectRiffleInterleaver()
        );

        var random = RandomGeneratorFactory
                .of("L64X128MixRandom")
                .create(42);

        operation.apply(deck, random);

        assertThat(deck).hasSize(52);
        assertThat(new HashSet<>(deck)).isEqualTo(originalCards);
    }

    @Test
    @DisplayName("A riffle shuffle should change the card order")
    void shouldChangeDeckOrder() {
        var deck = DeckFactory.standardDeck();
        var original = List.copyOf(deck);

        var operation = new RiffleShuffleOperation(
                new BalancedDeckSplitter(4),
                new PerfectRiffleInterleaver()
        );

        var random = RandomGeneratorFactory
                .of("L64X128MixRandom")
                .create(42);

        operation.apply(deck, random);

        assertThat(deck).isNotEqualTo(original);
    }

    @Test
    @DisplayName("A perfect riffle should alternate cards from both packets")
    void shouldInterleaveCardsFromBothPackets() {
        var deck = DeckFactory.standardDeck();
        var original = List.copyOf(deck);

        var operation = new RiffleShuffleOperation(
                new BalancedDeckSplitter(0),
                new PerfectRiffleInterleaver()
        );

        var random = RandomGeneratorFactory
                .of("L64X128MixRandom")
                .create(42);

        operation.apply(deck, random);

        assertThat(deck.get(0)).isEqualTo(original.get(0));
        assertThat(deck.get(1)).isEqualTo(original.get(26));
        assertThat(deck.get(2)).isEqualTo(original.get(1));
        assertThat(deck.get(3)).isEqualTo(original.get(27));
    }

    @Test
    @DisplayName("A perfect riffle shuffle should split the deck evenly and alternate the packets")
    void shouldPerformPerfectRiffleShuffle() {
        var deck = DeckFactory.standardDeck();
        var original = List.copyOf(deck);

        var operation = new RiffleShuffleOperation(
                new BalancedDeckSplitter(0),
                new PerfectRiffleInterleaver()
        );

        var random = RandomGeneratorFactory
                .of("L64X128MixRandom")
                .create(42);

        operation.apply(deck, random);

        assertThat(deck.get(0)).isEqualTo(original.get(0));
        assertThat(deck.get(1)).isEqualTo(original.get(26));
        assertThat(deck.get(2)).isEqualTo(original.get(1));
        assertThat(deck.get(3)).isEqualTo(original.get(27));
        assertThat(deck).hasSize(52);
    }
}