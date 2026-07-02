package se.pbt.shufflelab.deck.card;

import java.util.Objects;

/**
 * Represents a single immutable playing card.
 *
 * <p>A card consists of a suit and a rank and cannot be modified after
 * creation.</p>
 */
public record Card(Suit suit, Rank rank) {

    public Card {
        Objects.requireNonNull(suit, "suit must not be null");
        Objects.requireNonNull(rank, "rank must not be null");
    }
}
