package se.pbt.shufflelab.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import se.pbt.shufflelab.TestRandoms;
import se.pbt.shufflelab.handling.operation.cut.DeckCutter;
import se.pbt.shufflelab.handling.operation.split.BalancedDeckSplitter;
import se.pbt.shufflelab.handling.routine.RoutineProtocol;
import se.pbt.shufflelab.handling.shuffle.PileShuffle;
import se.pbt.shufflelab.skill.SkillLevel;
import se.pbt.shufflelab.skill.SkillProfile;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Routine factory")
class RoutineFactoryTest {

    /**
     * Every routine-producing method in {@link RoutineFactory}, paired with a
     * display name. Used to run the same behavioural checks against all of
     * them without duplicating each test method per routine.
     */
    private static Stream<Arguments> routineFactories() {
        return Stream.of(
                Arguments.of("simple riffle shuffle", (Function<SkillLevel, RoutineProtocol>) RoutineFactory::simpleRiffleShuffle),
                Arguments.of("repeated overhand shuffle", (Function<SkillLevel, RoutineProtocol>) RoutineFactory::repeatedOverhandShuffle),
                Arguments.of("standard riffle shuffle", (Function<SkillLevel, RoutineProtocol>) RoutineFactory::standardRiffleShuffle),
                Arguments.of("casual shuffle sequence", (Function<SkillLevel, RoutineProtocol>) RoutineFactory::casualShuffleSequence),
                Arguments.of("pile shuffle then riffle", (Function<SkillLevel, RoutineProtocol>) RoutineFactory::pileShuffleThenRiffle)
        );
    }

    /**
     * Every routine-producing method in {@link RoutineFactory}, paired with the
     * exact name it is expected to assign to the routine it creates.
     */
    private static Stream<Arguments> routineFactoriesWithExpectedNames() {
        return Stream.of(
                Arguments.of((Function<SkillLevel, RoutineProtocol>) RoutineFactory::simpleRiffleShuffle, "Simple riffle shuffle"),
                Arguments.of((Function<SkillLevel, RoutineProtocol>) RoutineFactory::repeatedOverhandShuffle, "Repeated overhand shuffle"),
                Arguments.of((Function<SkillLevel, RoutineProtocol>) RoutineFactory::standardRiffleShuffle, "Standard riffle shuffle"),
                Arguments.of((Function<SkillLevel, RoutineProtocol>) RoutineFactory::casualShuffleSequence, "Casual shuffle sequence"),
                Arguments.of((Function<SkillLevel, RoutineProtocol>) RoutineFactory::pileShuffleThenRiffle, "Pile shuffle then riffle")
        );
    }

    @Nested
    @DisplayName("Creation")
    class Creation {

        @ParameterizedTest(name = "{0}")
        @MethodSource("se.pbt.shufflelab.factory.RoutineFactoryTest#routineFactories")
        @DisplayName("Should create a configured routine")
        void shouldCreateAConfiguredRoutine(String name, Function<SkillLevel, RoutineProtocol> factory) {
            RoutineProtocol routine = factory.apply(SkillLevel.EXPERT);

            assertThat(routine)
                    .as("Factory should return a configured routine")
                    .isNotNull();
        }
    }

    @Nested
    @DisplayName("Determinism")
    class Determinism {

        @ParameterizedTest(name = "{0}")
        @MethodSource("se.pbt.shufflelab.factory.RoutineFactoryTest#routineFactories")
        @DisplayName("Should be deterministic with the same random seed")
        void shouldBeDeterministicWithSameRandomSeed(String name, Function<SkillLevel, RoutineProtocol> factory) {
            var firstDeck = DeckFactory.standardDeck();
            var secondDeck = DeckFactory.standardDeck();

            factory.apply(SkillLevel.INTERMEDIATE).execute(firstDeck, TestRandoms.seededRandom(42));
            factory.apply(SkillLevel.INTERMEDIATE).execute(secondDeck, TestRandoms.seededRandom(42));

            assertThat(firstDeck)
                    .as("The same routine, skill level and random seed should produce the same result")
                    .isEqualTo(secondDeck);
        }
    }

    @Nested
    @DisplayName("Routine composition")
    class RoutineComposition {

