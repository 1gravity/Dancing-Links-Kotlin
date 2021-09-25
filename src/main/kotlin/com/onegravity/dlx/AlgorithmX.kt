package com.onegravity.dlx

import com.onegravity.dlx.model.DLXNode
import com.onegravity.dlx.model.DataNode
import com.onegravity.dlx.model.Direction.*
import com.onegravity.dlx.model.HeaderNode
import com.onegravity.dlx.model.RootNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import java.util.Stack

/**
 * Solve the exact cover problem using Algorithm X and dancing links.
 *
 * @param collect the function called with each of the found solutions (there can be 0..n solutions).
 */
fun RootNode.solve(collect: (List<DLXNode>) -> Unit) {
    solveProblem { collect(it) }
}

/**
 * solveAll collects all solutions before returning a Collection of them to the caller.
 */
fun RootNode.solveAll(): Collection<List<DLXNode>> =
    ArrayList<List<DLXNode>>().apply {
        solveProblem { add(it) }
    }

/**
 * This is a convenience function for solveAll so the caller can concatenate calls -> fluent API.
 */
fun RootNode.solveAll(collect: (Collection<List<DLXNode>>) -> Unit) {
    collect(solveAll())
}

/**
 * The solve function using Coroutines/Channels.
 */
@ExperimentalCoroutinesApi
fun CoroutineScope.solve(rootNode: RootNode) = produce<List<DLXNode>> {
    rootNode.solveAll().forEach {
        channel.send(it)
    }
}

/**
 * The core of Algorithm X: https://en.wikipedia.org/wiki/Knuth%27s_Algorithm_X
 *
 * 1. Pick a column
 *    1.1. if there's no column -> the matrix is empty -> we found a solution
 * 2. Pick a row in the column
 * 3. Add the row to the solution set
 * 4. Cover all columns that this row links to (remove the constraints)
 * 5. Repeat this algorithm recursively on the reduced matrix
 *    5.1. Since step 2 (pick a row in the column) is done for all rows at this point we need to
 *         undo all changes for step 3 and 4
 */
@Suppress("MoveVariableDeclarationIntoWhen")
private fun RootNode.solveProblem(
    solution: Stack<DLXNode> = Stack<DLXNode>(),
    collect: (List<DLXNode>) -> Unit
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
            var node = header.next(Down)
            while (node != header) {
                // 3. Add the row to the solution set
                solution.push(node)

                // 4. Cover all columns that this row links to (remove the constraints)
                node.forAll(Right) {
                    (it as DataNode).header.cover()
                }

                // 5. Repeat this algorithm recursively on the reduced matrix
                solveProblem(solution, collect)

                // we're at a dead end (no solution) and need to backtrack
                // 5.1. undo all changes for step 3 and 4
                solution.pop()
                node.left.forAll(Left) {
                    (it as DataNode).header.uncover()
                }

                node = node.next(Down)
            }
        }
    }
}

private fun RootNode.findColumn(): HeaderNode? {
    var header: HeaderNode? = null
    var minNrOfNodes = Int.MAX_VALUE
    var next = right
    // if minNrOfNodes == 0 -> the constraint isn't covered -> there's no solution
    // if minNrOfNodes == 1 -> must be part of the solution (in Sudoku terms: it's a naked or hidden single ;-)
    while (next != this && minNrOfNodes > 1) {
        val nrOfNodes = (next as HeaderNode).nrOfNodes
        if (nrOfNodes < minNrOfNodes) {
            minNrOfNodes = nrOfNodes
            header = next
        }
        next = next.right
    }
    return header
}
