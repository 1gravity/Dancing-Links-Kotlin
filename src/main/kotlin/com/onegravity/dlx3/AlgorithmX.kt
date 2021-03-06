package com.onegravity.dlx3

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
fun DLXNode.solve(
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
            // Note: we cannot use header.forEach here because the lambda can't be a suspending function (or the whole
            // solve function must be suspending), and thus we cannot call solveProblem(solution, rootNode, collect)
            // within the lambda (step 5, see below)
            var node = header.down
            while (node != header) {
                // 3. Add the row to the solution set
                solution.push(node.row)

                // 4. Cover all columns that this row links to (remove the constraints)
                var nodeRight = node
                do {
                    nodeRight.header?.cover()
                    nodeRight = nodeRight.right
                } while (nodeRight != node)

                // 5. Repeat this algorithm recursively on the reduced matrix
                solve(solution, collect)

                // we're at a dead end (no solution) and need to backtrack
                // 5.1. undo all changes for step 3 and 4
                solution.pop()
                val startNode = node.left
                var nodeLeft = startNode
                do {
                    nodeLeft.header?.uncover()
                    nodeLeft = nodeLeft.left
                } while (nodeLeft != startNode)

                node = node.down
            }
        }
    }
}

private fun DLXNode.findColumn(): DLXNode? {
    var header: DLXNode? = null
    var minNrOfNodes = Int.MAX_VALUE
    var next = right
    while (next != this && minNrOfNodes > 1) {
        if (next.nrOfNodes < minNrOfNodes) {
            minNrOfNodes = next.nrOfNodes
            header = next
        }
        next = next.right
    }
    return header
}
