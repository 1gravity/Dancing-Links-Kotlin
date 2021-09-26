package com.onegravity.sudoku

import com.onegravity.sudoku.SudokuTests.testSudokuDLX

class KaggleSudokuTests {

//    @Test
    fun testDLXSolver() {
        val l = System.currentTimeMillis()
        var count = 0
        getPuzzles("kaggle.csv") { puzzle, solution ->
            val grid = getTestGrid(puzzle, null)
            testSudokuDLX(grid, solution)
            if ((++count).mod(1000) == 0) println("DLX.Kaggle: $count")
        }
        println("DLX.Kaggle took: ${System.currentTimeMillis() - l} ms")
    }

//    @Test
    fun testDLX2Solver() {
        val l = System.currentTimeMillis()
        var count = 0
        getPuzzles("kaggle.csv") { puzzle, solution ->
            val grid = getTestGrid(puzzle, null)
            testSudokuDLX(grid, solution)
            if ((++count).mod(1000) == 0) println("DLX2.Kaggle: $count")
        }
        println("DLX2.Kaggle took: ${System.currentTimeMillis() - l} ms")
    }

//    @Test
    fun testDL32Solver() {
        val l = System.currentTimeMillis()
        var count = 0
        getPuzzles("kaggle.csv") { puzzle, solution ->
            val grid = getTestGrid(puzzle, null)
            testSudokuDLX(grid, solution)
            if ((++count).mod(1000) == 0) println("DLX3.Kaggle: $count")
        }
        println("DLX3.Kaggle took: ${System.currentTimeMillis() - l} ms")
    }

}
