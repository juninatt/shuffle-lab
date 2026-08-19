package se.pbt.shufflelab.manipulation.shuffle;

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

@DisplayName("Pile shuffle")
class PileShuffleTest {

    @Nested
    @DisplayName("Shuffle behavior")
    class ShuffleBehavior {

        @Test
        @DisplayName("A pile shuffle should keep every card in the deck")
        void shouldPreserveAllCards() {
            var deck = DeckFactory.standardDeck();
            var originalCards = new HashSet<>(deck);

            var shuffle = new PileShuffle(4);

            shuffle.shuffle(
                    deck,
                    TestRandoms.fixedRandom()
            );

            assertThat(deck).hasSize(52);
            assertThat(new HashSet<>(deck)).isEqualTo(originalCards);
        }

        @Test
        @DisplayName("A pile shuffle should change the card order")
        void shouldChangeDeckOrder() {
            var deck = DeckFactory.standardDeck();
            var originalOrder = List.copyOf(deck);

            var shuffle = new PileShuffle(4);

            shuffle.shuffle(
                    deck,
                    TestRandoms.fixedRandom()
            );

            assertThat(deck).isNotEqualTo(originalOrder);
        }

        @Test
        @DisplayName("A pile shuffle should deal cards cyclically between piles")
        void shouldDealCardsCyclicallyBetweenPiles() {
            var fullDeck = DeckFactory.standardDeck();
            var deck = new Deck(fullDeck.subList(0, 12));
            var originalOrder = List.copyOf(deck);

            var shuffle = new PileShuffle(4);

            shuffle.shuffle(
                    deck,
                    TestRandoms.fixedRandom()
            );

            assertThat(deck).containsExactly(
                    originalOrder.get(3),
                    originalOrder.get(7),
                    originalOrder.get(11),

                    originalOrder.get(2),
                    originalOrder.get(6),
                    originalOrder.get(10),

                    originalOrder.get(1),
                    originalOrder.get(5),
                    originalOrder.get(9),

                    originalOrder.get(0),
                    originalOrder.get(4),
                    originalOrder.get(8)
            );
        }

        @Test
        @DisplayName("A pile shuffle should preserve card order within each pile")
        void shouldPreserveCardOrderWithinEachPile() {
            var fullDeck = DeckFactory.standardDeck();
            var deck = new Deck(fullDeck.subList(0, 12));
            var originalOrder = List.copyOf(deck);

            var shuffle = new PileShuffle(4);

            shuffle.shuffle(
                    deck,
                    TestRandoms.fixedRandom()
            );

            assertThat(deck.indexOf(originalOrder.get(3)))
                    .isLessThan(deck.indexOf(originalOrder.get(7)));

            assertThat(deck.indexOf(originalOrder.get(7)))
                    .isLessThan(deck.indexOf(originalOrder.get(11)));

            assertThat(deck.indexOf(originalOrder.get(2)))
                    .isLessThan(deck.indexOf(originalOrder.get(6)));

            assertThat(deck.indexOf(originalOrder.get(6)))
                    .isLessThan(deck.indexOf(originalOrder.get(10)));
        }

        @Test
        @DisplayName("A pile shuffle should produce the same result regardless of random generator")
        void shouldProduceDeterministicResult() {
            var firstDeck = DeckFactory.standardDeck();
            var secondDeck = DeckFactory.standardDeck();

            var firstShuffle = new PileShuffle(4);
            var secondShuffle = new PileShuffle(4);

            firstShuffle.shuffle(
                    firstDeck,
                    TestRandoms.fixedRandom()
            );

            secondShuffle.shuffle(
                    secondDeck,
                    TestRandoms.seededRandom(999)
            );

            assertThat(firstDeck).isEqualTo(secondDeck);
        }

        @Test
        @DisplayName("A pile shuffle should handle a deck smaller than the number of piles")
        void shouldHandleDeckSmallerThanPileCount() {
            var fullDeck = DeckFactory.standardDeck();
            var deck = new Deck(fullDeck.subList(0, 3));
            var originalOrder = List.copyOf(deck);

            var shuffle = new PileShuffle(5);

            shuffle.shuffle(
                    deck,
                    TestRandoms.fixedRandom()
            );

            assertThat(deck).containsExactly(
                    originalOrder.get(2),
                    originalOrder.get(1),
                    originalOrder.get(0)
            );
        }

        @Test
        @DisplayName("A pile shuffle should leave an empty deck unchanged")
        void shouldLeaveEmptyDeckUnchanged() {
            Deck deck = new Deck(List.of());

            var shuffle = new PileShuffle(4);

            shuffle.shuffle(
                    deck,
                    TestRandoms.fixedRandom()
            );

            assertThat(deck).isEmpty();
        }
    }

    @Nested
    @DisplayName("Validation")
    class Validation {

        @Test
        @DisplayName("A pile shuffle should reject a pile count below two")
        void shouldRejectPileCountBelowTwo() {
            assertThatThrownBy(() -> new PileShuffle(1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("pileCount must be at least 2");
        }

        @Test
        @DisplayName("A pile shuffle should reject a negative pile count")
        void shouldRejectNegativePileCount() {
            assertThatThrownBy(() -> new PileShuffle(-1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("pileCount must be at least 2");
        }

        @Test
        @DisplayName("A pile shuffle should reject a null deck")
        void shouldRejectNullDeck() {
            var shuffle = new PileShuffle(4);

            assertThatThrownBy(() -> shuffle.shuffle(
                    null,
                    TestRandoms.fixedRandom()
            ))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("deck must not be null");
        }

        @Test
        @DisplayName("A pile shuffle should reject a null random generator")
        void shouldRejectNullRandomGenerator() {
            var deck = DeckFactory.standardDeck();
            var shuffle = new PileShuffle(4);

            assertThatThrownBy(() -> shuffle.shuffle(deck, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("random must not be null");
        }
    }
}