package se.pbt.shufflelab.trial;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.TestRandoms;
import se.pbt.shufflelab.analysis.DeckAnalysis;
import se.pbt.shufflelab.factory.RoutineFactory;
import se.pbt.shufflelab.handling.routine.RoutineProtocol;
import se.pbt.shufflelab.skill.SkillLevel;

import java.util.List;
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.*;

class TrialRunnerTest {

    private static final RoutineProtocol ROUTINE = RoutineFactory.simpleRiffleShuffle(SkillLevel.EXPERT);

    @Nested
    class Execution {

        @Test
        void shouldProduceOneAnalysisPerTrial() {
            RandomGenerator random = TestRandoms.fixedRandom();

            List<DeckAnalysis> results = TrialRunner.run(ROUTINE, 10, random);

            assertEquals(10, results.size());
        }

        @Test
        void shouldAnalyzeEveryTrialAgainstAFullStandardDeck() {
            RandomGenerator random = TestRandoms.fixedRandom();

            List<DeckAnalysis> results = TrialRunner.run(ROUTINE, 5, random);

            for (DeckAnalysis result : results) {
                assertEquals(52, result.displacement().totalCards());
            }
        }

        @Test
        void shouldSupportRunningASingleTrial() {
            RandomGenerator random = TestRandoms.fixedRandom();

            List<DeckAnalysis> results = TrialRunner.run(ROUTINE, 1, random);

            assertEquals(1, results.size());
        }

        @Test
        void shouldReturnAnImmutableList() {
            RandomGenerator random = TestRandoms.fixedRandom();

            List<DeckAnalysis> results = TrialRunner.run(ROUTINE, 1, random);

            assertThrows(
                    UnsupportedOperationException.class,
                    () -> results.add(results.getFirst())
            );
        }

        @Test
        void shouldDrawFreshRandomnessForEachTrial() {
            RandomGenerator random = TestRandoms.fixedRandom();

            List<DeckAnalysis> results = TrialRunner.run(ROUTINE, 20, random);

            boolean allIdentical = results.stream()
                    .allMatch(result -> result.equals(results.getFirst()));

            assertFalse(allIdentical, "trials sharing one random source should not all produce the same result");
        }
    }

    @Nested
    class Validation {

        @Test
        void shouldRejectNullRoutine() {
            RandomGenerator random = TestRandoms.fixedRandom();

            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> TrialRunner.run(null, 10, random)
            );

            assertEquals(
                    "routine must not be null",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNullRandom() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> TrialRunner.run(ROUTINE, 10, null)
            );

            assertEquals(
                    "random must not be null",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectZeroTrials() {
            RandomGenerator random = TestRandoms.fixedRandom();

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> TrialRunner.run(ROUTINE, 0, random)
            );

            assertEquals(
                    "trials must be at least 1",
                    exception.getMessage()
            );
        }

        @Test
        void shouldRejectNegativeTrials() {
            RandomGenerator random = TestRandoms.fixedRandom();

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> TrialRunner.run(ROUTINE, -5, random)
            );

            assertEquals(
                    "trials must be at least 1",
                    exception.getMessage()
            );
        }
    }
}