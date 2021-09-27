package com.onegravity.sudoku

import com.onegravity.bruteforce.solve
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test

class BruteForceSolverTests {

    @Test
    fun test1() {
        val result = testSudoku1.solve()
        assertArrayEquals(testSudoku1Solution, result)
    }

    @Test
    fun testHardPuzzles() {
        getPuzzles("hardest.csv") { puzzle, solution ->
            val result = puzzle.solve()
            assertArrayEquals(solution, result)
        }
    }

    @Test
    fun testPerformance() {
        val l = System.currentTimeMillis()
        var count = 0
        getPuzzles("hardest.csv") { puzzle, solution ->
            count++
            puzzle.solve()
        }
        val time = System.currentTimeMillis() - l
        val average = time.toFloat().div(count)
        val puzzlesPerSec = 1000F.div(average)
        println("Took: $time ms, average: ${average.twoDecimals()} ms, puzzles/sec: ${puzzlesPerSec.twoDecimals()}")
    }

}
