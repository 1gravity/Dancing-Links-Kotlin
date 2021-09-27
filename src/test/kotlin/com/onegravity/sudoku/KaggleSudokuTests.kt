package com.onegravity.sudoku

import com.onegravity.bruteforce.solve
import com.onegravity.dlx.solve
import com.onegravity.dlx.toDLX
import com.onegravity.dlx3.solve
import com.onegravity.dlx3.toDLX3
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class KaggleSudokuTests {

    private val filename = "kaggle.csv"

//    @Test
    fun testDLX() {
        getPuzzles(filename) { puzzle, solution ->
            val grid = getTestGrid(puzzle, null)
            testAndValidateSudoku(grid, solution) { collect ->
                toDLX().solve { rows -> collect(rows) }
            }
        }
    }

//    @Test
    fun testDLX2() {
        getPuzzles(filename) { puzzle, solution ->
            val grid = getTestGrid(puzzle, null)
            testAndValidateSudoku(grid, solution) { collect ->
                toDLX().solve { rows -> collect(rows) }
            }
        }
    }

//    @Test
    fun testDLX3() {
        getPuzzles(filename) { puzzle, solution ->
            val grid = getTestGrid(puzzle, null)
            testAndValidateSudoku(grid, solution) { collect ->
                toDLX3().solve { rows -> collect(rows) }
            }
        }
    }

    @Test
    fun testBruteForce() {
        getPuzzles(filename) { puzzle, solution ->
            Assertions.assertArrayEquals(solution, puzzle.solve())
        }
    }

}
