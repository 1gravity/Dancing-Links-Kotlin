package com.onegravity.sudoku

import com.onegravity.dlx.PayloadProvider
import com.onegravity.dlx.toDLX
import com.onegravity.dlx.solve
import com.onegravity.sudoku.SudokuMatrix.Companion.sudokuMatrix
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

class HardestSudokuTests {

    private val sudokuPattern = """^([\d.]{81}).*$""".toRegex()

    @Test
    fun testHardestSudokus() {
        File(ClassLoader.getSystemResource("HardestSudokus.txt").file)
            .forEachLine { line ->
                // find Sudoku puzzles
                sudokuPattern.findAll(line)
                    .map { match ->
                        // get group with puzzle values
                        match.groupValues[1]
                    }
                    .map { sudoku ->
                        assertEquals(81, sudoku.length)
                        // convert values from a String to an IntArray
                        sudoku.map { c -> c.digitToIntOrNull() ?: 0 }.toIntArray()
                    }
                    .forEach {
                        testSudoku(it)
                    }
            }
    }

    private fun testSudoku(values: IntArray) {
        val grid = getTestGrid(values, null)

        val solutions = AtomicInteger(0)
        grid.sudokuMatrix()
            .toDLX(object: PayloadProvider {
                override fun getHeaderPayload(index: Int) = "h$index"
                override fun getDataPayload(col: Int, row: Int) = getIndexValue(row)
            })
            .solve { solution ->
                // check if the givens haven't been altered
                val solutionGrid = solution.toGrid(grid)
                for (index in 0..80) {
                    val cell = grid.getCell(index)
                    if (cell.isGiven) {
                        assertEquals(cell.value, solutionGrid.getCell(index).value)
                    }
                }

                solutions.incrementAndGet()
            }

        // make sure each puzzle has exactly one solution
        assertEquals(1, solutions.get())
    }

}
