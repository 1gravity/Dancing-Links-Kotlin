package com.onegravity.sudoku.model.region

import com.onegravity.sudoku.model.Puzzle
import com.onegravity.sudoku.model.computeNeighbors
import com.onegravity.sudoku.model.computeRegionIndices

class Color(puzzle: Puzzle, regionCode: Int) :
    ExtraRegion(puzzle, RegionType.COLOR, regionCode, regionCodes) {

    companion object {
        const val nrOfRegions: Int = 9

        private val regionCodes = intArrayOf(
            0, 1, 2, 0, 1, 2, 0, 1, 2,
            3, 4, 5, 3, 4, 5, 3, 4, 5,
            6, 7, 8, 6, 7, 8, 6, 7, 8,
            0, 1, 2, 0, 1, 2, 0, 1, 2,
            3, 4, 5, 3, 4, 5, 3, 4, 5,
            6, 7, 8, 6, 7, 8, 6, 7, 8,
            0, 1, 2, 0, 1, 2, 0, 1, 2,
            3, 4, 5, 3, 4, 5, 3, 4, 5,
            6, 7, 8, 6, 7, 8, 6, 7, 8,
        )

        val indices by lazy { computeRegionIndices(regionCodes) }

        val neighbors by lazy { computeNeighbors(regionCodes) }
    }

}
