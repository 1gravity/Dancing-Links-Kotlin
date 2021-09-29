package com.onegravity.sudoku.model

import com.onegravity.sudoku.getJigsawTestGrid
import com.onegravity.sudoku.getTestGrid
import com.onegravity.sudoku.model.region.RegionType
import com.onegravity.sudoku.testValues
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RegionTests {

    @Test
    fun testRows() {
        testRegions(RegionType.ROW)
    }

    @Test
    fun testColumns() {
        testRegions(RegionType.COLUMN)
    }

    @Test
    fun testBlocks() {
        testRegions(RegionType.BLOCK)
    }

    @Test
    fun testJigsaw() {
        val grid = getJigsawTestGrid(testValues, jigsawTest)
        testRegions(grid, RegionType.BLOCK)
    }

    @Test
    fun testXRegion() {
        testRegions(RegionType.X)
    }

    @Test
    fun testHyperRegion() {
        testRegions(RegionType.HYPER)
    }

    @Test
    fun testPercentRegion() {
        testRegions(RegionType.PERCENT)
    }

    @Test
    fun testCenterdotRegion() {
        testRegions(RegionType.CENTERDOT)
    }

    @Test
    fun testAsteriskRegion() {
        testRegions(RegionType.ASTERISK)
    }

    @Test
    fun testColorRegion() {
        testRegions(RegionType.COLOR)
    }

    @Test
    fun testIntersects() {
        val grid = getTestGrid(testValues, null)
        (0..8).forEach { index1 ->
            val columnRegion1 = grid.getRegionAtOrNull(index1, 0, RegionType.COLUMN)!!
            val rowRegion1 = grid.getRegionAtOrNull(0, index1,  RegionType.ROW)!!
            (0..8).forEach { index2 ->
                val columnRegion2 = grid.getRegionAtOrNull(index2, index1, RegionType.COLUMN)!!
                val rowRegion2 = grid.getRegionAtOrNull(index1, index2, RegionType.ROW)!!
                assert(rowRegion2.intersects(columnRegion1))
                assert(rowRegion1.intersects(columnRegion2))

                val rowRegion = grid.getRegionAtOrNull(index1, index2, RegionType.ROW)!!
                val columnRegion = grid.getRegionAtOrNull(index1, index2, RegionType.COLUMN)!!
                val blockRegion = grid.getRegionAtOrNull(index1, index2, RegionType.BLOCK)!!
                assert(rowRegion.intersects(blockRegion))
                assert(columnRegion.intersects(blockRegion))
            }
        }
    }

    @Test
    fun testIntersection() {
        val grid = getTestGrid(testValues, null)
        // columns
        for (row in 0..8) {
            val columnRegion = grid.getRegionAtOrNull(0, row,  RegionType.COLUMN)!!
            for (col in 0..8) {
                val rowRegion = grid.getRegionAtOrNull(col, row,  RegionType.ROW)!!
                val blockRegion = grid.getRegionAtOrNull(0, row, RegionType.BLOCK)!!
                assertEquals(3, columnRegion.intersection(blockRegion).size)
                assertEquals(1, columnRegion.intersection(rowRegion).size)
            }
        }
        // rows
        for (col in 0..8) {
            val rowRegion = grid.getRegionAtOrNull(col, 0,  RegionType.ROW)!!
            for (row in 0..8) {
                val columnRegion = grid.getRegionAtOrNull(col, row,  RegionType.COLUMN)!!
                val blockRegion = grid.getRegionAtOrNull(col, 0, RegionType.BLOCK)!!
                assertEquals(3, rowRegion.intersection(blockRegion).size)
                assertEquals(1, rowRegion.intersection(columnRegion).size)
            }
        }
    }

    @Test
    fun testRegionAt() {
        val grid = getTestGrid(testValues, null)
        for (col in 0..8) {
            for (row in 0..8) {
                testRegionAt(grid, col, row, RegionType.ROW)
                testRegionAt(grid, col, row, RegionType.COLUMN)
                testRegionAt(grid, col, row, RegionType.BLOCK)
            }
        }
        testNARegionAt(grid, 0, 0, RegionType.HYPER)
        testNARegionAt(grid, 1, 1, RegionType.ASTERISK)
    }

    private fun testRegionAt(grid: Grid, col: Int, row: Int, type: RegionType) {
        val cell = grid.getCell(col, row)
        val region = grid.getRegionAtOrThrow(col, row, type)
        assertEquals(type, region.regionType)
        assert(region.contains(cell))
    }

    private fun testNARegionAt(grid: Grid, col: Int, row: Int, type: RegionType) {
        try {
            grid.getRegionAtOrThrow(col, row, type)
            // there's no such region so this should throw an exception
            assert(false)
        } catch(e: NoSuchElementException) {}

        assertEquals(null, grid.getRegionAtOrNull(col, row, type))
    }

    private fun testRegions(regionType: RegionType) {
        val grid = getTestGrid(testValues, if (regionType.isExtraRegion) regionType else null)
        testRegions(grid, regionType)
    }

    private fun testRegions(grid: Grid, regionType: RegionType) {
        val regions = grid.getRegions(regionType)
        val regionIndices = grid.getIndices(regionType)
        assertEquals(regions.size, regionIndices.size)

        for (region in regions) {
            assertEquals(regionType, region.regionType)
            assertEquals(9, region.cells.size)
        }

        regions.forEachIndexed { regionIndex, region ->
            val indices = regionIndices[regionIndex]
            region.cells.forEachIndexed{ index, cell ->
                assertEquals(indices[index], cell.index)
                assertEquals(testValues[cell.index], cell.value)
            }
        }
    }

}
