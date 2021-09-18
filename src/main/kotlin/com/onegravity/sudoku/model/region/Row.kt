package com.onegravity.sudoku.model.region

import com.onegravity.sudoku.model.Cell
import com.onegravity.sudoku.model.Puzzle
import com.onegravity.sudoku.model.computeRegionIndices
import com.onegravity.sudoku.model.computeNeighbors

/**
 * A row in the Sudoku puzzle.
 */
class Row<C : Cell>(private val puzzle: Puzzle<C>, private val rowIndex: Int) :
    Region<C>(RegionType.ROW) {

    override val cells = (0 until 9).fold(ArrayList<C>()) { list, colIndex ->
        list.add(puzzle.getCell(colIndex, rowIndex))
        list
    }

    override fun toString() = "R$rowIndex"

    override fun compareTo(other: Region<C>) =
        when (other is Row) {
            true -> rowIndex.compareTo(other.rowIndex)
            else -> -1
        }

    companion object {
        // region codes for rows
        // (looks diagonally mirrored because we want to use (column, row) coordinates)
        private val regionCodes = arrayOf(
            intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8),
            intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8),
            intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8),
            intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8),
            intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8),
            intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8),
            intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8),
            intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8),
            intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8)
        )

        val indices by lazy { computeRegionIndices(regionCodes) }

        val neighbors by lazy { computeNeighbors(regionCodes) }
    }

}
