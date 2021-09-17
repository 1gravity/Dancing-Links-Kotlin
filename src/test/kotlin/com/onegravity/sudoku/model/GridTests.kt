package com.onegravity.sudoku.model

import com.onegravity.sudoku.model.region.RegionType
import com.onegravity.sudoku.checkFailure
import com.onegravity.sudoku.getTestGrid
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue
import kotlin.random.Random

class GridTests {

    @Test
    fun testValuesAndPosition() {
        val grid = Grid(null, false)
        var isGiven = true
        for (y in 0..8) {
            for (x in 0..8) {
                val value = Random.nextInt().absoluteValue % 9
                grid.setValue(x, y, value, isGiven)
                val cell = grid.getCell(x, y)
                assertEquals(value, cell.value)
                assertEquals(isGiven, cell.isGiven)
                assertEquals(x, cell.position.col)
                assertEquals(y, cell.position.row)
                isGiven = ! isGiven
            }
        }
    }

    @Test
    fun testAllCells() {
        val grid = getTestGrid()
        val cells = grid.getCells()
        for (y in 0..8) {
            for (x in 0..8) {
                assertEquals(cells[x][y], grid.getCell(x, y))
            }
        }
    }

    @Test
    fun testProperties() {
        var grid = Grid(RegionType.HYPER, false)
        assertEquals(RegionType.HYPER, grid.extraRegionType)

        grid = Grid(RegionType.X, true)
        assertEquals(true, grid.isJigsaw)
        assertEquals(RegionType.X, grid.extraRegionType)

        // the Grid construction needs to fail because RegionType.ROW isn't an extra region
        checkFailure("Extra region type not enforced") { grid = Grid(RegionType.ROW, true) }
    }

}
