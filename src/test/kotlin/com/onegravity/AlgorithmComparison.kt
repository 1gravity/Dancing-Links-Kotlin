package com.onegravity

import com.onegravity.dlx.*
import com.onegravity.dlx2.CoverMatrix.Companion.toDLXMatrix
import com.onegravity.dlx2.solve
import com.onegravity.sudoku.SudokuMatrix
import com.onegravity.sudoku.SudokuMatrix.Companion.toSudokuMatrix
import com.onegravity.sudoku.getTestGrid
import com.onegravity.sudoku.model.Grid
import com.onegravity.sudoku.*
import com.onegravity.sudoku.toGrid
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AlgorithmComparison {

    @Test
    fun test1() {
        println("sudoku 1\n--------")
        val grid = getTestGrid(testSudoku1, null)
        testSudokuDLX(grid, testSudoku1Solution)
        testSudokuDLX2(grid, testSudoku1Solution)
        testSudokuDLX(grid, testSudoku1Solution)
        testSudokuDLX2(grid, testSudoku1Solution)
    }

    @Test
    fun test2() {
        println("jigsaw\n------")
        val grid = getTestGrid(testSudokuJigsawValues, null, true, testSudokuJigsawBlocks)
        testSudokuDLX(grid, testSudokuJigsawSolution)
        testSudokuDLX2(grid, testSudokuJigsawSolution)
    }

    @Test
    fun testSudokuAlEscargot() {
        println("Al Escargot\n-----------")
        val grid = getTestGrid(testSudokuAlEscargot, null)
        repeat(1) {
            testSudokuDLX(grid, testSudokuAlEscargotSolution)
            testSudokuDLX2(grid, testSudokuAlEscargotSolution)
        }
    }

    private fun testSudokuDLX(grid: Grid, solution: IntArray) {
        val l = System.currentTimeMillis()
        var solutionFound = false
        grid.toSudokuMatrix()
            .toDLX(object: PayloadProvider {
                override fun getHeaderPayload(index: Int) = "h$index"
                override fun getDataPayload(col: Int, row: Int) = SudokuMatrix.getIndexValue(row)
            })
            .solve { nodes ->
                validateSolution(solution, grid, nodes.toGrid(grid))
                solutionFound = true
                println("DLX Took: ${System.currentTimeMillis() -l } ms")
            }
        assert(solutionFound)
    }

    private fun testSudokuDLX2(grid: Grid, solution: IntArray) {
        val l = System.currentTimeMillis()
        var solutionFound = false
        grid.toSudokuMatrix()
            .toDLXMatrix()
            .solve { rows ->
                validateSolution(solution, grid, rows.toGrid(grid))
                solutionFound = true
                println("DLX2 Took: ${System.currentTimeMillis() -l } ms")
            }
        assert(solutionFound)
    }

    private fun validateSolution(expected: IntArray, original: Grid, actual: Grid) {
        expected.forEachIndexed { index, value ->
            assertEquals(original.getCell(index).isGiven, actual.getCell(index).isGiven)
            assertEquals(value, actual.getCell(index).value)
        }
    }

}
