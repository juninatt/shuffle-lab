package se.pbt.shufflelab.analysis.displacement;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.statistics.Statistics;

import static org.junit.jupiter.api.Assertions.*;

class AggregatedDisplacementResultTest {

    @Nested
    class Construction {

        @Test
        void shouldCreateResultWithValidValues() {
            Statistics unmovedCards = new Statistics(20.0, 20.0, 10.0, 30.0, 8.1650);
            Statistics totalDisplacement = new Statistics(200.0, 200.0, 100.0, 300.0, 81.6497);
            Statistics maximumDisplacement = new Statistics(30.0, 30.0, 20.0, 40.0, 8.1650);

            AggregatedDisplacementResult result = new AggregatedDisplacementResult(
                    3,
                    52,
                    unmovedCards,
                    totalDisplacement,
                    maximumDisplacement
            );

            assertAll(
                    () -> assertEquals(3, result.sampleSize()),
                    () -> assertEquals(52, result.totalCards()),
                    () -> assertEquals(unmovedCards, result.unmovedCards()),
                    () -> assertEquals(totalDisplacement, result.totalDisplacement()),
                    () -> assertEquals(maximumDisplacement, result.maximumDisplacement())
            );
        }
    }

    @Nested
    class NullValidation {

        @Test
        void shouldRejectNullUnmovedCards() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> new AggregatedDisplacementResult(
                            3,
                            52,
                            null,
                            new Statistics(200.0, 200.0, 100.0, 300.0, 81.6497),
                            new Statistics(30.0, 30.0, 20.0, 40.0, 8.1650)
                    )
            );

            assertEquals(
                    "unmovedCards must not be null",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNullTotalDisplacement() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> new AggregatedDisplacementResult(
                            3,
                            52,
                            new Statistics(20.0, 20.0, 10.0, 30.0, 8.1650),
                            null,
                            new Statistics(30.0, 30.0, 20.0, 40.0, 8.1650)
                    )
            );

            assertEquals(
                    "totalDisplacement must not be null",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNullMaximumDisplacement() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> new AggregatedDisplacementResult(
                            3,
                            52,
                            new Statistics(20.0, 20.0, 10.0, 30.0, 8.1650),
                            new Statistics(200.0, 200.0, 100.0, 300.0, 81.6497),
                            null
                    )
            );

            assertEquals(
                    "maximumDisplacement must not be null",
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
                    () -> new AggregatedDisplacementResult(
                            0,
                            52,
                            new Statistics(20.0, 20.0, 10.0, 30.0, 8.1650),
                            new Statistics(200.0, 200.0, 100.0, 300.0, 81.6497),
                            new Statistics(30.0, 30.0, 20.0, 40.0, 8.1650)
                    )
            );

            assertEquals(
                    "sampleSize must be at least 1",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNegativeTotalCards() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new AggregatedDisplacementResult(
                            3,
                            -1,
                            new Statistics(20.0, 20.0, 10.0, 30.0, 8.1650),
                            new Statistics(200.0, 200.0, 100.0, 300.0, 81.6497),
                            new Statistics(30.0, 30.0, 20.0, 40.0, 8.1650)
                    )
            );

            assertEquals(
                    "totalCards must not be negative",
                    exception.getMessage()
            );
        }
    }
}