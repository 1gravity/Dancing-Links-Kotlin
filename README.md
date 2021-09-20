# Dancing-Links-Kotlin

This is a 100% Kotlin implementation of Donald Knuth's [Algorithm X](https://en.wikipedia.org/wiki/Knuth%27s_Algorithm_X) using [dancing links](https://en.wikipedia.org/wiki/Dancing_Links) to solve [exact cover problems](https://en.wikipedia.org/wiki/Exact_cover).

See also Donald Knuth's paper from 2020 on the topic: https://arxiv.org/pdf/cs/0011047.pdf.

Sudoku
------
A classic application of the exact cover problem is the Sudoku puzzle.
This repo implements a Sudoku solver that can solve not just standard Sudoku puzzles but also puzzles with extra regions:
- Asterisk-Sudoku
- Centerdot-Sudoku
- Color-Sudoku
- Hyper-Sudoku
- Percent-Sudoku
- X-Sudoku
 
It also supports Jigsaw puzzles (puzzles with non-square blocks).

A database of the hardest known Sudoku puzzles is part of the extensive test suite. 

The same database is also used to measure performance of the algorithm X / dancing link based algorithm compared to a fairly optimized conventional solving algorithm. The algorithm X based solver is around 3 times faster.
Note that if the conventional algorithm were a purely brute force algorithm (it's not) the difference would be considerably bigger.

Build & Run
-----------
- Clone the repo
- `cd dancing-links-kotlin`
- run `./gradlew build` to build and run the test suite

Resources
---------
- https://medium.com/javarevisited/building-a-sudoku-solver-in-java-with-dancing-links-180274b0b6c1
- https://garethrees.org/2007/06/10/zendoku-generation
- https://www.baeldung.com/java-sudoku
- https://github.com/rafalio/dancing-links-java
- https://github.com/benfowler/dancing-links
- https://www.kth.se/social/files/58861771f276547fe1dbf8d1/HLaestanderMHarrysson_dkand14.pdf

