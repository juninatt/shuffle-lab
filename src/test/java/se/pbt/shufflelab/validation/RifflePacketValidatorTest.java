package se.pbt.shufflelab.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.deck.card.Card;
import se.pbt.shufflelab.deck.DeckFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Riffle packet validator")
class RifflePacketValidatorTest {

    @Test
    @DisplayName("Should accept evenly sized packets")
    void shouldAcceptBalancedPackets() {
        var deck = DeckFactory.standardDeck();

        var top = deck.subList(0, 26);
        var bottom = deck.subList(26, 52);

        assertThatCode(() ->
                RifflePacketValidator.validate(top, bottom, 0.10)
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should accept packets within the allowed imbalance")
    void shouldAcceptPacketsWithinImbalanceLimit() {
        var deck = DeckFactory.standardDeck();

        var top = deck.subList(0, 24);
        var bottom = deck.subList(24, 52);

        assertThatCode(() ->
                RifflePacketValidator.validate(top, bottom, 0.10)
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should reject an empty left packet")
    void shouldRejectEmptyLeftPacket() {
        var deck = DeckFactory.standardDeck();

        var top = List.<Card>of();
        var bottom = deck.subList(0, 26);

        assertThatThrownBy(() ->
                RifflePacketValidator.validate(top, bottom, 0.10)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A riffle shuffle requires two non-empty packets");
    }

    @Test
    @DisplayName("Should reject an empty right packet")
    void shouldRejectEmptyRightPacket() {
        var deck = DeckFactory.standardDeck();

        var top = deck.subList(0, 26);
        var bottom = List.<Card>of();

        assertThatThrownBy(() ->
                RifflePacketValidator.validate(top, bottom, 0.10)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A riffle shuffle requires two non-empty packets");
    }

    @Test
    @DisplayName("Should reject packets with too much imbalance")
    void shouldRejectPacketsWithTooMuchImbalance() {
        var deck = DeckFactory.standardDeck();

        var top = deck.subList(0, 20);
        var bottom = deck.subList(20, 52);

        assertThatThrownBy(() ->
                RifflePacketValidator.validate(top, bottom, 0.10)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Packet imbalance is too large for this riffle shuffle");
    }

    @Test
    @DisplayName("Should reject two empty packets")
    void shouldRejectTwoEmptyPackets() {
        var top = List.<Card>of();
        var bottom = List.<Card>of();

        assertThatThrownBy(() ->
                RifflePacketValidator.validate(top, bottom, 0.10)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A riffle shuffle requires two non-empty packets");
    }
}