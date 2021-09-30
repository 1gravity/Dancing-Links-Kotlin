package com.onegravity.bruteforce

import com.onegravity.sudoku.model.Puzzle
import java.util.*

private data class Indices(val cell: Int, val row: Int, val col: Int, val block: Int)

fun Puzzle.solveNoExtraRegions(): IntArray {
    // initialize all data structures
    val digits = getCells().map { it.value }.toIntArray()

    val rows = IntArray(9) { 0x1ff }
    val columns = IntArray(9) { 0x1ff }
    val blocks = IntArray(9) { 0x1ff }

    val todo = ArrayList<Indices>()

    digits.forEachIndexed { cellIndex, digit ->
        val cell = getCell(cellIndex)
        val rowIndex = cell.row
        val colIndex = cell.col
        val blockIndex = cell.block
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

    fun getMostConstraint(todo: MutableList<Indices>, startIndex: Int): Pair<Int, Indices> {
        var minIndex = startIndex
        var minSetBits = Int.MAX_VALUE
        var candidates = 0

        todo.subList(startIndex, todo.size)
            .forEachIndexed { index, indices ->
                val tmp = rows[indices.row].and(columns[indices.col]).and(blocks[indices.block])
                val setBits = tmp.countOneBits()
                if (setBits < minSetBits) {
                    candidates = tmp
                    minSetBits = setBits
                    minIndex = startIndex + index
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
