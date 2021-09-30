package com.onegravity.sudoku.model.region

import com.onegravity.sudoku.model.Puzzle
import com.onegravity.sudoku.model.computeNeighbors
import com.onegravity.sudoku.model.computeRegionIndices

class Asterisk(puzzle: Puzzle, regionCode: Int) :
    ExtraRegion(puzzle, RegionType.ASTERISK, regionCode, regionCodes) {

    companion object {
        const val nrOfGroups = 1

        val regionCodes = intArrayOf(
            -1,-1,-1,-1,-1,-1,-1,-1,-1,
            -1,-1,-1,-1, 0,-1,-1,-1,-1,
            -1,-1, 0,-1,-1,-1, 0,-1,-1,
            -1,-1,-1,-1,-1,-1,-1,-1,-1,
            -1, 0,-1,-1, 0,-1,-1, 0,-1,
            -1,-1,-1,-1,-1,-1,-1,-1,-1,
            -1,-1, 0,-1,-1,-1, 0,-1,-1,
            -1,-1,-1,-1, 0,-1,-1,-1,-1,
            -1,-1,-1,-1,-1,-1,-1,-1,-1
        )

        val indices by lazy { computeRegionIndices(regionCodes) }

        val neighbors by lazy { computeNeighbors(regionCodes) }
    }

}
