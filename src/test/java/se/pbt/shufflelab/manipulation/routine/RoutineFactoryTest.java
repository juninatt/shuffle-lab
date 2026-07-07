package se.pbt.shufflelab.manipulation.routine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.TestRandoms;
import se.pbt.shufflelab.deck.DeckFactory;
import se.pbt.shufflelab.manipulation.operation.cut.DeckCutter;
import se.pbt.shufflelab.manipulation.operation.split.BalancedDeckSplitter;
import se.pbt.shufflelab.manipulation.shuffle.ShuffleFactory;
import se.pbt.shufflelab.skill.SkillLevel;
import se.pbt.shufflelab.skill.SkillProfile;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Routine factory")
class RoutineFactoryTest {


    @Nested
    @DisplayName("Creation")
    class Creation {

        @Test
        @DisplayName("Should create a simple riffle shuffle routine")
        void shouldCreateSimpleRiffleShuffleRoutine() {
            Routine routine = RoutineFactory.simpleRiffleShuffle(SkillLevel.EXPERT);

            assertThat(routine)
                    .as("Factory should return a configured routine")
                    .isNotNull();
        }
    }


    @Nested
    @DisplayName("Routine behaviour")
    class RoutineBehaviour {

        @Test
        @DisplayName("A simple riffle shuffle routine should preserve all cards")
        void simpleRiffleShuffleRoutineShouldPreserveAllCards() {
            var deck = DeckFactory.standardDeck();
            var originalCards = new HashSet<>(deck);

            Routine routine = RoutineFactory.simpleRiffleShuffle(SkillLevel.EXPERT);

            routine.execute(deck, TestRandoms.fixedRandom());

            assertThat(deck).hasSize(52);
            assertThat(new HashSet<>(deck)).isEqualTo(originalCards);
        }

        @Test
        @DisplayName("A simple riffle shuffle routine should change the deck order")
        void simpleRiffleShuffleRoutineShouldChangeDeckOrder() {
            var deck = DeckFactory.standardDeck();
            var originalOrder = List.copyOf(deck);

            Routine routine = RoutineFactory.simpleRiffleShuffle(SkillLevel.EXPERT);

            routine.execute(deck, TestRandoms.fixedRandom());

            assertThat(deck)
                    .as("A simple riffle shuffle routine should reorder the deck")
                    .isNotEqualTo(originalOrder);
        }

        @Test
        @DisplayName("A simple riffle shuffle routine should be deterministic with the same random seed")
        void simpleRiffleShuffleRoutineShouldBeDeterministicWithSameRandomSeed() {
            var firstDeck = DeckFactory.standardDeck();
            var secondDeck = DeckFactory.standardDeck();

            Routine firstRoutine = RoutineFactory.simpleRiffleShuffle(SkillLevel.INTERMEDIATE);
            Routine secondRoutine = RoutineFactory.simpleRiffleShuffle(SkillLevel.INTERMEDIATE);

            firstRoutine.execute(firstDeck, TestRandoms.seededRandom(42));
            secondRoutine.execute(secondDeck, TestRandoms.seededRandom(42));

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
            SkillProfile profile = SkillProfile.forLevel(skillLevel);

            Routine routine = RoutineFactory.simpleRiffleShuffle(skillLevel);

            var routineRandom = TestRandoms.seededRandom(42);
            var expectedRandom = TestRandoms.seededRandom(42);

            routine.execute(routineDeck, routineRandom);

            ShuffleFactory.riffle(skillLevel)
                    .shuffle(expectedDeck, expectedRandom);

            new DeckCutter(new BalancedDeckSplitter(profile.splitTolerance()))
                    .cut(expectedDeck, expectedRandom);

            assertThat(routineDeck)
                    .as("The simple routine should be equivalent to riffle shuffle followed by deck cut")
                    .isEqualTo(expectedDeck);
        }
    }


    @Nested
    @DisplayName("Validation")
    class Validation {

        @Test
        @DisplayName("Should reject null skill level")
        void shouldRejectNullSkillLevel() {
            assertThatThrownBy(() -> RoutineFactory.simpleRiffleShuffle(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("skillLevel must not be null");
        }
    }
}