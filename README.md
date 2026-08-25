# ShuffleLab

ShuffleLab is a Java library for exploring, simulating, and comparing card shuffling techniques.

The project began as an attempt to investigate how different shuffling techniques randomise a deck in practice.
It provides a framework for modelling individual shuffle techniques, player skill, and complete shuffle routines through simulation.

## Architecture

ShuffleLab is built around five levels of abstraction:

```
Operation
↓
Shuffle
↓
Routine
↓
Trial
↓
Report
```


- **Operations** are the fundamental building blocks used to manipulate the order of a deck. Examples include cutting, splitting, and interleaving packets.
- **Shuffles** combine one or more operations to implement techniques such as riffle, overhand, pile, Mongean, and Faro shuffles.
- **Routines** combine one or more shuffles, optionally interleaved with individual operations, to model complete real-world shuffling procedures.
- **Trials** run a routine once against a fresh deck and measure the outcome, since a single shuffle is too high-variance to judge a technique from alone.
- **Reports** aggregate the results of many trials into summary statistics and present them as a plain-text comparison across configurations.

### Skill simulation

ShuffleLab separates shuffle routines from execution quality.

A routine defines *what* is performed, while a `SkillProfile` defines *how precisely* the underlying operations are executed.
This allows the same shuffle routine to be simulated at different skill levels without changing its structure.
Custom skill profiles can also be created for experimentation.

### Analysis and experimentation

ShuffleLab can measure how well a shuffle actually randomises a deck, not just perform it.

A shuffled deck can be compared against its original order to measure card displacement and how much of the
original card order survives the shuffle. Running a routine repeatedly and aggregating the results produces
summary statistics — mean, median, minimum, maximum, and standard deviation — for each measure, making it
possible to compare configurations, such as different skill levels, against each other. Results can be
formatted as a plain-text comparison report, either for manual inspection or as part of a larger run.

### Features

**Current**

- Standard 52-card deck generation with multiple starting deck orders
- Deck cutting
- Balanced deck splitting with configurable deviation
- Perfect and human-style packet interleaving
- Perfect and human-style riffle shuffles
- Overhand shuffles
- Pile shuffles
- Mongean shuffles
- In and out Faro shuffles
- Skill-based shuffle configuration, including custom skill profiles
- Configurable shuffle architecture based on operations, shuffles, and routines
- Deck shuffle analysis: card displacement and preserved-order measurement
- Aggregated statistics across repeated trials
- Plain-text comparison reporting across multiple trial runs
- A runnable application comparing configured routines and skill levels
- Validation of shuffle constraints
- Comprehensive unit tests using JUnit 5 and AssertJ

**Planned**

- Additional deck splitters and cutting strategies
- Additional shuffle techniques
- Additional shuffle routines
- Performance benchmarking