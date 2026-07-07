package se.pbt.shufflelab.manipulation.shuffle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.TestRandoms;
import se.pbt.shufflelab.deck.DeckFactory;
import se.pbt.shufflelab.skill.SkillLevel;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Shuffle factory")
class ShuffleFactoryTest {


    @Nested
    @DisplayName("Creation")
    class Creation {

        @Test
        @DisplayName("Should create a riffle shuffle")
        void shouldCreateRiffleShuffle() {
            Shuffle shuffle = ShuffleFactory.riffle(SkillLevel.EXPERT);

            assertThat(shuffle)
                    .as("Factory should return a configured shuffle")
                    .isNotNull();
        }
    }


    @Nested
    @DisplayName("Shuffle behaviour")
    class ShuffleBehaviour {

        @Test
        @DisplayName("A factory-created riffle shuffle should preserve all cards")
        void factoryCreatedRiffleShuffleShouldPreserveAllCards() {
            var deck = DeckFactory.standardDeck();
            var originalCards = new HashSet<>(deck);

            Shuffle shuffle = ShuffleFactory.riffle(SkillLevel.EXPERT);

            shuffle.shuffle(deck, TestRandoms.fixedRandom());

            assertThat(deck).hasSize(52);
            assertThat(new HashSet<>(deck)).isEqualTo(originalCards);
        }

        @Test
        @DisplayName("A factory-created riffle shuffle should change the deck order")
        void factoryCreatedRiffleShuffleShouldChangeDeckOrder() {
            var deck = DeckFactory.standardDeck();
            var originalOrder = List.copyOf(deck);

            Shuffle shuffle = ShuffleFactory.riffle(SkillLevel.EXPERT);

            shuffle.shuffle(deck, TestRandoms.fixedRandom());

            assertThat(deck)
                    .as("A riffle shuffle should reorder the deck")
                    .isNotEqualTo(originalOrder);
        }

        @Test
        @DisplayName("A factory-created riffle shuffle should be deterministic with the same random seed")
        void factoryCreatedRiffleShuffleShouldBeDeterministicWithSameRandomSeed() {
            var firstDeck = DeckFactory.standardDeck();
            var secondDeck = DeckFactory.standardDeck();

            Shuffle firstShuffle = ShuffleFactory.riffle(SkillLevel.INTERMEDIATE);
            Shuffle secondShuffle = ShuffleFactory.riffle(SkillLevel.INTERMEDIATE);

            firstShuffle.shuffle(firstDeck, TestRandoms.seededRandom(42));
            secondShuffle.shuffle(secondDeck, TestRandoms.seededRandom(42));

            assertThat(firstDeck)
                    .as("The same skill level and same random seed should produce the same result")
                    .isEqualTo(secondDeck);
        }
    }


    @Nested
    @DisplayName("Validation")
    class Validation {

        @Test
        @DisplayName("Should reject null skill level")
        void shouldRejectNullSkillLevel() {
            assertThatThrownBy(() -> ShuffleFactory.riffle(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("skillLevel must not be null");
        }
    }
}