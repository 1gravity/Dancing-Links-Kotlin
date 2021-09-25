package com.onegravity.dlx2

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Rows:
 * row index -> column indices
 * 0 -> 0, 81, 162, 243
 * 1 -> 0, 82, 163, 244
 * 2 -> 0, 83, 164, 245
 *
 * Columns:
 * column index -> row indices
 * 0 ->  0, 1, 2, 3, 4, 5, 6, 7, 8
 * 1 ->  9,10,11,12,13,14,15,16,17
 * 3 -> 18,19,20,21,22,23,24,25,26
 */
@Suppress("ArrayInDataClass")
data class DLXMatrix(val rows: Array<IntArray>, val columns: MutableMap<Int, BitSet>) {

    companion object {
    }

    fun cover(row: Int) = LinkedList<BitSet>().also { removedCols ->
        rows[row].forEach { col: Int ->
            val it = BitSetIterator(columns[col]!!)
            while (it.hasNext()) {
                val rowIndex = it.next()
                rows[rowIndex].forEach { colIndex ->
                    if (colIndex != col) columns[colIndex]?.clear(rowIndex)
                }
            }

            removedCols.push(columns.remove(col))
        }
    }

    fun uncover(row: Int, removedColumns: LinkedList<BitSet>) {
        rows[row].reversed().forEach { col: Int ->
            columns[col] = removedColumns.pop()

            val it = BitSetIterator(columns[col]!!)
            while (it.hasNext()) {
                val rowIndex = it.next()
                rows[rowIndex].forEach { colIndex ->
                    if (colIndex != col) columns[colIndex]?.set(rowIndex)
                }
            }
        }
    }

}
