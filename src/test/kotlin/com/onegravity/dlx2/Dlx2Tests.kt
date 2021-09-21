package com.onegravity.dlx2

import com.onegravity.dlx.*
import com.onegravity.dlx2.CoverMatrix.Companion.toCoverMatrix
import com.onegravity.sudoku.*
import com.onegravity.sudoku.SudokuMatrix.Companion.toSudokuMatrix
import com.onegravity.sudoku.model.Grid
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Dlx2Tests {

    @Test
    fun test1() {
        var solutionFound = false
        matrixTest1
            .toCoverMatrix()
            .solveProblem { rows ->
                Assertions.assertEquals(1, rows[0])
                Assertions.assertEquals(3, rows[1])
                Assertions.assertEquals(5, rows[2])
                solutionFound = true
            }
        Assertions.assertEquals(true, solutionFound)
    }

    @Test
    fun test2() {
        var solutionsFound = 0
        matrixTest2
            .toCoverMatrix()
            .solveProblem { rows ->
                if (rows[0] == 1) {
                    // solution 1
                    Assertions.assertEquals(1, rows[0])
                    Assertions.assertEquals(2, rows[1])
                    Assertions.assertEquals(4, rows[2])
                    Assertions.assertEquals(7, rows[3])
                } else {
                    // solution 2
                    Assertions.assertEquals(0, rows[0])
                    Assertions.assertEquals(3, rows[1])
                    Assertions.assertEquals(5, rows[2])
                    Assertions.assertEquals(6, rows[3])
                }
                solutionsFound++
            }
        Assertions.assertEquals(2, solutionsFound)
    }

    @Test
    fun testSudoku() {
        val grid = getTestGrid(testSudokuAlEscargot, null)
        val l = System.currentTimeMillis()
        repeat(1000) {
            testSudoku(grid, testSudokuAlEscargotSolution)
        }
        println("MAP Took: ${System.currentTimeMillis() - l} ms")
    }

    private fun testSudoku(grid: Grid, solution: IntArray) {
        var solutionFound = false
        grid.toSudokuMatrix().toCoverMatrix()
            .solveProblem { rows ->
//                validateSolution(solution, grid, nodes.toGrid(grid))
                solutionFound = true
            }
        assert(solutionFound)
    }

    private fun validateSolution(expected: IntArray, original: Grid, actual: Grid) {
        expected.forEachIndexed { index, value ->
            Assertions.assertEquals(original.getCell(index).isGiven, actual.getCell(index).isGiven)
            Assertions.assertEquals(value, actual.getCell(index).value)
        }
    }
}
