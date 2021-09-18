package com.onegravity.sudoku.model.region

/**
 * All the Regions this Sudoku implementation supports.
 */
enum class RegionType(val isExtraRegion: Boolean) {
    // these are regular regions:
    ROW(false),
    COLUMN(false),
    BLOCK(false),

    // these are extra regions:
    X(true),
    HYPER(true),
    PERCENT(true),
    CENTERDOT(true),
    ASTERISK(true),
    COLOR(true)
}
