package se.pbt.shufflelab.manipulation.routine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.TestRandoms;
import se.pbt.shufflelab.factory.DeckFactory;
import se.pbt.shufflelab.manipulation.shuffle.Shuffle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.random.RandomGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Sequential routine")
class SequentialRoutineTest {

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

            var routine = new SequentialRoutine(List.of(first, second, third));

            routine.execute(DeckFactory.standardDeck(), TestRandoms.fixedRandom());

            assertThat(callOrder).containsExactly(1, 2, 3);
        }

        @Test
        @DisplayName("Should run a single operation")
        void shouldRunASingleOperation() {
            List<Integer> callOrder = new ArrayList<>();

            Shuffle onlyOperation = (deck, random) -> callOrder.add(1);

            var routine = new SequentialRoutine(List.of(onlyOperation));

            routine.execute(DeckFactory.standardDeck(), TestRandoms.fixedRandom());

            assertThat(callOrder).containsExactly(1);
        }

        @Test
        @DisplayName("Should do nothing for an empty list of operations")
        void shouldDoNothingForEmptyOperations() {
            var deck = DeckFactory.standardDeck();
            var original = List.copyOf(deck);

            var routine = new SequentialRoutine(List.of());

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

            var routine = new SequentialRoutine(List.of(reverse, reverse));

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

            var routine = new SequentialRoutine(List.of(first, second));
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
            var routine = new SequentialRoutine(mutableOperations);

            mutableOperations.clear();

            routine.execute(DeckFactory.standardDeck(), TestRandoms.fixedRandom());

            assertThat(callOrder)
                    .as("Clearing the original list after construction should not affect the routine")
                    .containsExactly(1);
        }
    }

    @Nested
    @DisplayName("Validation")
    class Validation {

        @Test
        @DisplayName("Should reject a null list of operations")
        void shouldRejectNullOperations() {
            assertThatThrownBy(() -> new SequentialRoutine(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("operations must not be null");
        }
    }
}