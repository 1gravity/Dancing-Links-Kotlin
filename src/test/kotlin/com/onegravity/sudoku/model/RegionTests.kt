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
                assertEquals(indices[index], cell.position.index())
                assertEquals(testValues[cell.position.index()], cell.value)
            }
        }
    }

}
