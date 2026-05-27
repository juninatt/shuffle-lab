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
}