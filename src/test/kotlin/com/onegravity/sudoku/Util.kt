package com.onegravity.sudoku

import com.onegravity.sudoku.SudokuMatrix.Companion.getIndexValue
import com.onegravity.sudoku.SudokuMatrix.Companion.toSudokuMatrix
import com.onegravity.sudoku.model.Grid
import org.junit.jupiter.api.Assertions
import java.io.File

fun testAndValidateSudoku(
    grid: Grid,
    solution: IntArray,
    solve: Array<BooleanArray>.(collect: (List<Int>) -> Unit) -> Unit
) {
    testSudoku(grid, solution, solve)
}

fun testSudoku(
    grid: Grid,
    solve: Array<BooleanArray>.(collect: (List<Int>) -> Unit) -> Unit
) {
    testSudoku(grid, null, solve)
}

private fun testSudoku(
    grid: Grid,
    solution: IntArray?,
    solve: Array<BooleanArray>.(collect: (List<Int>) -> Unit) -> Unit
) {
    var solutionFound = false
    grid.toSudokuMatrix()
        .solve { rows ->
            if (solution != null) validateSolution(solution, grid, rows.toGrid(grid))
            solutionFound = true
        }
    assert(solutionFound)
}

private fun validateSolution(expected: IntArray, original: Grid, actual: Grid) {
    expected.forEachIndexed { index, value ->
        Assertions.assertEquals(original.getCell(index).isGiven, actual.getCell(index).isGiven)
        Assertions.assertEquals(value, actual.getCell(index).value)
    }
}

/**
 * Converts an AlgorithmX solution (Collection of row indices) into a Sudoku Grid.
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
 * Convert values from the standard csv format to an IntArray.
 * Input: .......12........3..23..4....18....5.6..7.8.......9.....85.....9...4.5..47...6...
 * Output: [0,0,0,0,0,0,0,1,2,0,0,0,0,0,0,0,0,3 etc.]
 */
fun String.toIntArray() = this.map { c -> c.digitToIntOrNull() ?: 0 }.toIntArray()

private val sudokuPattern = """^([\d.]{81}),(\d{81}).*${'$'}""".toRegex()
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
