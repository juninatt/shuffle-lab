package se.pbt.shufflelab.operation.riffle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.card.Card;
import se.pbt.shufflelab.card.DeckFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Riffle packet validator")
class RifflePacketValidatorTest {

    @Test
    @DisplayName("Should accept evenly sized packets")
    void shouldAcceptBalancedPackets() {
        var deck = DeckFactory.standardDeck();

        var left = deck.subList(0, 26);
        var right = deck.subList(26, 52);

        assertThatCode(() ->
                RifflePacketValidator.validate(left, right, 0.10)
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should accept packets within the allowed imbalance")
    void shouldAcceptPacketsWithinImbalanceLimit() {
        var deck = DeckFactory.standardDeck();

        var left = deck.subList(0, 24);
        var right = deck.subList(24, 52);

        assertThatCode(() ->
                RifflePacketValidator.validate(left, right, 0.10)
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should reject an empty left packet")
    void shouldRejectEmptyLeftPacket() {
        var deck = DeckFactory.standardDeck();

        var left = List.<Card>of();
        var right = deck.subList(0, 26);

        assertThatThrownBy(() ->
                RifflePacketValidator.validate(left, right, 0.10)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A riffle shuffle requires two non-empty packets");
    }

    @Test
    @DisplayName("Should reject an empty right packet")
    void shouldRejectEmptyRightPacket() {
        var deck = DeckFactory.standardDeck();

        var left = deck.subList(0, 26);
        var right = List.<Card>of();

        assertThatThrownBy(() ->
                RifflePacketValidator.validate(left, right, 0.10)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A riffle shuffle requires two non-empty packets");
    }

    @Test
    @DisplayName("Should reject packets with too much imbalance")
    void shouldRejectPacketsWithTooMuchImbalance() {
        var deck = DeckFactory.standardDeck();

        var left = deck.subList(0, 20);
        var right = deck.subList(20, 52);

        assertThatThrownBy(() ->
                RifflePacketValidator.validate(left, right, 0.10)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Packet imbalance is too large for this riffle shuffle");
    }
}