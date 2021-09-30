package com.onegravity.sudoku.model.region

import com.onegravity.sudoku.model.Cell

/**
 * A block in the Sudoku puzzle.
 *
 * @param cells unlike Rows and Columns, Blocks don't retrieve the cells that are part of the block.
 * For performance reasons the Grid does that job and just passes the List of Cells to the blocks.
 * @param regionCode the regionCode defines which block this is (0 to 8).
 */
class Block(override val cells: List<Cell>, regionCode: Int) :
    Region(RegionType.BLOCK, regionCode) {

    init {
        assert(cells.size == 9)
        assert(regionCode in 0..8)
    }

    override fun toString() = "B$regionCode"

    override fun compareTo(other: Region) =
        when (other is Block) {
            true -> if (regionCode == other.regionCode) 0 else -1
            else -> -1
        }

    companion object {
        // cell indices for standard blocks (not Jigsaw/squiggly)
        val indices = arrayOf(
            intArrayOf( 0, 1, 2, 9,10,11,18,19,20),
            intArrayOf( 3, 4, 5,12,13,14,21,22,23),
            intArrayOf( 6, 7, 8,15,16,17,24,25,26),
            intArrayOf(27,28,29,36,37,38,45,46,47),
            intArrayOf(30,31,32,39,40,41,48,49,50),
            intArrayOf(33,34,35,42,43,44,51,52,53),
            intArrayOf(54,55,56,63,64,65,72,73,74),
            intArrayOf(57,58,59,66,67,68,75,76,77),
            intArrayOf(60,61,62,69,70,71,78,79,80),
        )

        // region codes for standard blocks (not Jigsaw/squiggly)
        val regionCodes = intArrayOf(
            0,0,0,1,1,1,2,2,2,
            0,0,0,1,1,1,2,2,2,
            0,0,0,1,1,1,2,2,2,
            3,3,3,4,4,4,5,5,5,
            3,3,3,4,4,4,5,5,5,
            3,3,3,4,4,4,5,5,5,
            6,6,6,7,7,7,8,8,8,
            6,6,6,7,7,7,8,8,8,
            6,6,6,7,7,7,8,8,8
        )
    }

}
