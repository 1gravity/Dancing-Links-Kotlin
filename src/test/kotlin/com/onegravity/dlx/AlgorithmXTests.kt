package com.onegravity.dlx

import com.onegravity.dlx.model.DLXNode
import com.onegravity.dlx.model.Direction.Right
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.*
import kotlin.collections.ArrayList

@ObsoleteCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
            .getDLX(PayloadProviderImpl)
            .solve { solution ->
                solutionFound = true
                val rows = ArrayList<Int>()
                solution.forEach { node ->
                    val payload = node.payload as PayloadProviderImpl.DefaultPayload
                    rows.add(payload.row)
                }
                verifySolution(solution, 7)
                assertEquals(1, rows[0])
                assertEquals(1, rows[0])
                assertEquals(3, rows[1])
                assertEquals(5, rows[2])
            }
        assertEquals(true, solutionFound)
    }

    @Test
    fun test1Channels() = runBlocking(Dispatchers.Main) {
        var solutionFound = false
        val dlxMatrix = matrixTest1.getDLX(PayloadProviderImpl)
        for (solution in solve(dlxMatrix)) {
            solutionFound = true
            val rows = ArrayList<Int>()
            solution.forEach { node ->
                val payload = node.payload as PayloadProviderImpl.DefaultPayload
                rows.add(payload.row)
            }
            verifySolution(solution, 7)
            assertEquals(1, rows[0])
            assertEquals(1, rows[0])
            assertEquals(3, rows[1])
            assertEquals(5, rows[2])
        }

        assertEquals(true, solutionFound)
    }

    @Test
    fun test2() {
        var solutionFound = false
        matrixTest2
            .getDLX(PayloadProviderImpl)
            .solve { solution ->
                solutionFound = true
                val rows = ArrayList<Int>()
                solution.forEach { node ->
                    val payload = node.payload as PayloadProviderImpl.DefaultPayload
                    rows.add(payload.row)
                }
                verifySolution(solution, 12)

                if (rows[0] == 1) {
                    // solution 1
                    assertEquals(1, rows[0])
                    assertEquals(2, rows[1])
                    assertEquals(4, rows[2])
                    assertEquals(7, rows[3])
                } else {
                    // solution 2
                    assertEquals(0, rows[0])
                    assertEquals(3, rows[1])
                    assertEquals(5, rows[2])
                    assertEquals(6, rows[3])
                }
            }
        assertEquals(true, solutionFound)
    }

    @Test
    fun testMultipleSolutions() {
        matrixTest1
            .getDLX(PayloadProviderImpl)
            .solveAll { solutions ->
                assertEquals(1, solutions.size)
            }

        matrixTest2
            .getDLX(PayloadProviderImpl)
            .solveAll { solutions ->
                assertEquals(2, solutions.size)
            }

        matrixTest3
            .getDLX(PayloadProviderImpl)
            .solveAll { solutions ->
                assertEquals(64, solutions.size)
                solutions.forEach { verifySolution(it, 12) }
            }
    }

    @Test
    fun testMultipleSolutionsChannel() = runBlocking(Dispatchers.Main) {
        var count = 0
        for (solution in solve(matrixTest1.getDLX(PayloadProviderImpl))) count++
        assertEquals(1, count)

        count = 0
        for (solution in solve(matrixTest2.getDLX(PayloadProviderImpl))) count++
        assertEquals(2, count)

        count = 0
        for (solution in solve(matrixTest3.getDLX(PayloadProviderImpl))) {
            verifySolution(solution, 12)
            count++
        }
        assertEquals(64, count)
    }

    @Test
    fun testNoSolution() {
        matrixTest4
            .getDLX(PayloadProviderImpl)
            .solve {
                // if we reach this point the algorithm failed since the matrix has no solution
                assert(false)
            }
    }

    @Test
    fun testNoSolutionChannels() = runBlocking(Dispatchers.Main) {
        val dlxMatrix = matrixTest4.getDLX(PayloadProviderImpl)
        for (solution in solve(dlxMatrix)) {
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
            (node.payload as PayloadProviderImpl.DefaultPayload).col.let {
                assertEquals(false, validation[it])
                validation[it] = true
            }
            node.forEach(Right) { neighbor ->
                (neighbor.payload as PayloadProviderImpl.DefaultPayload).col.let {
                    assertEquals(false, validation[it])
                    validation[it] = true
                }
            }
        }

        assertEquals(nrOfConstraints, validation.cardinality())
    }

}
