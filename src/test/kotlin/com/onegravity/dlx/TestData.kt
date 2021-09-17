package com.onegravity.dlx

// https://en.wikipedia.org/wiki/Knuth%27s_Algorithm_X
val matrixTest1 = arrayOf(
    booleanArrayOf(true, false, false, true, false, false, true),
    booleanArrayOf(true, false, false, true, false, false, false),
    booleanArrayOf(false, false, false, true, true, false, true),
    booleanArrayOf(false, false, true, false, true, true, false),
    booleanArrayOf(false, true, true, false, false, true, true),
    booleanArrayOf(false, true, false, false, false, false, true)
)

// https://garethrees.org/2007/06/10/zendoku-generation/#section-4.2
val matrixTest2 = arrayOf(
    booleanArrayOf(true,false,false,false,true,false,false,false,true,false,false,false),
    booleanArrayOf(true,false,false,false,false,false,true,false,false,false,true,false),
    booleanArrayOf(false,true,false,false,false,true,false,false,true,false,false,false),
    booleanArrayOf(false,true,false,false,false,false,false,true,false,false,true,false),
    booleanArrayOf(false,false,true,false,true,false,false,false,false,true,false,false),
    booleanArrayOf(false,false,true,false,false,false,true,false,false,false,false,true),
    booleanArrayOf(false,false,false,true,false,true,false,false,false,true,false,false),
    booleanArrayOf(false,false,false,true,false,false,false,true,false,false,false,true)
)

val matrixTest3 = arrayOf(
    booleanArrayOf(true,true,false,false,false,false,false,false,false,false,false,false),
    booleanArrayOf(true,false,false,false,false,false,false,false,false,false,false,false),
    booleanArrayOf(false,true,false,false,false,false,false,false,false,false,false,false),
    booleanArrayOf(false,false,true,true,false,false,false,false,false,false,false,false),
    booleanArrayOf(false,false,true,false,false,false,false,false,false,false,false,false),
    booleanArrayOf(false,false,false,true,false,false,false,false,false,false,false,false),
    booleanArrayOf(false,false,false,false,true,true,false,false,false,false,false,false),
    booleanArrayOf(false,false,false,false,true,false,false,false,false,false,false,false),
    booleanArrayOf(false,false,false,false,false,true,false,false,false,false,false,false),
    booleanArrayOf(false,false,false,false,false,false,true,true,false,false,false,false),
    booleanArrayOf(false,false,false,false,false,false,true,false,false,false,false,false),
    booleanArrayOf(false,false,false,false,false,false,false,true,false,false,false,false),
    booleanArrayOf(false,false,false,false,false,false,false,false,true,true,false,false),
    booleanArrayOf(false,false,false,false,false,false,false,false,true,false,false,false),
    booleanArrayOf(false,false,false,false,false,false,false,false,false,true,false,false),
    booleanArrayOf(false,false,false,false,false,false,false,false,false,false,true,true),
    booleanArrayOf(false,false,false,false,false,false,false,false,false,false,true,false),
    booleanArrayOf(false,false,false,false,false,false,false,false,false,false,false,true)
)

val matrixTest4 = arrayOf(
    booleanArrayOf(false,true,true,true,true,true,true,true,true,true,true,true),
    booleanArrayOf(true,false,true,true,true,true,true,true,true,true,true,true),
    booleanArrayOf(true,true,false,true,true,true,true,true,true,true,true,true),
    booleanArrayOf(true,true,true,false,true,true,true,true,true,true,true,true),
    booleanArrayOf(true,true,true,true,false,true,true,true,true,true,true,true),
    booleanArrayOf(true,true,true,true,true,false,true,true,true,true,true,true),
    booleanArrayOf(true,true,true,true,true,true,false,true,true,true,true,true),
    booleanArrayOf(true,true,true,true,true,true,true,false,true,true,true,true)
)
