package com.onegravity.dlx2

import com.onegravity.dlx.matrixTest1
import com.onegravity.dlx.matrixTest2
import com.onegravity.dlx2.CoverMatrix.Companion.toCoverMatrix
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

}
