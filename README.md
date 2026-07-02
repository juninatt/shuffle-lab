# ShuffleLab

ShuffleLab is a Java library for exploring, simulating, and comparing card shuffling techniques.

The project models both the fundamental operations used to manipulate a deck and the higher-level shuffling techniques built upon them. Its goal is to simulate realistic card handling, represent different levels of player skill, and analyze how effectively various shuffle routines randomize a deck.

## Architecture

ShuffleLab is built around three levels of abstraction:

```
Operation
        ↓
Shuffle
        ↓
Routine
```

- **Operations** are the fundamental building blocks used to manipulate the order of a deck. Examples include cutting a deck or interleaving packets.
- **Shuffles** combine one or more operations to implement a specific shuffling technique, such as a riffle shuffle.
- **Routines** combine one or more shuffles, optionally interleaved with individual operations, to model complete real-world shuffling procedures.

Current features:

Standard 52-card deck generation with multiple starting deck orders
- Deck cutting
- Deck splitting with configurable tolerance
- Perfect and human-style packet interleaving
- Perfect and human-like riffle shuffles
- Configurable shuffle architecture based on operations, shuffles, and routines
- Validation of shuffle constraints
- Comprehensive unit tests using JUnit 5 and AssertJ

Planned features:

- Overhand shuffles
- Hindu shuffles
- Additional deck splitters and cutters
- Configurable shuffle routines representing different skill levels
- Statistical analysis of shuffle quality
- Performance benchmarking

ShuffleLab is primarily an experiment in object-oriented design, API design, testing practices, and realistic card shuffling simulation.
