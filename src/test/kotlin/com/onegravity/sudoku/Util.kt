package com.onegravity.sudoku

import com.onegravity.dlx.model.DLXNode
import com.onegravity.sudoku.model.Grid
import com.onegravity.sudoku.SudokuMatrix.Companion.IndexValue
import com.onegravity.sudoku.SudokuMatrix.Companion.getIndexValue
import org.junit.jupiter.api.Assertions
import java.io.File

/**
 * Converts an AlgorithmX solution (Collection of DLXNodes) into a Sudoku Grid (original DLX algorithm).
 *
 * @param original the original Sudoku puzzle the algorithm solved so that we can set isGiven properly
 */
fun Collection<DLXNode>.toGrid(original: Grid) = Grid(original.extraRegionType, original.isJigsaw).apply {
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

/**
 * Converts an AlgorithmX solution (Collection of row nodes) into a Sudoku Grid (DLX2 algorithm).
 *
 * @param original the original Sudoku puzzle the algorithm solved so that we can set isGiven properly
 */
fun List<Int>.toGrid(original: Grid) = Grid(original.extraRegionType, original.isJigsaw).apply {
    sorted().forEach { row ->
        val (index, value) = getIndexValue(row)
        setValue(index, value, original.getCell(index).isGiven)
    }
}

/**
 * Converts an AlgorithmX solution (Collection of DLXNodes) into a Sudoku Grid (DLX3 algorithm).
 *
 * @param original the original Sudoku puzzle the algorithm solved so that we can set isGiven properly
 */
fun Collection<com.onegravity.dlx3.DLXNode>.toGridDLX3(original: Grid) =
    Grid(original.extraRegionType, original.isJigsaw).apply {
        val sudokuComparator = Comparator<com.onegravity.dlx3.DLXNode> { n0, n1 ->
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

private val sudokuPattern = """^([\d.]{81}),(\d{81}).*${'$'}""".toRegex()

// convert values from a String to an IntArray
private fun String.toIntArray() = this.map { c -> c.digitToIntOrNull() ?: 0 }.toIntArray()

fun getPuzzles(filename: String, process: (puzzle: IntArray, solution: IntArray) -> Unit) {
    File(ClassLoader.getSystemResource(filename).file)
        .forEachLine { line ->
            // find Sudoku puzzles
            sudokuPattern.findAll(line)
                .forEach { match ->
                    // get group with puzzle values
                    val puzzle = match.groupValues[1].toIntArray()
                    val solution = match.groupValues[2].toIntArray()
                    Assertions.assertEquals(81, puzzle.size)
                    Assertions.assertEquals(81, solution.size)
                    process(puzzle, solution)
                }
        }
}
