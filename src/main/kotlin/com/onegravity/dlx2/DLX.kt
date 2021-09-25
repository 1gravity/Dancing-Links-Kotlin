package com.onegravity.dlx2

import java.util.*

/**
 * This creates the dancing links data structure based on an exact cover matrix.
 * The exact cover matrix is an Array of Boolean Arrays (Array<BooleanArray>).
 *
 * @return the RootNode of the DLX data structure.
 */
fun Array<BooleanArray>.toDLX2(): DLXMatrix {
    val columns = HashMap<Int, BitSet>()
    val rows = Array(size) { rowIndex ->
        this[rowIndex]
            .foldIndexed(ArrayList<Int>()) { index, list, isSet ->
                if (isSet) {
                    list.add(index)
                    columns[index]
                        ?.set(rowIndex)
                        ?:run {
                            val bitset = BitSet()
                            columns[index] = bitset
                            bitset.set(rowIndex)
                        }
                }
                list
            }
            .toIntArray()
    }
    return DLXMatrix(rows, columns)
}
