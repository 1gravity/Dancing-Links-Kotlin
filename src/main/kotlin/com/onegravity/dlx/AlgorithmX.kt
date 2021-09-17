package com.onegravity.dlx

import com.onegravity.dlx.model.DLXNode
import com.onegravity.dlx.model.DataNode
import com.onegravity.dlx.model.Direction.*
import com.onegravity.dlx.model.HeaderNode
import com.onegravity.dlx.model.RootNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.yield

/**
 * Algorithm: https://en.wikipedia.org/wiki/Knuth%27s_Algorithm_X
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
fun RootNode.solve(collect: (List<DLXNode>) -> Unit) {
    solve(rootNode = this) { collect(it) }
}

/**
 * The same function using Coroutines/Channels
 */
@ExperimentalCoroutinesApi
fun CoroutineScope.solve(rootNode: RootNode) = produce<Collection<DLXNode>> {
    solve(rootNode, channel)
}

fun RootNode.solveAll(collect: (List<Collection<DLXNode>>) -> Unit) {
    val solutions = ArrayList<Collection<DLXNode>>()
    solve(rootNode = this) { solutions.add(it) }
    collect(solutions)
}

private fun solve(
    solution: ArrayList<DLXNode> = ArrayList(),
    rootNode: RootNode,
    collect: (List<DLXNode>) -> Unit
) {
   // 1. Pick a column
    when (val header = findColumn(rootNode)) {
        null, is RootNode -> {
            // 1.1. if there's no column -> the matrix is empty -> we found a solution
            //   we need to make a copy -> ArrayList(solution), because the solution list is mutable
            //   and we want to return a list that won't be modified by the solve function
            collect(ArrayList(solution))
        }
        is HeaderNode -> {
            // 1.2. else remove the found column from the matrix
            header.cover()

            // 2. Pick a row in the column
            header.forEach(Down) { node ->
                // 3. Add the row to the solution set
                solution.add(node)

                // 4. Cover all columns that this row links to (remove the constraints)
                node.forEach(Right) {
                    (it as DataNode).header.cover()
                }

                // 5. Repeat this algorithm recursively on the reduced matrix
                solve(solution, rootNode, collect)

                // 5.1. undo all changes for step 3 and 4
                solution.remove(node)
                node.forEach(Left) {
                    (it as DataNode).header.uncover()
                }
            }

            // we're at a dead end (no solution) and need to backtrack -> undo step 1.2
            header.uncover()
        }
    }
}

/**
 * The same function using Coroutines/Channels
 */
private suspend fun solve(
    rootNode: RootNode,
    channel: SendChannel<Collection<DLXNode>>,
    solution: ArrayList<DLXNode> = ArrayList()
)  {
    // let's be cooperative...
    yield()

    // 1. Pick a column
    when (val header = findColumn(rootNode)) {
        null, is RootNode -> {
            // 1.1. if there's no column -> the matrix is empty -> we found a solution
            //   we need to make a copy -> ArrayList(solution), because the solution list is mutable
            //   and we want to return a list that won't be modified by the solve function
            channel.send(ArrayList(solution))
        }
        is HeaderNode -> {
            // 1.2. else remove the found column from the matrix
            header.cover()

            // 2. Pick a row in the column
            header.forEachSuspended(Down) { node ->
                // 3. Add the row to the solution set
                solution.add(node)

                // 4. Cover all columns that this row links to (remove the constraints)
                node.forEach(Right) {
                    (it as DataNode).header.cover()
                }

                // 5. Repeat this algorithm recursively on the reduced matrix
                solve(rootNode, channel, solution)

                // 5.1. undo all changes for step 3 and 4
                solution.remove(node)
                node.forEach(Left) {
                    (it as DataNode).header.uncover()
                }
            }

            // we're at a dead end (no solution) and need to backtrack -> undo step 1.2
            header.uncover()
        }
    }
}

private fun findColumn(rootNode: RootNode): DLXNode? {
    var result: HeaderNode? = null
    rootNode.forEach(Right) {
        val header = it as HeaderNode
        result = if (header.nrOfNodes < (result?.nrOfNodes ?: Int.MAX_VALUE)) header else result
    }
    return result
}
