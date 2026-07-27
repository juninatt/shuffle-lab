package se.pbt.shufflelab.analysis;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.analysis.displacement.DisplacementResult;
import se.pbt.shufflelab.analysis.preservation.PreservedOrderResult;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeckAnalysisTest {

    @Nested
    class Construction {

        @Test
        void shouldCreateCompleteDeckAnalysis() {
            DisplacementResult displacement =
                    new DisplacementResult(
                            52,
                            4,
                            500,
                            35
                    );

            PreservedOrderResult preservedOrder =
                    new PreservedOrderResult(
                            2,
                            1,
                            4,
                            5,
                            5,
                            17.3077
                    );

            DeckAnalysis analysis = new DeckAnalysis(
                    displacement,
                    preservedOrder
            );

            assertAll(
                    () -> assertSame(
                            displacement,
                            analysis.displacement()
                    ),
                    () -> assertSame(
                            preservedOrder,
                            analysis.preservedOrder()
                    )
            );
        }

        @Test
        void shouldExposeNestedAnalysisValues() {
            DeckAnalysis analysis = new DeckAnalysis(
                    new DisplacementResult(
                            52,
                            2,
                            600,
                            40
                    ),
                    new PreservedOrderResult(
                            1,
                            1,
                            2,
                            4,
                            4,
                            11.5385
                    )
            );

            assertAll(
                    () -> assertEquals(
                            50,
                            analysis.displacement().movedCards()
                    ),
                    () -> assertEquals(
                            1,
                            analysis.preservedOrder().preservedPairs()
                    ),
                    () -> assertEquals(
                            1,
                            analysis.preservedOrder().preservedSequences()
                    ),
                    () -> assertEquals(
                            4,
                            analysis.preservedOrder().longestSequence()
                    )
            );
        }
    }

    @Nested
    class NullValidation {

        @Test
        void shouldRejectNullDisplacementResult() {
            PreservedOrderResult preservedOrder =
                    new PreservedOrderResult(
                            0,
                            0,
                            0,
                            0,
                            0,
                            0.0
                    );

            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> new DeckAnalysis(
                            null,
                            preservedOrder
                    )
            );

            assertEquals(
                    "displacement must not be null",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNullPreservedOrderResult() {
            DisplacementResult displacement =
                    new DisplacementResult(
                            52,
                            52,
                            0,
                            0
                    );

            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> new DeckAnalysis(
                            displacement,
                            null
                    )
            );

            assertEquals(
                    "preservedOrder must not be null",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectBothResultsBeingNull() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> new DeckAnalysis(
                            null,
                            null
                    )
            );

            assertEquals(
                    "displacement must not be null",
                    exception.getMessage()
            );
        }
    }
}