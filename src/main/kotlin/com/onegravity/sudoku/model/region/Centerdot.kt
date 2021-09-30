package com.onegravity.sudoku.model.region

import com.onegravity.sudoku.model.Puzzle
import com.onegravity.sudoku.model.computeNeighbors
import com.onegravity.sudoku.model.computeRegionIndices

class Centerdot(puzzle: Puzzle, regionCode: Int) :
    ExtraRegion(puzzle, RegionType.CENTERDOT, regionCode, regionCodes) {

    companion object {
        const val nrOfGroups = 1

        private val regionCodes = intArrayOf(
            -1,-1,-1,-1,-1,-1,-1,-1,-1,
            -1, 0,-1,-1, 0,-1,-1, 0,-1,
            -1,-1,-1,-1,-1,-1,-1,-1,-1,
            -1,-1,-1,-1,-1,-1,-1,-1,-1,
            -1, 0,-1,-1, 0,-1,-1, 0,-1,
            -1,-1,-1,-1,-1,-1,-1,-1,-1,
            -1,-1,-1,-1,-1,-1,-1,-1,-1,
            -1, 0,-1,-1, 0,-1,-1, 0,-1,
            -1,-1,-1,-1,-1,-1,-1,-1,-1
        )

        val indices by lazy { computeRegionIndices(regionCodes) }

        val neighbors by lazy { computeNeighbors(regionCodes) }
    }

}
