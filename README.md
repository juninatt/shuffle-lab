# ShuffleLab

ShuffleLab is a Java library and command-line tool for simulating, measuring, and comparing card shuffling techniques.

It models individual shuffling techniques, the skill of the person performing them, and complete real-world
shuffling routines, then measures how well each configuration actually randomises a deck — turning "which
shuffle is better?" into something you can run and compare directly.

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

ShuffleLab measures how well a shuffle actually randomises a deck, not just performs it.

A shuffled deck is compared against its original order to measure card displacement and how much of the
original card order survives the shuffle. Running a routine repeatedly and aggregating the results produces
summary statistics — mean, median, minimum, maximum, and standard deviation — for each measure, making it
possible to compare configurations, such as different skill levels, against each other. Results are
formatted as a plain-text comparison report, either for manual inspection or as part of a larger run.

## Usage

Run the command-line application to compare shuffle routines and skill levels:

```bash
mvn compile exec:java -Dexec.args="--routine STANDARD_RIFFLE_SHUFFLE --skill EXPERT --trials 1000"
```

Omit `--routine` or `--skill` to run every predefined combination. Use `--out` to choose where the resulting
plain-text report is written. Add `--csv <path>` and/or `--json <path>` to also export the same results in a
format meant for further processing, such as a spreadsheet or a plotting tool, rather than manual reading. Use
`--help` to see all available options.

## Features

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
- A catalog of predefined shuffle routines, discoverable and run programmatically
- An ideal random shuffle baseline (Fisher–Yates), for measuring how close a technique gets to true randomness
- Faro- and Mongean-shuffle-then-riffle routines, pairing each structured shuffle with a riffle and cut to actually randomise the deck
- Deck shuffle analysis: card displacement and preserved-order measurement
- Aggregated statistics across repeated trials
- Plain-text, CSV, and JSON comparison reporting across multiple trial runs
- A command-line application for running and comparing routines and skill levels
- Validation of shuffle constraints
- Comprehensive unit tests using JUnit 5 and AssertJ

**Planned**

- Additional deck splitters and cutting strategies
- Additional shuffle techniques
- Performance benchmarking
