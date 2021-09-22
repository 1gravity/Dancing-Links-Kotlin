package com.onegravity

import com.onegravity.dlx.*
import com.onegravity.dlx2.CoverMatrix.Companion.toDLXMatrix
import com.onegravity.dlx2.solve
import com.onegravity.sudoku.SudokuMatrix
import com.onegravity.sudoku.SudokuMatrix.Companion.toSudokuMatrix
import com.onegravity.sudoku.getTestGrid
import com.onegravity.sudoku.model.Grid
import com.onegravity.sudoku.testSudokuAlEscargotSolution
import com.onegravity.sudoku.toGrid
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@ObsoleteCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(ExperimentalCoroutinesApi::class)
class AlgorithmComparison {

    @Test
    fun test1() {
        var solutionFound = false
        println("matrix 2")
        matrixTest2
            .toDLX(DefaultPayloadProvider)
            .solve {
                solutionFound = true
            }
        assertEquals(true, solutionFound)
    }

    @Test
    fun testDLX2() {
        var solutionFound = false
        println("matrix 2")
        matrixTest2
            .toDLXMatrix()
            .solve {
                solutionFound = true
            }
        assertEquals(true, solutionFound)
    }

    @Test
    fun testSudokuAlEscargot() {
        println("Al Escargot")
        val grid = getTestGrid(com.onegravity.sudoku.testSudokuAlEscargot, null)
        testSudokuDLX(grid, testSudokuAlEscargotSolution)
        testSudokuDLX2(grid, testSudokuAlEscargotSolution)
    }

    private fun testSudokuDLX(grid: Grid, solution: IntArray) {
        var solutionFound = false
        grid.toSudokuMatrix()
            .toDLX(object: PayloadProvider {
                override fun getHeaderPayload(index: Int) = "h$index"
                override fun getDataPayload(col: Int, row: Int) = SudokuMatrix.getIndexValue(row)
            })
            .solve { nodes ->
                validateSolution(solution, grid, nodes.toGrid(grid))
                solutionFound = true
            }
        assert(solutionFound)
    }

    private fun testSudokuDLX2(grid: Grid, solution: IntArray) {
        var solutionFound = false
        grid.toSudokuMatrix()
            .toDLXMatrix()
            .solve { rows ->
                validateSolution(solution, grid, rows.toGrid(grid))
                solutionFound = true
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
