package com.onegravity.sudoku.model.region

import com.onegravity.sudoku.model.Cell
import com.onegravity.sudoku.model.Puzzle
import com.onegravity.sudoku.model.computeRegionIndices
import com.onegravity.sudoku.model.computeNeighbors

class Centerdot<C : Cell>(puzzle: Puzzle<C>, regionCode: Int) :
    ExtraRegion<C>(puzzle, RegionType.CENTERDOT, regionCode, regionCodes) {

    companion object {
        const val nrOfRegions: Int = 1

        private val regionCodes = arrayOf(
            intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1,-1),
            intArrayOf(-1, 0,-1,-1, 0,-1,-1, 0,-1),
            intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1,-1),
            intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1,-1),
            intArrayOf(-1, 0,-1,-1, 0,-1,-1, 0,-1),
            intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1,-1),
            intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1,-1),
            intArrayOf(-1, 0,-1,-1, 0,-1,-1, 0,-1),
            intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1,-1)
        )

        val indices by lazy { computeRegionIndices(regionCodes) }

        val neighbors by lazy { computeNeighbors(regionCodes) }
    }

}
