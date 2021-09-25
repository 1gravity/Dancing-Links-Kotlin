package com.onegravity.dlx3

import com.onegravity.dlx.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class AlgorithmXTests {

    @Test
    fun test1() {
        var solutionFound = false
        matrixTest1
            .toDLX3(DefaultPayloadProvider)
            .solve { solution ->
                solutionFound = true
                val rows = ArrayList<Int>()
                solution.forEach { node ->
                    val payload = node.payload as DefaultPayloadProvider.DefaultPayload
                    rows.add(payload.row)
                }
                val rowsSorted = rows.sortedBy { it }
                verifySolution(solution, 7)
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
            .solve { solution ->
                solutionFound = true
                val rows = ArrayList<Int>()
                solution.forEach { node ->
                    val payload = node.payload as DefaultPayloadProvider.DefaultPayload
                    rows.add(payload.row)
                }
                verifySolution(solution, 12)

                val rowsSorted = rows.sortedBy { it }
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

    /**
     * This verifies if all constraints are met exactly once
     */
    private fun verifySolution(solution: Collection<DLXNode>, nrOfConstraints: Int) {
        val validation = BitSet()

        solution.forEach { node ->
            (node.payload as DefaultPayloadProvider.DefaultPayload).col.let {
                assertEquals(false, validation[it])
                validation[it] = true
            }
            var current = node.right
            while (current != node) {
                (current.payload as DefaultPayloadProvider.DefaultPayload).col.let {
                    assertEquals(false, validation[it])
                    validation[it] = true
                }
                current = current.right
            }
        }

        assertEquals(nrOfConstraints, validation.cardinality())
    }

}
