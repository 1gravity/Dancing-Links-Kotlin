package com.onegravity.sudoku

import com.onegravity.bruteforce.solve
import com.onegravity.dlx.solve
import com.onegravity.dlx.toDLX
import com.onegravity.dlx2.solve
import com.onegravity.dlx2.toDLX2
import com.onegravity.dlx3.solve
import com.onegravity.dlx3.toDLX3
import com.onegravity.sudoku.model.Grid
import com.onegravity.sudoku.model.region.RegionType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SudokuTests {

    @Test
    fun testSudoku1() {
        val grid = getTestGrid(testSudoku1, null)
        testSudokuDLX(grid, testSudoku1Solution)
        testSudokuDLX2(grid, testSudoku1Solution)
        testSudokuDLX3(grid, testSudoku1Solution)
        testBruteForce(testSudoku1, testSudoku1Solution)
        testBruteForce2(grid, testSudoku1Solution)
    }

    @Test
    fun testSudokuAlEscargot() {
        val grid = getTestGrid(testSudokuAlEscargot, null)
        testSudokuDLX(grid, testSudokuAlEscargotSolution)
        testSudokuDLX2(grid, testSudokuAlEscargotSolution)
        testSudokuDLX3(grid, testSudokuAlEscargotSolution)
        testBruteForce(testSudokuAlEscargot, testSudokuAlEscargotSolution)
        testBruteForce2(grid, testSudokuAlEscargotSolution)
    }

    @Test
    fun testJigsawSudoku() {
        val grid = getTestGrid(testSudokuJigsawValues, null, true, testSudokuJigsawBlocks)
        testSudokuDLX(grid, testSudokuJigsawSolution)
        testSudokuDLX2(grid, testSudokuJigsawSolution)
        testSudokuDLX3(grid, testSudokuJigsawSolution)
        testBruteForce2(grid, testSudokuJigsawSolution)
    }

    @Test
    fun testSudokuWithExtraRegions() {
        // Hyper-Sudoku
        val gridHyper = getTestGrid(testHyperSudoku, extraRegionType = RegionType.HYPER)
        testSudokuDLX(gridHyper, testHyperSudokuSolution)
        testSudokuDLX2(gridHyper, testHyperSudokuSolution)
        testSudokuDLX3(gridHyper, testHyperSudokuSolution)
        testBruteForce2(gridHyper, testHyperSudokuSolution)

        // X-Sudoku
        val gridX = getTestGrid(testXSudoku, extraRegionType = RegionType.X)
        testSudokuDLX(gridX, testXSudokuSolution)
        testSudokuDLX2(gridX, testXSudokuSolution)
        testSudokuDLX3(gridX, testXSudokuSolution)
        testBruteForce2(gridX, testXSudokuSolution)

        // Color-Sudoku
        val gridColor = getTestGrid(testColorSudoku, extraRegionType = RegionType.COLOR)
        testSudokuDLX(gridColor, testColorSudokuSolution)
        testSudokuDLX2(gridColor, testColorSudokuSolution)
        testSudokuDLX3(gridColor, testColorSudokuSolution)
        testBruteForce2(gridColor, testColorSudokuSolution)
    }

    @Test
    fun testCompletedSudoku() {
        val grid = getTestGrid(testSudoku2, null)
        testSudokuDLX(grid, testSudoku2)
        testSudokuDLX2(grid, testSudoku2)
        testSudokuDLX3(grid, testSudoku2)
        testBruteForce(testSudoku2, testSudoku2)
        testBruteForce2(grid, testSudoku2)
    }

    private fun testSudokuDLX(grid: Grid, solution: IntArray) {
        testAndValidateSudoku(grid, solution) { collect ->
            toDLX().solve { rows -> collect(rows) }
        }
    }

    private fun testSudokuDLX2(grid: Grid, solution: IntArray) {
        testAndValidateSudoku(grid, solution) { collect ->
            toDLX2().solve { rows -> collect(rows) }
        }
    }

    private fun testSudokuDLX3(grid: Grid, solution: IntArray) {
        testAndValidateSudoku(grid, solution) { collect ->
            toDLX3().solve { rows -> collect(rows) }
        }
    }

    private fun testBruteForce(puzzle: IntArray, solution: IntArray) {
        Assertions.assertArrayEquals(solution, puzzle.solve())
    }

    private fun testBruteForce2(grid: Grid, solution: IntArray) {
        Assertions.assertArrayEquals(solution, grid.solve())
    }

}
