package com.onegravity.sudoku

import com.onegravity.dlx.PayloadProvider
import com.onegravity.dlx.toDLX
import com.onegravity.dlx.solve
import com.onegravity.dlx2.CoverMatrix.Companion.toDLXMatrix
import com.onegravity.dlx2.solve
import com.onegravity.sudoku.SudokuMatrix.Companion.getIndexValue
import com.onegravity.sudoku.SudokuMatrix.Companion.toSudokuMatrix
import com.onegravity.sudoku.model.Grid
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

class HardestSudokuTests {

    companion object {
        private val sudokuPattern = """^([\d.]{81}),(\d{81}).*${'$'}""".toRegex()

        // convert values from a String to an IntArray
        private fun String.toIntArray() = this.map { c -> c.digitToIntOrNull() ?: 0 }.toIntArray()

        fun getHardestPuzzles(process: (puzzle: IntArray, solution: IntArray) -> Unit) {
            File(ClassLoader.getSystemResource("HardestSudokus.txt").file)
                .forEachLine { line ->
                    // find Sudoku puzzles
                    sudokuPattern.findAll(line)
                        .forEach { match ->
                            // get group with puzzle values
                            val puzzle = match.groupValues[1].toIntArray()
                            val solution = match.groupValues[2].toIntArray()
                            assertEquals(81, puzzle.size)
                            assertEquals(81, solution.size)
                            process(puzzle, solution)
                        }
                }
        }
    }

    @Test
    fun testDLXSolver() {
        getHardestPuzzles { puzzle, solution ->
            testSudokuDLX(puzzle, solution)
        }
    }

    @Test
    fun testDLX2Solver() {
        getHardestPuzzles { puzzle, solution ->
            testSudokuDLX2(puzzle, solution)
        }
    }

    private fun testSudokuDLX(puzzle: IntArray, solution: IntArray) {
        val grid = getTestGrid(puzzle, null)

        val solutions = AtomicInteger(0)
        grid.toSudokuMatrix()
            .toDLX(object: PayloadProvider {
                override fun getHeaderPayload(index: Int) = "h$index"
                override fun getDataPayload(col: Int, row: Int) = getIndexValue(row)
            })
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
            .toDLXMatrix()
            .solve { rows ->
                validateSolution(solution, grid, rows.toGrid(grid))
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
