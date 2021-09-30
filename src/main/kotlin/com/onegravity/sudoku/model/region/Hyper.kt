package com.onegravity.sudoku.model.region

import com.onegravity.sudoku.model.Puzzle
import com.onegravity.sudoku.model.computeNeighbors
import com.onegravity.sudoku.model.computeRegionIndices

class Hyper(puzzle: Puzzle, regionCode: Int) :
    ExtraRegion(puzzle, RegionType.HYPER, regionCode, regionCodes) {

    companion object {
        const val nrOfGroups = 4

        private val regionCodes = intArrayOf(
            -1,-1,-1,-1,-1,-1,-1,-1,-1,
            -1, 0, 0, 0,-1, 1, 1, 1,-1,
            -1, 0, 0, 0,-1, 1, 1, 1,-1,
            -1, 0, 0, 0,-1, 1, 1, 1,-1,
            -1,-1,-1,-1,-1,-1,-1,-1,-1,
            -1, 2, 2, 2,-1, 3, 3, 3,-1,
            -1, 2, 2, 2,-1, 3, 3, 3,-1,
            -1, 2, 2, 2,-1, 3, 3, 3,-1,
            -1,-1,-1,-1,-1,-1,-1,-1,-1
        )

        val indices by lazy { computeRegionIndices(regionCodes) }

        val neighbors by lazy { computeNeighbors(regionCodes) }
    }

}
