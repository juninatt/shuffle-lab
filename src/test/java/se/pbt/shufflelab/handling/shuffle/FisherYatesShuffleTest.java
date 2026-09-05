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

@DisplayName("Fisher-Yates shuffle")
class FisherYatesShuffleTest {

    @Nested
    @DisplayName("Shuffle behavior")
    class ShuffleBehavior {

        @Test
        @DisplayName("A Fisher-Yates shuffle should keep every card in the deck")
        void shouldPreserveAllCards() {
            var deck = DeckFactory.standardDeck();
            var originalCards = new HashSet<>(deck);

            new FisherYatesShuffle().shuffle(
                    deck,
                    TestRandoms.fixedRandom()
            );

            assertThat(deck).hasSize(52);
            assertThat(new HashSet<>(deck)).isEqualTo(originalCards);
        }

        @Test
        @DisplayName("A Fisher-Yates shuffle should change the card order")
        void shouldChangeDeckOrder() {
            var deck = DeckFactory.standardDeck();
            var originalOrder = List.copyOf(deck);

            new FisherYatesShuffle().shuffle(
                    deck,
                    TestRandoms.fixedRandom()
            );

            assertThat(deck).isNotEqualTo(originalOrder);
        }

        @Test
        @DisplayName("A Fisher-Yates shuffle should produce the same result for the same random seed")
        void shouldBeDeterministicWithSameSeed() {
            var firstDeck = DeckFactory.standardDeck();
            var secondDeck = DeckFactory.standardDeck();

            new FisherYatesShuffle().shuffle(firstDeck, TestRandoms.seededRandom(42));
            new FisherYatesShuffle().shuffle(secondDeck, TestRandoms.seededRandom(42));

            assertThat(firstDeck).isEqualTo(secondDeck);
        }

        @Test
        @DisplayName("A Fisher-Yates shuffle should produce different results for different random seeds")
        void shouldDifferForDifferentSeeds() {
            var firstDeck = DeckFactory.standardDeck();
            var secondDeck = DeckFactory.standardDeck();

            new FisherYatesShuffle().shuffle(firstDeck, TestRandoms.seededRandom(1));
            new FisherYatesShuffle().shuffle(secondDeck, TestRandoms.seededRandom(2));

            assertThat(firstDeck).isNotEqualTo(secondDeck);
        }

        @Test
        @DisplayName("A Fisher-Yates shuffle should leave a single-card deck unchanged")
        void shouldLeaveSingleCardDeckUnchanged() {
            var fullDeck = DeckFactory.standardDeck();
            var deck = new Deck(fullDeck.subList(0, 1));
            var originalOrder = List.copyOf(deck);

            new FisherYatesShuffle().shuffle(
                    deck,
                    TestRandoms.fixedRandom()
            );

            assertThat(deck).isEqualTo(originalOrder);
        }

        @Test
        @DisplayName("A Fisher-Yates shuffle should leave an empty deck unchanged")
        void shouldLeaveEmptyDeckUnchanged() {
            Deck deck = new Deck(List.of());

            new FisherYatesShuffle().shuffle(
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
        @DisplayName("A Fisher-Yates shuffle should reject a null deck")
        void shouldRejectNullDeck() {
            var shuffle = new FisherYatesShuffle();

            assertThatThrownBy(() -> shuffle.shuffle(
                    null,
                    TestRandoms.fixedRandom()
            ))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("deck must not be null");
        }

        @Test
        @DisplayName("A Fisher-Yates shuffle should reject a null random generator")
        void shouldRejectNullRandomGenerator() {
            var deck = DeckFactory.standardDeck();
            var shuffle = new FisherYatesShuffle();

            assertThatThrownBy(() -> shuffle.shuffle(deck, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("random must not be null");
        }
    }
}