        @Test
        @DisplayName("A simple riffle shuffle routine should apply riffle shuffle before deck cut")
        void simpleRiffleShuffleRoutineShouldApplyRiffleBeforeCut() {
            var routineDeck = DeckFactory.standardDeck();
            var expectedDeck = DeckFactory.standardDeck();

            SkillLevel skillLevel = SkillLevel.INTERMEDIATE;
            SkillProfile profile = SkillProfile.withLevel(skillLevel);

            RoutineProtocol routine = RoutineFactory.simpleRiffleShuffle(skillLevel);

            var routineRandom = TestRandoms.seededRandom(42);
            var expectedRandom = TestRandoms.seededRandom(42);

            routine.execute(routineDeck, routineRandom);

            ShuffleFactory.riffle(skillLevel)
                    .shuffle(expectedDeck, expectedRandom);

            new DeckCutter(new BalancedDeckSplitter(profile.maxSplitDeviation()))
                    .cut(expectedDeck, expectedRandom);

            assertThat(routineDeck)
                    .as("The simple routine should be equivalent to riffle shuffle followed by deck cut")
                    .isEqualTo(expectedDeck);
        }

        @Test
        @DisplayName("A repeated overhand shuffle routine should apply the overhand shuffle three times in sequence")
        void repeatedOverhandShuffleRoutineShouldApplyOverhandShuffleThreeTimes() {
            var routineDeck = DeckFactory.standardDeck();
            var expectedDeck = DeckFactory.standardDeck();

            SkillLevel skillLevel = SkillLevel.INTERMEDIATE;

            RoutineProtocol routine = RoutineFactory.repeatedOverhandShuffle(skillLevel);

            var routineRandom = TestRandoms.seededRandom(42);
            var expectedRandom = TestRandoms.seededRandom(42);

            routine.execute(routineDeck, routineRandom);

            var overhandShuffle = ShuffleFactory.overhand(skillLevel);
            overhandShuffle.shuffle(expectedDeck, expectedRandom);
            overhandShuffle.shuffle(expectedDeck, expectedRandom);
            overhandShuffle.shuffle(expectedDeck, expectedRandom);

            assertThat(routineDeck)
                    .as("The repeated routine should be equivalent to three sequential overhand shuffles")
                    .isEqualTo(expectedDeck);
        }

        @Test
        @DisplayName("A standard riffle shuffle routine should apply riffle shuffle three times before deck cut")
        void standardRiffleShuffleRoutineShouldApplyThreeRifflesBeforeCut() {
            var routineDeck = DeckFactory.standardDeck();
            var expectedDeck = DeckFactory.standardDeck();

            SkillLevel skillLevel = SkillLevel.INTERMEDIATE;
            SkillProfile profile = SkillProfile.withLevel(skillLevel);

            RoutineProtocol routine = RoutineFactory.standardRiffleShuffle(skillLevel);

            var routineRandom = TestRandoms.seededRandom(42);
            var expectedRandom = TestRandoms.seededRandom(42);

            routine.execute(routineDeck, routineRandom);

            var riffleShuffle = ShuffleFactory.riffle(skillLevel);
            riffleShuffle.shuffle(expectedDeck, expectedRandom);
            riffleShuffle.shuffle(expectedDeck, expectedRandom);
            riffleShuffle.shuffle(expectedDeck, expectedRandom);

            new DeckCutter(new BalancedDeckSplitter(profile.maxSplitDeviation()))
                    .cut(expectedDeck, expectedRandom);

            assertThat(routineDeck)
                    .as("The standard routine should be equivalent to three riffle shuffles followed by a deck cut")
                    .isEqualTo(expectedDeck);
        }

        @Test
        @DisplayName("A casual shuffle sequence should apply overhand shuffle, two riffle shuffles, then deck cut")
        void casualShuffleSequenceShouldApplyOverhandThenTwoRifflesThenCut() {
            var routineDeck = DeckFactory.standardDeck();
            var expectedDeck = DeckFactory.standardDeck();

            SkillLevel skillLevel = SkillLevel.INTERMEDIATE;
            SkillProfile profile = SkillProfile.withLevel(skillLevel);

            RoutineProtocol routine = RoutineFactory.casualShuffleSequence(skillLevel);

            var routineRandom = TestRandoms.seededRandom(42);
            var expectedRandom = TestRandoms.seededRandom(42);

            routine.execute(routineDeck, routineRandom);

            ShuffleFactory.overhand(skillLevel)
                    .shuffle(expectedDeck, expectedRandom);

            var riffleShuffle = ShuffleFactory.riffle(skillLevel);
            riffleShuffle.shuffle(expectedDeck, expectedRandom);
            riffleShuffle.shuffle(expectedDeck, expectedRandom);

            new DeckCutter(new BalancedDeckSplitter(profile.maxSplitDeviation()))
                    .cut(expectedDeck, expectedRandom);

            assertThat(routineDeck)
                    .as("The casual sequence should be equivalent to overhand, two riffles, then a deck cut")
                    .isEqualTo(expectedDeck);
        }

