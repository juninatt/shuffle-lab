package se.pbt.shufflelab.routine;

import se.pbt.shufflelab.card.Card;
import se.pbt.shufflelab.shuffle.Shuffle;

import java.util.List;
import java.util.random.RandomGenerator;

/**
 * A shuffle strategy composed of multiple shuffle operations.
 *
 * <p>This strategy acts as a recipe, executing a sequence of
 * {@link Shuffle}s in the order they were defined.
 * It can be used to model real-world shuffling techniques,
 * such as repeatedly cutting and riffle shuffling a deck.</p>
 */
public class CompositeShuffleRoutine implements ShuffleRoutine {

    private final String name;
    private final List<Shuffle> operations;

    /**
     * Creates a new shuffle strategy consisting of the given operations.
     *
     * @param name a human-readable name describing the strategy
     * @param operations the operations that make up the strategy
     */
    public CompositeShuffleRoutine(
            String name,
            List<Shuffle> operations) {

        this.name = name;
        this.operations = List.copyOf(operations);
    }

    /**
     * Applies each operation in sequence to the given deck.
     *
     * @param deck the deck to shuffle
     * @param random a source of controlled randomness
     */
    @Override
    public void shuffle(List<Card> deck, RandomGenerator random) {
        for (Shuffle operation : operations) {
            operation.apply(deck, random);
        }
    }

    @Override
    public String name() {
        return name;
    }
}