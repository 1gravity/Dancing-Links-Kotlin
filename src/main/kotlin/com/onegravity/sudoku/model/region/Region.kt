package com.onegravity.sudoku.model.region

import com.onegravity.sudoku.model.Cell

/**
 * Region defines a region within a Sudoku puzzle like a block, row, column or an ExtraRegion.
 */
abstract class Region<C: Cell>(val regionType: RegionType, val regionCode: Int) : Comparable<Region<C>>, Cloneable {

    /**
     * The cells of this region sorted by cell index.
     */
    abstract val cells: List<C>

    /**
     * Determines whether the Regions contains a specific cell.
     *
     * @param cell the Cell we want to check against
     *
     * @return True if the region contains the Cell, False otherwise
     */
    fun contains(cell: C) = cells.contains(cell)

    /**
     * Return intersection between this and another Region
     *
     * @param other the other region
     *
     * @return the cells that are part of both regions
     */
    fun intersection(other: Region<C>): Collection<Cell> = cells.intersect(other.cells.toList())

    /**
     * Test whether this region intersects another region.
     * A region intersects another region if they have at least one common cell.
     *
     * @param other the other region
     *
     * @return whether this region crosses the other region.
     */
    fun intersects(other: Region<C>) = !intersection(other).isEmpty()

    /**
     * Get a string representation of this region
     */
    abstract override fun toString(): String

}
