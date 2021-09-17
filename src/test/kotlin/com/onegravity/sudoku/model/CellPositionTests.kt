package com.onegravity.sudoku.model

import com.onegravity.sudoku.checkRange
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CellPositionTests {
    @Test
    fun testBasics() {
        for (col in 0..8) {
            for (row in 0..8) {
                val pos = CellPosition(col, row)
                assertEquals(col, pos.col)
                assertEquals(row, pos.row)
            }
        }

        for (index in 0..80) {
            val pos = CellPosition(index)
            assertEquals(index, pos.index())
        }
    }

    @Test
    fun testRowStart() {
        for (index in 0..80) {
            val pos = CellPosition(index)
            val rowStart = when (index) {
                in 0..8 -> 0
                in 9..17 -> 9
                in 18..26 -> 18
                in 27..35 -> 27
                in 36..44 -> 36
                in 45..53 -> 45
                in 54..62 -> 54
                in 63..71 -> 63
                in 72..80 -> 72
                else -> -1
            }
            assertEquals(rowStart, pos.indexRowStart())
        }
    }

    @Test
    fun testColumnStart() {
        for (index in 0..80) {
            val pos = CellPosition(index)
            val columnStart = index % 9
            assertEquals(columnStart, pos.indexColumnStart())
        }
    }

    @Test
    fun testRange() {
        for (col in -5..-1) {
            checkRange { CellPosition(col, 1) }
        }
        for (col in 9..12) {
            checkRange { CellPosition(col, 1) }
        }
        for (row in -5..-1) {
            checkRange { CellPosition(3, row) }
        }
        for (row in 9..12) {
            checkRange { CellPosition(3, row) }
        }
        for (index in -5..-1) {
            checkRange { CellPosition(index) }
        }
        for (index in 81..85) {
            checkRange { CellPosition(index) }
        }
    }

    @Test
    fun testPos2Index() {
        var index = 0
        for (row in 0..8) {
            for (col in 0..8) {
                val pos = CellPosition(col, row)
                assertEquals(index++, pos.index())
            }
        }
    }

    @Test
    fun testIndexToPos() {
        var index = 0
        for (row in 0..8) {
            for (col in 0..8) {
                val pos = CellPosition(col, row)
                assertEquals(index++, pos.index())
            }
        }
    }

}
