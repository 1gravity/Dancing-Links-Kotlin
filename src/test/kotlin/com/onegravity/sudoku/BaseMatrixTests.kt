package com.onegravity.sudoku

import com.onegravity.sudoku.SudokuMatrix.Companion.getIndexValue
import com.onegravity.sudoku.SudokuMatrix.Companion.IndexValue
import com.onegravity.sudoku.model.CellPosition
import com.onegravity.sudoku.model.Grid
import com.onegravity.sudoku.model.region.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BaseMatrixTests {

    @Test
    fun testGetIndex() {
        for (subset in 0 until 9 * 9 * 9) {
            assertEquals(subset.div(9), getIndexValue(subset).index)
        }
    }

    @Test
    fun testGetIndexValue() {
        for (subset in 0 until 9 * 9 * 9) {
            val indexValue = getIndexValue(subset)
            assertEquals(subset.div(9), indexValue.index)
            assertEquals(subset.mod(9) + 1, indexValue.value)
        }
    }

    @Test
    fun testRegularSudoku() {
        testBaseMatrix(null, 0)
    }

    @Test
    fun testSudokusWithExtraRegions() {
        testBaseMatrix(RegionType.X, X.nrOfRegions)
        testBaseMatrix(RegionType.HYPER, Hyper.nrOfRegions)
        testBaseMatrix(RegionType.ASTERISK, Asterisk.nrOfRegions)
        testBaseMatrix(RegionType.COLOR, Color.nrOfRegions)
        testBaseMatrix(RegionType.CENTERDOT, Centerdot.nrOfRegions)
        testBaseMatrix(RegionType.PERCENT, Percent.nrOfRegions)
    }

    private fun testBaseMatrix(type: RegionType?, nrOfRegions: Int) {
        val grid = getTestGrid(extraRegionType = type)
        val baseMatrix = SudokuMatrix(grid).baseMatrix()

        // maps the subsets and the constraints to X (Boolean)
        // maps the subsets and the `cell` constraints to X (Boolean)
        // it's basically the cell part of the base matrix
        val cellMatrix = Array(729) { row ->
            Array(81) { col ->
                val base = col.times(9)
                val range = base until base+9
                row in range
            }
        }
        val rowMatrix = getRegionMatrix(grid, RegionType.ROW)
        val columnMatrix = getRegionMatrix(grid, RegionType.COLUMN)
        val blockMatrix = getRegionMatrix(grid, RegionType.BLOCK)

        // maps the four matrices to the offset in the constraint array
        val matrices = mutableMapOf(
            cellMatrix to Pair(0, 9 * 9),
            rowMatrix to Pair(9 * 9, 9 * 9),
            columnMatrix to Pair(9 * 9 * 2, 9 * 9),
            blockMatrix to Pair(9 * 9 * 3, 9 * 9)
        )
        if (type != null) {
            val extraRegionMatrix = getRegionMatrix(grid, type)
            matrices[extraRegionMatrix] = Pair(9 * 9 * 4, nrOfRegions * 9)
        }

        matrices.forEach {
            val (matrix, pair) = it
            val (offset, columns) = pair
            for (col in 0 until columns) {
                for (row in 0 until 9*9*9) {
                    checkRegion(expectedMatrix = matrix, actualMatrix = baseMatrix, offset = offset, row, col)
                }
            }
        }
    }

    private fun getRegionMatrix(grid: Grid, type: RegionType) = Array(729) { matrixRow ->
        val indices = grid.getIndices(type)
        val (cellIndex, cellValue) = getIndexValue(matrixRow)
        val cellPos = CellPosition(cellIndex)
        Array(81) { matrixCol ->
            val (regionNr, matrixValue) = matrixCol.getIndexValue()
            val regionIndices = if (indices.size > regionNr) indices[regionNr] else intArrayOf()
            regionIndices.contains(cellPos.index()) && matrixValue == (cellValue-1)
        }
    }

    private fun Int.getIndexValue() = IndexValue(this.div(9), this.mod(9))

    private fun checkRegion(
        expectedMatrix: Array<Array<Boolean>>,
        actualMatrix: Array<BooleanArray>,
        offset: Int,
        row: Int,
        col: Int
    ) {
        val expected = expectedMatrix[row][col]
        val actual = actualMatrix[row][col + offset]
        assertEquals(expected, actual)
    }

}
