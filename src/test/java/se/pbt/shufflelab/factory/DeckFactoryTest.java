package se.pbt.shufflelab.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.deck.DeckOrder;
import se.pbt.shufflelab.deck.card.Card;
import se.pbt.shufflelab.deck.card.Rank;
import se.pbt.shufflelab.deck.card.Suit;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Deck factory")
class DeckFactoryTest {

    @Nested
    @DisplayName("Standard deck composition")
    class StandardDeckComposition {

        @Test
        @DisplayName("A standard deck should contain 52 cards")
        void standardDeckShouldContain52Cards() {
            var deck = DeckFactory.standardDeck();

            assertThat(deck)
                    .as("A standard deck should contain exactly 52 cards")
                    .hasSize(52);
        }

        @Test
        @DisplayName("A standard deck should contain 52 unique cards")
        void standardDeckShouldContain52UniqueCards() {
            var deck = DeckFactory.standardDeck();

            assertThat(new HashSet<>(deck))
                    .as("A standard deck should not contain duplicate cards")
                    .hasSize(52);
        }

        @Test
        @DisplayName("A standard deck should contain all four suits")
        void standardDeckShouldContainAllSuits() {
            var deck = DeckFactory.standardDeck();

            assertThat(
                    deck.stream()
                            .map(Card::suit)
                            .distinct()
            )
                    .as("A standard deck should contain clubs, diamonds, hearts and spades")
                    .hasSize(4);
        }

        @Test
        @DisplayName("A standard deck should contain all thirteen ranks for each suit")
        void standardDeckShouldContainAllRanksForEachSuit() {
            var deck = DeckFactory.standardDeck();

            for (Suit suit : Suit.values()) {
                long count = deck.stream()
                        .filter(card -> card.suit() == suit)
                        .count();

                assertThat(count)
                        .as("Suit %s should contain exactly 13 cards", suit)
                        .isEqualTo(13);
            }
        }
    }

    @Nested
    @DisplayName("Deck instance behavior")
    class DeckInstanceBehavior {

        @Test
        @DisplayName("A standard deck should be mutable")
        void standardDeckShouldReturnMutableDeck() {
            var deck = DeckFactory.standardDeck();

            deck.removeFirst();

            assertThat(deck)
                    .as("Cards should be removable from the returned deck")
                    .hasSize(51);
        }

        @Test
        @DisplayName("A standard deck should return a new deck instance each time")
        void standardDeckShouldReturnNewDeckEachTime() {
            var firstDeck = DeckFactory.standardDeck();
            var secondDeck = DeckFactory.standardDeck();

            assertThat(firstDeck)
                    .as("Each call should create a new deck instance")
                    .isNotSameAs(secondDeck);

            assertThat(firstDeck)
                    .as("New deck instances should contain the same cards in the same order")
                    .isEqualTo(secondDeck);
        }
    }

    @Nested
    @DisplayName("Deck order behavior")
    class DeckOrderBehavior {

        @Test
        @DisplayName("A standard deck should use new-deck order")
        void standardDeckShouldUseNewDeckOrder() {
            var deck = DeckFactory.standardDeck();

            assertThat(deck.getFirst())
                    .as("NEW_DECK should start with ace of clubs")
                    .isEqualTo(new Card(Suit.CLUBS, Rank.ACE));

            assertThat(deck.get(12))
                    .as("NEW_DECK should contain king of clubs as the last club")
                    .isEqualTo(new Card(Suit.CLUBS, Rank.KING));

            assertThat(deck.get(13))
                    .as("NEW_DECK should continue with ace of diamonds after clubs")
                    .isEqualTo(new Card(Suit.DIAMONDS, Rank.ACE));

            assertThat(deck.getLast())
                    .as("NEW_DECK should end with king of spades")
                    .isEqualTo(new Card(Suit.SPADES, Rank.KING));
        }

        @Test
        @DisplayName("A casino inspection deck should start with spades in ascending rank order")
        void casinoInspectionDeckShouldUseCasinoInspectionOrder() {
            var deck = DeckFactory.create(DeckOrder.CASINO_INSPECTION);

            assertThat(deck.getFirst())
                    .as("CASINO_INSPECTION should start with ace of spades")
                    .isEqualTo(new Card(Suit.SPADES, Rank.ACE));

            assertThat(deck.get(12))
                    .as("CASINO_INSPECTION should contain king of spades as the last spade")
                    .isEqualTo(new Card(Suit.SPADES, Rank.KING));

            assertThat(deck.get(13))
                    .as("CASINO_INSPECTION should continue with ace of hearts after spades")
                    .isEqualTo(new Card(Suit.HEARTS, Rank.ACE));

            assertThat(deck.getLast())
                    .as("CASINO_INSPECTION should end with king of clubs")
                    .isEqualTo(new Card(Suit.CLUBS, Rank.KING));
        }

        @Test
        @DisplayName("A bridge deck should group suits and order ranks from ace down to two")
        void bridgeDeckShouldUseBridgeOrder() {
            var deck = DeckFactory.create(DeckOrder.BRIDGE);

            assertThat(deck.getFirst())
                    .as("BRIDGE should start with ace of spades")
                    .isEqualTo(new Card(Suit.SPADES, Rank.ACE));

            assertThat(deck.get(1))
                    .as("BRIDGE should place king of spades after ace of spades")
                    .isEqualTo(new Card(Suit.SPADES, Rank.KING));

            assertThat(deck.get(12))
                    .as("BRIDGE should contain two of spades as the last spade")
                    .isEqualTo(new Card(Suit.SPADES, Rank.TWO));

            assertThat(deck.getLast())
                    .as("BRIDGE should end with two of clubs")
                    .isEqualTo(new Card(Suit.CLUBS, Rank.TWO));
        }
    }
}