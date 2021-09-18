package com.onegravity.sudoku.model.region

import com.onegravity.sudoku.model.Cell
import com.onegravity.sudoku.model.Puzzle
import com.onegravity.sudoku.model.computeRegionIndices
import com.onegravity.sudoku.model.computeNeighbors

/**
 * A column in the Sudoku puzzle.
 */
class Column<C : Cell>(private val puzzle: Puzzle<C>, private val columnIndex: Int) :
    Region<C>(RegionType.COLUMN) {

    override val cells = (0 until 9).fold(ArrayList<C>()) { list, rowIndex ->
        list.add(puzzle.getCell(columnIndex, rowIndex))
        list
    }

    override fun toString() = "C$columnIndex"

    override fun compareTo(other: Region<C>) =
        when (other is Column) {
            true -> columnIndex.compareTo(other.columnIndex)
            else -> -1
        }

    companion object {
        // region codes for columns
        // (looks diagonally mirrored because we want to use (column, row) coordinates)
        private val regionCodes = arrayOf(
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1),
            intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2),
            intArrayOf(3, 3, 3, 3, 3, 3, 3, 3, 3),
            intArrayOf(4, 4, 4, 4, 4, 4, 4, 4, 4),
            intArrayOf(5, 5, 5, 5, 5, 5, 5, 5, 5),
            intArrayOf(6, 6, 6, 6, 6, 6, 6, 6, 6),
            intArrayOf(7, 7, 7, 7, 7, 7, 7, 7, 7),
            intArrayOf(8, 8, 8, 8, 8, 8, 8, 8, 8)
        )

        val indices by lazy { computeRegionIndices(regionCodes) }

        val neighbors by lazy { computeNeighbors(regionCodes) }
    }
}