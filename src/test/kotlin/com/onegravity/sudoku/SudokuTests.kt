package com.onegravity.sudoku

import com.onegravity.sudoku.model.Grid
import com.onegravity.sudoku.model.region.RegionType
import com.onegravity.dlx.PayloadProvider
import com.onegravity.dlx.toDLX
import com.onegravity.dlx.solve
import com.onegravity.dlx2.solve
import com.onegravity.dlx2.toDLX2
import com.onegravity.dlx3.solve
import com.onegravity.dlx3.toDLX3
import com.onegravity.sudoku.SudokuMatrix.Companion.getIndexValue
import com.onegravity.sudoku.SudokuMatrix.Companion.toSudokuMatrix
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SudokuTests {

    @Test
    fun testSudoku1() {
        val grid = getTestGrid(testSudoku1, null)
        testSudokuDLX(grid, testSudoku1Solution)
        testSudokuDLX2(grid, testSudoku1Solution)
        testSudokuDLX3(grid, testSudoku1Solution)
    }

    @Test
    fun testSudokuAlEscargot() {
        val grid = getTestGrid(testSudokuAlEscargot, null)
        testSudokuDLX(grid, testSudokuAlEscargotSolution)
        testSudokuDLX2(grid, testSudokuAlEscargotSolution)
        testSudokuDLX3(grid, testSudokuAlEscargotSolution)
    }

    @Test
    fun testJigsawSudoku() {
        val grid = getTestGrid(testSudokuJigsawValues, null, true, testSudokuJigsawBlocks)
        testSudokuDLX(grid, testSudokuJigsawSolution)
        testSudokuDLX2(grid, testSudokuJigsawSolution)
        testSudokuDLX3(grid, testSudokuJigsawSolution)
    }

    @Test
    fun testSudokuWithExtraRegions() {
        testSudokuDLX(getTestGrid(testXSudoku, extraRegionType = RegionType.X), testXSudokuSolution)
        testSudokuDLX2(getTestGrid(testXSudoku, extraRegionType = RegionType.X), testXSudokuSolution)
        testSudokuDLX3(getTestGrid(testXSudoku, extraRegionType = RegionType.X), testXSudokuSolution)
        testSudokuDLX(getTestGrid(testColorSudoku, extraRegionType = RegionType.COLOR), testColorSudokuSolution)
        testSudokuDLX2(getTestGrid(testColorSudoku, extraRegionType = RegionType.COLOR), testColorSudokuSolution)
        testSudokuDLX3(getTestGrid(testColorSudoku, extraRegionType = RegionType.COLOR), testColorSudokuSolution)
    }

    @Test
    fun testCompletedSudoku() {
        val grid = getTestGrid(testSudoku2, null)
        testSudokuDLX(grid, testSudoku2)
        testSudokuDLX2(grid, testSudoku2)
        testSudokuDLX3(grid, testSudoku2)
    }

    private fun testSudokuDLX(grid: Grid, solution: IntArray) {
        var solutionFound = false
        grid.toSudokuMatrix()
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

    private fun testSudokuDLX2(grid: Grid, solution: IntArray) {
        var solutionFound = false
        grid.toSudokuMatrix()
            .toDLX2()
            .solve { rows ->
                validateSolution(solution, grid, rows.toGrid(grid))
                solutionFound = true
            }
        assert(solutionFound)
    }

    private fun testSudokuDLX3(grid: Grid, solution: IntArray) {
        var solutionFound = false
        grid.toSudokuMatrix()
            .toDLX3(object: PayloadProvider {
                override fun getHeaderPayload(index: Int) = "h$index"
                override fun getDataPayload(col: Int, row: Int) = getIndexValue(row)
            })
            .solve { nodes ->
                validateSolution(solution, grid, nodes.toGridDLX3(grid))
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
