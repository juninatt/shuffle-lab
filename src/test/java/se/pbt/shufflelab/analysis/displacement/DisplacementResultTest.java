package se.pbt.shufflelab.analysis.displacement;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DisplacementResultTest {

    @Nested
    class Construction {

        @Test
        void shouldCreateResultWithValidValues() {
            DisplacementResult result = new DisplacementResult(
                    52,
                    4,
                    500,
                    35
            );

            assertAll(
                    () -> assertEquals(52, result.totalCards()),
                    () -> assertEquals(4, result.unmovedCards()),
                    () -> assertEquals(500, result.totalDisplacement()),
                    () -> assertEquals(35, result.maximumDisplacement())
            );
        }

        @Test
        void shouldAllowAllCardsToRemainUnmoved() {
            DisplacementResult result = new DisplacementResult(
                    52,
                    52,
                    0,
                    0
            );

            assertAll(
                    () -> assertEquals(52, result.totalCards()),
                    () -> assertEquals(52, result.unmovedCards()),
                    () -> assertEquals(0, result.totalDisplacement()),
                    () -> assertEquals(0, result.maximumDisplacement())
            );
        }

        @Test
        void shouldAllowNoCardsToRemainUnmoved() {
            DisplacementResult result = new DisplacementResult(
                    52,
                    0,
                    700,
                    51
            );

            assertAll(
                    () -> assertEquals(52, result.totalCards()),
                    () -> assertEquals(0, result.unmovedCards()),
                    () -> assertEquals(700, result.totalDisplacement()),
                    () -> assertEquals(51, result.maximumDisplacement())
            );
        }

        @Test
        void shouldAllowEmptyAnalysisResult() {
            DisplacementResult result = new DisplacementResult(
                    0,
                    0,
                    0,
                    0
            );

            assertAll(
                    () -> assertEquals(0, result.totalCards()),
                    () -> assertEquals(0, result.unmovedCards()),
                    () -> assertEquals(0, result.totalDisplacement()),
                    () -> assertEquals(0, result.maximumDisplacement())
            );
        }

        @Test
        void shouldAllowMaximumPossibleDisplacement() {
            DisplacementResult result = new DisplacementResult(
                    52,
                    0,
                    51,
                    51
            );

            assertEquals(51, result.maximumDisplacement());
        }
    }

    @Nested
    class DerivedValues {

        @Test
        void shouldCalculateNumberOfMovedCards() {
            DisplacementResult result = new DisplacementResult(
                    52,
                    7,
                    400,
                    30
            );

            assertEquals(45, result.movedCards());
        }

        @Test
        void shouldCalculateMovedCardPercentage() {
            DisplacementResult result = new DisplacementResult(
                    50,
                    10,
                    300,
                    25
            );

            assertEquals(
                    80.0,
                    result.movedCardPercentage(),
                    0.0001
            );
        }

        @Test
        void shouldCalculateUnmovedCardPercentage() {
            DisplacementResult result = new DisplacementResult(
                    50,
                    10,
                    300,
                    25
            );

            assertEquals(
                    20.0,
                    result.unmovedCardPercentage(),
                    0.0001
            );
        }

        @Test
        void shouldCalculateAverageDisplacementAcrossAllCards() {
            DisplacementResult result = new DisplacementResult(
                    40,
                    5,
                    200,
                    20
            );

            assertEquals(
                    5.0,
                    result.averageDisplacement(),
                    0.0001
            );
        }

        @Test
        void shouldPreserveDecimalPrecisionWhenCalculatingAverageDisplacement() {
            DisplacementResult result = new DisplacementResult(
                    3,
                    0,
                    10,
                    2
            );

            assertEquals(
                    10.0 / 3.0,
                    result.averageDisplacement(),
                    0.0001
            );
        }

        @Test
        void shouldReturnZeroDerivedValuesForEmptyAnalysis() {
            DisplacementResult result = new DisplacementResult(
                    0,
                    0,
                    0,
                    0
            );

            assertAll(
                    () -> assertEquals(0, result.movedCards()),
                    () -> assertEquals(
                            0.0,
                            result.movedCardPercentage(),
                            0.0001
                    ),
                    () -> assertEquals(
                            0.0,
                            result.unmovedCardPercentage(),
                            0.0001
                    ),
                    () -> assertEquals(
                            0.0,
                            result.averageDisplacement(),
                            0.0001
                    )
            );
        }

        @Test
        void movedAndUnmovedPercentagesShouldAddUpToOneHundred() {
            DisplacementResult result = new DisplacementResult(
                    52,
                    9,
                    450,
                    29
            );

            double totalPercentage =
                    result.movedCardPercentage()
                            + result.unmovedCardPercentage();

            assertEquals(100.0, totalPercentage, 0.0001);
        }
    }

    @Nested
    class Validation {

        @Test
        void shouldRejectNegativeTotalCards() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new DisplacementResult(
                            -1,
                            0,
                            0,
                            0
                    )
            );

            assertEquals(
                    "totalCards must not be negative",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNegativeUnmovedCards() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new DisplacementResult(
                            52,
                            -1,
                            0,
                            0
                    )
            );

            assertEquals(
                    "unmovedCards must not be negative",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectMoreUnmovedCardsThanTotalCards() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new DisplacementResult(
                            52,
                            53,
                            0,
                            0
                    )
            );

            assertEquals(
                    "unmovedCards must not exceed totalCards",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNegativeTotalDisplacement() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new DisplacementResult(
                            52,
                            4,
                            -1,
                            0
                    )
            );

            assertEquals(
                    "totalDisplacement must not be negative",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNegativeMaximumDisplacement() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new DisplacementResult(
                            52,
                            4,
                            100,
                            -1
                    )
            );

            assertEquals(
                    "maximumDisplacement must not be negative",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectMaximumDisplacementGreaterThanDeckAllows() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new DisplacementResult(
                            52,
                            4,
                            100,
                            52
                    )
            );

            assertEquals(
                    "maximumDisplacement exceeds the maximum possible displacement",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNonZeroMaximumDisplacementForEmptyAnalysis() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new DisplacementResult(
                            0,
                            0,
                            0,
                            1
                    )
            );

            assertEquals(
                    "maximumDisplacement must be zero when totalCards is zero",
                    exception.getMessage()
            );
        }
    }
}