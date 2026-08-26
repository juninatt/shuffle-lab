package se.pbt.shufflelab.handling.shuffle;

import se.pbt.shufflelab.deck.Deck;
import se.pbt.shufflelab.deck.card.Card;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.random.RandomGenerator;

/**
 * Shuffles a deck using the Mongean shuffle technique.
 *
 * <p>Cards are removed one at a time from the top of the original deck.
 * The first card starts a new receiving pile, after which each subsequent
 * card is placed alternately on the top and bottom of that pile.</p>
 *
 * <p>The {@link MongeanStart} configuration determines whether the second
 * card is placed on the top or bottom of the receiving pile. This allows
 * both common Mongean shuffle variants to be represented by the same
 * implementation.</p>
 *
 * <p>A Mongean shuffle is deterministic. For a given starting deck and
 * placement configuration, it always produces the same result.</p>
 */
public final class MongeanShuffle implements Shuffle {

    private final MongeanStart start;

    /**
     * Creates a Mongean shuffle that places the second card on top of the
     * receiving pile.
     */
    public MongeanShuffle() {
        this(MongeanStart.TOP);
    }

    /**
     * Creates a Mongean shuffle with the supplied placement start.
     *
     * @param start determines whether the second card is placed on the top
     *              or bottom of the receiving pile
     * @throws NullPointerException if {@code start} is {@code null}
     */
    public MongeanShuffle(MongeanStart start) {
        this.start = Objects.requireNonNull(
                start,
                "start must not be null"
        );
    }

    /**
     * Performs a Mongean shuffle on the given deck.
     *
     * <p>The first card starts the receiving pile. The remaining cards are
     * then placed alternately on its top and bottom according to the
     * configured {@link MongeanStart}.</p>
     *
     * <p>The supplied random generator is not used because this shuffle is
     * deterministic. It remains part of the method signature to satisfy the
     * common {@link Shuffle} contract.</p>
     *
     * @param deck the deck to shuffle
     * @param random a source of controlled randomness
     * @throws NullPointerException if {@code deck} or {@code random} is
     *                              {@code null}
     */
    @Override
    public void shuffle(Deck deck, RandomGenerator random) {
        Objects.requireNonNull(deck, "deck must not be null");
        Objects.requireNonNull(random, "random must not be null");

        ArrayDeque<Card> shuffledDeck = new ArrayDeque<>(deck.size());

        for (int cardIndex = 0; cardIndex < deck.size(); cardIndex++) {
            Card card = deck.get(cardIndex);

            if (cardIndex == 0) {
                shuffledDeck.add(card);
            } else if (shouldPlaceOnTop(cardIndex)) {
                shuffledDeck.addFirst(card);
            } else {
                shuffledDeck.addLast(card);
            }
        }

        deck.clear();
        deck.addAll(shuffledDeck);
    }

    /**
     * Determines whether the card at the supplied index should be placed
     * on top of the receiving pile.
     *
     * @param cardIndex the zero-based index of the card in the source deck
     * @return {@code true} when the card should be placed on top
     */
    private boolean shouldPlaceOnTop(int cardIndex) {
        return switch (start) {
            case TOP -> cardIndex % 2 == 1;
            case BOTTOM -> cardIndex % 2 == 0;
        };
    }
}
