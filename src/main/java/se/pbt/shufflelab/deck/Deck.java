package se.pbt.shufflelab.deck;

import se.pbt.shufflelab.deck.card.Card;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

/**
 * Represents a mutable deck of playing cards in a defined order.
 *
 * <p>The deck stores its own copy of the supplied card collection and
 * supports the standard indexed operations provided by {@link List}.
 * Duplicate cards are permitted, but {@code null} cards are not.</p>
 */
public final class Deck extends AbstractList<Card> implements RandomAccess {

    private final List<Card> cards;

    /**
     * Creates a deck containing the supplied cards in their current order.
     *
     * <p>Subsequent changes to the supplied collection do not affect the
     * created deck.</p>
     *
     * @param cards the cards to include
     * @throws NullPointerException if {@code cards} or any contained card
     *                              is {@code null}
     */
    public Deck(Collection<? extends Card> cards) {
        this.cards = validatedCopyOf(cards);
    }

    /**
     * Creates a deck containing the supplied cards in the given order.
     *
     * @param cards the cards to include
     * @return a new deck containing the supplied cards
     * @throws NullPointerException if {@code cards} or any contained card
     *                              is {@code null}
     */
    public static Deck of(Card... cards) {
        Objects.requireNonNull(cards, "cards must not be null");
        return new Deck(Arrays.asList(cards));
    }

    /**
     * Returns an immutable snapshot of the current card order.
     *
     * <p>Subsequent changes to this deck do not affect the returned list.</p>
     *
     * @return an immutable snapshot of the cards
     */
    public List<Card> cards() {
        return List.copyOf(cards);
    }

    /**
     * Returns the card at the supplied position.
     *
     * @param index the card position
     * @return the card at the supplied position
     * @throws IndexOutOfBoundsException if the index is outside the deck
     */
    @Override
    public Card get(int index) {
        return cards.get(index);
    }

    /**
     * Returns the number of cards in the deck.
     *
     * @return the number of cards
     */
    @Override
    public int size() {
        return cards.size();
    }

    /**
     * Replaces the card at the supplied position.
     *
     * @param index the card position
     * @param card the replacement card
     * @return the previously stored card
     * @throws NullPointerException if {@code card} is {@code null}
     * @throws IndexOutOfBoundsException if the index is outside the deck
     */
    @Override
    public Card set(int index, Card card) {
        return cards.set(
                index,
                Objects.requireNonNull(card, "card must not be null")
        );
    }

    /**
     * Inserts a card at the supplied position.
     *
     * @param index the insertion position
     * @param card the card to insert
     * @throws NullPointerException if {@code card} is {@code null}
     * @throws IndexOutOfBoundsException if the index is outside the valid
     *                                   insertion range
     */
    @Override
    public void add(int index, Card card) {
        cards.add(
                index,
                Objects.requireNonNull(card, "card must not be null")
        );
        modCount++;
    }

    /**
     * Removes and returns the card at the supplied position.
     *
     * @param index the card position
     * @return the removed card
     * @throws IndexOutOfBoundsException if the index is outside the deck
     */
    @Override
    public Card remove(int index) {
        Card removedCard = cards.remove(index);
        modCount++;
        return removedCard;
    }

    /**
     * Adds the supplied cards to the end of the deck.
     *
     * @param cards the cards to add
     * @return {@code true} if the deck changed
     * @throws NullPointerException if {@code cards} or any contained card
     *                              is {@code null}
     */
    @Override
    public boolean addAll(Collection<? extends Card> cards) {
        List<Card> validatedCards = validatedCopyOf(cards);

        boolean changed = this.cards.addAll(validatedCards);

        if (changed) {
            modCount++;
        }

        return changed;
    }

    /**
     * Inserts the supplied cards at the given position.
     *
     * @param index the insertion position
     * @param cards the cards to insert
     * @return {@code true} if the deck changed
     * @throws NullPointerException if {@code cards} or any contained card
     *                              is {@code null}
     * @throws IndexOutOfBoundsException if the index is outside the valid
     *                                   insertion range
     */
    @Override
    public boolean addAll(
            int index,
            Collection<? extends Card> cards) {

        List<Card> validatedCards = validatedCopyOf(cards);

        boolean changed = this.cards.addAll(index, validatedCards);

        if (changed) {
            modCount++;
        }

        return changed;
    }

    /**
     * Removes all cards from the deck.
     */
    @Override
    public void clear() {
        if (!cards.isEmpty()) {
            cards.clear();
            modCount++;
        }
    }

    /**
     * Creates a validated mutable copy of a card collection.
     *
     * @param cards the cards to validate and copy
     * @return a mutable copy of the supplied cards
     * @throws NullPointerException if {@code cards} or any contained card
     *                              is {@code null}
     */
    private static List<Card> validatedCopyOf(
            Collection<? extends Card> cards) {

        Objects.requireNonNull(cards, "cards must not be null");

        List<Card> validatedCards = new ArrayList<>(cards.size());

        for (Card card : cards) {
            validatedCards.add(
                    Objects.requireNonNull(
                            card,
                            "deck must not contain null cards"
                    )
            );
        }

        return validatedCards;
    }
}