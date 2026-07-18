package se.pbt.shufflelab.deck;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.deck.card.Card;
import se.pbt.shufflelab.deck.card.Rank;
import se.pbt.shufflelab.deck.card.Suit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Deck")
class DeckTest {

    private static final Card ACE_OF_SPADES =
            new Card(Suit.SPADES, Rank.ACE);

    private static final Card KING_OF_HEARTS =
            new Card(Suit.HEARTS, Rank.KING);

    private static final Card QUEEN_OF_DIAMONDS =
            new Card(Suit.DIAMONDS, Rank.QUEEN);

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("A deck should preserve the supplied card order")
        void shouldPreserveSuppliedCardOrder() {
            Deck deck = new Deck(List.of(
                    ACE_OF_SPADES,
                    KING_OF_HEARTS,
                    QUEEN_OF_DIAMONDS
            ));

            assertThat(deck).containsExactly(
                    ACE_OF_SPADES,
                    KING_OF_HEARTS,
                    QUEEN_OF_DIAMONDS
            );
        }

        @Test
        @DisplayName("A deck should copy the supplied collection")
        void shouldCopySuppliedCollection() {
            List<Card> source = new ArrayList<>(List.of(
                    ACE_OF_SPADES,
                    KING_OF_HEARTS
            ));

            Deck deck = new Deck(source);
            source.clear();

            assertThat(deck).containsExactly(
                    ACE_OF_SPADES,
                    KING_OF_HEARTS
            );
        }

        @Test
        @DisplayName("A deck may be empty")
        void shouldAllowEmptyDeck() {
            Deck deck = new Deck(List.of());

            assertThat(deck).isEmpty();
        }

        @Test
        @DisplayName("A deck may contain duplicate cards")
        void shouldAllowDuplicateCards() {
            Deck deck = new Deck(List.of(
                    ACE_OF_SPADES,
                    ACE_OF_SPADES
            ));

            assertThat(deck).containsExactly(
                    ACE_OF_SPADES,
                    ACE_OF_SPADES
            );
        }

