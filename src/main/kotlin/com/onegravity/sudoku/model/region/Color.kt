package com.onegravity.sudoku.model.region

import com.onegravity.sudoku.model.Cell
import com.onegravity.sudoku.model.Puzzle
import com.onegravity.sudoku.model.computeRegionIndices
import com.onegravity.sudoku.model.computeNeighbors

class Color<C : Cell>(puzzle: Puzzle<C>, regionIndex: Int) :
    ExtraRegion<C>(puzzle, RegionType.COLOR, regionIndex, regionCodes) {

    companion object {
        const val nrOfRegions: Int = 9

        // (looks diagonally mirrored because we want to use (column, row) coordinates)
        private val regionCodes = arrayOf(
            intArrayOf(0, 3, 6, 0, 3, 6, 0, 3, 6),
            intArrayOf(1, 4, 7, 1, 4, 7, 1, 4, 7),
            intArrayOf(2, 5, 8, 2, 5, 8, 2, 5, 8),
            intArrayOf(0, 3, 6, 0, 3, 6, 0, 3, 6),
            intArrayOf(1, 4, 7, 1, 4, 7, 1, 4, 7),
            intArrayOf(2, 5, 8, 2, 5, 8, 2, 5, 8),
            intArrayOf(0, 3, 6, 0, 3, 6, 0, 3, 6),
            intArrayOf(1, 4, 7, 1, 4, 7, 1, 4, 7),
            intArrayOf(2, 5, 8, 2, 5, 8, 2, 5, 8)
        )

        val indices by lazy { computeRegionIndices(regionCodes) }

        val neighbors by lazy { computeNeighbors(regionCodes) }
    }

}
