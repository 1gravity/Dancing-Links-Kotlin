package com.onegravity.dlx2

import com.onegravity.dlx.matrixTest1
import com.onegravity.dlx.matrixTest2
import com.onegravity.dlx.matrixTest4
import com.onegravity.dlx2.CoverMatrix.Companion.toDLXMatrix
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class AlgorithmXTests {

    @Test
    fun test1() {
        var solutionFound = false
        matrixTest1
            .toDLXMatrix()
            .solve { solution ->
                verifySolution(matrixTest1, solution, 7)
                assertEquals(1, solution[0])
                assertEquals(3, solution[1])
                assertEquals(5, solution[2])
                solutionFound = true
            }
        assertEquals(true, solutionFound)
    }

    @Test
    fun test2() {
        var solutionFound = false
        matrixTest2
            .toDLXMatrix()
            .solve { solution ->
                verifySolution(matrixTest2, solution, 12)

                if (solution[0] == 1) {
                    // solution 1
                    assertEquals(1, solution[0])
                    assertEquals(2, solution[1])
                    assertEquals(4, solution[2])
                    assertEquals(7, solution[3])
                } else {
                    // solution 2
                    assertEquals(0, solution[0])
                    assertEquals(3, solution[1])
                    assertEquals(5, solution[2])
                    assertEquals(6, solution[3])
                }

                solutionFound = true
            }
        assertEquals(true, solutionFound)
    }

    @Test
    fun testNoSolution() {
        matrixTest4
            .toDLXMatrix()
            .solve {
                // if we reach this point the algorithm failed since the matrix has no solution
                assert(false)
            }
    }

    /**
     * This verifies if all constraints are met exactly once
     */
    private fun verifySolution(problem: Array<BooleanArray>, solution: Collection<Int>, nrOfConstraints: Int) {
        val validation = BitSet()

        solution.sorted().forEach { row ->
            problem[row].forEachIndexed { col, value ->
                if (value) {
                    assertEquals(false, validation[col])
                    validation.set(col)
                }
            }
        }

        assertEquals(nrOfConstraints, validation.cardinality())
    }

}
