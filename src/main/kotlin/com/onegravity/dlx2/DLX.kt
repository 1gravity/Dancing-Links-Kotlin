package com.onegravity.dlx2

import java.util.*
import java.util.concurrent.ConcurrentHashMap
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
data class CoverMatrix(val rows: Array<IntArray>, val columns: MutableMap<Int, MutableSet<Int>>) {

    companion object {
        fun Array<BooleanArray>.toCoverMatrix(): CoverMatrix {
            val firstRow = if (isNotEmpty()) this[0] else throw IllegalStateException("cover matrix cannot be empty")
            val rowIndices = firstRow.indices.toList()
            val rows = Array(size) { rowIndex ->
                this[rowIndex]
                    .mapIndexed { index, isSet -> if (isSet) rowIndices[index] else -1 }
                    .filter { it >= 0 }
                    .toIntArray()
            }

            val columns = ConcurrentHashMap<Int, MutableSet<Int>>()
            rows.forEachIndexed { rowIndex, columnIndices ->
                columnIndices.forEach { colIndex ->
                    val set = columns[colIndex] ?: HashSet()
                    columns[colIndex] = set
                    set.add(rowIndex)
                }
            }

            return CoverMatrix(rows, columns)
        }
    }

    fun cover(row: Int) = LinkedList<MutableSet<Int>>().also { removedCols ->
        rows[row].forEach { col: Int ->
            columns[col]?.forEach { rowIndex ->
                rows[rowIndex].forEach { colIndex ->
                    if (colIndex != col) columns[colIndex]?.remove(rowIndex)
                }
            }

            removedCols.push(columns.remove(col))
        }
    }

    fun uncover(row: Int, removedColumns: LinkedList<MutableSet<Int>>) {
        rows[row].reversed().forEach { col: Int ->
            columns[col] = removedColumns.pop()

            columns[col]?.forEach { rowIndex ->
                rows[rowIndex].forEach { colIndex ->
                    if (colIndex != col) columns[colIndex]?.add(rowIndex)
                }
            }
        }
    }

}
