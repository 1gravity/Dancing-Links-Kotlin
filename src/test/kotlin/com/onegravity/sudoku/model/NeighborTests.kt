package com.onegravity.sudoku.model

import com.onegravity.sudoku.getJigsawTestGrid
import com.onegravity.sudoku.getTestGrid
import com.onegravity.sudoku.jigsawIndices
import com.onegravity.sudoku.model.Regions.type2Indices
import com.onegravity.sudoku.model.region.RegionType
import com.onegravity.sudoku.testValues
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NeighborTests {

    @Test
    fun testRows() {
        testNeighbors(RegionType.ROW)
    }

    @Test
    fun testColumns() {
        testNeighbors(RegionType.COLUMN)
    }

    @Test
    fun testBlocks() {
        testNeighbors(RegionType.BLOCK)
    }

    @Test
    fun testJigsaw() {
        val grid = getJigsawTestGrid(testValues, jigsawTest)
        testNeighbors(grid, RegionType.BLOCK)
    }

    @Test
    fun testXRegions() {
        val grid = getTestGrid(testValues, RegionType.X)
        val neighbors = grid.getNeighbors(RegionType.X)
        val indices = grid.getIndices(RegionType.X)

        assertEquals(81, neighbors.size)

        assertArrayEquals(type2Indices[RegionType.X], indices)

        for (index in 0..80) {
            val expected = if (index == 40) {
                (computeNeighbors(indices[0], index) + computeNeighbors(indices[1], index)).sorted()
            } else {
                val regionIndices = indices.firstOrNull { it.contains(index) }
                if (regionIndices != null) computeNeighbors(regionIndices, index) else null
            }

            if (expected != null) {
                assertEquals(expected, neighbors[index].toList())
            }
        }
    }

    @Test
    fun testHyperRegions() {
        testNeighbors(RegionType.HYPER)
    }

    @Test
    fun testPercentRegions() {
        testNeighbors(RegionType.PERCENT)
    }

    @Test
    fun testCenterdotRegion() {
        testNeighbors(RegionType.CENTERDOT)
    }

    @Test
    fun testAsteriskRegion() {
        testNeighbors(RegionType.ASTERISK)
    }

    @Test
    fun testColorRegion() {
        testNeighbors(RegionType.COLOR)
    }

    private fun testNeighbors(regionType: RegionType) {
        val grid = getTestGrid(testValues, if (regionType.isExtraRegion) regionType else null)
        testNeighbors(grid, regionType)
    }

    private fun testNeighbors(grid: Grid, regionType: RegionType) {
        val neighbors = grid.getNeighbors(regionType)
        val indices = grid.getIndices(regionType)

        assertEquals(81, neighbors.size)

        if (! grid.isJigsaw) {
            assertArrayEquals(type2Indices[regionType], indices)
        } else {
            assertArrayEquals(jigsawIndices, indices)
        }

        for (index in 0..80) {
            val regionIndices = indices.firstOrNull { it.contains(index) }
            if (regionIndices != null) {
                val expected = computeNeighbors(regionIndices, index)
                assertEquals(expected, neighbors[index].toList())
            }
        }
    }

    private fun computeNeighbors(indices: IntArray, index: Int) =
        indices.foldIndexed(arrayListOf<Int>()) { x, result, element ->
            if (indices[x] != index) result.add(element)
            result
        }

}
