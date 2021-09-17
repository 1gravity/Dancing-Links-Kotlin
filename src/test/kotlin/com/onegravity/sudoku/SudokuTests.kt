package com.onegravity.sudoku

import com.onegravity.sudoku.model.Grid
import com.onegravity.sudoku.model.region.RegionType
import com.onegravity.dlx.PayloadProvider
import com.onegravity.dlx.getDLX
import com.onegravity.dlx.model.DLXNode
import com.onegravity.dlx.solve
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
            .getDLX(object: PayloadProvider {
                override fun getHeaderPayload(index: Int) = "h$index"
                override fun getDataPayload(col: Int, row: Int) = getIndexValue(row)
            })
            .solve {
                validateSolution(solution, getGrid(grid, it))
                solutionFound = true
            }
        assert(solutionFound)
    }

    private fun validateSolution(expected: IntArray, actual: Grid) {
        expected.forEachIndexed { index, value ->
            assertEquals(value, actual.getCell(index).value)
        }
    }

    private val sudokuComparator = Comparator<DLXNode> { n0, n1 ->
        val p0 = (n0.payload as IndexValue)
        val p1 = (n1.payload as IndexValue)
        p0.index.compareTo(p1.index)
    }

    private fun getGrid(original: Grid, solution: Collection<DLXNode>): Grid {
        val grid = Grid(null, false)
        solution.sortedWith(sudokuComparator).forEach { node ->
            with(node.payload as IndexValue) {
                grid.setValue(index, value, original.getCell(index).isGiven)
            }
        }
        return grid
    }
}
