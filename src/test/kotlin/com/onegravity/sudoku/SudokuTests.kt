package com.onegravity.sudoku

import com.onegravity.sudoku.solver.solve
import com.onegravity.dlx.solve
import com.onegravity.dlx.toDLX
import com.onegravity.dlx2.solve
import com.onegravity.dlx2.toDLX2
import com.onegravity.dlx3.solve
import com.onegravity.dlx3.toDLX3
import com.onegravity.sudoku.SudokuMatrix.Companion.toSudokuMatrix
import com.onegravity.sudoku.model.Puzzle
import com.onegravity.sudoku.model.getTestGrid
import com.onegravity.sudoku.model.region.RegionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger

class SudokuTests : BaseClass4CoroutineTests() {

    @Test
    fun testSudoku1() {
        val grid = getTestGrid(testSudoku1)
        testSudokuDLX(grid, testSudoku1Solution)
        testSudokuDLX2(grid, testSudoku1Solution)
        testSudokuDLX3(grid, testSudoku1Solution)
        testBruteForce(grid, testSudoku1Solution)
    }

    @Test
    fun testSudokuAlEscargot() {
        val grid = getTestGrid(testSudokuAlEscargot)
        testSudokuDLX(grid, testSudokuAlEscargotSolution)
        testSudokuDLX2(grid, testSudokuAlEscargotSolution)
        testSudokuDLX3(grid, testSudokuAlEscargotSolution)
        testBruteForce(grid, testSudokuAlEscargotSolution)
    }

    @Test
    fun testJigsawSudoku() {
        val grid = getTestGrid(testSudokuJigsawValues, null, true, testSudokuJigsawBlocks)
        testSudokuDLX(grid, testSudokuJigsawSolution)
        testSudokuDLX2(grid, testSudokuJigsawSolution)
        testSudokuDLX3(grid, testSudokuJigsawSolution)
        testBruteForce(grid, testSudokuJigsawSolution)
    }

    @Test
    fun testSudokuWithExtraRegions() {
        // Hyper-Sudoku
        val gridHyper = getTestGrid(testHyperSudoku, extraRegionType = RegionType.HYPER)
        testSudokuDLX(gridHyper, testHyperSudokuSolution)
        testSudokuDLX2(gridHyper, testHyperSudokuSolution)
        testSudokuDLX3(gridHyper, testHyperSudokuSolution)
        testBruteForce(gridHyper, testHyperSudokuSolution)

        // X-Sudoku
        val gridX = getTestGrid(testXSudoku, extraRegionType = RegionType.X)
        testSudokuDLX(gridX, testXSudokuSolution)
        testSudokuDLX2(gridX, testXSudokuSolution)
        testSudokuDLX3(gridX, testXSudokuSolution)
        testBruteForce(gridX, testXSudokuSolution)

        // Color-Sudoku
        val gridColor = getTestGrid(testColorSudoku, extraRegionType = RegionType.COLOR)
        testSudokuDLX(gridColor, testColorSudokuSolution)
        testSudokuDLX2(gridColor, testColorSudokuSolution)
        testSudokuDLX3(gridColor, testColorSudokuSolution)
        testBruteForce(gridColor, testColorSudokuSolution)
    }

    @Test
    fun testCompletedSudoku() {
        val grid = getTestGrid(testSudokuCompleted)
        testSudokuDLX(grid, testSudokuCompleted)
        testSudokuDLX2(grid, testSudokuCompleted)
        testSudokuDLX3(grid, testSudokuCompleted)
        testBruteForce(grid, testSudokuCompleted)
    }

    @Test
    fun testMultipleSolutionsDLX() {
        testMultipleSolutions { puzzle, collect ->
            puzzle.toSudokuMatrix().toDLX().solve { collect(it) }
        }
        testMultipleSolutions { puzzle, collect ->
            puzzle.toSudokuMatrix().toDLX2().solve { collect(it) }
        }
        testMultipleSolutions { puzzle, collect ->
            puzzle.toSudokuMatrix().toDLX3().solve { collect(it) }
        }
    }

    @Test
    fun testMultipleSolutionsBF() {
        val solutionsFound = AtomicInteger(0)
        val puzzle = getTestGrid(testSudokuMultipleSolutions)
        runBlocking(Dispatchers.Main) {
            puzzle.solve { solution ->
                when (solution[80]) {
                    9 -> assertArrayEquals(testSudokuMultipleSolutionsSolution1, solution)
                    3 -> assertArrayEquals(testSudokuMultipleSolutionsSolution2, solution)
                    else -> assert(false)
                }
                solutionsFound.incrementAndGet()
            }
        }
        assertEquals(2, solutionsFound.get())
    }

    @Test
    fun testNoSolutions() {
        val grid = getTestGrid(testSudokuNoSolution)
        grid.toSudokuMatrix().toDLX().solve { assert(false) }
        grid.toSudokuMatrix().toDLX2().solve { assert(false) }
        grid.toSudokuMatrix().toDLX3().solve { assert(false) }
        runBlocking(Dispatchers.Main) { grid.solve { assert(false) } }
    }

    private fun testMultipleSolutions(solve: (puzzle: Puzzle, collect: (List<Int>) -> Unit) -> Unit) {
        val solutionsFound = AtomicInteger(0)
        val grid = getTestGrid(testSudokuMultipleSolutions)
        solve(grid) { rows ->
            val solution = rows.toGrid(grid)
            when (solution.getCell(80).value) {
                9 -> validateSolution(testSudokuMultipleSolutionsSolution1, grid, solution)
                3 -> validateSolution(testSudokuMultipleSolutionsSolution2, grid, solution)
                else -> assert(false)
            }
            solutionsFound.incrementAndGet()
        }
        assertEquals(2, solutionsFound.get())
    }

    private fun testSudokuDLX(puzzle: Puzzle, solution: IntArray) {
        testAndValidateSudoku(puzzle, solution) { collect ->
            toDLX().solve { rows -> collect(rows) }
        }
    }

    private fun testSudokuDLX2(puzzle: Puzzle, solution: IntArray) {
        testAndValidateSudoku(puzzle, solution) { collect ->
            toDLX2().solve { rows -> collect(rows) }
        }
    }

    private fun testSudokuDLX3(puzzle: Puzzle, solution: IntArray) {
        testAndValidateSudoku(puzzle, solution) { collect ->
            toDLX3().solve { rows -> collect(rows) }
        }
    }

    private fun testBruteForce(puzzle: Puzzle, solution: IntArray) {
        var solutionFound = false
        runBlocking(Dispatchers.Main) {
            puzzle.solve {
                assertArrayEquals(solution, it)
                solutionFound = true
            }
        }
        assertEquals(true, solutionFound)
    }

}
