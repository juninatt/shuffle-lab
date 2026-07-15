# ShuffleLab

ShuffleLab is a Java library for exploring, simulating, and comparing card shuffling techniques.
The project began out of curiosity. 
After many card games, I wanted a way to compare different shuffling techniques and determine which ones produce the best randomisation in practice. 
ShuffleLab provides a framework for modelling techniques, player skill, and complete shuffle routines to answer those questions through simulation.

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

### Skill simulation

ShuffleLab separates shuffle routines from execution quality.

A routine defines *what* is performed, while a `SkillProfile` defines *how precisely* the underlying operations are executed. 
This makes it possible to compare the effectiveness of identical shuffle routines performed by players of different skill levels, 
as well as experiment with custom skill profiles.

### Features

**Current**  

- Standard 52-card deck generation with multiple starting deck orders
- Deck cutting
- Balanced deck splitting with configurable deviation
- Perfect and human-style packet interleaving
- Perfect and human-style riffle shuffles
- Overhand shuffles
- Configurable shuffle architecture based on operations, shuffles, and routines
- Validation of shuffle constraints
- Comprehensive unit tests using JUnit 5 and AssertJ

**Planned**

- Additional deck splitters and cutting strategies
- Additional shuffle techniques
- Additional shuffle routines
- Custom skill profiles
- Statistical analysis of shuffle quality
- Performance benchmarking
