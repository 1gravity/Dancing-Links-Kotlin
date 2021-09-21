package com.onegravity.sudoku

import com.onegravity.dlx.PayloadProvider
import com.onegravity.dlx.solve
import com.onegravity.dlx.toDLX
import com.onegravity.dlx2.CoverMatrix.Companion.toCoverMatrix
import com.onegravity.dlx2.solveProblem
import com.onegravity.sudoku.SudokuMatrix.Companion.getIndexValue
import com.onegravity.sudoku.SudokuMatrix.Companion.toSudokuMatrix
import com.onegravity.sudoku.legacy.Accumulator
import com.onegravity.sudoku.legacy.Hint
import com.onegravity.sudoku.legacy.SolutionHint
import com.onegravity.sudoku.legacy.SolutionProducer
import com.onegravity.sudoku.model.Grid
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

class PerformanceTests {

    @Test
    fun testSudoku1() {
        val grid = getTestGrid(testSudoku1, null)
        val dlx = testSudoku(grid, testSudoku1Solution)
        val legacy = testSudokuLegacy(grid, testSudoku1Solution)
        println("Puzzle 1        - legacy: $legacy ms, dlx: $dlx ms")
    }

    @Test
    fun testSudoku2() {
        val grid = getTestGrid(testSudoku2, null)
        val dlx = testSudoku(grid, testSudoku2)
        val legacy = testSudokuLegacy(grid, testSudoku2)
        println("Puzzle 2        - legacy: $legacy ms, dlx: $dlx ms")
    }

    @Test
    fun testSudoku3() {
        val grid = getTestGrid(testSudokuAlEscargot, null)
        val dlx = testSudoku(grid, testSudokuAlEscargotSolution)
        val legacy = testSudokuLegacy(grid, testSudokuAlEscargotSolution)
        println("Puzzle 3        - legacy: $legacy ms, dlx: $dlx ms")
    }

    /**
     * Here we measure how fast Algorithm X with dancing links solves the hardest puzzle collection compared to a fairly
     * optimized regular brute force algorithm.
     *
     * DLS is around 4 times faster.
     */
    @Test
    fun testHardest() {
        var dlx = 0L
        var dlx2 = 0L
        var legacy = 0L
        var count = 0

        HardestSudokuTests.getHardestPuzzles { puzzle ->
            val grid = getTestGrid(puzzle, null)

            var l = System.currentTimeMillis()
            grid.toSudokuMatrix()
                .toDLX(object: PayloadProvider {
                    override fun getHeaderPayload(index: Int) = "h$index"
                    override fun getDataPayload(col: Int, row: Int) = getIndexValue(row)
                })
                .solve {
                    dlx += (System.currentTimeMillis() - l)
                }

            l = System.currentTimeMillis()
            grid.toSudokuMatrix()
                .toCoverMatrix()
                .solveProblem {
                    dlx2 += (System.currentTimeMillis() - l)
                }

            l = System.currentTimeMillis()
            SolutionProducer().getHints(grid, object : Accumulator {
                override fun add(hint: Hint?) {
                    if (hint is SolutionHint) {
                        legacy += (System.currentTimeMillis() - l)
                    }
                }
                override fun getHints() = emptyList<Hint>()
            })

            count++
        }

        println("Hardest Total   - legacy: $legacy ms, dlx: $dlx ms, dlx2: $dlx2 ms")
        println("Hardest Average - legacy: ${legacy.div(count)} ms, dlx: ${dlx.div(count)} ms, dlx2: ${dlx2.div(count)} ms")
    }

    private fun testSudoku(grid: Grid, solution: IntArray) = measureTimeMillis {
        var solutionFound = false

        grid.toSudokuMatrix()
            .toDLX(object: PayloadProvider {
                override fun getHeaderPayload(index: Int) = "h$index"
                override fun getDataPayload(col: Int, row: Int) = SudokuMatrix.getIndexValue(row)
            })
            .solve { nodes ->
                validateSolution(solution, grid, nodes.toGrid(grid))
                solutionFound = true
            }

        assert(solutionFound)
    }

    private fun testSudokuLegacy(grid: Grid, expected: IntArray) = measureTimeMillis {
        var solutionFound = false

        SolutionProducer().getHints(grid, object : Accumulator {
            override fun add(hint: Hint?) {
                if (hint is SolutionHint) {
                    solutionFound = true
                    validateSolution(expected, grid, hint.solution)
                }
            }

            override fun getHints() = emptyList<Hint>()
        })
        assert(solutionFound)
    }

    private fun validateSolution(expected: IntArray, original: Grid, actual: Grid) {
        expected.forEachIndexed { index, value ->
            assertEquals(original.getCell(index).isGiven, actual.getCell(index).isGiven)
            assertEquals(value, actual.getCell(index).value)
        }
    }

}
