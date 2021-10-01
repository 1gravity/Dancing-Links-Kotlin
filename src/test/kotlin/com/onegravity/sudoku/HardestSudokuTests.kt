package com.onegravity.sudoku

import com.onegravity.sudoku.solver.solve
import com.onegravity.dlx.solve
import com.onegravity.dlx.toDLX
import com.onegravity.dlx2.solve
import com.onegravity.dlx2.toDLX2
import com.onegravity.dlx3.solve
import com.onegravity.dlx3.toDLX3
import com.onegravity.sudoku.model.getTestGrid
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class HardestSudokuTests : BaseClass4CoroutineTests() {

    private val filename = "hardest.csv"

    @Test
    fun testDLX() {
        getPuzzles(filename) { puzzle, solution ->
            val grid = getTestGrid(puzzle)
            testAndValidateSudoku(grid, solution) { collect ->
                toDLX().solve { rows -> collect(rows) }
            }
        }
    }

    @Test
    fun testDLX2() {
        getPuzzles(filename) { puzzle, solution ->
            val grid = getTestGrid(puzzle)
            testAndValidateSudoku(grid, solution) { collect ->
                toDLX2().solve { rows -> collect(rows) }
            }
        }
    }

    @Test
    fun testDLX3() {
        getPuzzles(filename) { puzzle, solution ->
            val grid = getTestGrid(puzzle)
            testAndValidateSudoku(grid, solution) { collect ->
                toDLX3().solve { rows -> collect(rows) }
            }
        }
    }

    @DelicateCoroutinesApi
    @Test
    fun testBruteForce() {
        getPuzzles(filename) { puzzle, solution ->
            val grid = getTestGrid(puzzle)
            var solutionFound = false
            runBlocking(Dispatchers.Main) {
                grid.solve {
                    Assertions.assertArrayEquals(solution, it)
                    solutionFound = true
                }
            }
            Assertions.assertEquals(true, solutionFound)
        }
    }

}
