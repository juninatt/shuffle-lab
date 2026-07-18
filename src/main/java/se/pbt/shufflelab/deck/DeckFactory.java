package se.pbt.shufflelab.deck;

import se.pbt.shufflelab.deck.card.Card;
import se.pbt.shufflelab.deck.card.Rank;
import se.pbt.shufflelab.deck.card.Suit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Factory for creating standard playing-card decks.
 */
public final class DeckFactory {

    private static final List<Rank> ASCENDING_RANKS = List.of(
            Rank.ACE, Rank.TWO, Rank.THREE, Rank.FOUR, Rank.FIVE,
            Rank.SIX, Rank.SEVEN, Rank.EIGHT, Rank.NINE, Rank.TEN,
            Rank.JACK, Rank.QUEEN, Rank.KING
    );

    private static final List<Rank> DESCENDING_RANKS = List.of(
            Rank.ACE, Rank.KING, Rank.QUEEN, Rank.JACK, Rank.TEN,
            Rank.NINE, Rank.EIGHT, Rank.SEVEN, Rank.SIX, Rank.FIVE,
            Rank.FOUR, Rank.THREE, Rank.TWO
    );

    private DeckFactory() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Creates a new standard 52-card deck in new-deck order.
     *
     * @return a mutable standard deck
     */
    public static Deck standardDeck() {
        return create(DeckOrder.NEW_DECK);
    }

    /**
     * Creates a new standard 52-card deck in the given starting order.
     *
     * @param deckOrder the starting order to use
     * @return a mutable standard deck
     * @throws NullPointerException if deckOrder is null
     */
    public static Deck create(DeckOrder deckOrder) {
        Objects.requireNonNull(deckOrder, "deckOrder must not be null");

        return switch (deckOrder) {
            case NEW_DECK -> createDeck(
                    List.of(
                            Suit.CLUBS,
                            Suit.DIAMONDS,
                            Suit.HEARTS,
                            Suit.SPADES
                    ),
                    ASCENDING_RANKS
            );

            case CASINO_INSPECTION -> createDeck(
                    List.of(
                            Suit.SPADES,
                            Suit.HEARTS,
                            Suit.DIAMONDS,
                            Suit.CLUBS
                    ),
                    ASCENDING_RANKS
            );

            case BRIDGE -> createDeck(
                    List.of(
                            Suit.SPADES,
                            Suit.HEARTS,
                            Suit.DIAMONDS,
                            Suit.CLUBS
                    ),
                    DESCENDING_RANKS
            );
        };
    }

    /**
     * Creates a deck by combining each suit with each rank in the supplied order.
     *
     * @param suits the suit order to use
     * @param ranks the rank order to use within each suit
     * @return a mutable deck in the requested order
     */
    private static Deck createDeck(
            List<Suit> suits,
            List<Rank> ranks) {

        List<Card> cards =
                new ArrayList<>(suits.size() * ranks.size());

        for (Suit suit : suits) {
            for (Rank rank : ranks) {
                cards.add(new Card(suit, rank));
            }
        }

        return new Deck(cards);
    }
}