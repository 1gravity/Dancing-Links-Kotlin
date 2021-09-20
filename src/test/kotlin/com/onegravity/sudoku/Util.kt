package com.onegravity.sudoku

import com.onegravity.dlx.model.DLXNode
import com.onegravity.sudoku.model.Grid
import com.onegravity.sudoku.SudokuMatrix.Companion.IndexValue

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
