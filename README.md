# Dancing-Links-Kotlin

This repo contains multiple Kotlin implementations of Donald Knuth's [Algorithm X](https://en.wikipedia.org/wiki/Knuth%27s_Algorithm_X) using [dancing links](https://en.wikipedia.org/wiki/Dancing_Links) to solve [exact cover problems](https://en.wikipedia.org/wiki/Exact_cover) 
(see also Donald Knuth's 2020 paper: https://arxiv.org/pdf/cs/0011047.pdf.).

It also contains a fairly fast brute force Sudoku solver.

### DLX Solver 1
The first [Algorithm X](https://en.wikipedia.org/wiki/Knuth%27s_Algorithm_X) solver has a proper object-oriented design for the [dancing links](https://en.wikipedia.org/wiki/Dancing_Links).
It's the cleanest implementation from a design point of view, alas one could argue that it's over-engineered (hence DLX Solver 3).

### DLX Solver 2
The second [Algorithm X](https://en.wikipedia.org/wiki/Knuth%27s_Algorithm_X) solver uses maps/dictionaries for the [dancing links](https://en.wikipedia.org/wiki/Dancing_Links) implementation.

The resulting code is much shorter and easier to get right. However, it performs considerably worse than the dancing links data structure.
While both data structures can insert and delete elements in O(1), there's still more overhead dealing with hashed data structures compared to just manipulating nodes in linked lists.

See also: https://www.cs.mcgill.ca/~aassaf9/python/algorithm_x.html

### DLX Solver 3 
The third [Algorithm X](https://en.wikipedia.org/wiki/Knuth%27s_Algorithm_X) solver is very similar to the first solver but uses a minimal OO design (just one node class) and less Kotlin "magic" (extension functions, scopes etc.).
As a result the code is much shorter and imo easier to read/understand.

## Sudoku
Sudoku puzzles can be expressed as [exact cover problems](https://en.wikipedia.org/wiki/Exact_cover).
This repo has extensions for above-mentioned DLX solvers to not just solve general [exact cover problems](https://en.wikipedia.org/wiki/Exact_cover) but Sudoku puzzles specifically.

On top of regular 9x9 Sudokus, all the extensions support Jigsaw puzzles and puzzles with extra regions:
- Asterisk-Sudoku
- Centerdot-Sudoku
- Color-Sudoku
- Hyper-Sudoku
- Percent-Sudoku
- X-Sudoku
 
A set of the hardest known Sudoku puzzles and also a set of one million easy puzzles (https://www.kaggle.com/bryanpark/sudoku) are part of the extensive test suite.

### Brute Force
I implemented two fairly fast brute force Sudoku solvers.

The first one is a more or less 1:1 Kotlin version of the C version from https://t-dillon.github.io/tdoku but also supports jigsaw puzzles.
The second one is a more generic version that also supports puzzles with extra regions.

Both are used as part of the brute force solver depending on the type of puzzle to solve.

### Performance

Performance of Sudoku solvers depends a lot on the difficulty of the puzzles to solve. To reflect that fact, we use two data sets, one with very hard puzzles and one with a lot of easy puzzles.
The following numbers are meant to get an idea how the different algorithms / implementations perform compared to each other but not to compare with other Sudoku solvers out there (most only support standard puzzles anyway):
#### Easy Puzzles
- DLX 1: **>2000** puzzles/sec
- DLX 2: **>1000** puzzles/sec
- DLX 3: **~1200** puzzles/sec
- Brute Force: **~100'000** puzzles/sec

#### Hard Puzzles
- DLX 1: **~210** puzzles/sec
- DLX 2: **~80** puzzles/sec
- DLX 3: **~165** puzzles/sec
- Brute Force: **~540** puzzles/sec

These numbers are obviously very slow compared to some other solvers (see https://t-dillon.github.io/tdoku). 
The goal here wasn't to write the fastest solver there is (I wouldn't pick Kotlin for that) but to get an idea how different dancing link implementations compare and how DLX compares to more traditional Sudoku solver implementations.
The numbers are clear: DLX is slower than just plain brute force.

## Build & Run
- Clone the repo
- `cd dancing-links-kotlin`
- run `./gradlew build` to build and run the test suite
- if you want to run the Kaggle tests (1 million puzzles) you need to install [Git LFS](https://git-lfs.github.com/) 

## Resources
- https://arxiv.org/pdf/cs/0011047.pdf.
- https://medium.com/javarevisited/building-a-sudoku-solver-in-java-with-dancing-links-180274b0b6c1
- https://garethrees.org/2007/06/10/zendoku-generation
- https://www.baeldung.com/java-sudoku
- https://github.com/rafalio/dancing-links-java
- https://github.com/benfowler/dancing-links
- https://www.cs.mcgill.ca/~aassaf9/python/algorithm_x.html
- https://www.kth.se/social/files/58861771f276547fe1dbf8d1/HLaestanderMHarrysson_dkand14.pdf
