package se.pbt.shufflelab.card;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CardTest {

    @Test
    void shouldNotAllowNullSuit() {
        assertThatThrownBy(() -> new Card(null, Rank.ACE))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldNotAllowNullRank() {
        assertThatThrownBy(() -> new Card(Suit.SPADES, null))
                .isInstanceOf(NullPointerException.class);
    }
}