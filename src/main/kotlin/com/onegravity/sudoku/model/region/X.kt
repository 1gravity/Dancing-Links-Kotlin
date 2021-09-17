package com.onegravity.sudoku.model.region

import com.onegravity.sudoku.model.Cell
import com.onegravity.sudoku.model.Puzzle
import com.onegravity.sudoku.model.computeRegionIndices
import com.onegravity.sudoku.model.computeNeighbors

class X<C : Cell>(puzzle: Puzzle<C>, regionIndex: Int) :
    ExtraRegion<C>(puzzle, RegionType.X, regionIndex, regionCodes1, regionCodes2) {

    companion object {
        const val nrOfRegions: Int = 2

        // we need two block definitions for the X-Sudoku because that's the only puzzle type with
        // overlapping regions (R4C4 is in both regions)
        private val regionCodes1 = arrayOf(
            intArrayOf( 0,-1,-1,-1,-1,-1,-1,-1,-1),
            intArrayOf(-1, 0,-1,-1,-1,-1,-1,-1,-1),
            intArrayOf(-1,-1, 0,-1,-1,-1,-1,-1,-1),
            intArrayOf(-1,-1,-1, 0,-1,-1,-1,-1,-1),
            intArrayOf(-1,-1,-1,-1, 0,-1,-1,-1,-1),
            intArrayOf(-1,-1,-1,-1,-1, 0,-1,-1,-1),
            intArrayOf(-1,-1,-1,-1,-1,-1, 0,-1,-1),
            intArrayOf(-1,-1,-1,-1,-1,-1,-1, 0,-1),
            intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1, 0),
        )
        private val regionCodes2 = arrayOf(
            intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1, 1),
            intArrayOf(-1,-1,-1,-1,-1,-1,-1, 1,-1),
            intArrayOf(-1,-1,-1,-1,-1,-1, 1,-1,-1),
            intArrayOf(-1,-1,-1,-1,-1, 1,-1,-1,-1),
            intArrayOf(-1,-1,-1,-1, 1,-1,-1,-1,-1),
            intArrayOf(-1,-1,-1, 1,-1,-1,-1,-1,-1),
            intArrayOf(-1,-1, 1,-1,-1,-1,-1,-1,-1),
            intArrayOf(-1, 1,-1,-1,-1,-1,-1,-1,-1),
            intArrayOf( 1,-1,-1,-1,-1,-1,-1,-1,-1),
        )

        val indices by lazy { computeRegionIndices(regionCodes1, regionCodes2) }

        val neighbors by lazy { computeNeighbors(regionCodes1, regionCodes2) }
    }
}
