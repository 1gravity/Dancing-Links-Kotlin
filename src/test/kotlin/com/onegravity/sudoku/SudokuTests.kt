package com.onegravity.sudoku

import com.onegravity.sudoku.model.Grid
import com.onegravity.sudoku.model.region.RegionType
import com.onegravity.dlx.PayloadProvider
import com.onegravity.dlx.toDLX
import com.onegravity.dlx.solve
import com.onegravity.sudoku.SudokuMatrix.Companion.getIndexValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SudokuTests {

    @Test
    fun testSudoku1() {
        val grid = getTestGrid(testSudoku1, null)
        testSudoku(grid, testSudoku1Solution)
    }

    @Test
    fun testSudokuAlEscargot() {
        val grid = getTestGrid(testSudokuAlEscargot, null)
        testSudoku(grid, testSudokuAlEscargotSolution)
    }

    @Test
    fun testJigsawSudoku() {
        val grid = getTestGrid(testSudokuJigsawValues, null, true, testSudokuJigsawBlocks)
        testSudoku(grid, testSudokuJigsawSolution)
    }

    @Test
    fun testSudokuWithExtraRegions() {
        testSudoku(getTestGrid(testXSudoku, extraRegionType = RegionType.X), testXSudokuSolution)
        testSudoku(getTestGrid(testColorSudoku, extraRegionType = RegionType.COLOR), testColorSudokuSolution)
    }

    @Test
    fun testCompletedSudoku() {
        val grid = getTestGrid(testSudoku2, null)
        testSudoku(grid, testSudoku2)
    }

    private fun testSudoku(grid: Grid, solution: IntArray) {
        var solutionFound = false
        SudokuMatrix(grid)
            .sudokuMatrix
            .toDLX(object: PayloadProvider {
                override fun getHeaderPayload(index: Int) = "h$index"
                override fun getDataPayload(col: Int, row: Int) = getIndexValue(row)
            })
            .solve { nodes ->
                validateSolution(solution, grid, nodes.toGrid(grid))
                solutionFound = true
            }
        assert(solutionFound)
    }

    private fun validateSolution(expected: IntArray, original: Grid, actual: Grid) {
        expected.forEachIndexed { index, value ->
            assertEquals(original.getCell(index).isGiven, actual.getCell(index).isGiven)
            assertEquals(value, actual.getCell(index).value)
        }
    }

}
