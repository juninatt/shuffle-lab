package se.pbt.shufflelab.statistics;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StatisticsTest {

    private static final double DELTA = 0.0001;

    @Nested
    class Construction {

        @Test
        void shouldCreateStatisticsWithValidValues() {
            Statistics statistics = new Statistics(20.0, 20.0, 10.0, 30.0, 8.1650);

            assertAll(
                    () -> assertEquals(20.0, statistics.mean()),
                    () -> assertEquals(20.0, statistics.median()),
                    () -> assertEquals(10.0, statistics.minimum()),
                    () -> assertEquals(30.0, statistics.maximum()),
                    () -> assertEquals(8.1650, statistics.standardDeviation())
            );
        }
    }

    @Nested
    class Validation {

        @Test
        void shouldRejectMinimumGreaterThanMaximum() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new Statistics(20.0, 20.0, 30.0, 10.0, 8.1650)
            );

            assertEquals(
                    "minimum must not be greater than maximum",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectMedianBelowMinimum() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new Statistics(20.0, 5.0, 10.0, 30.0, 8.1650)
            );

            assertEquals(
                    "median must be between minimum and maximum",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectMedianAboveMaximum() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new Statistics(20.0, 35.0, 10.0, 30.0, 8.1650)
            );

            assertEquals(
                    "median must be between minimum and maximum",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNegativeStandardDeviation() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new Statistics(20.0, 20.0, 10.0, 30.0, -1.0)
            );

            assertEquals(
                    "standardDeviation must not be negative",
                    exception.getMessage()
            );
        }
    }

    @Nested
    class Of {

        @Test
        void shouldComputeStatisticsForAnOddNumberOfSamples() {
            Statistics statistics = Statistics.of(4, 8, 2, 10, 6);

            assertAll(
                    () -> assertEquals(6.0, statistics.mean(), DELTA),
                    () -> assertEquals(6.0, statistics.median(), DELTA),
                    () -> assertEquals(2.0, statistics.minimum(), DELTA),
                    () -> assertEquals(10.0, statistics.maximum(), DELTA),
                    () -> assertEquals(2.8284271247461903, statistics.standardDeviation(), DELTA)
            );
        }

        @Test
        void shouldComputeStatisticsForAnEvenNumberOfSamples() {
            Statistics statistics = Statistics.of(1, 2, 3, 4);

            assertAll(
                    () -> assertEquals(2.5, statistics.mean(), DELTA),
                    () -> assertEquals(2.5, statistics.median(), DELTA),
                    () -> assertEquals(1.0, statistics.minimum(), DELTA),
                    () -> assertEquals(4.0, statistics.maximum(), DELTA),
                    () -> assertEquals(1.118033988749895, statistics.standardDeviation(), DELTA)
            );
        }

        @Test
        void shouldComputeStatisticsForASingleSample() {
            Statistics statistics = Statistics.of(7);

            assertAll(
                    () -> assertEquals(7.0, statistics.mean(), DELTA),
                    () -> assertEquals(7.0, statistics.median(), DELTA),
                    () -> assertEquals(7.0, statistics.minimum(), DELTA),
                    () -> assertEquals(7.0, statistics.maximum(), DELTA),
                    () -> assertEquals(0.0, statistics.standardDeviation(), DELTA)
            );
        }

        @Test
        void shouldNotBeAffectedByInputOrder() {
            Statistics statistics = Statistics.of(10, 2, 6, 4, 8);

            assertAll(
                    () -> assertEquals(6.0, statistics.mean(), DELTA),
                    () -> assertEquals(6.0, statistics.median(), DELTA)
            );
        }

        @Test
        void shouldRejectEmptySamples() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    Statistics::of
            );

            assertEquals(
                    "at least one sample is required",
                    exception.getMessage()
            );
        }
    }
}