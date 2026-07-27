package se.pbt.shufflelab.analysis.displacement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.deck.Deck;
import se.pbt.shufflelab.deck.DeckFactory;
import se.pbt.shufflelab.deck.card.Card;
import se.pbt.shufflelab.deck.card.Rank;
import se.pbt.shufflelab.deck.card.Suit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Displacement analyzer")
class DisplacementAnalyzerTest {

    private static final Card CARD_A = new Card(Suit.CLUBS, Rank.ACE);
    private static final Card CARD_B = new Card(Suit.CLUBS, Rank.TWO);
    private static final Card CARD_C = new Card(Suit.CLUBS, Rank.THREE);
    private static final Card CARD_D = new Card(Suit.CLUBS, Rank.FOUR);
    private static final Card CARD_E = new Card(Suit.CLUBS, Rank.FIVE);

    @Nested
    @DisplayName("Displacement calculation")
    class DisplacementCalculation {

        @Test
        @DisplayName("An unchanged deck should report zero displacement")
        void shouldReportZeroDisplacementForIdenticalOrder() {
            var original = Deck.of(CARD_A, CARD_B, CARD_C, CARD_D, CARD_E);
            var shuffled = Deck.of(CARD_A, CARD_B, CARD_C, CARD_D, CARD_E);

            var result = DisplacementAnalyzer.analyze(original, shuffled);

            assertAll(
                    () -> assertEquals(5, result.totalCards()),
                    () -> assertEquals(5, result.unmovedCards()),
                    () -> assertEquals(0, result.totalDisplacement()),
                    () -> assertEquals(0, result.maximumDisplacement())
            );
        }

        @Test
        @DisplayName("A fully reversed deck should report maximum displacement")
        void shouldReportMaximumDisplacementForReversedOrder() {
            var original = Deck.of(CARD_A, CARD_B, CARD_C, CARD_D, CARD_E);
            var shuffled = Deck.of(CARD_E, CARD_D, CARD_C, CARD_B, CARD_A);

            var result = DisplacementAnalyzer.analyze(original, shuffled);

            assertAll(
                    () -> assertEquals(5, result.totalCards()),
                    () -> assertEquals(1, result.unmovedCards()),
                    () -> assertEquals(12, result.totalDisplacement()),
                    () -> assertEquals(4, result.maximumDisplacement())
            );
        }

        @Test
        @DisplayName("A single adjacent swap should only displace the swapped cards")
        void shouldReportDisplacementForSingleSwap() {
            var original = Deck.of(CARD_A, CARD_B, CARD_C, CARD_D, CARD_E);
            var shuffled = Deck.of(CARD_B, CARD_A, CARD_C, CARD_D, CARD_E);

            var result = DisplacementAnalyzer.analyze(original, shuffled);

            assertAll(
                    () -> assertEquals(3, result.unmovedCards()),
                    () -> assertEquals(2, result.totalDisplacement()),
                    () -> assertEquals(1, result.maximumDisplacement())
            );
        }

        @Test
        @DisplayName("A standard deck should report zero displacement against itself")
        void shouldReportNoDisplacementForStandardDeckAgainstItself() {
            var original = DeckFactory.standardDeck();
            var shuffled = DeckFactory.standardDeck();

            var result = DisplacementAnalyzer.analyze(original, shuffled);

            assertAll(
                    () -> assertEquals(52, result.totalCards()),
                    () -> assertEquals(52, result.unmovedCards()),
                    () -> assertEquals(0, result.totalDisplacement()),
                    () -> assertEquals(0, result.maximumDisplacement())
            );
        }

        @Test
        @DisplayName("Empty decks should report zero displacement")
        void shouldReportZeroDisplacementForEmptyDecks() {
            var original = Deck.of();
            var shuffled = Deck.of();

            var result = DisplacementAnalyzer.analyze(original, shuffled);

            assertAll(
                    () -> assertEquals(0, result.totalCards()),
                    () -> assertEquals(0, result.unmovedCards()),
                    () -> assertEquals(0, result.totalDisplacement()),
                    () -> assertEquals(0, result.maximumDisplacement())
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
                    () -> DisplacementAnalyzer.analyze(original, shuffled)
            );
        }

        @Test
        @DisplayName("A null original deck should be rejected")
        void shouldRejectNullOriginal() {
            var shuffled = Deck.of(CARD_A, CARD_B, CARD_C, CARD_D, CARD_E);

            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> DisplacementAnalyzer.analyze(null, shuffled)
            );

            assertEquals("original must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("A null shuffled deck should be rejected")
        void shouldRejectNullShuffled() {
            var original = Deck.of(CARD_A, CARD_B, CARD_C, CARD_D, CARD_E);

            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> DisplacementAnalyzer.analyze(original, null)
            );

            assertEquals("shuffled must not be null", exception.getMessage());
        }
    }
}