        @Test
        @DisplayName("A deck should reject a null collection")
        void shouldRejectNullCollection() {
            assertThatThrownBy(() -> new Deck(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("cards must not be null");
        }

        @Test
        @DisplayName("A deck should reject a null card")
        void shouldRejectNullCard() {
            List<Card> cards = Arrays.asList(
                    ACE_OF_SPADES,
                    null
            );

            assertThatThrownBy(() -> new Deck(cards))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("deck must not contain null cards");
        }
    }

    @Nested
    @DisplayName("Factory method")
    class FactoryMethod {

        @Test
        @DisplayName("Of should create a deck in the supplied order")
        void shouldCreateDeckFromCards() {
            Deck deck = Deck.of(
                    ACE_OF_SPADES,
                    KING_OF_HEARTS
            );

            assertThat(deck).containsExactly(
                    ACE_OF_SPADES,
                    KING_OF_HEARTS
            );
        }

        @Test
        @DisplayName("Of should create an empty deck")
        void shouldCreateEmptyDeck() {
            Deck deck = Deck.of();

            assertThat(deck).isEmpty();
        }

        @Test
        @DisplayName("Of should reject a null card array")
        void shouldRejectNullCardArray() {
            assertThatThrownBy(() -> Deck.of((Card[]) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("cards must not be null");
        }

        @Test
        @DisplayName("Of should reject a null card")
        void shouldRejectNullCard() {
            assertThatThrownBy(() -> Deck.of(
                    ACE_OF_SPADES,
                    null
            ))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("deck must not contain null cards");
        }
    }

    @Nested
    @DisplayName("Card access")
    class CardAccess {

        @Test
        @DisplayName("Get should return the card at the supplied index")
        void shouldReturnCardAtIndex() {
            Deck deck = Deck.of(
                    ACE_OF_SPADES,
                    KING_OF_HEARTS
            );

            assertThat(deck.get(1)).isEqualTo(KING_OF_HEARTS);
        }

        @Test
        @DisplayName("Size should return the number of cards")
        void shouldReturnDeckSize() {
            Deck deck = Deck.of(
                    ACE_OF_SPADES,
                    KING_OF_HEARTS
            );

            assertThat(deck).hasSize(2);
        }

        @Test
        @DisplayName("Get should reject an invalid index")
        void shouldRejectInvalidIndex() {
            Deck deck = Deck.of(ACE_OF_SPADES);

            assertThatThrownBy(() -> deck.get(1))
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }
    }

    @Nested
    @DisplayName("Modification")
    class Modification {

        @Test
        @DisplayName("A card should be added to the end of the deck")
        void shouldAddCardToEnd() {
            Deck deck = Deck.of(ACE_OF_SPADES);

            deck.add(KING_OF_HEARTS);

            assertThat(deck).containsExactly(
                    ACE_OF_SPADES,
                    KING_OF_HEARTS
            );
        }

        @Test
        @DisplayName("A card should be inserted at the supplied index")
        void shouldInsertCardAtIndex() {
            Deck deck = Deck.of(
                    ACE_OF_SPADES,
                    QUEEN_OF_DIAMONDS
            );

            deck.add(1, KING_OF_HEARTS);

            assertThat(deck).containsExactly(
                    ACE_OF_SPADES,
                    KING_OF_HEARTS,
                    QUEEN_OF_DIAMONDS
            );
        }

        @Test
        @DisplayName("A card should be replaced at the supplied index")
        void shouldReplaceCardAtIndex() {
            Deck deck = Deck.of(
                    ACE_OF_SPADES,
                    KING_OF_HEARTS
            );

            Card replacedCard = deck.set(
                    1,
                    QUEEN_OF_DIAMONDS
            );

            assertThat(replacedCard).isEqualTo(KING_OF_HEARTS);
            assertThat(deck).containsExactly(
                    ACE_OF_SPADES,
                    QUEEN_OF_DIAMONDS
            );
        }

        @Test
        @DisplayName("A card should be removed from the supplied index")
        void shouldRemoveCardAtIndex() {
            Deck deck = Deck.of(
                    ACE_OF_SPADES,
                    KING_OF_HEARTS
            );

            Card removedCard = deck.removeFirst();

            assertThat(removedCard).isEqualTo(ACE_OF_SPADES);
            assertThat(deck).containsExactly(KING_OF_HEARTS);
        }

        @Test
        @DisplayName("Clear should remove all cards")
        void shouldClearDeck() {
            Deck deck = Deck.of(
                    ACE_OF_SPADES,
                    KING_OF_HEARTS
            );

            deck.clear();

            assertThat(deck).isEmpty();
        }

        @Test
        @DisplayName("Adding a null card should be rejected")
        void shouldRejectNullCardWhenAdded() {
            Deck deck = Deck.of(ACE_OF_SPADES);

            assertThatThrownBy(() -> deck.add(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("card must not be null");

            assertThat(deck).containsExactly(ACE_OF_SPADES);
        }

        @Test
        @DisplayName("Replacing a card with null should be rejected")
        void shouldRejectNullReplacementCard() {
            Deck deck = Deck.of(ACE_OF_SPADES);

            assertThatThrownBy(() -> deck.set(0, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("card must not be null");

            assertThat(deck).containsExactly(ACE_OF_SPADES);
        }
    }

    @Nested
    @DisplayName("Adding collections")
    class AddingCollections {

        @Test
        @DisplayName("Cards should be added to the end of the deck")
        void shouldAddCardsToEnd() {
            Deck deck = Deck.of(ACE_OF_SPADES);

            boolean changed = deck.addAll(List.of(
                    KING_OF_HEARTS,
                    QUEEN_OF_DIAMONDS
            ));

            assertThat(changed).isTrue();
            assertThat(deck).containsExactly(
                    ACE_OF_SPADES,
                    KING_OF_HEARTS,
                    QUEEN_OF_DIAMONDS
            );
        }

        @Test
        @DisplayName("Cards should be inserted at the supplied index")
        void shouldInsertCardsAtIndex() {
            Deck deck = Deck.of(
                    ACE_OF_SPADES,
                    QUEEN_OF_DIAMONDS
            );

            boolean changed = deck.addAll(
                    1,
                    List.of(KING_OF_HEARTS)
            );

            assertThat(changed).isTrue();
            assertThat(deck).containsExactly(
                    ACE_OF_SPADES,
                    KING_OF_HEARTS,
                    QUEEN_OF_DIAMONDS
            );
        }

        @Test
        @DisplayName("Adding an empty collection should not change the deck")
        void shouldNotChangeDeckForEmptyCollection() {
            Deck deck = Deck.of(ACE_OF_SPADES);

            boolean changed = deck.addAll(List.of());

            assertThat(changed).isFalse();
            assertThat(deck).containsExactly(ACE_OF_SPADES);
        }

        @Test
        @DisplayName("Adding a null collection should be rejected")
        void shouldRejectNullCollection() {
            Deck deck = Deck.of(ACE_OF_SPADES);

            assertThatThrownBy(() -> deck.addAll(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("cards must not be null");

            assertThat(deck).containsExactly(ACE_OF_SPADES);
        }

        @Test
        @DisplayName("A collection containing null should be rejected atomically")
        void shouldRejectCollectionContainingNull() {
            Deck deck = Deck.of(ACE_OF_SPADES);

            List<Card> cards = Arrays.asList(
                    KING_OF_HEARTS,
                    null
            );

            assertThatThrownBy(() -> deck.addAll(cards))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("deck must not contain null cards");

            assertThat(deck).containsExactly(ACE_OF_SPADES);
        }

        @Test
        @DisplayName("An invalid insertion index should be rejected for an empty collection")
        void shouldRejectInvalidIndexForEmptyCollection() {
            Deck deck = Deck.of(ACE_OF_SPADES);

            assertThatThrownBy(() -> deck.addAll(2, List.of()))
                    .isInstanceOf(IndexOutOfBoundsException.class);

            assertThat(deck).containsExactly(ACE_OF_SPADES);
        }
    }

    @Nested
    @DisplayName("Card snapshots")
    class CardSnapshots {

        @Test
        @DisplayName("Cards should return the current card order")
        void shouldReturnCurrentCardOrder() {
            Deck deck = Deck.of(
                    ACE_OF_SPADES,
                    KING_OF_HEARTS
            );

            assertThat(deck.cards()).containsExactly(
                    ACE_OF_SPADES,
                    KING_OF_HEARTS
            );
        }

        @Test
        @DisplayName("The returned card snapshot should be immutable")
        void shouldReturnImmutableSnapshot() {
            Deck deck = Deck.of(ACE_OF_SPADES);
            List<Card> snapshot = deck.cards();

            assertThatThrownBy(
                    () -> snapshot.add(KING_OF_HEARTS)
            ).isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("The returned snapshot should not change with the deck")
        void shouldReturnIndependentSnapshot() {
            Deck deck = Deck.of(ACE_OF_SPADES);
            List<Card> snapshot = deck.cards();

            deck.add(KING_OF_HEARTS);

            assertThat(snapshot).containsExactly(ACE_OF_SPADES);
            assertThat(deck).containsExactly(
                    ACE_OF_SPADES,
                    KING_OF_HEARTS
            );
        }
    }
}