        @Test
        @DisplayName("A pile-shuffle-then-riffle routine should apply the pile shuffle, then riffle shuffle, then deck cut")
        void pileShuffleThenRiffleRoutineShouldApplyPileThenRiffleThenCut() {
            var routineDeck = DeckFactory.standardDeck();
            var expectedDeck = DeckFactory.standardDeck();

            SkillLevel skillLevel = SkillLevel.INTERMEDIATE;
            SkillProfile profile = SkillProfile.withLevel(skillLevel);

            RoutineProtocol routine = RoutineFactory.pileShuffleThenRiffle(skillLevel);

            var routineRandom = TestRandoms.seededRandom(42);
            var expectedRandom = TestRandoms.seededRandom(42);

            routine.execute(routineDeck, routineRandom);

            new PileShuffle(4)
                    .shuffle(expectedDeck, expectedRandom);

            ShuffleFactory.riffle(skillLevel)
                    .shuffle(expectedDeck, expectedRandom);

            new DeckCutter(new BalancedDeckSplitter(profile.maxSplitDeviation()))
                    .cut(expectedDeck, expectedRandom);

            assertThat(routineDeck)
                    .as("The routine should be equivalent to a pile shuffle, a riffle shuffle, then a deck cut")
                    .isEqualTo(expectedDeck);
        }
    }

    @Nested
    @DisplayName("Naming")
    class Naming {

        @ParameterizedTest(name = "{1}")
        @MethodSource("se.pbt.shufflelab.factory.RoutineFactoryTest#routineFactoriesWithExpectedNames")
        @DisplayName("Should assign the expected name to the routine it creates")
        void shouldAssignExpectedName(Function<SkillLevel, RoutineProtocol> factory, String expectedName) {
            RoutineProtocol routine = factory.apply(SkillLevel.EXPERT);

            assertThat(routine.toString())
                    .as("The routine's name should identify the technique it represents")
                    .isEqualTo(expectedName);
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("se.pbt.shufflelab.factory.RoutineFactoryTest#routineFactories")
        @DisplayName("Should use the same name regardless of skill level")
        void shouldUseSameNameRegardlessOfSkillLevel(String name, Function<SkillLevel, RoutineProtocol> factory) {
            String noviceName = factory.apply(SkillLevel.NOVICE).toString();
            String expertName = factory.apply(SkillLevel.EXPERT).toString();

            assertThat(expertName)
                    .as("A routine's name should identify its technique, not the skill level it was built with")
                    .isEqualTo(noviceName);
        }

        @Test
        @DisplayName("Every routine should have a unique name")
        void everyRoutineShouldHaveAUniqueName() {
            List<String> names = List.of(
                    RoutineFactory.simpleRiffleShuffle(SkillLevel.EXPERT).toString(),
                    RoutineFactory.repeatedOverhandShuffle(SkillLevel.EXPERT).toString(),
                    RoutineFactory.standardRiffleShuffle(SkillLevel.EXPERT).toString(),
                    RoutineFactory.casualShuffleSequence(SkillLevel.EXPERT).toString(),
                    RoutineFactory.pileShuffleThenRiffle(SkillLevel.EXPERT).toString()
            );

            assertThat(names)
                    .as("Duplicate routine names would make report rows indistinguishable")
                    .doesNotHaveDuplicates();
        }
    }

    @Nested
    @DisplayName("Validation")
    class Validation {

        @ParameterizedTest(name = "{0}")
        @MethodSource("se.pbt.shufflelab.factory.RoutineFactoryTest#routineFactories")
        @DisplayName("Should reject a null skill level")
        void shouldRejectNullSkillLevel(String name, Function<SkillLevel, RoutineProtocol> factory) {
            assertThatThrownBy(() -> factory.apply(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("skillLevel must not be null");
        }
    }
}