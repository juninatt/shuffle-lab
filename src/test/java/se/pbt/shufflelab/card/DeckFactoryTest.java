package se.pbt.shufflelab.card;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Deck factory")
class DeckFactoryTest {

    @Test
    @DisplayName("A standard deck should contain 52 cards")
    void standardDeckShouldContain52Cards() {
        var deck = DeckFactory.standardDeck();

        assertThat(deck).hasSize(52);
    }

    @Test
    @DisplayName("A standard deck should contain 52 unique cards")
    void standardDeckShouldContain52UniqueCards() {
        var deck = DeckFactory.standardDeck();

        assertThat(new HashSet<>(deck)).hasSize(52);
    }

    @Test
    @DisplayName("A standard deck should be mutable")
    void standardDeckShouldReturnMutableDeck() {
        var deck = DeckFactory.standardDeck();

        deck.removeFirst();

        assertThat(deck).hasSize(51);
    }

    @Test
    @DisplayName("A standard deck should return a new deck instance each time")
    void standardDeckShouldReturnNewDeckEachTime() {
        var firstDeck = DeckFactory.standardDeck();
        var secondDeck = DeckFactory.standardDeck();

        assertThat(firstDeck).isNotSameAs(secondDeck);
        assertThat(firstDeck).isEqualTo(secondDeck);
    }

    @Test
    @DisplayName("A standard deck should contain all four suits")
    void standardDeckShouldContainAllSuits() {
        var deck = DeckFactory.standardDeck();

        assertThat(
                deck.stream()
                        .map(Card::suit)
                        .distinct()
        ).hasSize(4);
    }

    @Test
    @DisplayName("A standard deck should contain all thirteen ranks for each suit")
    void standardDeckShouldContainAllRanksForEachSuit() {
        var deck = DeckFactory.standardDeck();

        for (Suit suit : Suit.values()) {
            long count = deck.stream()
                    .filter(card -> card.suit() == suit)
                    .count();

            assertThat(count).isEqualTo(13);
        }
    }
}