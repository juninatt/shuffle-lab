package se.pbt.shufflelab.card;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Card")
class CardTest {

    @Test
    @DisplayName("A card should require a suit")
    void shouldNotAllowNullSuit() {
        assertThatThrownBy(() -> new Card(null, Rank.ACE))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("A card should require a rank")
    void shouldNotAllowNullRank() {
        assertThatThrownBy(() -> new Card(Suit.SPADES, null))
                .isInstanceOf(NullPointerException.class);
    }
}