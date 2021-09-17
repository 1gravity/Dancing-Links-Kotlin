package com.onegravity.sudoku

import com.onegravity.sudoku.SudokuMatrix.Companion.getSubset
import com.onegravity.sudoku.model.CellPosition
import com.onegravity.sudoku.model.Grid
import com.onegravity.sudoku.model.region.Block
import com.onegravity.sudoku.model.region.RegionType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SudokuMatrixTests {

    @Test
    fun testGetSubset() {
        for (index in 0 until 9 * 9) {
            for (value in 1..9) {
                assertEquals(index * 9 + value - 1, getSubset(index, value))
            }
        }
    }

    @Test
    fun testSudokuMatrix() {
        val grid = getTestGrid(testSudoku1, null)
        val matrix = SudokuMatrix(grid).sudokuMatrix

        val candidates = arrayOf(
            intArrayOf(3,7,8,9),intArrayOf(4),intArrayOf(3,6,7,9),intArrayOf(5),intArrayOf(2,3,6,7),intArrayOf(2,3,8,9),intArrayOf(2,8),intArrayOf(1),intArrayOf(2,3,8,9),
            intArrayOf(1,3,7,8,9),intArrayOf(3,6,7,8,9),intArrayOf(1,3,5,6,7,9),intArrayOf(1,3,4,6,8,9),intArrayOf(1,2,3,4,6,7),intArrayOf(2,3,4,8,9),intArrayOf(2,4,5,8),intArrayOf(2,3,4,5,8,9),intArrayOf(2,3,5,8,9),
            intArrayOf(1,3,8,9),intArrayOf(3,8,9),intArrayOf(2),intArrayOf(1,3,4,8,9),intArrayOf(1,3,4),intArrayOf(3,4,8,9),intArrayOf(4,5,8),intArrayOf(7),intArrayOf(6),
            intArrayOf(1,2,3,7),intArrayOf(2,3,7),intArrayOf(8),intArrayOf(1,3),intArrayOf(9),intArrayOf(3,5),intArrayOf(6),intArrayOf(2,5),intArrayOf(4),
            intArrayOf(1,2,9),intArrayOf(2,6,9),intArrayOf(1,6,9),intArrayOf(1,4,6,8),intArrayOf(1,4,5,6),intArrayOf(7),intArrayOf(3),intArrayOf(2,5,8,9),intArrayOf(1,2,5,8,9),
            intArrayOf(1,3,7,9),intArrayOf(5),intArrayOf(4),intArrayOf(2),intArrayOf(1,3,6),intArrayOf(3,8),intArrayOf(1,7,8),intArrayOf(8,9),intArrayOf(1,7,8,9),
            intArrayOf(2,3,4,7,8,9),intArrayOf(1),intArrayOf(3,7,9),intArrayOf(3,4,9),intArrayOf(2,3,4,5),intArrayOf(2,3,4,5,9),intArrayOf(2,4,5,7,8),intArrayOf(2,3,4,5,6,8),intArrayOf(2,3,5,7,8),
            intArrayOf(5),intArrayOf(2,3,7,8,9),intArrayOf(3,7,9),intArrayOf(3,4,9),intArrayOf(2,3,4),intArrayOf(6),intArrayOf(1,2,4,7,8),intArrayOf(2,3,4,8),intArrayOf(1,2,3,7,8),
            intArrayOf(6),intArrayOf(2,3),intArrayOf(3),intArrayOf(7),intArrayOf(8),intArrayOf(1),intArrayOf(9),intArrayOf(2,3,4,5),intArrayOf(2,3,5),
        )
        for (cellIndex in candidates.indices) {
            val cellPos = CellPosition(cellIndex)
            // verify that all candidates are covered by the matrix
            for (value in candidates[cellIndex]) {
                checkRegions(grid, matrix, cellPos, value, true)
            }

            // verify that all eliminated candidates are NOT covered by the matrix
            val allCandidates = intArrayOf(1,2,3,4,5,6,7,8,9).toMutableList()
            val notCandidates = allCandidates.minus(candidates[cellIndex].toList())
            for (value in notCandidates) {
                checkRegions(grid, matrix, cellPos, value, false)
            }
        }
    }

    private fun checkRegions(
        grid: Grid,
        matrix: Array<BooleanArray>,
        cellPos: CellPosition,
        value: Int,
        expectedValue: Boolean
    ) {
        val subset = getSubset(cellPos.index(), value)
        // cell constraint
        assertEquals(expectedValue, matrix[subset][cellPos.index()])
        // row constraint
        assertEquals(expectedValue, matrix[subset][81 + cellPos.row * 9 + value-1])
        // column constraint
        assertEquals(expectedValue, matrix[subset][81*2 + cellPos.col * 9 + value-1])
        // block constraint
        val block = grid.getRegionAtOrThrow(cellPos.col, cellPos.row, RegionType.BLOCK) as Block
        assertEquals(expectedValue, matrix[subset][81*3 + block.blockCode * 9 + value-1])
    }

}
