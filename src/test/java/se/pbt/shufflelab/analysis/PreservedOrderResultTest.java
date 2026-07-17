package se.pbt.shufflelab.analysis;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PreservedOrderResultTest {

    @Nested
    class Construction {

        @Test
        void shouldCreateResultWithoutPreservedPatterns() {
            PreservedOrderResult result = new PreservedOrderResult(
                    0,
                    0,
                    0,
                    0,
                    0,
                    0.0
            );

            assertAll(
                    () -> assertEquals(0, result.preservedPairs()),
                    () -> assertEquals(0, result.preservedSequences()),
                    () -> assertEquals(0, result.cardsInPairs()),
                    () -> assertEquals(0, result.cardsInSequences()),
                    () -> assertEquals(0, result.longestSequence()),
                    () -> assertEquals(
                            0.0,
                            result.preservedCardPercentage(),
                            0.0001
                    )
            );
        }

        @Test
        void shouldCreateResultContainingOnlyPairs() {
            PreservedOrderResult result = new PreservedOrderResult(
                    3,
                    0,
                    6,
                    0,
                    0,
                    11.5385
            );

            assertAll(
                    () -> assertEquals(3, result.preservedPairs()),
                    () -> assertEquals(0, result.preservedSequences()),
                    () -> assertEquals(6, result.cardsInPairs()),
                    () -> assertEquals(0, result.cardsInSequences()),
                    () -> assertEquals(0, result.longestSequence()),
                    () -> assertEquals(
                            11.5385,
                            result.preservedCardPercentage(),
                            0.0001
                    )
            );
        }

        @Test
        void shouldCreateResultContainingOnlySequences() {
            PreservedOrderResult result = new PreservedOrderResult(
                    0,
                    2,
                    0,
                    7,
                    4,
                    13.4615
            );

            assertAll(
                    () -> assertEquals(0, result.preservedPairs()),
                    () -> assertEquals(2, result.preservedSequences()),
                    () -> assertEquals(0, result.cardsInPairs()),
                    () -> assertEquals(7, result.cardsInSequences()),
                    () -> assertEquals(4, result.longestSequence()),
                    () -> assertEquals(
                            13.4615,
                            result.preservedCardPercentage(),
                            0.0001
                    )
            );
        }

        @Test
        void shouldCreateResultContainingPairsAndSequences() {
            PreservedOrderResult result = new PreservedOrderResult(
                    2,
                    2,
                    4,
                    9,
                    5,
                    25.0
            );

            assertAll(
                    () -> assertEquals(2, result.preservedPairs()),
                    () -> assertEquals(2, result.preservedSequences()),
                    () -> assertEquals(4, result.cardsInPairs()),
                    () -> assertEquals(9, result.cardsInSequences()),
                    () -> assertEquals(5, result.longestSequence()),
                    () -> assertEquals(
                            25.0,
                            result.preservedCardPercentage(),
                            0.0001
                    )
            );
        }

        @Test
        void shouldAllowPercentageBoundaryValues() {
            PreservedOrderResult zeroPercentage =
                    new PreservedOrderResult(
                            0,
                            0,
                            0,
                            0,
                            0,
                            0.0
                    );

            PreservedOrderResult fullPercentage =
                    new PreservedOrderResult(
                            0,
                            1,
                            0,
                            52,
                            52,
                            100.0
                    );

            assertAll(
                    () -> assertEquals(
                            0.0,
                            zeroPercentage.preservedCardPercentage(),
                            0.0001
                    ),
                    () -> assertEquals(
                            100.0,
                            fullPercentage.preservedCardPercentage(),
                            0.0001
                    )
            );
        }

        @Test
        void shouldAllowSequenceWithMinimumLengthOfThree() {
            PreservedOrderResult result = new PreservedOrderResult(
                    0,
                    1,
                    0,
                    3,
                    3,
                    5.7692
            );

            assertAll(
                    () -> assertEquals(1, result.preservedSequences()),
                    () -> assertEquals(3, result.cardsInSequences()),
                    () -> assertEquals(3, result.longestSequence())
            );
        }
    }

    @Nested
    class NegativeValueValidation {

        @Test
        void shouldRejectNegativePreservedPairs() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new PreservedOrderResult(
                            -1,
                            0,
                            0,
                            0,
                            0,
                            0.0
                    )
            );

            assertEquals(
                    "preservedPairs must not be negative",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNegativePreservedSequences() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new PreservedOrderResult(
                            0,
                            -1,
                            0,
                            0,
                            0,
                            0.0
                    )
            );

            assertEquals(
                    "preservedSequences must not be negative",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNegativeCardsInPairs() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new PreservedOrderResult(
                            0,
                            0,
                            -1,
                            0,
                            0,
                            0.0
                    )
            );

            assertEquals(
                    "cardsInPairs must not be negative",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNegativeCardsInSequences() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new PreservedOrderResult(
                            0,
                            0,
                            0,
                            -1,
                            0,
                            0.0
                    )
            );

            assertEquals(
                    "cardsInSequences must not be negative",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNegativeLongestSequence() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new PreservedOrderResult(
                            0,
                            0,
                            0,
                            0,
                            -1,
                            0.0
                    )
            );

            assertEquals(
                    "longestSequence must not be negative",
                    exception.getMessage()
            );
        }
    }

    @Nested
    class PairValidation {

        @Test
        void shouldRejectCardsInPairsThatDoNotMatchPairCount() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new PreservedOrderResult(
                            2,
                            0,
                            3,
                            0,
                            0,
                            5.0
                    )
            );

            assertEquals(
                    "cardsInPairs must equal preservedPairs multiplied by two",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectCardsInPairsWhenNoPairsExist() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new PreservedOrderResult(
                            0,
                            0,
                            2,
                            0,
                            0,
                            5.0
                    )
            );

            assertEquals(
                    "cardsInPairs must equal preservedPairs multiplied by two",
                    exception.getMessage()
            );
        }
    }

    @Nested
    class SequenceValidation {

        @Test
        void shouldRejectSequenceCardsWhenNoSequencesExist() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new PreservedOrderResult(
                            0,
                            0,
                            0,
                            3,
                            0,
                            5.0
                    )
            );

            assertEquals(
                    "cardsInSequences must be zero when no preserved sequences exist",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectLongestSequenceWhenNoSequencesExist() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new PreservedOrderResult(
                            0,
                            0,
                            0,
                            0,
                            3,
                            5.0
                    )
            );

            assertEquals(
                    "longestSequence must be zero when no preserved sequences exist",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectInsufficientCardsForNumberOfSequences() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new PreservedOrderResult(
                            0,
                            2,
                            0,
                            5,
                            3,
                            10.0
                    )
            );

            assertEquals(
                    "each preserved sequence must contain at least three cards",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectLongestSequenceShorterThanThreeCards() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new PreservedOrderResult(
                            0,
                            1,
                            0,
                            3,
                            2,
                            5.0
                    )
            );

            assertEquals(
                    "longestSequence must contain at least three cards",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectLongestSequenceGreaterThanTotalSequenceCards() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new PreservedOrderResult(
                            0,
                            1,
                            0,
                            4,
                            5,
                            10.0
                    )
            );

            assertEquals(
                    "longestSequence must not exceed cardsInSequences",
                    exception.getMessage()
            );
        }
    }

    @Nested
    class PercentageValidation {

        @Test
        void shouldRejectNegativePercentage() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new PreservedOrderResult(
                            0,
                            0,
                            0,
                            0,
                            0,
                            -0.01
                    )
            );

            assertEquals(
                    "preservedCardPercentage must be between 0.0 and 100.0",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectPercentageGreaterThanOneHundred() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new PreservedOrderResult(
                            0,
                            0,
                            0,
                            0,
                            0,
                            100.01
                    )
            );

            assertEquals(
                    "preservedCardPercentage must be between 0.0 and 100.0",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNaNPercentage() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new PreservedOrderResult(
                            0,
                            0,
                            0,
                            0,
                            0,
                            Double.NaN
                    )
            );

            assertEquals(
                    "preservedCardPercentage must be between 0.0 and 100.0",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectPositiveInfinityPercentage() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new PreservedOrderResult(
                            0,
                            0,
                            0,
                            0,
                            0,
                            Double.POSITIVE_INFINITY
                    )
            );

            assertEquals(
                    "preservedCardPercentage must be between 0.0 and 100.0",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNegativeInfinityPercentage() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new PreservedOrderResult(
                            0,
                            0,
                            0,
                            0,
                            0,
                            Double.NEGATIVE_INFINITY
                    )
            );

            assertEquals(
                    "preservedCardPercentage must be between 0.0 and 100.0",
                    exception.getMessage()
            );
        }
    }
}