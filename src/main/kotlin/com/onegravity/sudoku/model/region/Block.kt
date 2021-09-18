package com.onegravity.sudoku.model.region

import com.onegravity.sudoku.model.Cell

/**
 * A block in the Sudoku puzzle.
 *
 * @param cells unlike Rows and Columns, Blocks don't retrieve the cells that are part of the block. For performance
 * reasons the Grid does that job and just passes the List of Cells to the blocks.
 * @param blockCode the blockCode defines which block this is (0 to 8). It's the equivalent of a region code for extra
 * regions.
 */
class Block<C : Cell>(override val cells: List<C>, val blockCode: Int) :
    Region<C>(RegionType.BLOCK) {

    init {
        assert(cells.size == 9)
        assert(blockCode in 0..8)
    }

    override fun toString() = "B$blockCode"

    override fun compareTo(other: Region<C>) =
        when (other is Block) {
            true -> if (blockCode == other.blockCode) 0 else -1
            else -> -1
        }

    companion object {
        // region codes for standard blocks (not Jigsaw/squiggly)
        // (looks diagonally mirrored because we want to use (column, row) coordinates)
        val regionCodes = arrayOf(
            intArrayOf(0, 0, 0, 3, 3, 3, 6, 6, 6),
            intArrayOf(0, 0, 0, 3, 3, 3, 6, 6, 6),
            intArrayOf(0, 0, 0, 3, 3, 3, 6, 6, 6),
            intArrayOf(1, 1, 1, 4, 4, 4, 7, 7, 7),
            intArrayOf(1, 1, 1, 4, 4, 4, 7, 7, 7),
            intArrayOf(1, 1, 1, 4, 4, 4, 7, 7, 7),
            intArrayOf(2, 2, 2, 5, 5, 5, 8, 8, 8),
            intArrayOf(2, 2, 2, 5, 5, 5, 8, 8, 8),
            intArrayOf(2, 2, 2, 5, 5, 5, 8, 8, 8)
        )
    }

}
