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

@DisplayName("Mongean shuffle")
class MongeanShuffleTest {

    @Nested
    @DisplayName("General shuffle behavior")
    class GeneralShuffleBehavior {

        @Test
        @DisplayName("A Mongean shuffle should keep every card in the deck")
        void shouldPreserveAllCards() {
            var deck = DeckFactory.standardDeck();
            var originalCards = new HashSet<>(deck);

            var shuffle = new MongeanShuffle();

            shuffle.shuffle(
                    deck,
                    TestRandoms.fixedRandom()
            );

            assertThat(deck).hasSize(52);
            assertThat(new HashSet<>(deck)).isEqualTo(originalCards);
        }

        @Test
        @DisplayName("A Mongean shuffle should change the card order")
        void shouldChangeDeckOrder() {
            var deck = DeckFactory.standardDeck();
            var originalOrder = List.copyOf(deck);

            var shuffle = new MongeanShuffle();

            shuffle.shuffle(
                    deck,
                    TestRandoms.fixedRandom()
            );

            assertThat(deck).isNotEqualTo(originalOrder);
        }

        @Test
        @DisplayName("A Mongean shuffle should produce the same result regardless of random generator")
        void shouldProduceDeterministicResult() {
            var firstDeck = DeckFactory.standardDeck();
            var secondDeck = DeckFactory.standardDeck();

            var shuffle = new MongeanShuffle(MongeanStart.TOP);

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

        @Test
        @DisplayName("A Mongean shuffle should leave an empty deck unchanged")
        void shouldLeaveEmptyDeckUnchanged() {
            Deck deck = new Deck(List.of());

            var shuffle = new MongeanShuffle();

            shuffle.shuffle(
                    deck,
                    TestRandoms.fixedRandom()
            );

            assertThat(deck).isEmpty();
        }

        @Test
        @DisplayName("A Mongean shuffle should leave a single-card deck unchanged")
        void shouldLeaveSingleCardDeckUnchanged() {
            var deck = new Deck(
                    DeckFactory.standardDeck().subList(0, 1)
            );

            var originalOrder = List.copyOf(deck);

            var shuffle = new MongeanShuffle();

            shuffle.shuffle(
                    deck,
                    TestRandoms.fixedRandom()
            );

            assertThat(deck).isEqualTo(originalOrder);
        }
    }

    @Nested
    @DisplayName("Top start behavior")
    class TopStartBehavior {

        @Test
        @DisplayName("A top-start Mongean shuffle should place the second card on top")
        void shouldPlaceSecondCardOnTop() {
            var fullDeck = DeckFactory.standardDeck();
            Deck deck = new Deck(fullDeck.subList(0, 2));
            var originalOrder = List.copyOf(deck);

            var shuffle = new MongeanShuffle(MongeanStart.TOP);

            shuffle.shuffle(
                    deck,
                    TestRandoms.fixedRandom()
            );

            assertThat(deck).containsExactly(
                    originalOrder.get(1),
                    originalOrder.get(0)
            );
        }

        @Test
        @DisplayName("A top-start Mongean shuffle should produce the expected order")
        void shouldProduceExpectedTopStartOrder() {
            var fullDeck = DeckFactory.standardDeck();
            Deck deck = new Deck(fullDeck.subList(0, 6));
            var originalOrder = List.copyOf(deck);

            var shuffle = new MongeanShuffle(MongeanStart.TOP);

            shuffle.shuffle(
                    deck,
                    TestRandoms.fixedRandom()
            );

            assertThat(deck).containsExactly(
                    originalOrder.get(5),
                    originalOrder.get(3),
                    originalOrder.get(1),
                    originalOrder.get(0),
                    originalOrder.get(2),
                    originalOrder.get(4)
            );
        }

        @Test
        @DisplayName("The default Mongean shuffle should use top-start behavior")
        void defaultShuffleShouldUseTopStartBehavior() {
            var defaultDeck = DeckFactory.standardDeck();
            var explicitTopDeck = DeckFactory.standardDeck();

            var defaultShuffle = new MongeanShuffle();
            var topStartShuffle = new MongeanShuffle(MongeanStart.TOP);

            defaultShuffle.shuffle(
                    defaultDeck,
                    TestRandoms.fixedRandom()
            );

            topStartShuffle.shuffle(
                    explicitTopDeck,
                    TestRandoms.seededRandom(999)
            );

            assertThat(defaultDeck).isEqualTo(explicitTopDeck);
        }
    }

    @Nested
    @DisplayName("Bottom start behavior")
    class BottomStartBehavior {

        @Test
        @DisplayName("A bottom-start Mongean shuffle should place the second card on the bottom")
        void shouldPlaceSecondCardOnBottom() {
            var fullDeck = DeckFactory.standardDeck();
            Deck deck = new Deck(fullDeck.subList(0, 2));
            var originalOrder = List.copyOf(deck);

            var shuffle = new MongeanShuffle(MongeanStart.BOTTOM);

            shuffle.shuffle(
                    deck,
                    TestRandoms.fixedRandom()
            );

            assertThat(deck).containsExactly(
                    originalOrder.get(0),
                    originalOrder.get(1)
            );
        }

        @Test
        @DisplayName("A bottom-start Mongean shuffle should produce the expected order")
        void shouldProduceExpectedBottomStartOrder() {
            var fullDeck = DeckFactory.standardDeck();
            Deck deck = new Deck(fullDeck.subList(0, 6));
            var originalOrder = List.copyOf(deck);

            var shuffle = new MongeanShuffle(MongeanStart.BOTTOM);

            shuffle.shuffle(
                    deck,
                    TestRandoms.fixedRandom()
            );

            assertThat(deck).containsExactly(
                    originalOrder.get(4),
                    originalOrder.get(2),
                    originalOrder.get(0),
                    originalOrder.get(1),
                    originalOrder.get(3),
                    originalOrder.get(5)
            );
        }
    }

    @Nested
    @DisplayName("Start configuration")
    class StartConfiguration {

        @Test
        @DisplayName("Different Mongean starts should produce different deck orders")
        void differentStartsShouldProduceDifferentOrders() {
            var topStartDeck = DeckFactory.standardDeck();
            var bottomStartDeck = DeckFactory.standardDeck();

            var topStartShuffle = new MongeanShuffle(MongeanStart.TOP);
            var bottomStartShuffle = new MongeanShuffle(MongeanStart.BOTTOM);

            topStartShuffle.shuffle(
                    topStartDeck,
                    TestRandoms.fixedRandom()
            );

            bottomStartShuffle.shuffle(
                    bottomStartDeck,
                    TestRandoms.fixedRandom()
            );

            assertThat(topStartDeck).isNotEqualTo(bottomStartDeck);
        }

        @Test
        @DisplayName("A Mongean shuffle should reject a null start")
        void shouldRejectNullStart() {
            assertThatThrownBy(() -> new MongeanShuffle(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("start must not be null");
        }
    }

    @Nested
    @DisplayName("Validation")
    class Validation {

        @Test
        @DisplayName("A Mongean shuffle should reject a null deck")
        void shouldRejectNullDeck() {
            var shuffle = new MongeanShuffle();

            assertThatThrownBy(() -> shuffle.shuffle(
                    null,
                    TestRandoms.fixedRandom()
            ))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("deck must not be null");
        }

        @Test
        @DisplayName("A Mongean shuffle should reject a null random generator")
        void shouldRejectNullRandomGenerator() {
            var deck = DeckFactory.standardDeck();
            var shuffle = new MongeanShuffle();

            assertThatThrownBy(() -> shuffle.shuffle(
                    deck,
                    null
            ))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("random must not be null");
        }
    }
}
