package com.onegravity.sudoku.model.region

import com.onegravity.sudoku.model.Cell
import com.onegravity.sudoku.model.Puzzle
import com.onegravity.sudoku.model.computeNeighbors

/**
 * A row in the Sudoku puzzle.
 */
class Row(private val puzzle: Puzzle, rowIndex: Int) :
    Region(RegionType.ROW, rowIndex) {

    override val cells = (0 until 9).fold(ArrayList<Cell>()) { list, colIndex ->
        list.add(puzzle.getCell(colIndex, rowIndex))
        list
    }

    override fun toString() = "R$regionCode"

    override fun compareTo(other: Region) =
        when (other is Row) {
            true -> regionCode.compareTo(other.regionCode)
            else -> -1
        }

    companion object {
        // region codes for rows
        private val regionCodes = intArrayOf(
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            1, 1, 1, 1, 1, 1, 1, 1, 1,
            2, 2, 2, 2, 2, 2, 2, 2, 2,
            3, 3, 3, 3, 3, 3, 3, 3, 3,
            4, 4, 4, 4, 4, 4, 4, 4, 4,
            5, 5, 5, 5, 5, 5, 5, 5, 5,
            6, 6, 6, 6, 6, 6, 6, 6, 6,
            7, 7, 7, 7, 7, 7, 7, 7, 7,
            8, 8, 8, 8, 8, 8, 8, 8, 8
        )

        val indices = arrayOf(
            intArrayOf( 0, 1, 2, 3, 4, 5, 6, 7, 8),
            intArrayOf( 9,10,11,12,13,14,15,16,17),
            intArrayOf(18,19,20,21,22,23,24,25,26),
            intArrayOf(27,28,29,30,31,32,33,34,35),
            intArrayOf(36,37,38,39,40,41,42,43,44),
            intArrayOf(45,46,47,48,49,50,51,52,53),
            intArrayOf(54,55,56,57,58,59,60,61,62),
            intArrayOf(63,64,65,66,67,68,69,70,71),
            intArrayOf(72,73,74,75,76,77,78,79,80),
        )
        // equivalent to
        // val indices by lazy { computeRegionIndices(regionCodes) }

        val neighbors by lazy { computeNeighbors(regionCodes) }
    }

}
