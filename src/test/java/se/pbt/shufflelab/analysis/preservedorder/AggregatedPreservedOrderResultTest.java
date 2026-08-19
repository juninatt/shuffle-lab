package se.pbt.shufflelab.analysis.preservedorder;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.statistics.Statistics;

import static org.junit.jupiter.api.Assertions.*;

class AggregatedPreservedOrderResultTest {

    private static final Statistics PRESERVED_PAIRS = new Statistics(2.0, 2.0, 1.0, 3.0, 0.8165);
    private static final Statistics PRESERVED_SEQUENCES = new Statistics(1.0, 1.0, 0.0, 2.0, 0.8165);
    private static final Statistics CARDS_IN_PAIRS = new Statistics(4.0, 4.0, 2.0, 6.0, 1.6330);
    private static final Statistics CARDS_IN_SEQUENCES = new Statistics(3.6667, 3.0, 0.0, 8.0, 3.2998);
    private static final Statistics LONGEST_SEQUENCE = new Statistics(2.6667, 3.0, 0.0, 5.0, 2.0548);
    private static final Statistics PRESERVED_CARD_PERCENTAGE = new Statistics(20.0, 20.0, 10.0, 30.0, 8.1650);

    @Nested
    class Construction {

        @Test
        void shouldCreateResultWithValidValues() {
            AggregatedPreservedOrderResult result = new AggregatedPreservedOrderResult(
                    3,
                    PRESERVED_PAIRS,
                    PRESERVED_SEQUENCES,
                    CARDS_IN_PAIRS,
                    CARDS_IN_SEQUENCES,
                    LONGEST_SEQUENCE,
                    PRESERVED_CARD_PERCENTAGE
            );

            assertAll(
                    () -> assertEquals(3, result.sampleSize()),
                    () -> assertEquals(PRESERVED_PAIRS, result.preservedPairs()),
                    () -> assertEquals(PRESERVED_SEQUENCES, result.preservedSequences()),
                    () -> assertEquals(CARDS_IN_PAIRS, result.cardsInPairs()),
                    () -> assertEquals(CARDS_IN_SEQUENCES, result.cardsInSequences()),
                    () -> assertEquals(LONGEST_SEQUENCE, result.longestSequence()),
                    () -> assertEquals(PRESERVED_CARD_PERCENTAGE, result.preservedCardPercentage())
            );
        }
    }

    @Nested
    class NullValidation {

        @Test
        void shouldRejectNullPreservedPairs() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> new AggregatedPreservedOrderResult(
                            3,
                            null,
                            PRESERVED_SEQUENCES,
                            CARDS_IN_PAIRS,
                            CARDS_IN_SEQUENCES,
                            LONGEST_SEQUENCE,
                            PRESERVED_CARD_PERCENTAGE
                    )
            );

            assertEquals(
                    "preservedPairs must not be null",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNullPreservedSequences() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> new AggregatedPreservedOrderResult(
                            3,
                            PRESERVED_PAIRS,
                            null,
                            CARDS_IN_PAIRS,
                            CARDS_IN_SEQUENCES,
                            LONGEST_SEQUENCE,
                            PRESERVED_CARD_PERCENTAGE
                    )
            );

            assertEquals(
                    "preservedSequences must not be null",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNullCardsInPairs() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> new AggregatedPreservedOrderResult(
                            3,
                            PRESERVED_PAIRS,
                            PRESERVED_SEQUENCES,
                            null,
                            CARDS_IN_SEQUENCES,
                            LONGEST_SEQUENCE,
                            PRESERVED_CARD_PERCENTAGE
                    )
            );

            assertEquals(
                    "cardsInPairs must not be null",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNullCardsInSequences() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> new AggregatedPreservedOrderResult(
                            3,
                            PRESERVED_PAIRS,
                            PRESERVED_SEQUENCES,
                            CARDS_IN_PAIRS,
                            null,
                            LONGEST_SEQUENCE,
                            PRESERVED_CARD_PERCENTAGE
                    )
            );

            assertEquals(
                    "cardsInSequences must not be null",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNullLongestSequence() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> new AggregatedPreservedOrderResult(
                            3,
                            PRESERVED_PAIRS,
                            PRESERVED_SEQUENCES,
                            CARDS_IN_PAIRS,
                            CARDS_IN_SEQUENCES,
                            null,
                            PRESERVED_CARD_PERCENTAGE
                    )
            );

            assertEquals(
                    "longestSequence must not be null",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNullPreservedCardPercentage() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> new AggregatedPreservedOrderResult(
                            3,
                            PRESERVED_PAIRS,
                            PRESERVED_SEQUENCES,
                            CARDS_IN_PAIRS,
                            CARDS_IN_SEQUENCES,
                            LONGEST_SEQUENCE,
                            null
                    )
            );

            assertEquals(
                    "preservedCardPercentage must not be null",
                    exception.getMessage()
            );
        }
    }

    @Nested
    class Validation {

        @Test
        void shouldRejectSampleSizeBelowOne() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new AggregatedPreservedOrderResult(
                            0,
                            PRESERVED_PAIRS,
                            PRESERVED_SEQUENCES,
                            CARDS_IN_PAIRS,
                            CARDS_IN_SEQUENCES,
                            LONGEST_SEQUENCE,
                            PRESERVED_CARD_PERCENTAGE
                    )
            );

            assertEquals(
                    "sampleSize must be at least 1",
                    exception.getMessage()
            );
        }
    }
}