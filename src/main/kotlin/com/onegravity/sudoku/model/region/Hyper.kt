package com.onegravity.sudoku.model.region

import com.onegravity.sudoku.model.Cell
import com.onegravity.sudoku.model.Puzzle
import com.onegravity.sudoku.model.computeRegionIndices
import com.onegravity.sudoku.model.computeNeighbors

class Hyper<C : Cell>(puzzle: Puzzle<C>, regionCode: Int) :
    ExtraRegion<C>(puzzle, RegionType.HYPER, regionCode, regionCodes) {

    companion object {
        const val nrOfRegions: Int = 4

        // (looks diagonally mirrored because we want to use (column, row) coordinates)
        private val regionCodes = arrayOf(
            intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1,-1),
            intArrayOf(-1, 0, 0, 0,-1, 2, 2, 2,-1),
            intArrayOf(-1, 0, 0, 0,-1, 2, 2, 2,-1),
            intArrayOf(-1, 0, 0, 0,-1, 2, 2, 2,-1),
            intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1,-1),
            intArrayOf(-1, 1, 1, 1,-1, 3, 3, 3,-1),
            intArrayOf(-1, 1, 1, 1,-1, 3, 3, 3,-1),
            intArrayOf(-1, 1, 1, 1,-1, 3, 3, 3,-1),
            intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1,-1)
        )

        val indices by lazy { computeRegionIndices(regionCodes) }

        val neighbors by lazy { computeNeighbors(regionCodes) }
    }

}
