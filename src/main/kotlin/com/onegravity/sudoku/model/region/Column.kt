package com.onegravity.sudoku.model.region

import com.onegravity.sudoku.model.Cell
import com.onegravity.sudoku.model.Puzzle
import com.onegravity.sudoku.model.computeNeighbors

/**
 * A column in the Sudoku puzzle.
 */
class Column<C : Cell>(private val puzzle: Puzzle<C>, columnIndex: Int) :
    Region<C>(RegionType.COLUMN, columnIndex) {

    override val cells = (0 until 9).fold(ArrayList<C>()) { list, rowIndex ->
        list.add(puzzle.getCell(columnIndex, rowIndex))
        list
    }

    override fun toString() = "C$regionCode"

    override fun compareTo(other: Region<C>) =
        when (other is Column) {
            true -> regionCode.compareTo(other.regionCode)
            else -> -1
        }

    companion object {
        // region codes for columns
        private val regionCodes = intArrayOf(
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            0, 1, 2, 3, 4, 5, 6, 7, 8
        )

        val indices = arrayOf(
            intArrayOf( 0, 9,18,27,36,45,54,63,72),
            intArrayOf( 1,10,19,28,37,46,55,64,73),
            intArrayOf( 2,11,20,29,38,47,56,65,74),
            intArrayOf( 3,12,21,30,39,48,57,66,75),
            intArrayOf( 4,13,22,31,40,49,58,67,76),
            intArrayOf( 5,14,23,32,41,50,59,68,77),
            intArrayOf( 6,15,24,33,42,51,60,69,78),
            intArrayOf( 7,16,25,34,43,52,61,70,79),
            intArrayOf( 8,17,26,35,44,53,62,71,80)
        )
        // equivalent to
        // val indices by lazy { computeRegionIndices(regionCodes) }

        val neighbors by lazy { computeNeighbors(regionCodes) }
    }
}