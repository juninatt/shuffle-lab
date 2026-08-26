package se.pbt.shufflelab.handling.shuffle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.TestRandoms;
import se.pbt.shufflelab.factory.DeckFactory;
import se.pbt.shufflelab.handling.operation.interleave.HumanInterleaver;
import se.pbt.shufflelab.handling.operation.interleave.InterleaveStart;
import se.pbt.shufflelab.handling.operation.interleave.PerfectInterleaver;
import se.pbt.shufflelab.handling.operation.split.BalancedDeckSplitter;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Riffle shuffle operation")
class RiffleShuffleTest {

    @Nested
    @DisplayName("Perfect riffle behavior")
    class PerfectRiffleBehavior {

        @Test
        @DisplayName("A perfect riffle shuffle should keep every card in the deck")
        void shouldPreserveAllCards() {
            var deck = DeckFactory.standardDeck();
            var originalCards = new HashSet<>(deck);

            var operation = new RiffleShuffle(
                    new BalancedDeckSplitter(0),
                    new PerfectInterleaver(InterleaveStart.BOTTOM)
            );

            var random = TestRandoms.fixedRandom();

            operation.shuffle(deck, random);

            assertThat(deck).hasSize(52);
            assertThat(new HashSet<>(deck)).isEqualTo(originalCards);
        }

        @Test
        @DisplayName("A perfect riffle shuffle should change the card order")
        void shouldChangeDeckOrder() {
            var deck = DeckFactory.standardDeck();
            var original = List.copyOf(deck);

            var operation = new RiffleShuffle(
                    new BalancedDeckSplitter(0),
                    new PerfectInterleaver(InterleaveStart.BOTTOM)
            );

            var random = TestRandoms.fixedRandom();

            operation.shuffle(deck, random);

            assertThat(deck).isNotEqualTo(original);
        }

        @Test
        @DisplayName("A perfect riffle shuffle should alternate cards from both packets")
        void shouldAlternateCardsFromBothPackets() {
            var deck = DeckFactory.standardDeck();
            var original = List.copyOf(deck);

            var operation = new RiffleShuffle(
                    new BalancedDeckSplitter(0),
                    new PerfectInterleaver()
            );

            var random = TestRandoms.fixedRandom();

            operation.shuffle(deck, random);

            assertThat(deck.get(0)).withFailMessage("Top card should be unchanged").isEqualTo(original.get(0));
            assertThat(deck.get(1)).withFailMessage("Second card should be first card of top packet").isEqualTo(original.get(26));
            assertThat(deck.get(2)).withFailMessage("This card should be ").isEqualTo(original.get(1));
            assertThat(deck.get(3)).isEqualTo(original.get(27));
        }
    }

    @Nested
    @DisplayName("Human riffle behavior")
    class HumanInterleaverBehavior {

        @Test
        @DisplayName("A human riffle shuffle should keep every card in the deck")
        void shouldPreserveAllCards() {
            var deck = DeckFactory.standardDeck();
            var originalCards = new HashSet<>(deck);

            var operation = new RiffleShuffle(
                    new BalancedDeckSplitter(3),
                    new HumanInterleaver(InterleaveStart.BOTTOM, 3, 0.15)
            );

            var random = TestRandoms.fixedRandom();

            operation.shuffle(deck, random);

            assertThat(deck).hasSize(52);
            assertThat(new HashSet<>(deck)).isEqualTo(originalCards);
        }

        @Test
        @DisplayName("A human riffle shuffle should change the card order")
        void shouldChangeDeckOrder() {
            var deck = DeckFactory.standardDeck();
            var original = List.copyOf(deck);

            var operation = new RiffleShuffle(
                    new BalancedDeckSplitter(3),
                    new HumanInterleaver(InterleaveStart.BOTTOM, 3, 0.15)
            );

            var random = TestRandoms.fixedRandom();

            operation.shuffle(deck, random);

            assertThat(deck).isNotEqualTo(original);
        }

        @Test
        @DisplayName("A human riffle shuffle should not produce the same full order as a perfect riffle")
        void shouldNotProduceSameOrderAsPerfectRiffle() {
            var humanDeck = DeckFactory.standardDeck();
            var perfectDeck = DeckFactory.standardDeck();

            var random =  TestRandoms.fixedRandom();

            var humanOperation = new RiffleShuffle(
                    new BalancedDeckSplitter(0),
                    new HumanInterleaver(InterleaveStart.BOTTOM, 3, 0.15)
            );

            var perfectOperation = new RiffleShuffle(
                    new BalancedDeckSplitter(0),
                    new PerfectInterleaver(InterleaveStart.BOTTOM)
            );

            humanOperation.shuffle(humanDeck, random);

            perfectOperation.shuffle(
                    perfectDeck,
                    TestRandoms.fixedRandom()
            );

            assertThat(humanDeck).isNotEqualTo(perfectDeck);
        }
    }
}