package se.pbt.shufflelab.deck;

/**
 * Defines supported starting orders for newly created standard decks.
 */
public enum DeckOrder {

    /**
     * A simple new-deck order grouped by suit, using ascending ranks.
     */
    NEW_DECK,

    /**
     * A casino-style inspection order, grouped by suit to make deck checks easy.
     */
    CASINO_INSPECTION,

    /**
     * A bridge-style order, grouped by suit with ranks ordered from ace down to two.
     */
    BRIDGE
}