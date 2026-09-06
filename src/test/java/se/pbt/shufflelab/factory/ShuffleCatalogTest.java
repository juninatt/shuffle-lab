package se.pbt.shufflelab.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.TestRandoms;
import se.pbt.shufflelab.handling.routine.RoutineProtocol;
import se.pbt.shufflelab.skill.SkillLevel;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Shuffle catalog")
class ShuffleCatalogTest {

    @Nested
    @DisplayName("Creation")
    class Creation {

        @Test
        @DisplayName("Every catalog entry should create a shuffle for every skill level")
        void everyEntryShouldCreateAShuffleForEverySkillLevel() {
            for (ShuffleCatalog entry : ShuffleCatalog.values()) {
                for (SkillLevel skillLevel : SkillLevel.values()) {
                    RoutineProtocol shuffle = entry.create(skillLevel);

                    assertThat(shuffle)
                            .as("%s should create a shuffle for %s", entry, skillLevel)
                            .isNotNull();
                }
            }
        }

        @Test
        @DisplayName("Should reject a null skill level")
        void shouldRejectNullSkillLevel() {
            assertThatThrownBy(() -> ShuffleCatalog.RIFFLE.create(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("skillLevel must not be null");
        }
    }

    @Nested
    @DisplayName("Metadata")
    class Metadata {

        @Test
        @DisplayName("Every entry should have a non-blank display name and description")
        void everyEntryShouldHaveMetadata() {
            for (ShuffleCatalog entry : ShuffleCatalog.values()) {
                assertThat(entry.displayName()).as("%s displayName", entry).isNotBlank();
                assertThat(entry.description()).as("%s description", entry).isNotBlank();
            }
        }

        @Test
        @DisplayName("Every entry should have a unique display name")
        void everyEntryShouldHaveAUniqueDisplayName() {
            List<String> names = Arrays.stream(ShuffleCatalog.values())
                    .map(ShuffleCatalog::displayName)
                    .toList();

            assertThat(names)
                    .as("Duplicate display names would make report rows indistinguishable")
                    .doesNotHaveDuplicates();
        }
    }

    @Nested
    @DisplayName("Determinism")
    class Determinism {

        @Test
        @DisplayName("A deterministic shuffle should produce the same result for any random generator")
        void deterministicShufflesShouldIgnoreRandomness() {
            var firstDeck = DeckFactory.standardDeck();
            var secondDeck = DeckFactory.standardDeck();

            ShuffleCatalog.PILE.create(SkillLevel.EXPERT)
                    .execute(firstDeck, TestRandoms.fixedRandom());
            ShuffleCatalog.PILE.create(SkillLevel.EXPERT)
                    .execute(secondDeck, TestRandoms.seededRandom(999));

            assertThat(firstDeck).isEqualTo(secondDeck);
        }
    }
}
