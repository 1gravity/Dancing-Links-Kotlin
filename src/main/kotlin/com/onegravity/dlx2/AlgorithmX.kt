package com.onegravity.dlx2

import java.util.*

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
fun DLXMatrix.solve(
    solution: Stack<Int> = Stack<Int>(),
    collect: (List<Int>) -> Unit
) {
    // 1. Pick a column (the one with the least amount of nodes
    val header = findColumn()
    when (header) {
        null -> {
            // 1.1. if there's no column -> the matrix is empty -> we found a solution
            //   we need to make a copy -> ArrayList(solution), because the solution list is mutable,
            //   and we want to return a list that won't be modified by the solve function
            collect(ArrayList(solution))
        }
        else -> {
            // 2. Pick a row in the column
            // we can't do header.value.forEach because the underlying map will be modified in the forEach lambda,
            // and we end up with a ConcurrentModificationException
            val it = BitSetIterator(header)
            while (it.hasNext()) {
                val rowIndex = it.next()
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

private fun DLXMatrix.findColumn(): BitSet? {
    var header: BitSet? = null
    var minNrOfNodes = Int.MAX_VALUE
    // if minNrOfNodes == 0 -> the constraint isn't covered -> there's no solution
    // if minNrOfNodes == 1 -> must be part of the solution (in Sudoku terms: it's a naked or hidden single ;-)
    for (column in columns) {
        if (column.value.cardinality() < minNrOfNodes) {
            minNrOfNodes = column.value.cardinality()
            header = column.value
            if (minNrOfNodes <= 1) break
        }
    }
    return header
}
