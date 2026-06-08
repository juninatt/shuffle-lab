package se.pbt.shufflelab.card;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

class DeckFactoryTest {

    @Test
    void standardDeckShouldContain52Cards() {
        var deck = DeckFactory.standardDeck();

        assertThat(deck).hasSize(52);
    }

    @Test
    void standardDeckShouldContain52UniqueCards() {
        var deck = DeckFactory.standardDeck();

        assertThat(new HashSet<>(deck)).hasSize(52);
    }

    @Test
    void standardDeckShouldReturnMutableDeck() {
        var deck = DeckFactory.standardDeck();

        deck.removeFirst();

        assertThat(deck).hasSize(51);
    }

    @Test
    void standardDeckShouldReturnNewDeckEachTime() {
        var firstDeck = DeckFactory.standardDeck();
        var secondDeck = DeckFactory.standardDeck();

        assertThat(firstDeck).isNotSameAs(secondDeck);
        assertThat(firstDeck).isEqualTo(secondDeck);
    }

    @Test
    void standardDeckShouldContainAllSuits() {
        var deck = DeckFactory.standardDeck();

        assertThat(
                deck.stream()
                        .map(Card::suit)
                        .distinct()
        ).hasSize(4);
    }

    @Test
    void shouldContainAllRanksForEachSuit() {
        var deck = DeckFactory.standardDeck();

        for (Suit suit : Suit.values()) {
            long count = deck.stream()
                    .filter(card -> card.suit() == suit)
                    .count();

            assertThat(count).isEqualTo(13);
        }
    }
}