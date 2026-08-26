package se.pbt.shufflelab.handling.shuffle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.TestRandoms;
import se.pbt.shufflelab.deck.Deck;
import se.pbt.shufflelab.factory.DeckFactory;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Faro shuffle")
class FaroShuffleTest {

    @Nested
    @DisplayName("General shuffle behavior")
    class GeneralShuffleBehavior {

        @Test
        @DisplayName("A Faro shuffle should keep every card in the deck")
        void shouldPreserveAllCards() {
            var deck = DeckFactory.standardDeck();
            var originalCards = new HashSet<>(deck);

            var shuffle = new FaroShuffle(FaroType.OUT);

            shuffle.shuffle(deck, TestRandoms.fixedRandom());

            assertThat(deck).hasSize(52);
            assertThat(new HashSet<>(deck)).isEqualTo(originalCards);
        }

        @Test
        @DisplayName("A Faro shuffle should change the card order")
        void shouldChangeDeckOrder() {
            var deck = DeckFactory.standardDeck();
            var originalOrder = List.copyOf(deck);

            var shuffle = new FaroShuffle(FaroType.OUT);

            shuffle.shuffle(deck, TestRandoms.fixedRandom());

            assertThat(deck).isNotEqualTo(originalOrder);
        }

        @Test
        @DisplayName("A Faro shuffle should produce the same result regardless of random generator")
        void shouldProduceDeterministicResult() {
            var firstDeck = DeckFactory.standardDeck();
            var secondDeck = DeckFactory.standardDeck();

            var shuffle = new FaroShuffle(FaroType.OUT);

            shuffle.shuffle(
                    firstDeck,
                    TestRandoms.fixedRandom()
            );

            shuffle.shuffle(
                    secondDeck,
                    TestRandoms.seededRandom(999)
            );

            assertThat(firstDeck).isEqualTo(secondDeck);
        }
    }

    @Nested
    @DisplayName("Out Faro behavior")
    class OutFaroBehavior {

        @Test
        @DisplayName("An out Faro shuffle should keep the top card in place")
        void shouldKeepTopCard() {
            var deck = DeckFactory.standardDeck();
            var original = List.copyOf(deck);

            new FaroShuffle(FaroType.OUT)
                    .shuffle(deck, TestRandoms.fixedRandom());

            assertThat(deck.getFirst()).isEqualTo(original.getFirst());
        }

        @Test
        @DisplayName("An out Faro shuffle should keep the bottom card in place")
        void shouldKeepBottomCard() {
            var deck = DeckFactory.standardDeck();
            var original = List.copyOf(deck);

            new FaroShuffle(FaroType.OUT)
                    .shuffle(deck, TestRandoms.fixedRandom());

            assertThat(deck.getLast()).isEqualTo(original.getLast());
        }

        @Test
        @DisplayName("An out Faro shuffle should perfectly interleave both packets")
        void shouldProduceExpectedOrder() {
            var fullDeck = DeckFactory.standardDeck();
            var deck = new Deck(fullDeck.subList(0, 8));
            var original = List.copyOf(deck);

            new FaroShuffle(FaroType.OUT)
                    .shuffle(deck, TestRandoms.fixedRandom());

            assertThat(deck).containsExactly(
                    original.get(0),
                    original.get(4),
                    original.get(1),
                    original.get(5),
                    original.get(2),
                    original.get(6),
                    original.get(3),
                    original.get(7)
            );
        }
    }

    @Nested
    @DisplayName("In Faro behavior")
    class InFaroBehavior {

        @Test
        @DisplayName("An in Faro shuffle should move the top card inward")
        void shouldMoveTopCardInward() {
            var deck = DeckFactory.standardDeck();
            var original = List.copyOf(deck);

            new FaroShuffle(FaroType.IN)
                    .shuffle(deck, TestRandoms.fixedRandom());

            assertThat(deck.getFirst()).isNotEqualTo(original.getFirst());
        }

        @Test
        @DisplayName("An in Faro shuffle should move the bottom card inward")
        void shouldMoveBottomCardInward() {
            var deck = DeckFactory.standardDeck();
            var original = List.copyOf(deck);

            new FaroShuffle(FaroType.IN)
                    .shuffle(deck, TestRandoms.fixedRandom());

            assertThat(deck.getLast()).isNotEqualTo(original.getLast());
        }

        @Test
        @DisplayName("An in Faro shuffle should perfectly interleave both packets")
        void shouldProduceExpectedOrder() {
            var fullDeck = DeckFactory.standardDeck();
            var deck = new Deck(fullDeck.subList(0, 8));
            var original = List.copyOf(deck);

            new FaroShuffle(FaroType.IN)
                    .shuffle(deck, TestRandoms.fixedRandom());

            assertThat(deck).containsExactly(
                    original.get(4),
                    original.get(0),
                    original.get(5),
                    original.get(1),
                    original.get(6),
                    original.get(2),
                    original.get(7),
                    original.get(3)
            );
        }
    }

    @Nested
    @DisplayName("Type configuration")
    class TypeConfiguration {

        @Test
        @DisplayName("Different Faro types should produce different deck orders")
        void differentTypesShouldProduceDifferentOrders() {
            var outDeck = DeckFactory.standardDeck();
            var inDeck = DeckFactory.standardDeck();

            new FaroShuffle(FaroType.OUT)
                    .shuffle(outDeck, TestRandoms.fixedRandom());

            new FaroShuffle(FaroType.IN)
                    .shuffle(inDeck, TestRandoms.fixedRandom());

            assertThat(outDeck).isNotEqualTo(inDeck);
        }

        @Test
        @DisplayName("A Faro shuffle should reject a null type")
        void shouldRejectNullType() {
            assertThatThrownBy(() -> new FaroShuffle(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("type must not be null");
        }
    }

    @Nested
    @DisplayName("Validation")
    class Validation {

        @Test
        @DisplayName("A Faro shuffle should reject a null deck")
        void shouldRejectNullDeck() {
            var shuffle = new FaroShuffle(FaroType.OUT);

            assertThatThrownBy(() ->
                    shuffle.shuffle(null, TestRandoms.fixedRandom())
            )
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("deck must not be null");
        }

        @Test
        @DisplayName("A Faro shuffle should reject a null random generator")
        void shouldRejectNullRandomGenerator() {
            var shuffle = new FaroShuffle(FaroType.OUT);

            assertThatThrownBy(() ->
                    shuffle.shuffle(DeckFactory.standardDeck(), null)
            )
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("random must not be null");
        }
    }
}