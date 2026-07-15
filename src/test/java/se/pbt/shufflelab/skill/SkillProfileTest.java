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
            SkillProfile profile = SkillProfile.withLevel(SkillLevel.NOVICE);

            assertThat(profile.maxInterleavePacketSize()).isEqualTo(8);
            assertThat(profile.maxSplitDeviation()).isEqualTo(6);
            assertThat(profile.maxOverhandPacketSize()).isEqualTo(8);
        }

        @Test
        @DisplayName("Should create intermediate profile")
        void shouldCreateIntermediateProfile() {
            SkillProfile profile = SkillProfile.withLevel(SkillLevel.INTERMEDIATE);

            assertThat(profile.maxInterleavePacketSize()).isEqualTo(4);
            assertThat(profile.maxSplitDeviation()).isEqualTo(3);
            assertThat(profile.maxOverhandPacketSize()).isEqualTo(4);
        }

        @Test
        @DisplayName("Should create expert profile")
        void shouldCreateExpertProfile() {
            SkillProfile profile = SkillProfile.withLevel(SkillLevel.EXPERT);

            assertThat(profile.maxInterleavePacketSize()).isEqualTo(2);
            assertThat(profile.maxSplitDeviation()).isEqualTo(1);
            assertThat(profile.maxOverhandPacketSize()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Custom profiles")
    class CustomProfiles {

        @Test
        @DisplayName("Should allow a custom valid profile")
        void shouldAllowCustomValidProfile() {
            SkillProfile profile = new SkillProfile(
                    5,
                    2,
                    6
            );

            assertThat(profile.maxInterleavePacketSize()).isEqualTo(5);
            assertThat(profile.maxSplitDeviation()).isEqualTo(2);
            assertThat(profile.maxOverhandPacketSize()).isEqualTo(6);
        }

        @Test
        @DisplayName("Custom profile should expose all supplied values")
        void shouldExposeAllSuppliedValues() {
            SkillProfile profile = new SkillProfile(
                    3,
                    4,
                    7
            );

            assertThat(profile)
                    .extracting(
                            SkillProfile::maxInterleavePacketSize,
                            SkillProfile::maxSplitDeviation,
                            SkillProfile::maxOverhandPacketSize
                    )
                    .containsExactly(3, 4, 7);
        }
    }

    @Nested
    @DisplayName("Validation")
    class Validation {

        @Test
        @DisplayName("Should reject null skill level")
        void shouldRejectNullSkillLevel() {
            assertThatThrownBy(() -> SkillProfile.withLevel(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("level must not be null");
        }

        @Test
        @DisplayName("Should reject an interleave packet size below one")
        void shouldRejectInterleavePacketSizeBelowOne() {
            assertThatThrownBy(() -> new SkillProfile(
                    0,
                    2,
                    4
            ))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("maxInterleavePacketSize must be at least 1");
        }

        @Test
        @DisplayName("Should reject a negative split deviation")
        void shouldRejectNegativeSplitDeviation() {
            assertThatThrownBy(() -> new SkillProfile(
                    2,
                    -1,
                    4
            ))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("maxSplitDeviation must not be negative");
        }

        @Test
        @DisplayName("Should reject an overhand packet size below one")
        void shouldRejectOverhandPacketSizeBelowOne() {
            assertThatThrownBy(() -> new SkillProfile(
                    2,
                    1,
                    0
            ))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("maxOverhandPacketSize must be at least 1");
        }

        @Test
        @DisplayName("Should reject a negative interleave packet size")
        void shouldRejectNegativeInterleavePacketSize() {
            assertThatThrownBy(() -> new SkillProfile(
                    -1,
                    2,
                    4
            ))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("maxInterleavePacketSize must be at least 1");
        }

        @Test
        @DisplayName("Should reject a negative overhand packet size")
        void shouldRejectNegativeOverhandPacketSize() {
            assertThatThrownBy(() -> new SkillProfile(
                    2,
                    1,
                    -1
            ))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("maxOverhandPacketSize must be at least 1");
        }

        @Test
        @DisplayName("Should allow zero split deviation")
        void shouldAllowZeroSplitDeviation() {
            SkillProfile profile = new SkillProfile(
                    2,
                    0,
                    2
            );

            assertThat(profile.maxSplitDeviation()).isZero();
        }
    }
}