# ShuffleLab

ShuffleLab is a Java library for exploring, simulating, and comparing card shuffling techniques.

The project began as an attempt to investigate how different shuffling techniques randomise a deck in practice. 
It provides a framework for modelling individual shuffle techniques, player skill, and complete shuffle routines through simulation.

## Architecture

ShuffleLab is built around three levels of abstraction:

```
Operation
        ↓
Shuffle
        ↓
Routine
```

- **Operations** are the fundamental building blocks used to manipulate the order of a deck. Examples include cutting, splitting, and interleaving packets.
- **Shuffles** combine one or more operations to implement techniques such as riffle, overhand, pile, Mongean, and Faro shuffles.
- **Routines** combine one or more shuffles, optionally interleaved with individual operations, to model complete real-world shuffling procedures.

### Skill simulation

ShuffleLab separates shuffle routines from execution quality.

A routine defines *what* is performed, while a `SkillProfile` defines *how precisely* the underlying operations are executed.
This allows the same shuffle routine to be simulated at different skill levels without changing its structure. 
Custom skill profiles can also be created for experimentation.

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
- Skill-based shuffle configuration
- Configurable shuffle architecture based on operations, shuffles, and routines
- Validation of shuffle constraints
- Comprehensive unit tests using JUnit 5 and AssertJ

**Planned**

- Additional deck splitters and cutting strategies
- Additional shuffle techniques
- Additional shuffle routines
- Custom skill profiles
- Shuffle analysis and comparison tools
- Statistical analysis of shuffle quality
- Performance benchmarking
