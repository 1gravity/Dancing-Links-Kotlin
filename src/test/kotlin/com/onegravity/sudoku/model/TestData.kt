package com.onegravity.sudoku.model

import com.onegravity.sudoku.model.region.Block
import com.onegravity.sudoku.model.region.RegionType
import com.onegravity.sudoku.testValues

fun getJigsawTestGrid(values:IntArray, blockCodes: IntArray) =
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

fun getTestGrid(
    values: IntArray = testValues,
    extraRegionType: RegionType? = null,
    isJigsaw: Boolean = false,
    blockCodes: IntArray = Block.regionCodes
) = Grid(extraRegionType, isJigsaw, blockCodes).apply {
    for (index in 0..80) {
        val value = values[index]
        setValue(index, value, value != 0)
    }
}

val jigsawTest = intArrayOf(
    0, 0, 1, 1, 1, 1, 1, 2, 2, 
    0, 0, 1, 2, 2, 2, 2, 2, 5, 
    0, 1, 1, 1, 4, 2, 2, 5, 5, 
    0, 0, 4, 4, 4, 5, 5, 5, 5, 
    0, 0, 3, 3, 4, 5, 5, 8, 8, 
    3, 3, 3, 3, 4, 4, 4, 8, 8, 
    3, 3, 6, 6, 4, 7, 7, 7, 8, 
    3, 6, 6, 6, 6, 6, 7, 8, 8, 
    6, 6, 7, 7, 7, 7, 7, 8, 8
)

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

object Regions {
    // the region indices here match the ones in the code to test, but we use these here to verify that the code to test
    // returns identical ones
    private val row = arrayOf(
        intArrayOf( 0, 1, 2, 3, 4, 5, 6, 7, 8),
        intArrayOf( 9,10,11,12,13,14,15,16,17),
        intArrayOf(18,19,20,21,22,23,24,25,26),
        intArrayOf(27,28,29,30,31,32,33,34,35),
        intArrayOf(36,37,38,39,40,41,42,43,44),
        intArrayOf(45,46,47,48,49,50,51,52,53),
        intArrayOf(54,55,56,57,58,59,60,61,62),
        intArrayOf(63,64,65,66,67,68,69,70,71),
        intArrayOf(72,73,74,75,76,77,78,79,80)
    )

    private val column = arrayOf(
        intArrayOf( 0, 9,18,27,36,45,54,63,72),
        intArrayOf( 1,10,19,28,37,46,55,64,73),
        intArrayOf( 2,11,20,29,38,47,56,65,74),
        intArrayOf( 3,12,21,30,39,48,57,66,75),
        intArrayOf( 4,13,22,31,40,49,58,67,76),
        intArrayOf( 5,14,23,32,41,50,59,68,77),
        intArrayOf( 6,15,24,33,42,51,60,69,78),
        intArrayOf( 7,16,25,34,43,52,61,70,79),
        intArrayOf( 8,17,26,35,44,53,62,71,80)
    )

    private val block = arrayOf(
        intArrayOf( 0, 1, 2, 9,10,11,18,19,20),
        intArrayOf( 3, 4, 5,12,13,14,21,22,23),
        intArrayOf( 6, 7, 8,15,16,17,24,25,26),
        intArrayOf(27,28,29,36,37,38,45,46,47),
        intArrayOf(30,31,32,39,40,41,48,49,50),
        intArrayOf(33,34,35,42,43,44,51,52,53),
        intArrayOf(54,55,56,63,64,65,72,73,74),
        intArrayOf(57,58,59,66,67,68,75,76,77),
        intArrayOf(60,61,62,69,70,71,78,79,80),
    )

    private val x = arrayOf(
        intArrayOf(0,10,20,30,40,50,60,70,80),
        intArrayOf(8,16,24,32,40,48,56,64,72)
    )

    private val hyper = arrayOf(
        intArrayOf(10,11,12,19,20,21,28,29,30),
        intArrayOf(14,15,16,23,24,25,32,33,34),
        intArrayOf(46,47,48,55,56,57,64,65,66),
        intArrayOf(50,51,52,59,60,61,68,69,70)
    )

    private val percent = arrayOf(
        intArrayOf(10,11,12,19,20,21,28,29,30),
        intArrayOf(50,51,52,59,60,61,68,69,70),
        intArrayOf(8,16,24,32,40,48,56,64,72)
    )

    private val centerdot = arrayOf(
        intArrayOf(10,13,16,37,40,43,64,67,70)
    )

    private val asterisk = arrayOf(
        intArrayOf(13,20,24,37,40,43,56,60,67)
    )

    private val color = arrayOf(
        intArrayOf(0,3,6,27,30,33,54,57,60),
        intArrayOf(1,4,7,28,31,34,55,58,61),
        intArrayOf(2,5,8,29,32,35,56,59,62),
        intArrayOf(9,12,15,36,39,42,63,66,69),
        intArrayOf(10,13,16,37,40,43,64,67,70),
        intArrayOf(11,14,17,38,41,44,65,68,71),
        intArrayOf(18,21,24,45,48,51,72,75,78),
        intArrayOf(19,22,25,46,49,52,73,76,79),
        intArrayOf(20,23,26,47,50,53,74,77,80)
    )

    val type2Indices = mapOf(
        RegionType.ROW to row,
        RegionType.COLUMN to column,
        RegionType.BLOCK to block,
        RegionType.X to x,
        RegionType.HYPER to hyper,
        RegionType.PERCENT to percent,
        RegionType.CENTERDOT to centerdot,
        RegionType.ASTERISK to asterisk,
        RegionType.COLOR to color
    )
}
