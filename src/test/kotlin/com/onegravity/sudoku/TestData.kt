package com.onegravity.sudoku

import com.onegravity.sudoku.model.Grid
import com.onegravity.sudoku.model.region.Block
import com.onegravity.sudoku.model.region.RegionType

val testValues = intArrayOf(
    0, 1, 2, 3, 4, 5, 6, 7, 8,
    1, 2, 3, 4, 5, 6, 7, 8, 9,
    2, 3, 4, 5, 6, 7, 8, 9, 0,
    3, 4, 5, 6, 7, 8, 9, 0, 1,
    4, 5, 6, 7, 8, 9, 0, 1, 2,
    5, 6, 7, 8, 9, 0, 1, 2, 3,
    6, 7, 8, 9, 0, 1, 2, 3, 4,
    7, 8, 9, 0, 1, 2, 3, 4, 5,
    8, 9, 0, 1, 2, 3, 4, 5, 6
)

val testSudoku1 = intArrayOf(
    0, 4, 0, 5, 0, 0, 0, 1, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 2, 0, 0, 0, 0, 7, 6,
    0, 0, 8, 0, 9, 0, 6, 0, 4,
    0, 0, 0, 0, 0, 7, 3, 0, 0,
    0, 5, 4, 2, 0, 0, 0, 0, 0,
    0, 1, 0, 0, 0, 0, 0, 0, 0,
    5, 0, 0, 0, 0, 6, 0, 0, 0,
    6, 0, 0, 7, 8, 1, 9, 0, 0
)

val testSudoku1Solution = intArrayOf(
    8,4,6,5,7,9,2,1,3,
    7,3,5,6,1,2,4,8,9,
    1,9,2,8,3,4,5,7,6,
    2,7,8,1,9,3,6,5,4,
    9,6,1,4,5,7,3,2,8,
    3,5,4,2,6,8,7,9,1,
    4,1,9,3,2,5,8,6,7,
    5,8,7,9,4,6,1,3,2,
    6,2,3,7,8,1,9,4,5
)

val testSudoku2 = intArrayOf(
    8,4,6,5,7,9,2,1,3,
    7,3,5,6,1,2,4,8,9,
    1,9,2,8,3,4,5,7,6,
    2,7,8,1,9,3,6,5,4,
    9,6,1,4,5,7,3,2,8,
    3,5,4,2,6,8,7,9,1,
    4,1,9,3,2,5,8,6,7,
    5,8,7,9,4,6,1,3,2,
    6,2,3,7,8,1,9,4,5
)

val testSudokuAlEscargot = intArrayOf(
    1,0,0,0,0,7,0,9,0,
    0,3,0,0,2,0,0,0,8,
    0,0,9,6,0,0,5,0,0,
    0,0,5,3,0,0,9,0,0,
    0,1,0,0,8,0,0,0,2,
    6,0,0,0,0,4,0,0,0,
    3,0,0,0,0,0,0,1,0,
    0,4,0,0,0,0,0,0,7,
    0,0,7,0,0,0,3,0,0
)

val testSudokuAlEscargotSolution = intArrayOf(
    1,6,2,8,5,7,4,9,3,
    5,3,4,1,2,9,6,7,8,
    7,8,9,6,4,3,5,2,1,
    4,7,5,3,1,2,9,8,6,
    9,1,3,5,8,6,7,4,2,
    6,2,8,7,9,4,1,3,5,
    3,5,6,4,7,8,2,1,9,
    2,4,1,9,3,5,8,6,7,
    8,9,7,2,6,1,3,5,4
)

val testSudokuJigsawBlocks = arrayOf(
    intArrayOf(0,0,0,3,3,3,3,3,6),
    intArrayOf(0,0,0,3,3,4,6,3,6),
    intArrayOf(0,0,1,3,4,4,6,6,6),
    intArrayOf(0,1,1,1,4,7,6,7,6),
    intArrayOf(1,1,2,1,4,7,6,7,7),
    intArrayOf(2,1,2,1,4,7,7,7,8),
    intArrayOf(2,2,2,4,4,5,7,8,8),
    intArrayOf(2,5,2,4,5,5,8,8,8),
    intArrayOf(2,5,5,5,5,5,8,8,8)
)
val testSudokuJigsawValues = intArrayOf(
    0,0,2,0,6,0,0,0,0,
    0,0,0,0,0,0,0,0,9,
    8,0,0,0,4,0,0,0,0,
    2,0,3,0,0,0,0,7,0,
    0,6,0,0,8,0,0,4,0,
    0,5,0,0,0,0,3,0,2,
    0,0,0,0,1,0,0,0,8,
    1,0,0,0,0,0,0,0,0,
    0,0,0,0,9,0,1,0,0
)
val testSudokuJigsawSolution = intArrayOf(
    4,9,2,5,6,7,8,1,3,
    6,1,7,2,3,4,5,8,9,
    8,3,1,7,4,6,9,2,5,
    2,4,3,8,5,9,6,7,1,
    5,6,9,3,8,1,2,4,7,
    9,5,4,1,7,8,3,6,2,
    7,2,6,9,1,5,4,3,8,
    1,8,5,6,2,3,7,9,4,
    3,7,8,4,9,2,1,5,6
)

