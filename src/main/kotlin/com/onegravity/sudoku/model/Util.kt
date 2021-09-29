package com.onegravity.sudoku.model

import java.util.*

/**
 * Map region codes to cell indices
 *
 * Region codes for rows would look like this:
 *  0 0 0 0 0 0 0 0 0
 *  1 1 1 1 1 1 1 1 1
 *  2 2 2 2 2 2 2 2 2
 *  etc.
 *
 * Region codes for columns would look like this:
 *  0 1 2 3 4 5 6 7 8
 *  0 1 2 3 4 5 6 7 8
 *  0 1 2 3 4 5 6 7 8
 *  etc.
 *
 * Region codes for (standard) blocks would look like this:
 *  0 0 0 1 1 1 2 2 2
 *  0 0 0 1 1 1 2 2 2
 *  0 0 0 1 1 1 2 2 2
 *  3 3 3 4 4 4 5 5 5
 *  3 3 3 4 4 4 5 5 5
 *  3 3 3 4 4 4 5 5 5
 *  etc.
 *
 * @param codesArray one or multiple arrays [0-80] each one holding region codes.
 *
 * @return A Map mapping the region code to an IntArray of the region's cells (or rather their indices from 0..80)
 */
fun mapCodes2Indices(vararg codesArray: IntArray): MutableMap<Int, IntArray> =
    TreeMap<Int, IntArray>().apply {
        for (codes in codesArray) {
            assert(codes.size == 81)
            for (index in 0..80) {
                val code = codes[index]
                if (code >= 0) {
                    val codeList = this[code]?.toMutableList() ?: ArrayList()
                    codeList.add(index)
                    this[code] = codeList.toIntArray()
                }
            }
        }
    }

/**
 * Computes the cell indices for regions identified by their region codes.
 **
 * @return An Array of cell indices (IntArray). The Array index matches the region codes.
 * E.g. a Hyper puzzle has four extra regions so the Array holds 4 lists of cell indices:
 * region 0 -> (10,11,12,19,20,21,28,29,30)
 * region 1 -> (14,15,16,23,24,25,32,33,34)
 * region 2 -> (46,47,48,55,56,57,64,65,66)
 * region 3 -> (50,51,52,59,60,61,68,69,70)
 */
fun computeRegionIndices(vararg codesArray: IntArray): Array<IntArray> =
    mapCodes2Indices(*codesArray).run {
        keys.map { code ->
            this[code]
                ?.toTypedArray()
                ?.toIntArray()!!
        }.toTypedArray()
    }

/**
 * Generic function to compute neighbors for all cells.
 * The regions are defined by region codes (see mapCodes2Positions).
 *
 * The resulting array(s) are [81][9] arrays holding the neighbors for all 81 cell -> [cellIndex][0-8]:
 *
 * row:
 *  index 0 -> indices 1  2  3  4  5  6  7  8
 *  index 1 -> indices 0  2  3  4  5  6  7  8
 *  etc.
 *
 * column:
 *  index 0 -> indices 9  18 27 36 45 54 63 72
 *  index 1 -> indices 10 19 28 37 46 55 64 73
 *  etc.
 *
 * blocks
 *  index 0 -> indices 1  2  9 10 11 18 19 20
 *  index 1 -> indices 0  2  9 10 11 18 19 20
 *  etc.
 */
fun computeNeighbors(vararg codesArray: IntArray): Array<IntArray> {
    val indices = computeRegionIndices(*codesArray)

    return (0..80).map { index ->
        codesArray.fold(ArrayList<Int>()) { list, codes ->
            val code = codes[index]
            val neighbors = if (code >= 0) {
                indices[code].filter { it != index }
            } else emptyList()
            list.addAll(neighbors)
            list
        }.sorted().toIntArray()
    }.toTypedArray()
}
