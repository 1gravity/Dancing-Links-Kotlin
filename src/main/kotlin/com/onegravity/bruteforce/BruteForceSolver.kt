package com.onegravity.bruteforce

import java.util.*

private val rowIndices = intArrayOf(
    0,0,0,0,0,0,0,0,0,
    1,1,1,1,1,1,1,1,1,
    2,2,2,2,2,2,2,2,2,
    3,3,3,3,3,3,3,3,3,
    4,4,4,4,4,4,4,4,4,
    5,5,5,5,5,5,5,5,5,
    6,6,6,6,6,6,6,6,6,
    7,7,7,7,7,7,7,7,7,
    8,8,8,8,8,8,8,8,8,
)

private val colIndices = intArrayOf(
    0,1,2,3,4,5,6,7,8,
    0,1,2,3,4,5,6,7,8,
    0,1,2,3,4,5,6,7,8,
    0,1,2,3,4,5,6,7,8,
    0,1,2,3,4,5,6,7,8,
    0,1,2,3,4,5,6,7,8,
    0,1,2,3,4,5,6,7,8,
    0,1,2,3,4,5,6,7,8,
    0,1,2,3,4,5,6,7,8
)

private val blockIndices = intArrayOf(
    0,0,0,1,1,1,2,2,2,
    0,0,0,1,1,1,2,2,2,
    0,0,0,1,1,1,2,2,2,
    3,3,3,4,4,4,5,5,5,
    3,3,3,4,4,4,5,5,5,
    3,3,3,4,4,4,5,5,5,
    6,6,6,7,7,7,8,8,8,
    6,6,6,7,7,7,8,8,8,
    6,6,6,7,7,7,8,8,8
)

private val bits2Digits = mapOf(
    1   to 1,
    2   to 2,
    4   to 3,
    8   to 4,
    16  to 5,
    32  to 6,
    64  to 7,
    128 to 8,
    256 to 9,
)

private data class Indices(val cell: Int, val row: Int, val col: Int, val block: Int)

fun IntArray.solve(): IntArray {
    val digits = clone()

    val rows = Array(9) { 0x1ff }
    val columns = Array(9) { 0x1ff }
    val blocks = Array(9) { 0x1ff }

    val todo = ArrayList<Indices>()

    digits.forEachIndexed { cellIndex, digit ->
        val rowIndex = rowIndices[cellIndex]
        val colIndex = colIndices[cellIndex]
        val blockIndex = blockIndices[cellIndex]
        when (digit) {
            0 -> todo.add(Indices(cellIndex, rowIndex, colIndex, blockIndex))
            else -> {
                // remove the digit as a candidate
                val bit = 1 shl (digit - 1)
                rows[rowIndex] = rows[rowIndex] xor bit
                columns[colIndex] = columns[colIndex] xor bit
                blocks[blockIndex] = blocks[blockIndex] xor bit
            }
        }
    }

    fun getMostConstraint(todo: MutableList<Indices>, todoIndex: Int): Pair<Int, Indices> {
        var minIndex = todoIndex
        var minSetBits = Int.MAX_VALUE
        var candidates = 0

        todo.subList(todoIndex, todo.size)
            .forEachIndexed { index, indices ->
                val tmp = rows[indices.row].and(columns[indices.col]).and(blocks[indices.block])
                val setBits = tmp.countOneBits()
                if (setBits < minSetBits) {
                    candidates = tmp
                    minSetBits = setBits
                    minIndex = todoIndex + index
                }
                if (minSetBits == 1) return@forEachIndexed
            }

        if (todoIndex != minIndex) {
            val tmp = todo[todoIndex]
            todo[todoIndex] = todo[minIndex]
            todo[minIndex] = tmp
        }

        return Pair(candidates, todo[todoIndex])
    }

    fun solve(digits: IntArray, todo: MutableList<Indices>, todoIndex: Int): Boolean {
        if (todo.isEmpty()) return true

        var (candidates, indices) = getMostConstraint(todo, todoIndex)
        val (cellIndex, rowIndex, colIndex, blockIndex) = indices

        while (candidates > 0) {
            // get the lowest bit
            val lowestBit = candidates.takeLowestOneBit()

            // remove the candidate
            rows[rowIndex] = rows[rowIndex] xor lowestBit
            columns[colIndex] = columns[colIndex] xor lowestBit
            blocks[blockIndex] = blocks[blockIndex] xor lowestBit

            if (todoIndex == todo.size-1 || solve(digits, todo, todoIndex + 1)) {
                val candidate = bits2Digits[lowestBit] ?: throw IllegalStateException("candidate not in range [1, 1ff]")
                digits[cellIndex] = candidate
                return true
            }

            // restore the candidate
            rows[rowIndex] = rows[rowIndex] or lowestBit
            columns[colIndex] = columns[colIndex] or lowestBit
            blocks[blockIndex] = blocks[blockIndex] or lowestBit

            // clear the lowest bit
            candidates = candidates.xor(lowestBit)
        }

        return false
    }

    return if (solve(digits, todo, 0)) digits else throw Exception("No solution")
}

