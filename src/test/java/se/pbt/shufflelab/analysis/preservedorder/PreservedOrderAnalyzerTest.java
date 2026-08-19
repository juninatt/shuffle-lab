package se.pbt.shufflelab.analysis.preservedorder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.deck.Deck;
import se.pbt.shufflelab.deck.card.Card;
import se.pbt.shufflelab.deck.card.Rank;
import se.pbt.shufflelab.deck.card.Suit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Preserved order analyzer")
class PreservedOrderAnalyzerTest {

    private static final Card CARD_A = new Card(Suit.CLUBS, Rank.ACE);
    private static final Card CARD_B = new Card(Suit.CLUBS, Rank.TWO);
    private static final Card CARD_C = new Card(Suit.CLUBS, Rank.THREE);
    private static final Card CARD_D = new Card(Suit.CLUBS, Rank.FOUR);
    private static final Card CARD_E = new Card(Suit.CLUBS, Rank.FIVE);

    @Nested
    @DisplayName("Preserved order calculation")
    class PreservedOrderCalculation {

        @Test
        @DisplayName("An unchanged deck should be one preserved sequence spanning the whole deck")
        void shouldTreatIdenticalOrderAsOneLongSequence() {
            var original = Deck.of(CARD_A, CARD_B, CARD_C, CARD_D, CARD_E);
            var shuffled = Deck.of(CARD_A, CARD_B, CARD_C, CARD_D, CARD_E);

            var result = PreservedOrderAnalyzer.analyze(original, shuffled);

            assertAll(
                    () -> assertEquals(0, result.preservedPairs()),
                    () -> assertEquals(1, result.preservedSequences()),
                    () -> assertEquals(0, result.cardsInPairs()),
                    () -> assertEquals(5, result.cardsInSequences()),
                    () -> assertEquals(5, result.longestSequence()),
                    () -> assertEquals(100.0, result.preservedCardPercentage())
            );
        }

        @Test
        @DisplayName("A fully reversed deck should have no preserved order")
        void shouldReportNoPreservedOrderForReversedDeck() {
            var original = Deck.of(CARD_A, CARD_B, CARD_C, CARD_D, CARD_E);
            var shuffled = Deck.of(CARD_E, CARD_D, CARD_C, CARD_B, CARD_A);

            var result = PreservedOrderAnalyzer.analyze(original, shuffled);

            assertAll(
                    () -> assertEquals(0, result.preservedPairs()),
                    () -> assertEquals(0, result.preservedSequences()),
                    () -> assertEquals(0, result.cardsInPairs()),
                    () -> assertEquals(0, result.cardsInSequences()),
                    () -> assertEquals(0, result.longestSequence()),
                    () -> assertEquals(0.0, result.preservedCardPercentage())
            );
        }

        @Test
        @DisplayName("A single preserved pair should be counted separately from unrelated cards")
        void shouldCountAnIsolatedPreservedPair() {
            var original = Deck.of(CARD_A, CARD_B, CARD_C, CARD_D, CARD_E);
            var shuffled = Deck.of(CARD_A, CARD_B, CARD_D, CARD_C, CARD_E);

            var result = PreservedOrderAnalyzer.analyze(original, shuffled);

            assertAll(
                    () -> assertEquals(1, result.preservedPairs()),
                    () -> assertEquals(0, result.preservedSequences()),
                    () -> assertEquals(2, result.cardsInPairs()),
                    () -> assertEquals(0, result.cardsInSequences()),
                    () -> assertEquals(0, result.longestSequence()),
                    () -> assertEquals(40.0, result.preservedCardPercentage())
            );
        }

        @Test
        @DisplayName("A preserved pair and a preserved sequence should both be counted")
        void shouldCountBothAPreservedPairAndASequence() {
            var original = Deck.of(CARD_A, CARD_B, CARD_C, CARD_D, CARD_E);
            var shuffled = Deck.of(CARD_C, CARD_D, CARD_E, CARD_A, CARD_B);

            var result = PreservedOrderAnalyzer.analyze(original, shuffled);

            assertAll(
                    () -> assertEquals(1, result.preservedPairs()),
                    () -> assertEquals(1, result.preservedSequences()),
                    () -> assertEquals(2, result.cardsInPairs()),
                    () -> assertEquals(3, result.cardsInSequences()),
                    () -> assertEquals(3, result.longestSequence()),
                    () -> assertEquals(100.0, result.preservedCardPercentage())
            );
        }

        @Test
        @DisplayName("An empty deck should report no preserved order")
        void shouldReportNoPreservedOrderForEmptyDecks() {
            var original = Deck.of();
            var shuffled = Deck.of();

            var result = PreservedOrderAnalyzer.analyze(original, shuffled);

            assertAll(
                    () -> assertEquals(0, result.preservedPairs()),
                    () -> assertEquals(0, result.preservedSequences()),
                    () -> assertEquals(0.0, result.preservedCardPercentage())
            );
        }

        @Test
        @DisplayName("A single-card deck should report no preserved order")
        void shouldReportNoPreservedOrderForSingleCardDeck() {
            var original = Deck.of(CARD_A);
            var shuffled = Deck.of(CARD_A);

            var result = PreservedOrderAnalyzer.analyze(original, shuffled);

            assertAll(
                    () -> assertEquals(0, result.preservedPairs()),
                    () -> assertEquals(0, result.preservedSequences()),
                    () -> assertEquals(0.0, result.preservedCardPercentage())
            );
        }
    }

    @Nested
    @DisplayName("Validation")
    class Validation {

        @Test
        @DisplayName("A shuffled deck containing an unknown card should be rejected")
        void shouldRejectCardNotPresentInOriginal() {
            var original = Deck.of(CARD_A, CARD_B, CARD_C, CARD_D, CARD_E);
            var unknownCard = new Card(Suit.HEARTS, Rank.ACE);
            var shuffled = Deck.of(CARD_A, CARD_B, CARD_C, CARD_D, unknownCard);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> PreservedOrderAnalyzer.analyze(original, shuffled)
            );
        }

        @Test
        @DisplayName("A null original deck should be rejected")
        void shouldRejectNullOriginal() {
            var shuffled = Deck.of(CARD_A, CARD_B, CARD_C, CARD_D, CARD_E);

            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> PreservedOrderAnalyzer.analyze(null, shuffled)
            );

            assertEquals("original must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("A null shuffled deck should be rejected")
        void shouldRejectNullShuffled() {
            var original = Deck.of(CARD_A, CARD_B, CARD_C, CARD_D, CARD_E);

            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> PreservedOrderAnalyzer.analyze(original, null)
            );

            assertEquals("shuffled must not be null", exception.getMessage());
        }
    }
}