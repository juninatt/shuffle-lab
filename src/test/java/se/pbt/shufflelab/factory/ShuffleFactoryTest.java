package se.pbt.shufflelab.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.handling.shuffle.Shuffle;
import se.pbt.shufflelab.skill.SkillLevel;

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

        @Test
        @DisplayName("Should create an overhand shuffle")
        void shouldCreateOverhandShuffle() {
            Shuffle shuffle = ShuffleFactory.overhand(SkillLevel.EXPERT);

            assertThat(shuffle).isNotNull();
        }
    }


    @Nested
    @DisplayName("Validation")
    class Validation {

        @Test
        @DisplayName("Should reject null skill level when creating a riffle shuffle")
        void shouldRejectNullSkillLevelForRiffleShuffle() {
            assertThatThrownBy(() -> ShuffleFactory.riffle(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("skillLevel must not be null");
        }
    }

    @Test
    @DisplayName("Should reject null skill level when creating an overhand shuffle")
    void shouldRejectNullSkillLevelForOverhandShuffle() {
        assertThatThrownBy(() -> ShuffleFactory.overhand(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("skillLevel must not be null");
    }
}
