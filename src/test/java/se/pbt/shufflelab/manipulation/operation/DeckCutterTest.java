package se.pbt.shufflelab.manipulation.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.TestRandoms;
import se.pbt.shufflelab.deck.DeckFactory;
import se.pbt.shufflelab.manipulation.operation.cut.DeckCutter;
import se.pbt.shufflelab.manipulation.operation.split.BalancedDeckSplitter;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Cut deck operation")
class DeckCutterTest {

    @Nested
    @DisplayName("Perfect cut behavior")
    class PerfectCutBehavior {

        @Test
        @DisplayName("A perfect cut should preserve all cards in the deck")
        void shouldPreserveAllCards() {
            var deck = DeckFactory.standardDeck();
            var originalCards = new HashSet<>(deck);

            var operation = new DeckCutter(
                    new BalancedDeckSplitter(0)
            );

            var random = TestRandoms.fixedRandom();

            operation.cut(deck, random);

            assertThat(deck).hasSize(52);
            assertThat(new HashSet<>(deck)).isEqualTo(originalCards);
        }

        @Test
        @DisplayName("A perfect cut should change the deck order")
        void shouldChangeDeckOrder() {
            var deck = DeckFactory.standardDeck();
            var original = List.copyOf(deck);

            var operation = new DeckCutter(
                    new BalancedDeckSplitter(0)
            );

            var random = TestRandoms.fixedRandom();

            operation.cut(deck, random);

            assertThat(deck).isNotEqualTo(original);
        }

        @Test
        @DisplayName("A perfect cut should move the top packet below the bottom packet")
        void shouldMoveTopPacketBelowBottomPacket() {
            var deck = DeckFactory.standardDeck();
            var original = List.copyOf(deck);

            var operation = new DeckCutter(
                    new BalancedDeckSplitter(0)
            );

            var random = TestRandoms.fixedRandom();

            operation.cut(deck, random);

            assertThat(deck.getFirst()).isEqualTo(original.get(26));
            assertThat(deck.get(25)).isEqualTo(original.get(51));

            assertThat(deck.get(26)).isEqualTo(original.getFirst());
            assertThat(deck.get(51)).isEqualTo(original.get(25));
        }

        @Test
        @DisplayName("A perfect cut should preserve the order within each packet")
        void shouldPreserveOrderWithinEachPacket() {
            var deck = DeckFactory.standardDeck();
            var original = List.copyOf(deck);

            var operation = new DeckCutter(
                    new BalancedDeckSplitter(0)
            );

            var random = TestRandoms.fixedRandom();

            operation.cut(deck, random);

            assertThat(deck.subList(0, 26))
                    .containsExactlyElementsOf(original.subList(26, 52));

            assertThat(deck.subList(26, 52))
                    .containsExactlyElementsOf(original.subList(0, 26));
        }
    }

    @Nested
    @DisplayName("Human cut behavior")
    class HumanCutBehavior {

        @Test
        @DisplayName("A human cut should preserve all cards in the deck")
        void shouldPreserveAllCards() {
            var deck = DeckFactory.standardDeck();
            var originalCards = new HashSet<>(deck);

            var operation = new DeckCutter(
                    new BalancedDeckSplitter(4)
            );

            var random = TestRandoms.fixedRandom();

            operation.cut(deck, random);

            assertThat(deck).hasSize(52);
            assertThat(new HashSet<>(deck)).isEqualTo(originalCards);
        }

        @Test
        @DisplayName("A human cut should change the deck order")
        void shouldChangeDeckOrder() {
            var deck = DeckFactory.standardDeck();
            var original = List.copyOf(deck);

            var operation = new DeckCutter(
                    new BalancedDeckSplitter(4)
            );

            var random = TestRandoms.fixedRandom();

            operation.cut(deck, random);

            assertThat(deck).isNotEqualTo(original);
        }

        @Test
        @DisplayName("A human cut should support naturally uneven packets")
        void shouldSupportUnevenPackets() {
            var deck = DeckFactory.standardDeck();

            var operation = new DeckCutter(
                    new BalancedDeckSplitter(4)
            );

            var random = TestRandoms.fixedRandom();

            operation.cut(deck, random);

            assertThat(deck).hasSize(52);
            assertThat(new HashSet<>(deck)).hasSize(52);
        }
    }
}
