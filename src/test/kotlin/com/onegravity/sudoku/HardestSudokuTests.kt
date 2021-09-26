package com.onegravity.sudoku

import com.onegravity.dlx.solve
import com.onegravity.dlx.toDLX
import com.onegravity.dlx3.solve
import com.onegravity.dlx3.toDLX3
import org.junit.jupiter.api.Test

class HardestSudokuTests {

    @Test
    fun testDLXSolver() {
        getPuzzles("hardest.csv") { puzzle, solution ->
            val grid = getTestGrid(puzzle, null)
            testAndValidateSudoku(grid, solution) { collect ->
                toDLX().solve { rows -> collect(rows) }
            }
        }
    }

    @Test
    fun testDLX2Solver() {
        getPuzzles("hardest.csv") { puzzle, solution ->
            val grid = getTestGrid(puzzle, null)
            testAndValidateSudoku(grid, solution) { collect ->
                toDLX().solve { rows -> collect(rows) }
            }
        }
    }

    @Test
    fun testDLX3Solver() {
        getPuzzles("hardest.csv") { puzzle, solution ->
            val grid = getTestGrid(puzzle, null)
            testAndValidateSudoku(grid, solution) { collect ->
                toDLX3().solve { rows -> collect(rows) }
            }
        }
    }

}
