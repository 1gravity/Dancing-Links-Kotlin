package com.onegravity.sudoku.model.region

import com.onegravity.sudoku.model.Cell
import com.onegravity.sudoku.model.Puzzle
import com.onegravity.sudoku.model.mapCodes2Positions

abstract class ExtraRegion<C : Cell>(
    private val puzzle: Puzzle<C>,
    regionType: RegionType,
    val regionIndex: Int,
    private vararg val codesArray: Array<IntArray>
) : Region<C>(regionType) {

    open val nrOfRegions: Int = 1

    @Suppress("UNCHECKED_CAST")
    override val cells by lazy {
        val cells = ArrayList<C>()
        val codes2CellPositions = mapCodes2Positions(*codesArray)
        val region = codes2CellPositions[regionIndex]
        region?.forEach { position ->
            cells.add(puzzle.getCell(position.col, position.row))
        }
        cells
    }

    override fun toString() = "${regionType.name} $regionIndex/$nrOfRegions"

    override fun compareTo(other: Region<C>) =
        when (other is ExtraRegion) {
            true -> if (
                regionType == other.regionType &&
                regionIndex == other.regionIndex &&
                cells.containsAll(other.cells)
            ) 0 else -1
            else -> -1
        }

}
