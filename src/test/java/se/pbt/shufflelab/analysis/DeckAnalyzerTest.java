package se.pbt.shufflelab.analysis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.analysis.displacement.DisplacementAnalyzer;
import se.pbt.shufflelab.analysis.displacement.DisplacementResult;
import se.pbt.shufflelab.analysis.preservedorder.PreservedOrderAnalyzer;
import se.pbt.shufflelab.analysis.preservedorder.PreservedOrderResult;
import se.pbt.shufflelab.deck.Deck;
import se.pbt.shufflelab.deck.card.Card;
import se.pbt.shufflelab.deck.card.Rank;
import se.pbt.shufflelab.deck.card.Suit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Deck analyzer")
class DeckAnalyzerTest {

    private static final Card CARD_A = new Card(Suit.CLUBS, Rank.ACE);
    private static final Card CARD_B = new Card(Suit.CLUBS, Rank.TWO);
    private static final Card CARD_C = new Card(Suit.CLUBS, Rank.THREE);
    private static final Card CARD_D = new Card(Suit.CLUBS, Rank.FOUR);
    private static final Card CARD_E = new Card(Suit.CLUBS, Rank.FIVE);

    @Nested
    @DisplayName("Combined analysis")
    class CombinedAnalysis {

        @Test
        @DisplayName("The analysis should combine displacement and preserved order metrics")
        void shouldCombineDisplacementAndPreservedOrderResults() {
            var original = Deck.of(CARD_A, CARD_B, CARD_C, CARD_D, CARD_E);
            var shuffled = Deck.of(CARD_C, CARD_D, CARD_E, CARD_A, CARD_B);

            var analysis = DeckAnalyzer.analyze(original, shuffled);

            assertAll(
                    () -> assertEquals(
                            new DisplacementResult(5, 0, 12, 3),
                            analysis.displacement()
                    ),
                    () -> assertEquals(
                            new PreservedOrderResult(1, 1, 2, 3, 3, 100.0),
                            analysis.preservedOrder()
                    )
            );
        }

        @Test
        @DisplayName("The displacement result should match the dedicated analyzer's output")
        void shouldMatchDisplacementAnalyzerOutput() {
            var original = Deck.of(CARD_A, CARD_B, CARD_C, CARD_D, CARD_E);
            var shuffled = Deck.of(CARD_B, CARD_A, CARD_C, CARD_D, CARD_E);

            var analysis = DeckAnalyzer.analyze(original, shuffled);
            var expected = DisplacementAnalyzer.analyze(original, shuffled);

            assertEquals(expected, analysis.displacement());
        }

        @Test
        @DisplayName("The preserved order result should match the dedicated analyzer's output")
        void shouldMatchPreservedOrderAnalyzerOutput() {
            var original = Deck.of(CARD_A, CARD_B, CARD_C, CARD_D, CARD_E);
            var shuffled = Deck.of(CARD_B, CARD_A, CARD_C, CARD_D, CARD_E);

            var analysis = DeckAnalyzer.analyze(original, shuffled);
            var expected = PreservedOrderAnalyzer.analyze(original, shuffled);

            assertEquals(expected, analysis.preservedOrder());
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
                    () -> DeckAnalyzer.analyze(original, shuffled)
            );
        }

        @Test
        @DisplayName("A null original deck should be rejected")
        void shouldRejectNullOriginal() {
            var shuffled = Deck.of(CARD_A, CARD_B, CARD_C, CARD_D, CARD_E);

            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> DeckAnalyzer.analyze(null, shuffled)
            );

            assertEquals("original must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("A null shuffled deck should be rejected")
        void shouldRejectNullShuffled() {
            var original = Deck.of(CARD_A, CARD_B, CARD_C, CARD_D, CARD_E);

            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> DeckAnalyzer.analyze(original, null)
            );

            assertEquals("shuffled must not be null", exception.getMessage());
        }
    }
}