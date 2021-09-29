package com.onegravity.sudoku.model.region

import com.onegravity.sudoku.model.Cell
import com.onegravity.sudoku.model.Puzzle
import com.onegravity.sudoku.model.computeRegionIndices
import com.onegravity.sudoku.model.computeNeighbors

class Percent<C : Cell>(puzzle: Puzzle<C>, regionCode: Int) :
    ExtraRegion<C>(puzzle, RegionType.PERCENT, regionCode, regionCodes) {

    companion object {
        const val nrOfGroups = 3

        private val regionCodes = intArrayOf(
            -1,-1,-1,-1,-1,-1,-1,-1, 2,
            -1, 0, 0, 0,-1,-1,-1, 2,-1,
            -1, 0, 0, 0,-1,-1, 2,-1,-1,
            -1, 0, 0, 0,-1, 2,-1,-1,-1,
            -1,-1,-1,-1, 2,-1,-1,-1,-1,
            -1,-1,-1, 2,-1, 1, 1, 1,-1,
            -1,-1, 2,-1,-1, 1, 1, 1,-1,
            -1, 2,-1,-1,-1, 1, 1, 1,-1,
             2,-1,-1,-1,-1,-1,-1,-1,-1
        )

        val indices by lazy { computeRegionIndices(regionCodes) }

        val neighbors by lazy { computeNeighbors(regionCodes) }
    }

}
