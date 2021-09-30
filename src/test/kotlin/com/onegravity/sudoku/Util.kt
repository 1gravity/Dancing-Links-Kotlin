package com.onegravity.sudoku

import com.onegravity.sudoku.SudokuMatrix.Companion.getIndexValue
import com.onegravity.sudoku.SudokuMatrix.Companion.toSudokuMatrix
import com.onegravity.sudoku.model.Grid
import com.onegravity.sudoku.model.Puzzle
import org.junit.jupiter.api.Assertions
import java.io.File
import java.text.DecimalFormat

fun testAndValidateSudoku(
    puzzle: Puzzle,
    solution: IntArray,
    solve: Array<BooleanArray>.(collect: (List<Int>) -> Unit) -> Unit
) {
    testSudoku(puzzle, solution, solve)
}

private fun testSudoku(
    puzzle: Puzzle,
    solution: IntArray?,
    solve: Array<BooleanArray>.(collect: (List<Int>) -> Unit) -> Unit
) {
    var solutionFound = false
    puzzle.toSudokuMatrix()
        .solve { rows ->
            if (solution != null) validateSolution(solution, puzzle, rows.toGrid(puzzle))
            solutionFound = true
        }
    assert(solutionFound)
}

fun validateSolution(expected: IntArray, original: Puzzle, actual: Puzzle) {
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
fun List<Int>.toGrid(original: Puzzle) = Grid(original.extraRegionType, original.isJigsaw)
    .apply {
        sorted().forEach { row ->
            val (index, value) = getIndexValue(row)
            setValue(index, value, original.getCell(index).isGiven)
        }
    }

/**
 * Convert values from the standard csv format to an IntArray.
 * Input:  .......12........3..23..4....18....5.6..7.8.......9.....85.....9...4.5..47...6...
 * Output: [0,0,0,0,0,0,0,1,2,0,0,0,0,0,0,0,0,3 etc.]
 */
fun String.toIntArray() = this.map { c -> c.digitToIntOrNull() ?: 0 }.toIntArray()

/**
 * Reads puzzles from a file in the standard csv format with comma separated solution.
 *
 * @param fileName The name of the file to read
 * @param process the function called with each Sudoku passing in the puzzle and the solution as IntArrays
 */
fun getPuzzles(fileName: String, limit: Int = Int.MAX_VALUE, process: (puzzle: IntArray, solution: IntArray) -> Unit) {
    var count = 0
    File(ClassLoader.getSystemResource(fileName).file)
        .forEachLine { line ->
            // find Sudoku puzzles
            sudokuPattern.findAll(line)
                .forEach { matchResult ->
                    if (count++ < limit) {
                        // get group with puzzle values
                        val puzzle = matchResult.groupValues[1].toIntArray()
                        val solution = matchResult.groupValues[2].toIntArray()
                        Assertions.assertEquals(81, puzzle.size)
                        Assertions.assertEquals(81, solution.size)
                        process(puzzle, solution)
                    } else
                        return@forEachLine
                }
        }
}
private val sudokuPattern = """^([\d.]{81}),(\d{81}).*${'$'}""".toRegex()

fun testPerformance(testSet: String, puzzle: IntArray, solve: (puzzle: IntArray) -> Unit) {
    val l = System.currentTimeMillis()
    solve(puzzle)
    val time = System.currentTimeMillis() - l
    val puzzlesPerSec = 1000F.div(time)
    println("$testSet took: $time ms, puzzles/sec: ${puzzlesPerSec.twoDecimals()}")
}

fun testPerformance(testSet: String, filename: String, limit: Int = Int.MAX_VALUE, solve: (puzzle: IntArray) -> Unit) {
    var count = 0
    var totalTime = 0L
    getPuzzles(filename, limit) { puzzle, _ ->
        count++
        val l = System.currentTimeMillis()
        solve(puzzle)
        totalTime += System.currentTimeMillis() - l
    }
    val average = totalTime.toFloat().div(count)
    val puzzlesPerSec = 1000F.div(average)
    println("$filename - $testSet took: $totalTime ms, average: ${average.twoDecimals()} ms, puzzles/sec: ${puzzlesPerSec.twoDecimals()}")
}

private val df = DecimalFormat("0.00")
fun Float.twoDecimals(): String = df.format(this)
