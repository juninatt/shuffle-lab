package se.pbt.shufflelab.skill;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Skill profile")
class SkillProfileTest {


    @Nested
    @DisplayName("Predefined profiles")
    class PredefinedProfiles {

        @Test
        @DisplayName("Should create novice profile")
        void shouldCreateNoviceProfile() {
            SkillProfile profile = SkillProfile.forLevel(SkillLevel.NOVICE);

            assertThat(profile.maxDropSize()).isEqualTo(8);
            assertThat(profile.splitTolerance()).isEqualTo(6);
        }

        @Test
        @DisplayName("Should create intermediate profile")
        void shouldCreateIntermediateProfile() {
            SkillProfile profile = SkillProfile.forLevel(SkillLevel.INTERMEDIATE);

            assertThat(profile.maxDropSize()).isEqualTo(4);
            assertThat(profile.splitTolerance()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should create expert profile")
        void shouldCreateExpertProfile() {
            SkillProfile profile = SkillProfile.forLevel(SkillLevel.EXPERT);

            assertThat(profile.maxDropSize()).isEqualTo(2);
            assertThat(profile.splitTolerance()).isEqualTo(1);
        }
    }


    @Nested
    @DisplayName("Custom profiles")
    class CustomProfiles {

        @Test
        @DisplayName("Should allow custom valid profile")
        void shouldAllowCustomValidProfile() {
            SkillProfile profile = new SkillProfile(5, 2);

            assertThat(profile.maxDropSize()).isEqualTo(5);
            assertThat(profile.splitTolerance()).isEqualTo(2);
        }
    }


    @Nested
    @DisplayName("Validation")
    class Validation {

        @Test
        @DisplayName("Should reject null skill level")
        void shouldRejectNullSkillLevel() {
            assertThatThrownBy(() -> SkillProfile.forLevel(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("level must not be null");
        }

        @Test
        @DisplayName("Should reject max drop size below one")
        void shouldRejectMaxDropSizeBelowOne() {
            assertThatThrownBy(() -> new SkillProfile(0, 2))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("maxDropSize must be at least 1");
        }

        @Test
        @DisplayName("Should reject negative split tolerance")
        void shouldRejectNegativeSplitTolerance() {
            assertThatThrownBy(() -> new SkillProfile(2, -1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("splitTolerance must not be negative");
        }
    }
}