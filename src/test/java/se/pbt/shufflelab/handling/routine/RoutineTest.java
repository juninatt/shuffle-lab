package se.pbt.shufflelab.handling.routine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.TestRandoms;
import se.pbt.shufflelab.factory.DeckFactory;
import se.pbt.shufflelab.factory.ShuffleFactory;
import se.pbt.shufflelab.handling.operation.cut.DeckCutter;
import se.pbt.shufflelab.handling.operation.split.BalancedDeckSplitter;
import se.pbt.shufflelab.handling.shuffle.Shuffle;
import se.pbt.shufflelab.skill.SkillLevel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.random.RandomGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Routine")
class RoutineTest {

    @Nested
    @DisplayName("Execution order")
    class ExecutionOrder {

        @Test
        @DisplayName("Should run each operation in the order it was given")
        void shouldRunOperationsInOrder() {
            List<Integer> callOrder = new ArrayList<>();

            Shuffle first = (deck, random) -> callOrder.add(1);
            Shuffle second = (deck, random) -> callOrder.add(2);
            Shuffle third = (deck, random) -> callOrder.add(3);

            var routine = new Routine("Test Routine", List.of(first, second, third));

            routine.execute(DeckFactory.standardDeck(), TestRandoms.fixedRandom());

            assertThat(callOrder).containsExactly(1, 2, 3);
        }

        @Test
        @DisplayName("Should run a single operation")
        void shouldRunASingleOperation() {
            List<Integer> callOrder = new ArrayList<>();

            Shuffle onlyOperation = (deck, random) -> callOrder.add(1);

            var routine = new Routine("Test Routine", List.of(onlyOperation));

            routine.execute(DeckFactory.standardDeck(), TestRandoms.fixedRandom());

            assertThat(callOrder).containsExactly(1);
        }

        @Test
        @DisplayName("Should do nothing for an empty list of operations")
        void shouldDoNothingForEmptyOperations() {
            var deck = DeckFactory.standardDeck();
            var original = List.copyOf(deck);

            var routine = new Routine("Test Routine", List.of());

            routine.execute(deck, TestRandoms.fixedRandom());

            assertThat(deck).isEqualTo(original);
        }
    }

    @Nested
    @DisplayName("Shared state between operations")
    class SharedStateBetweenOperations {

        @Test
        @DisplayName("Each operation should see the previous operation's effect on the deck")
        void operationsShouldShareTheSameDeckInstance() {
            var deck = DeckFactory.standardDeck();
            var original = List.copyOf(deck);

            Shuffle reverse = (currentDeck, random) -> Collections.reverse(currentDeck);

            var routine = new Routine("Test Routine", List.of(reverse, reverse));

            routine.execute(deck, TestRandoms.fixedRandom());

            assertThat(deck)
                    .as("Reversing twice in sequence should restore the original order, "
                            + "proving both operations acted on the same deck")
                    .isEqualTo(original);
        }

        @Test
        @DisplayName("Every operation should receive the same random generator instance")
        void operationsShouldShareTheSameRandomGenerator() {
            List<RandomGenerator> capturedRandoms = new ArrayList<>();

            Shuffle first = (deck, random) -> capturedRandoms.add(random);
            Shuffle second = (deck, random) -> capturedRandoms.add(random);

            var routine = new Routine("Test Routine", List.of(first, second));
            var suppliedRandom = TestRandoms.fixedRandom();

            routine.execute(DeckFactory.standardDeck(), suppliedRandom);

            assertThat(capturedRandoms)
                    .as("Every operation should receive the exact same random generator instance")
                    .allMatch(random -> random == suppliedRandom);
        }
    }

    @Nested
    @DisplayName("Defensive copying")
    class DefensiveCopying {

        @Test
        @DisplayName("Should not be affected by later changes to the supplied operations list")
        void shouldCopyTheSuppliedOperationsList() {
            List<Integer> callOrder = new ArrayList<>();
            Shuffle onlyOperation = (deck, random) -> callOrder.add(1);

            List<Shuffle> mutableOperations = new ArrayList<>(List.of(onlyOperation));
            var routine = new Routine("Test Routine", mutableOperations);

            mutableOperations.clear();

            routine.execute(DeckFactory.standardDeck(), TestRandoms.fixedRandom());

            assertThat(callOrder)
                    .as("Clearing the original list after construction should not affect the routine")
                    .containsExactly(1);
        }
    }

    @Nested
    @DisplayName("Behaviour with real shuffle operations")
    class BehaviourWithRealOperations {

        @Test
        @DisplayName("A routine combining real shuffle operations should preserve all cards")
        void shouldPreserveAllCardsWithRealOperations() {
            var deck = DeckFactory.standardDeck();
            var originalCards = new HashSet<>(deck);

            var routine = new Routine(
                    "Riffle then cut",
                    List.of(
                            ShuffleFactory.riffle(SkillLevel.EXPERT),
                            new DeckCutter(new BalancedDeckSplitter(1))::cut
                    )
            );

            routine.execute(deck, TestRandoms.fixedRandom());

            assertThat(deck).hasSize(52);
            assertThat(new HashSet<>(deck)).isEqualTo(originalCards);
        }

        @Test
        @DisplayName("A routine combining real shuffle operations should change the deck order")
        void shouldChangeDeckOrderWithRealOperations() {
            var deck = DeckFactory.standardDeck();
            var originalOrder = List.copyOf(deck);

            var routine = new Routine(
                    "Riffle then cut",
                    List.of(
                            ShuffleFactory.riffle(SkillLevel.EXPERT),
                            new DeckCutter(new BalancedDeckSplitter(1))::cut
                    )
            );

            routine.execute(deck, TestRandoms.fixedRandom());

            assertThat(deck)
                    .as("Combining real operations through a routine should reorder the deck")
                    .isNotEqualTo(originalOrder);
        }
    }

    @Nested
    @DisplayName("Validation")
    class Validation {

        @Test
        @DisplayName("Should reject a null list of operations")
        void shouldRejectNullOperations() {
            assertThatThrownBy(() -> new Routine("Test Routine", null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("operations must not be null");
        }


        @Test
        @DisplayName("Should reject a null name")
        void shouldRejectNullName() {
            Shuffle noop = (deck, random) -> {
            };

            assertThatThrownBy(() -> new Routine(null, List.of(noop)))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("name must not be null");
        }
    }


        @Nested
        @DisplayName("Identity")
        class Identity {

            @Test
            @DisplayName("Should expose the name it was created with")
            void shouldExposeItsName() {
                Shuffle noop = (deck, random) -> { };

                var routine = new Routine("Standard riffle shuffle", List.of(noop));

                assertThat(routine.name()).isEqualTo("Standard riffle shuffle");
            }

            @Test
            @DisplayName("Should use its name as its string representation")
            void shouldUseNameAsStringRepresentation() {
                Shuffle noop = (deck, random) -> { };

                var routine = new Routine("Standard riffle shuffle", List.of(noop));

                assertThat(routine.toString()).isEqualTo("Standard riffle shuffle");
            }
    }
}
