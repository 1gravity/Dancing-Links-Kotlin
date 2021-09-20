package com.onegravity.sudoku

import com.onegravity.dlx.model.DLXNode
import com.onegravity.sudoku.model.Grid

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

/**
 * Converts an AlgorithmX solution (Collection of DLXNodes) into a Sudoku Grid.
 *
 * @param original the original Sudoku puzzle the algorithm solved so that we can set isGiven properly
 */
fun Collection<DLXNode>.toGrid(original: Grid) = Grid(null, false).apply {
    val sudokuComparator = Comparator<DLXNode> { n0, n1 ->
        val p0 = (n0.payload as IndexValue)
        val p1 = (n1.payload as IndexValue)
        p0.index.compareTo(p1.index)
    }

    sortedWith(sudokuComparator).forEach { node ->
        with(node.payload as IndexValue) {
            setValue(index, value, original.getCell(index).isGiven)
        }
    }
}
