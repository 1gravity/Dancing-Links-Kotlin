package com.onegravity.sudoku.model.region

import com.onegravity.sudoku.model.Cell
import com.onegravity.sudoku.model.Puzzle
import com.onegravity.sudoku.model.mapCodes2Positions

/**
 * ExtraRegion defines an extra region in the puzzle (beyond the regular Regions Block, Row and Column).
 *
 * @param puzzle We need the Puzzle to retrieve the Cells that are part of this ExtraRegion.
 * @param regionType The type of ExtraRegion (X, Hyper, Color etc.)
 * @param regionCode The regionCode defines which ExtraRegion this is. It's the equivalent to the blockCode for Blocks.
 * @param regionCodes The regionCodes defines which Cells are part of which region. Together with the regionCode this
 * ExtraRegion can determine which cells are part of this region.
 */
abstract class ExtraRegion<C : Cell>(
    private val puzzle: Puzzle<C>,
    regionType: RegionType,
    val regionCode: Int,
    private vararg val regionCodes: Array<IntArray>
) : Region<C>(regionType) {

    init {
        assert(regionType.isExtraRegion)
    }

    open val nrOfRegions: Int = 1

    @Suppress("UNCHECKED_CAST")
    override val cells by lazy {
        val cells = ArrayList<C>()
        val codes2CellPositions = mapCodes2Positions(regionCodes[0])
        val region = codes2CellPositions[regionCode]
        region?.forEach { position ->
            cells.add(puzzle.getCell(position.col, position.row))
        }
        cells
    }

    override fun toString() = "${regionType.name} $regionCode/$nrOfRegions"

    override fun compareTo(other: Region<C>) =
        when (other is ExtraRegion) {
            true -> if (
                regionType == other.regionType &&
                regionCode == other.regionCode &&
                cells.containsAll(other.cells)
            ) 0 else -1
            else -> -1
        }

}
