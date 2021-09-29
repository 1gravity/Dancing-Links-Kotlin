package com.onegravity.bruteforce

import com.onegravity.sudoku.model.Grid
import kotlin.collections.ArrayList

fun Grid.solve(): IntArray {
    // initialize all data structures
    val digits = getCells().map { it.value }.toIntArray()

    val rows = IntArray(9) { 0x1ff }
    val columns = IntArray(9) { 0x1ff }
    val blocks = IntArray(9) { 0x1ff }

    // we map every extra region group to a constraint because that's the only way we can model it to fit X-Sudoku,
    // the only puzzle type with overlapping groups (cell 4/4)
    val nrOfExtraRegions = getRegions(extraRegionType).size
    val hasExtraRegions = nrOfExtraRegions > 0
    val extraRegions = if (hasExtraRegions) Array(nrOfExtraRegions) { IntArray(1) { 0x1ff } } else emptyArray()

    val constraints = arrayOf(rows, columns, blocks, *extraRegions)

    val todo = ArrayList<IntArray>()
    digits.forEachIndexed { cellIndex, digit ->
        val cell = getCell(cellIndex)

        val indicesByGroup = if (extraRegionType != null) getIndices(extraRegionType) else emptyArray()
        val extraRegionIndices = indicesByGroup.fold(ArrayList<Int>()) { extraRegionIndices, groupIndices ->
            // since each region group is a separate constraint, the group index will always be 0 (or -1)
            val index2Add = if (groupIndices.contains(cellIndex)) 0 else -1
            extraRegionIndices.add(index2Add)
            extraRegionIndices
        }.toIntArray()
        val indicesToDo = intArrayOf(cellIndex, cell.row, cell.col, cell.block, *extraRegionIndices)

        when (digit) {
            0 -> todo.add(indicesToDo)
            else -> {
                // remove the digit as a candidate
                val bit = 1 shl (digit - 1)
                clearCandidate(constraints, indicesToDo, bit)
            }
        }
    }

    // solve the puzzle
    return if (solve(digits, todo, 0, constraints)) digits else throw Exception("No solution")
}

private fun solve(
    digits: IntArray,
    todo: MutableList<IntArray>,
    startIndex: Int,
    constraints: Array<IntArray>
): Boolean {
    if (todo.isEmpty()) return true

    var (candidates, indices) = getMostConstraint(todo, startIndex, constraints)
    val (cellIndex) = indices   // the first elements is always the cell index

    while (candidates > 0) {
        // get the lowest bit
        val lowestBit = candidates.takeLowestOneBit()

        // remove the candidate
        clearCandidate(constraints, indices, lowestBit)

        if (startIndex == todo.size-1 || solve(digits, todo, startIndex + 1, constraints)) {
            val candidate = bits2Digits[lowestBit] ?: throw IllegalStateException("candidate not in range [1, 1ff]")
            digits[cellIndex] = candidate
            return true
        }

        // restore the candidate
        setCandidate(constraints, indices, lowestBit)

        // clear the lowest bit
        candidates = candidates.xor(lowestBit)
    }

    return false
}

/**
 * @param todo all empty cells as a list of IntArray with IntArray being the row/column/block/extraregion region
 *        codes of the cell to process so:
 *           0:  intArrayOf(0, 0, 0, 0) (cell 0, row 0, col 0, block 0)
 *           1:  intArrayOf(4, 0, 4, 1) (cell 4, row 0, col 4, block 1)
 * @param constraints the candidates of the regions (row, column etc.) so:
 *           0 (row)     -> intArrayOf(0x1ff, 0x180, 0x080, ...)
 *           1 (column)  -> intArrayOf(0x001, 0x0f0, 0x0ff, ...)
 *           2 (block)   -> intArrayOf(0x104, 0x002, 0x001, ...)
 *           3 (extra 1) -> intArrayOf(0x104, 0x002, 0x001, ...)
 *           4 (extra 2) -> intArrayOf(0x104, 0x002, 0x001, ...)
 */
private fun getMostConstraint(
    todo: MutableList<IntArray>,
    startIndex: Int,
    constraints: Array<IntArray>
): Pair<Int, IntArray> {
    var minIndex = startIndex
    var minSetBits = Int.MAX_VALUE
    var candidates = 0

    todo.subList(startIndex, todo.size)
        .forEachIndexed { todoIndex, indices ->
            val tmp = indices.foldIndexed(0x1ff) { index, acc, regionCode ->
                if (index > 0 && regionCode >= 0)  // skip the cell index (index == 0)
                    acc.and(constraints[index - 1][regionCode])
                else
                    acc
            }

            val setBits = tmp.countOneBits()
            if (setBits < minSetBits) {
                candidates = tmp
                minSetBits = setBits
                minIndex = startIndex + todoIndex
            }
            if (minSetBits == 1) return@forEachIndexed
        }

    if (startIndex != minIndex) {
        val tmp = todo[startIndex]
        todo[startIndex] = todo[minIndex]
        todo[minIndex] = tmp
    }

    return Pair(candidates, todo[startIndex])
}

private fun clearCandidate(constraints: Array<IntArray>, indices: IntArray, bit: Int) {
    set(constraints, indices, bit) { value, bit2Clear -> value xor bit2Clear }
}

private fun setCandidate(constraints: Array<IntArray>, indices: IntArray, bit: Int) {
    set(constraints, indices, bit) { value, bit2Set -> value or bit2Set }
}

private fun set(constraints: Array<IntArray>, indices: IntArray, bit: Int, op: (value: Int, bit: Int) -> Int) {
    indices.forEachIndexed { index, regionCode ->
        if (index > 0 && regionCode >= 0) {    // skip the cell index (index == 0)
            constraints[index - 1][regionCode] = op(constraints[index - 1][regionCode], bit)
        }
    }
}
