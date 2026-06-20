package se.pbt.shufflelab.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.TestRandoms;
import se.pbt.shufflelab.card.DeckFactory;
import se.pbt.shufflelab.operation.riffle.HumanRiffleInterleaver;
import se.pbt.shufflelab.operation.riffle.PerfectRiffleInterleaver;
import se.pbt.shufflelab.split.BalancedDeckSplitter;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Riffle shuffle operation")
class RiffleShuffleOperationTest {

    @Nested
    @DisplayName("Perfect riffle behavior")
    class PerfectRiffleBehavior {

        @Test
        @DisplayName("A perfect riffle shuffle should keep every card in the deck")
        void shouldPreserveAllCards() {
            var deck = DeckFactory.standardDeck();
            var originalCards = new HashSet<>(deck);

            var operation = new RiffleShuffleOperation(
                    new BalancedDeckSplitter(0),
                    new PerfectRiffleInterleaver()
            );

            var random = TestRandoms.fixedRandom();

            operation.apply(deck, random);

            assertThat(deck).hasSize(52);
            assertThat(new HashSet<>(deck)).isEqualTo(originalCards);
        }

        @Test
        @DisplayName("A perfect riffle shuffle should change the card order")
        void shouldChangeDeckOrder() {
            var deck = DeckFactory.standardDeck();
            var original = List.copyOf(deck);

            var operation = new RiffleShuffleOperation(
                    new BalancedDeckSplitter(0),
                    new PerfectRiffleInterleaver()
            );

            var random = TestRandoms.fixedRandom();

            operation.apply(deck, random);

            assertThat(deck).isNotEqualTo(original);
        }

        @Test
        @DisplayName("A perfect riffle shuffle should alternate cards from both packets")
        void shouldAlternateCardsFromBothPackets() {
            var deck = DeckFactory.standardDeck();
            var original = List.copyOf(deck);

            var operation = new RiffleShuffleOperation(
                    new BalancedDeckSplitter(0),
                    new PerfectRiffleInterleaver()
            );

            var random = TestRandoms.fixedRandom();

            operation.apply(deck, random);

            assertThat(deck.get(0)).isEqualTo(original.get(0));
            assertThat(deck.get(1)).isEqualTo(original.get(26));
            assertThat(deck.get(2)).isEqualTo(original.get(1));
            assertThat(deck.get(3)).isEqualTo(original.get(27));
        }
    }

    @Nested
    @DisplayName("Human riffle behavior")
    class HumanRiffleBehavior {

        @Test
        @DisplayName("A human riffle shuffle should keep every card in the deck")
        void shouldPreserveAllCards() {
            var deck = DeckFactory.standardDeck();
            var originalCards = new HashSet<>(deck);

            var operation = new RiffleShuffleOperation(
                    new BalancedDeckSplitter(3),
                    new HumanRiffleInterleaver(3)
            );

            var random = TestRandoms.fixedRandom();

            operation.apply(deck, random);

            assertThat(deck).hasSize(52);
            assertThat(new HashSet<>(deck)).isEqualTo(originalCards);
        }

        @Test
        @DisplayName("A human riffle shuffle should change the card order")
        void shouldChangeDeckOrder() {
            var deck = DeckFactory.standardDeck();
            var original = List.copyOf(deck);

            var operation = new RiffleShuffleOperation(
                    new BalancedDeckSplitter(3),
                    new HumanRiffleInterleaver(3)
            );

            var random = TestRandoms.fixedRandom();

            operation.apply(deck, random);

            assertThat(deck).isNotEqualTo(original);
        }

        @Test
        @DisplayName("A human riffle shuffle should not produce the same full order as a perfect riffle")
        void shouldNotProduceSameOrderAsPerfectRiffle() {
            var humanDeck = DeckFactory.standardDeck();
            var perfectDeck = DeckFactory.standardDeck();

            var random =  TestRandoms.fixedRandom();

            var humanOperation = new RiffleShuffleOperation(
                    new BalancedDeckSplitter(0),
                    new HumanRiffleInterleaver(3)
            );

            var perfectOperation = new RiffleShuffleOperation(
                    new BalancedDeckSplitter(0),
                    new PerfectRiffleInterleaver()
            );

            humanOperation.apply(humanDeck, random);

            perfectOperation.apply(
                    perfectDeck,
                    TestRandoms.fixedRandom()
            );

            assertThat(humanDeck).isNotEqualTo(perfectDeck);
        }
    }
}