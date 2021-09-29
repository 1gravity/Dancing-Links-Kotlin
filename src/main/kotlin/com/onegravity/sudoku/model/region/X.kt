package com.onegravity.sudoku.model.region

import com.onegravity.sudoku.model.Cell
import com.onegravity.sudoku.model.Puzzle
import com.onegravity.sudoku.model.computeRegionIndices
import com.onegravity.sudoku.model.computeNeighbors

class X<C : Cell>(puzzle: Puzzle<C>, regionCode: Int) :
    ExtraRegion<C>(puzzle, RegionType.X, regionCode, regionCodes[regionCode]) {

    companion object {
        const val nrOfGroups = 2

        // we need two block definitions for the X-Sudoku because that's the only puzzle type with
        // overlapping regions (R4C4 is in both regions)
        private val regionCodes = arrayOf(
            intArrayOf(
                 0,-1,-1,-1,-1,-1,-1,-1,-1,
                -1, 0,-1,-1,-1,-1,-1,-1,-1,
                -1,-1, 0,-1,-1,-1,-1,-1,-1,
                -1,-1,-1, 0,-1,-1,-1,-1,-1,
                -1,-1,-1,-1, 0,-1,-1,-1,-1,
                -1,-1,-1,-1,-1, 0,-1,-1,-1,
                -1,-1,-1,-1,-1,-1, 0,-1,-1,
                -1,-1,-1,-1,-1,-1,-1, 0,-1,
                -1,-1,-1,-1,-1,-1,-1,-1, 0,
            ),
            intArrayOf(
                -1,-1,-1,-1,-1,-1,-1,-1, 1,
                -1,-1,-1,-1,-1,-1,-1, 1,-1,
                -1,-1,-1,-1,-1,-1, 1,-1,-1,
                -1,-1,-1,-1,-1, 1,-1,-1,-1,
                -1,-1,-1,-1, 1,-1,-1,-1,-1,
                -1,-1,-1, 1,-1,-1,-1,-1,-1,
                -1,-1, 1,-1,-1,-1,-1,-1,-1,
                -1, 1,-1,-1,-1,-1,-1,-1,-1,
                 1,-1,-1,-1,-1,-1,-1,-1,-1,
            )
        )

        val indices by lazy { computeRegionIndices(*regionCodes) }

        val neighbors by lazy { computeNeighbors(*regionCodes) }
    }
}
