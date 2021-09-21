package com.onegravity.dlx2

import java.util.*
import kotlin.collections.ArrayList

/**
 * The core of Algorithm X: https://en.wikipedia.org/wiki/Knuth%27s_Algorithm_X
 *
 * 1. Pick a column
 *    1.1. if there's no column -> the matrix is empty -> we found a solution
 *    1.2. else remove the found column from the matrix -> cover the column
 * 2. Pick a row in the column
 * 3. Add the row to the solution set
 * 4. Cover all columns that this row links to (remove the constraints)
 * 5. Repeat this algorithm recursively on the reduced matrix
 *    5.1. Since step 2 (pick a row in the column) is done for all rows at this point we need to
 *         undo all changes for step 3 and 4
 */
@Suppress("MoveVariableDeclarationIntoWhen")
fun CoverMatrix.solve(
    solution: Stack<Int> = Stack<Int>(),
    collect: (List<Int>) -> Unit
) {
    // 1. Pick a column (the one with the least amount of nodes
    val header = columns.minByOrNull { it.value.size }

    when (header) {
        null -> {
            // 1.1. if there's no column -> the matrix is empty -> we found a solution
            //   we need to make a copy -> ArrayList(solution), because the solution list is mutable,
            //   and we want to return a list that won't be modified by the solve function
            collect(ArrayList(solution))
        }
        else -> {
            // 2. Pick a row in the column
            // we can't just do header.value.forEach because the underlying map will be modified in the forEach lambda
            header.value.toList().forEach { rowIndex ->
                // 3. Add the row to the solution set
                solution.push(rowIndex)

                // 4. Cover all columns that this row links to (remove the constraints)
                val removedColumns = cover(rowIndex)

                // 5. Repeat this algorithm recursively on the reduced matrix
                solve(solution, collect)

                // we're at a dead end (no solution) and need to backtrack
                // 5.1. undo all changes for step 3 and 4
                solution.pop()
                uncover(rowIndex, removedColumns)
            }
        }
    }
}
