package com.onegravity.sudoku.model.region

import com.onegravity.sudoku.model.Cell
import com.onegravity.sudoku.model.Puzzle
import com.onegravity.sudoku.model.mapCodes2Indices

/**
 * ExtraRegion defines an extra region in the puzzle (beyond the regular Regions Block, Row and Column).
 *
 * @param puzzle We need the Puzzle to retrieve the Cells that are part of this ExtraRegion.
 * @param regionType The type of ExtraRegion (X, Hyper, Color etc.)
 * @param regionCode The regionCode defines which ExtraRegion this is. It's the equivalent to the blockCode for Blocks.
 * @param regionCodes The regionCodes defines which Cells are part of which region. Together with the regionCode this
 * ExtraRegion can determine which cells are part of this region.
 */
abstract class ExtraRegion(
    private val puzzle: Puzzle,
    regionType: RegionType,
    regionCode: Int,
    private vararg val regionCodes: IntArray
) : Region(regionType, regionCode) {

    init {
        assert(regionType.isExtraRegion)
    }

    open val nrOfRegions: Int = 1

    @Suppress("UNCHECKED_CAST")
    override val cells by lazy {
        val cells = ArrayList<Cell>()
        val cellIndices = mapCodes2Indices(regionCodes[0])[regionCode]
        cellIndices?.forEach { index ->
            cells.add(puzzle.getCell(index))
        }
        cells
    }

    override fun toString() = "${regionType.name} $regionCode/$nrOfRegions"

    override fun compareTo(other: Region) =
        when (other is ExtraRegion) {
            true -> if (
                regionType == other.regionType &&
                regionCode == other.regionCode &&
                cells.containsAll(other.cells)
            ) 0 else -1
            else -> -1
        }

}