val testXSudoku = intArrayOf(
    0,3,0,9,1,0,0,0,0,
    8,0,0,6,0,0,0,0,0,
    5,0,0,0,0,0,0,0,0,
    0,5,0,0,0,0,0,0,0,
    0,0,0,0,7,0,0,0,6,
    0,0,0,0,0,0,0,3,7,
    7,8,0,0,0,0,0,0,0,
    0,0,5,0,0,9,0,0,1,
    0,0,9,0,0,0,2,6,0
)
val testXSudokuSolution = intArrayOf(
    2,3,4,9,1,5,6,7,8,
    8,9,1,6,2,7,3,4,5,
    5,7,6,8,3,4,9,1,2,
    3,5,7,4,8,6,1,2,9,
    9,1,8,2,7,3,4,5,6,
    4,6,2,5,9,1,8,3,7,
    7,8,3,1,6,2,5,9,4,
    6,2,5,3,4,9,7,8,1,
    1,4,9,7,5,8,2,6,3
)

val testColorSudoku = intArrayOf(
    0,0,0,0,0,8,0,0,7,
    2,5,0,0,0,7,0,0,0,
    0,0,1,2,0,0,4,0,0,
    0,0,0,0,0,0,0,3,6,
    0,0,0,0,9,0,0,0,0,
    0,0,3,0,0,0,5,0,0,
    0,0,4,7,0,0,2,0,0,
    0,0,0,0,0,0,0,8,0,
    1,0,0,0,0,0,0,9,0
)
val testColorSudokuSolution = intArrayOf(
    3,4,9,1,5,8,6,2,7,
    2,5,6,9,4,7,8,1,3,
    7,8,1,2,3,6,4,5,9,
    4,7,2,5,8,1,9,3,6,
    5,6,8,3,9,4,1,7,2,
    9,1,3,6,7,2,5,4,8,
    8,9,4,7,1,3,2,6,5,
    6,3,5,4,2,9,7,8,1,
    1,2,7,8,6,5,3,9,4
)

fun getTestGrid(
    values: IntArray = testValues,
    extraRegionType: RegionType? = null,
    isJigsaw: Boolean = false,
    blockCodes: Array<IntArray> = Block.regionCodes
) = Grid(extraRegionType, isJigsaw, blockCodes).apply {
        for (index in 0..80) {
            val value = values[index]
            setValue(index, value, value != 0)
        }
    }

val jigsawIndices = arrayOf(
    intArrayOf( 0, 1, 9,10,18,27,28,36,37),
    intArrayOf( 2, 3, 4, 5, 6,11,19,20,21),
    intArrayOf( 7, 8,12,13,14,15,16,23,24),
    intArrayOf(38,39,45,46,47,48,54,55,63),
    intArrayOf(22,29,30,31,40,49,50,51,58),
    intArrayOf(17,25,26,32,33,34,35,41,42),
    intArrayOf(56,57,64,65,66,67,68,72,73),
    intArrayOf(59,60,61,69,74,75,76,77,78),
    intArrayOf(43,44,52,53,62,70,71,79,80),
)

fun getJigsawTestGrid(values:IntArray, blockCodes: Array<IntArray>) =
    Grid(null, true, blockCodes).apply {
        for (index in 0..80) {
            setValue(index, values[index], true)
        }
    }

fun checkFailure(errorMsg: String, check: () -> Unit) {
    var failed = false
    try {
        check()
        failed = true
    } catch (e: AssertionError) { }
    if (failed) throw AssertionError(errorMsg)
}

fun checkRange(check: () -> Unit) {
    checkFailure("Range not enforced", check)
}
