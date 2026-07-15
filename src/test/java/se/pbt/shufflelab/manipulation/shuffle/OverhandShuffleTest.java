package se.pbt.shufflelab.manipulation.shuffle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.TestRandoms;
import se.pbt.shufflelab.deck.DeckFactory;
import se.pbt.shufflelab.deck.card.Card;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.random.RandomGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Overhand shuffle")
class OverhandShuffleTest {

    @Nested
    @DisplayName("Shuffle behavior")
    class ShuffleBehavior {

        @Test
        @DisplayName("An overhand shuffle should keep every card in the deck")
        void shouldPreserveAllCards() {
            var deck = DeckFactory.standardDeck();
            var originalCards = new HashSet<>(deck);

            var shuffle = new OverhandShuffle(4);

            shuffle.shuffle(
                    deck,
                    TestRandoms.fixedRandom()
            );

            assertThat(deck).hasSize(52);
            assertThat(new HashSet<>(deck)).isEqualTo(originalCards);
        }

        @Test
        @DisplayName("An overhand shuffle should change the card order")
        void shouldChangeDeckOrder() {
            var deck = DeckFactory.standardDeck();
            var originalOrder = List.copyOf(deck);

            var shuffle = new OverhandShuffle(4);

            shuffle.shuffle(
                    deck,
                    TestRandoms.fixedRandom()
            );

            assertThat(deck).isNotEqualTo(originalOrder);
        }

        @Test
        @DisplayName("An overhand shuffle should produce the same result with the same random seed")
        void shouldProduceSameResultWithSameRandomSeed() {
            var firstDeck = DeckFactory.standardDeck();
            var secondDeck = DeckFactory.standardDeck();

            var firstShuffle = new OverhandShuffle(4);
            var secondShuffle = new OverhandShuffle(4);

            firstShuffle.shuffle(
                    firstDeck,
                    TestRandoms.seededRandom(42)
            );

            secondShuffle.shuffle(
                    secondDeck,
                    TestRandoms.seededRandom(42)
            );

            assertThat(firstDeck).isEqualTo(secondDeck);
        }

        @Test
        @DisplayName("An overhand shuffle with packet size one should reverse the deck")
        void shouldReverseDeckWhenMaximumPacketSizeIsOne() {
            var deck = DeckFactory.standardDeck();
            var expectedOrder = new ArrayList<>(deck);

            expectedOrder.reversed();

            var shuffle = new OverhandShuffle(1);

            shuffle.shuffle(
                    deck,
                    TestRandoms.fixedRandom()
            );

            assertThat(deck)
                    .containsExactlyElementsOf(expectedOrder.reversed());
        }

        @Test
        @DisplayName("An overhand shuffle should preserve the order within transferred packets")
        void shouldPreserveOrderWithinTransferredPackets() {
            var fullDeck = DeckFactory.standardDeck();
            var deck = new ArrayList<>(fullDeck.subList(0, 6));
            var originalOrder = List.copyOf(deck);

            var shuffle = new OverhandShuffle(3);

            var random = new PacketSizeSequenceRandom(
                    2,
                    3,
                    1
            );

            shuffle.shuffle(deck, random);

            assertThat(deck).containsExactly(
                    originalOrder.get(5),
                    originalOrder.get(2),
                    originalOrder.get(3),
                    originalOrder.get(4),
                    originalOrder.get(0),
                    originalOrder.get(1)
            );
        }

        @Test
        @DisplayName("An overhand shuffle should leave an empty deck unchanged")
        void shouldLeaveEmptyDeckUnchanged() {
            List<Card> deck = new ArrayList<>();

            var shuffle = new OverhandShuffle(4);

            shuffle.shuffle(
                    deck,
                    TestRandoms.fixedRandom()
            );

            assertThat(deck).isEmpty();
        }
    }

    @Nested
    @DisplayName("Validation")
    class Validation {

        @Test
        @DisplayName("An overhand shuffle should reject a maximum packet size below one")
        void shouldRejectMaximumPacketSizeBelowOne() {
            assertThatThrownBy(() -> new OverhandShuffle(0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("maxPacketSize must be at least 1");
        }

        @Test
        @DisplayName("An overhand shuffle should reject a null deck")
        void shouldRejectNullDeck() {
            var shuffle = new OverhandShuffle(4);

            assertThatThrownBy(() -> shuffle.shuffle(
                    null,
                    TestRandoms.fixedRandom()
            ))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("deck must not be null");
        }

        @Test
        @DisplayName("An overhand shuffle should reject a null random generator")
        void shouldRejectNullRandomGenerator() {
            var deck = DeckFactory.standardDeck();
            var shuffle = new OverhandShuffle(4);

            assertThatThrownBy(() -> shuffle.shuffle(deck, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("random must not be null");
        }
    }

    /**
     * A deterministic {@link RandomGenerator} that returns a predefined
     * sequence of packet sizes.
     *
     * <p>Used to make overhand shuffle behavior predictable so that packet
     * ordering can be verified precisely.</p>
     */
    private static final class PacketSizeSequenceRandom
            implements RandomGenerator {

        private final int[] packetSizes;
        private int currentIndex;

        private PacketSizeSequenceRandom(int... packetSizes) {
            this.packetSizes = packetSizes;
        }

        @Override
        public int nextInt(int origin, int bound) {
            if (currentIndex >= packetSizes.length) {
                throw new IllegalStateException(
                        "No configured packet size remains"
                );
            }

            int packetSize = packetSizes[currentIndex++];

            if (packetSize < origin || packetSize >= bound) {
                throw new IllegalArgumentException(
                        "Configured packet size "
                                + packetSize
                                + " is outside the requested range ["
                                + origin
                                + ", "
                                + bound
                                + ")"
                );
            }

            return packetSize;
        }

        @Override
        public long nextLong() {
            throw new UnsupportedOperationException(
                    "Only bounded integer generation is supported"
            );
        }
    }
}