package com.onegravity.dlx

import org.junit.jupiter.api.Assertions
import java.util.*

/**
 * This verifies if all constraints are met exactly once
 */
fun verifySolution(problem: Array<BooleanArray>, solution: Collection<Int>, nrOfConstraints: Int) {
    val validation = BitSet()

    solution.sorted().forEach { row ->
        problem[row].forEachIndexed { col, value ->
            if (value) {
                Assertions.assertEquals(false, validation[col])
                validation.set(col)
            }
        }
    }

    Assertions.assertEquals(nrOfConstraints, validation.cardinality())
}

