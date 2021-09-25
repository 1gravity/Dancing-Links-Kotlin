package com.onegravity.dlx

import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.*

@ObsoleteCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(ExperimentalCoroutinesApi::class)
class AlgorithmXTests {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @BeforeAll
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @AfterAll
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun test1() {
        var solutionFound = false
        matrixTest1
            .toDLX(DefaultPayloadProvider)
            .solve { solution ->
                solutionFound = true
                verifySolution(matrixTest1, solution, 7)
                val sortedRows = solution.sorted()
                assertEquals(1, sortedRows[0])
                assertEquals(3, sortedRows[1])
                assertEquals(5, sortedRows[2])
            }
        assertEquals(true, solutionFound)
    }

    @Test
    fun test1Channels() = runBlocking(Dispatchers.Main) {
        var solutionFound = false
        val dlxMatrix = matrixTest1.toDLX(DefaultPayloadProvider)
        for (solution in solve(dlxMatrix)) {
            solutionFound = true
            verifySolution(matrixTest1, solution, 7)
            val sortedRows = solution.sorted()
            assertEquals(1, sortedRows[0])
            assertEquals(3, sortedRows[1])
            assertEquals(5, sortedRows[2])
        }

        assertEquals(true, solutionFound)
    }

    @Test
    fun test2() {
        var solutionFound = false
        matrixTest2
            .toDLX(DefaultPayloadProvider)
            .solve { solution ->
                solutionFound = true
                verifySolution(matrixTest2, solution, 12)
                val sortedRows = solution.sorted()

                if (sortedRows[0] == 1) {
                    // solution 1
                    assertEquals(1, sortedRows[0])
                    assertEquals(2, sortedRows[1])
                    assertEquals(4, sortedRows[2])
                    assertEquals(7, sortedRows[3])
                } else {
                    // solution 2
                    assertEquals(0, sortedRows[0])
                    assertEquals(3, sortedRows[1])
                    assertEquals(5, sortedRows[2])
                    assertEquals(6, sortedRows[3])
                }
            }
        assertEquals(true, solutionFound)
    }

    @Test
    fun testMultipleSolutions() {
        matrixTest1
            .toDLX(DefaultPayloadProvider)
            .solveAll { solutions ->
                assertEquals(1, solutions.size)
            }

        matrixTest2
            .toDLX(DefaultPayloadProvider)
            .solveAll { solutions ->
                assertEquals(2, solutions.size)
            }

        matrixTest3
            .toDLX(DefaultPayloadProvider)
            .solveAll { solutions ->
                assertEquals(64, solutions.size)
                solutions.forEach { verifySolution(matrixTest3, it, 12) }
            }
    }

    @Test
    fun testMultipleSolutionsChannel() = runBlocking(Dispatchers.Main) {
        var count = 0
        for (solution in solve(matrixTest1.toDLX(DefaultPayloadProvider))) count++
        assertEquals(1, count)

        count = 0
        for (solution in solve(matrixTest2.toDLX(DefaultPayloadProvider))) count++
        assertEquals(2, count)

        count = 0
        for (solution in solve(matrixTest3.toDLX(DefaultPayloadProvider))) {
            verifySolution(matrixTest3, solution, 12)
            count++
        }
        assertEquals(64, count)
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

    @Test
    fun testNoSolutionChannels() = runBlocking(Dispatchers.Main) {
        val dlxMatrix = matrixTest4.toDLX(DefaultPayloadProvider)
        for (solution in solve(dlxMatrix)) {
            // if we reach this point the algorithm failed since the matrix has no solution
            assert(false)
        }
    }

}
