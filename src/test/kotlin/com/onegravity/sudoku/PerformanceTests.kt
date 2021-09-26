package com.onegravity.sudoku

import com.onegravity.dlx.solve
import com.onegravity.dlx.toDLX
import com.onegravity.dlx2.solve
import com.onegravity.dlx2.toDLX2
import com.onegravity.dlx3.solve
import com.onegravity.dlx3.toDLX3
import com.onegravity.sudoku.SudokuMatrix.Companion.toSudokuMatrix
import com.onegravity.sudoku.legacy.Accumulator
import com.onegravity.sudoku.legacy.Hint
import com.onegravity.sudoku.legacy.SolutionHint
import com.onegravity.sudoku.legacy.SolutionProducer
import com.onegravity.sudoku.model.Grid
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

class PerformanceTests {

    @Test
    fun testSudoku1() {
        val grid = getTestGrid(testSudoku1, null)

        // warmup
        testSudoku(grid)

        val dlx = testSudoku(grid)
        val dlx2 = testSudokuDLX2(grid)
        val dlx3 = testSudokuDLX3(grid)
        val legacy = testSudokuLegacy(grid)
        println("Puzzle 1 - legacy: $legacy ms, dlx: $dlx ms, dlx2: $dlx2 ms, dlx3: $dlx3 ms")
    }

    @Test
    fun testSudoku2() {
        val grid = getTestGrid(testSudoku2, null)

        // warmup
        testSudoku(grid)

        val dlx = testSudoku(grid)
        val dlx2 = testSudokuDLX2(grid)
        val dlx3 = testSudokuDLX3(grid)
        val legacy = testSudokuLegacy(grid)
        println("Puzzle 2 - legacy: $legacy ms, dlx: $dlx ms, dlx2: $dlx2 ms, dlx3: $dlx3 ms")
    }

    @Test
    fun testSudoku3() {
        val grid = getTestGrid(testSudokuAlEscargot, null)

        // warmup
        testSudoku(grid)

        val dlx = testSudoku(grid)
        val dlx2 = testSudokuDLX2(grid)
        val dlx3 = testSudokuDLX3(grid)
        val legacy = testSudokuLegacy(grid)
        println("Puzzle 2 - legacy: $legacy ms, dlx: $dlx ms, dlx2: $dlx2 ms, dlx3: $dlx3 ms")
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
        var dlx3= 0L
        var legacy = 0L
        var count = 0

        getPuzzles("hardest.csv") { puzzle, _ ->
            val grid = getTestGrid(puzzle, null)
            val matrix = grid.toSudokuMatrix()

            var l: Long = System.currentTimeMillis()
            matrix
                .toDLX3()
                .solve {
                    dlx3 += (System.currentTimeMillis() - l)
                }

            l = System.currentTimeMillis()
            matrix
                .toDLX()
                .solve {
                    dlx += (System.currentTimeMillis() - l)
                }

            l = System.currentTimeMillis()
            matrix
                .toDLX2()
                .solve {
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

        println("Hardest Total   - legacy: $legacy ms, dlx: $dlx ms, dlx2: $dlx2 ms, dlx3: $dlx3 ms")
        println("Hardest Average - legacy: ${legacy.div(count)} ms, dlx: ${dlx.div(count)} ms, dlx2: ${dlx2.div(count)} ms, dlx3: ${dlx3.div(count)} ms")
    }

    private fun testSudoku(grid: Grid) = measureTimeMillis {
        testSudoku(grid) { collect ->
            toDLX().solve { rows -> collect(rows) }
        }
    }

    private fun testSudokuDLX2(grid: Grid) = measureTimeMillis {
        testSudoku(grid) { collect ->
            toDLX2().solve { rows -> collect(rows) }
        }
    }

    private fun testSudokuDLX3(grid: Grid) = measureTimeMillis {
        testSudoku(grid) { collect ->
            toDLX3().solve { rows -> collect(rows) }
        }
    }

    private fun testSudokuLegacy(grid: Grid) = measureTimeMillis {
        var solutionFound = false

        SolutionProducer().getHints(grid, object : Accumulator {
            override fun add(hint: Hint?) {
                if (hint is SolutionHint) {
                    solutionFound = true
                }
            }

            override fun getHints() = emptyList<Hint>()
        })
        assert(solutionFound)
    }

}
