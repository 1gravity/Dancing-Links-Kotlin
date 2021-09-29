package com.onegravity.sudoku.model.region

import com.onegravity.sudoku.model.Cell
import com.onegravity.sudoku.model.Puzzle
import com.onegravity.sudoku.model.computeRegionIndices
import com.onegravity.sudoku.model.computeNeighbors

class Color<C : Cell>(puzzle: Puzzle<C>, regionCode: Int) :
    ExtraRegion<C>(puzzle, RegionType.COLOR, regionCode, regionCodes) {

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
