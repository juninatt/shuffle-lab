package se.pbt.shufflelab.analysis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.deck.Deck;
import se.pbt.shufflelab.deck.card.Card;
import se.pbt.shufflelab.deck.card.Rank;
import se.pbt.shufflelab.deck.card.Suit;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Card position lookup")
class CardPositionLookupTest {

    private static final Card CARD_A = new Card(Suit.CLUBS, Rank.ACE);
    private static final Card CARD_B = new Card(Suit.CLUBS, Rank.TWO);
    private static final Card CARD_C = new Card(Suit.CLUBS, Rank.THREE);

    @Nested
    @DisplayName("Index construction")
    class IndexConstruction {

        @Test
        @DisplayName("Each card should be mapped to its position in the deck")
        void shouldMapEachCardToItsPosition() {
            var deck = Deck.of(CARD_A, CARD_B, CARD_C);

            Map<Card, Integer> positions = CardPositionLookup.indexByPosition(deck);

            assertEquals(0, positions.get(CARD_A));
            assertEquals(1, positions.get(CARD_B));
            assertEquals(2, positions.get(CARD_C));
        }

        @Test
        @DisplayName("An empty deck should produce an empty index")
        void shouldReturnEmptyMapForEmptyDeck() {
            var deck = Deck.of();

            Map<Card, Integer> positions = CardPositionLookup.indexByPosition(deck);

            assertTrue(positions.isEmpty());
        }

        @Test
        @DisplayName("Each call should build an independent index")
        void shouldBuildIndependentMapEachCall() {
            var deck = Deck.of(CARD_A, CARD_B, CARD_C);

            Map<Card, Integer> firstCall = CardPositionLookup.indexByPosition(deck);
            firstCall.put(CARD_A, 99);

            Map<Card, Integer> secondCall = CardPositionLookup.indexByPosition(deck);

            assertEquals(0, secondCall.get(CARD_A));
        }
    }

    @Nested
    @DisplayName("Position lookup")
    class PositionLookup {

        @Test
        @DisplayName("A known card should return its indexed position")
        void shouldReturnPositionOfKnownCard() {
            var deck = Deck.of(CARD_A, CARD_B, CARD_C);
            Map<Card, Integer> positions = CardPositionLookup.indexByPosition(deck);

            int position = CardPositionLookup.positionOf(positions, CARD_B);

            assertEquals(1, position);
        }

        @Test
        @DisplayName("A card not present in the lookup should be rejected")
        void shouldRejectCardNotInLookup() {
            var deck = Deck.of(CARD_A, CARD_B);
            Map<Card, Integer> positions = CardPositionLookup.indexByPosition(deck);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> CardPositionLookup.positionOf(positions, CARD_C)
            );

            assertTrue(exception.getMessage().contains(CARD_C.toString()));
        }
    }
}