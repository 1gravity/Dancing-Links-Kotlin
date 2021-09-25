package com.onegravity.sudoku

import com.onegravity.dlx.solve
import com.onegravity.dlx.toDLX
import com.onegravity.dlx2.solve
import com.onegravity.dlx2.toDLX2
import com.onegravity.dlx3.solve
import com.onegravity.dlx3.toDLX3
import com.onegravity.sudoku.SudokuMatrix.Companion.toSudokuMatrix
import com.onegravity.sudoku.model.Grid
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger

class HardestSudokuTests {

    @Test
    fun testDLXSolver() {
        getPuzzles("HardestSudokus.csv") { puzzle, solution ->
            testSudokuDLX(puzzle, solution)
        }
    }

    @Test
    fun testDLX2Solver() {
        getPuzzles("HardestSudokus.csv") { puzzle, solution ->
            testSudokuDLX2(puzzle, solution)
        }
    }

    @Test
    fun testDLX3Solver() {
        getPuzzles("HardestSudokus.csv") { puzzle, solution ->
            testSudokuDLX3(puzzle, solution)
        }
    }

    private fun testSudokuDLX(puzzle: IntArray, solution: IntArray) {
        val grid = getTestGrid(puzzle, null)

        val solutions = AtomicInteger(0)
        grid.toSudokuMatrix()
            .toDLX()
            .solve { nodes ->
                validateSolution(solution, grid, nodes.toGrid(grid))
                solutions.incrementAndGet()
            }

        // make sure each puzzle has exactly one solution
        assertEquals(1, solutions.get())
    }

    private fun testSudokuDLX2(puzzle: IntArray, solution: IntArray) {
        val grid = getTestGrid(puzzle, null)

        val solutions = AtomicInteger(0)
        grid.toSudokuMatrix()
            .toDLX2()
            .solve { rows ->
                validateSolution(solution, grid, rows.toGrid(grid))
                solutions.incrementAndGet()
            }

        // make sure each puzzle has exactly one solution
        assertEquals(1, solutions.get())
    }

    private fun testSudokuDLX3(puzzle: IntArray, solution: IntArray) {
        val grid = getTestGrid(puzzle, null)

        val solutions = AtomicInteger(0)
        grid.toSudokuMatrix()
            .toDLX3()
            .solve { rows ->
                validateSolution(solution, grid, rows.toGridDLX3(grid))
                solutions.incrementAndGet()
            }

        // make sure each puzzle has exactly one solution
        assertEquals(1, solutions.get())
    }

    private fun validateSolution(expected: IntArray, original: Grid, actual: Grid) {
        expected.forEachIndexed { index, value ->
            assertEquals(original.getCell(index).isGiven, actual.getCell(index).isGiven)
            assertEquals(value, actual.getCell(index).value)
        }
    }

}
