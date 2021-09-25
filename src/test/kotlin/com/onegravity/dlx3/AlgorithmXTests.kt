package com.onegravity.dlx3

import com.onegravity.dlx.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AlgorithmXTests {

    @Test
    fun test1() {
        var solutionFound = false
        matrixTest1
            .toDLX3(DefaultPayloadProvider)
            .solve { rows ->
                solutionFound = true
                val rowsSorted = rows.sortedBy { it }
                verifySolution(matrixTest1, rows, 7)
                assertEquals(1, rowsSorted[0])
                assertEquals(3, rowsSorted[1])
                assertEquals(5, rowsSorted[2])
            }
        assertEquals(true, solutionFound)
    }

    @Test
    fun test2() {
        var solutionFound = false
        matrixTest2
            .toDLX3(DefaultPayloadProvider)
            .solve { rows ->
                solutionFound = true
                val rowsSorted = rows.sortedBy { it }
                verifySolution(matrixTest2, rows, 12)

                if (rowsSorted[0] == 1) {
                    // solution 1
                    assertEquals(1, rowsSorted[0])
                    assertEquals(2, rowsSorted[1])
                    assertEquals(4, rowsSorted[2])
                    assertEquals(7, rowsSorted[3])
                } else {
                    // solution 2
                    assertEquals(0, rowsSorted[0])
                    assertEquals(3, rowsSorted[1])
                    assertEquals(5, rowsSorted[2])
                    assertEquals(6, rowsSorted[3])
                }
            }
        assertEquals(true, solutionFound)
    }

    @Test
    fun testMultipleSolutions() {
        var nrOfSolutions = 0
        matrixTest1
            .toDLX3(DefaultPayloadProvider)
            .solve { nrOfSolutions++ }
        assertEquals(1, nrOfSolutions)

        nrOfSolutions = 0
        matrixTest2
            .toDLX3(DefaultPayloadProvider)
            .solve { nrOfSolutions++ }
        assertEquals(2, nrOfSolutions)

        nrOfSolutions = 0
        matrixTest3
            .toDLX3(DefaultPayloadProvider)
            .solve { nrOfSolutions++ }
        assertEquals(64, nrOfSolutions)
    }

    @Test
    fun testNoSolution() {
        matrixTest4
            .toDLX(DefaultPayloadProvider)
            .solve {
                // if we reach this point the algorithm failed since the matrix has no solution
                assert(false)
            }
    }

}
