package se.pbt.shufflelab.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.shufflelab.TestRandoms;
import se.pbt.shufflelab.handling.routine.RoutineProtocol;
import se.pbt.shufflelab.skill.SkillLevel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Routine catalog")
class RoutineCatalogTest {

    @Nested
    @DisplayName("Creation")
    class Creation {

        @Test
        @DisplayName("Every catalog entry should create a routine for every skill level")
        void everyEntryShouldCreateARoutineForEverySkillLevel() {
            for (RoutineCatalog entry : RoutineCatalog.values()) {
                for (SkillLevel skillLevel : SkillLevel.values()) {
                    RoutineProtocol routine = entry.create(skillLevel);

                    assertThat(routine)
                            .as("%s should create a routine for %s", entry, skillLevel)
                            .isNotNull();
                }
            }
        }

        @Test
        @DisplayName("Should list every routine defined in RoutineFactory")
        void shouldListEveryRoutineDefinedInFactory() {
            assertThat(RoutineCatalog.values())
                    .as("Adding a routine to RoutineFactory should add a matching catalog entry")
                    .hasSize(5);
        }
    }

    @Nested
    @DisplayName("Delegation")
    class Delegation {

        @Test
        @DisplayName("A catalog entry should produce the same result as calling the factory method directly")
        void catalogEntryShouldMatchDirectFactoryCall() {
            var catalogDeck = DeckFactory.standardDeck();
            var directDeck = DeckFactory.standardDeck();

            SkillLevel skillLevel = SkillLevel.EXPERT;

            RoutineProtocol fromCatalog = RoutineCatalog.STANDARD_RIFFLE_SHUFFLE.create(skillLevel);
            RoutineProtocol fromFactory = RoutineFactory.standardRiffleShuffle(skillLevel);

            fromCatalog.execute(catalogDeck, TestRandoms.seededRandom(42));
            fromFactory.execute(directDeck, TestRandoms.seededRandom(42));

            assertThat(catalogDeck)
                    .as("RoutineCatalog should delegate to RoutineFactory without altering behaviour")
                    .isEqualTo(directDeck);
        }
    }

    @Nested
    @DisplayName("Validation")
    class Validation {

        @Test
        @DisplayName("Should reject a null skill level")
        void shouldRejectNullSkillLevel() {
            assertThatThrownBy(() -> RoutineCatalog.SIMPLE_RIFFLE_SHUFFLE.create(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("skillLevel must not be null");
        }
    }
}