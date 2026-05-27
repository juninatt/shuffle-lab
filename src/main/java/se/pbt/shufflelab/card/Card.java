package se.pbt.shufflelab.card;

import java.util.Objects;

/**
 * Represents a single immutable playing card.
 */
public record Card(Suit suit, Rank rank) {

    public Card {
        Objects.requireNonNull(suit, "suit must not be null");
        Objects.requireNonNull(rank, "rank must not be null");
    }
}
