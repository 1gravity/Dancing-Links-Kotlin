package com.onegravity.sudoku

data class IndexValue(val index: Int, val value: Int)

fun getIndexValue(subset: Int) = subset2IndexValue[subset]
    ?: throw NoSuchElementException("something's wrong with getIndexValue()")

/**
 * Maps the 729 subsets / rows to cell indices + values, e.g.
 * subset 0 -> 0/1
 * subset 1 -> 0/2
 * subset 2 -> 0/3
 * ...
 * subset 9 -> 1/1
 * subset 10 -> 1/2
 * etc.
 *
 * (see also sudoku_matrix.txt)
 */
private val subset2IndexValue = HashMap<Int, IndexValue>().apply {
    for (index in 0..728) {
        this[index] = IndexValue(index.div(9), index.mod(9) + 1)
    }
}
