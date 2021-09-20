package com.onegravity.sudoku

import com.onegravity.sudoku.model.CellPosition
import com.onegravity.sudoku.model.Grid
import com.onegravity.sudoku.model.region.Block
import com.onegravity.sudoku.model.region.RegionType
import org.jetbrains.annotations.TestOnly
import java.util.*

/**
 * To understand the exact cover matrix for a Sudoku puzzle see sudoku_matrix.txt.
 *
 * Rows:
 * 81 cells (9*9) with each 9 possible digits -> 729 matrix rows
 *
 * Columns:
 * 81 cell, 81 row, 81 column, 81 block constraints + 9 constraints per extra region
 * (a Hyper Sudoku e.g. has 4 extra regions, an X Sudoku has 2)
 */
class SudokuMatrix(private val grid: Grid) {

    // helper functions
    companion object {
        /**
         * Convenience function to have a fluent api, e.g.:
         * grid.toSudokuMatrix()
         *     .toDLX()
         *     .solve { }
         */
        fun Grid.toSudokuMatrix() = SudokuMatrix(this).sudokuMatrix

        /**
         * Maps cell indices and value to a subset / row, e.g.
         * index 0 / value 1 -> 0
         * index 0 / value 2 -> 1
         * index 0 / value 3 -> 2
         * etc.
         *
         * (see also sudoku_matrix.txt)
         */
        fun getSubset(index: Int, value: Int) = indexValue2Subset["$index#$value"] ?: 0

        private val indexValue2Subset = HashMap<String, Int>().apply {
            for (index in 0..80) {
                for (value in 1..9) {
                    this["$index#$value"] = index * 9 + value-1
                }
            }
        }

        data class IndexValue(val index: Int, val value: Int)

        fun getIndexValue(subset: Int) = subset2IndexValue[subset]
            ?: throw NoSuchElementException("something's wrong with getIndexValue()")

        /**
         * Maps the 729 subsets / rows to cell indices + values, e.g.
         * subset 0 -> 0/1
         * subset 1 -> 0/2
         * subset 2 -> 0/3
         * ...
         * subset 9 -> 1/1
         * subset 10 -> 1/2
         * etc.
         *
         * (see also sudoku_matrix.txt)
         */
        private val subset2IndexValue = HashMap<Int, IndexValue>().apply {
            for (index in 0..728) {
                this[index] = IndexValue(index.div(9), index.mod(9) + 1)
            }
        }
    }

    // 81 cells (9*9) with each 9 possible digits -> 729 matrix rows
    private fun emptyMatrix() = Array(9  * 9 * 9) {
        // 81 cell, 81 row, 81 column, 81 block constraints + 9 constraints per extra region
        // (a Hyper Sudoku e.g. has 4 extra regions, an X Sudoku has 2)
        val extraRegions = grid.getRegions(grid.extraRegionType).size
        BooleanArray(4 * 9 * 9 + extraRegions * 9)
    }

    // the cover matrix for a generic Sudoku puzzle specified by the Grid (givens and placed digits not eliminated yet)
    @TestOnly
    fun baseMatrix() = emptyMatrix().apply {
        val regions = grid.getRegions(grid.extraRegionType)
        for (index in 0 until 81) {
            val pos = CellPosition(index)
            for (nr in 1..9) {
                val row = index * 9 + nr - 1
                this[row][index] = true                                     // cell constraint
                this[row][81 + pos.row * 9 + nr - 1] = true                 // row constraint
                this[row][2 * 81 + pos.col * 9 + nr - 1] = true             // column constraint
                // block constraint
                val block = grid.getRegionAtOrThrow(pos.col, pos.row, RegionType.BLOCK) as Block
                this[row][3 * 81 + block.blockCode * 9 + nr - 1] = true
                // extra region constraint
                regions.forEachIndexed { regionIndex, region ->
                    if (region.contains(grid.getCell(index))) {
                        this[row][4 * 81 + regionIndex * 9 + nr - 1] = true
                    }
                }
            }
        }
    }

    // the cover matrix for this specific Grid with givens already eliminated
    val sudokuMatrix = baseMatrix().apply {
        for (index in 0..80) {
            val cell = grid.getCell(index)
            if (cell.isGiven && !cell.isEmpty()) {
                // subset is the row describing the given value and as such part of the solution
                val subset = getSubset(index, cell.value)
                this[subset].forEachIndexed { col, isSet ->
                    if (isSet) {
                        // -> eliminate all rows that conflict with the subset/given value
                        cover(this, col, subset)
                    }
                }
            }
        }
    }

    /**
     * All rows that cover constraints that the given already covers are eliminated from the cover matrix.
     *
     * @param matrix the cover matrix
     * @param col the column = constraint that the given already covers
     * @param excludeRow don't eliminate the given's row itself
     */
    private fun cover(matrix: Array<BooleanArray>, col: Int, excludeRow: Int) {
        for (row in matrix.indices) {
            if (row != excludeRow && matrix[row][col]) {
                Arrays.fill(matrix[row], false)     // clear the row
            }
        }
    }

}
