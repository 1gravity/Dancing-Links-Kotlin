package com.onegravity.sudoku.model.region

enum class RegionType(val isExtraRegion: Boolean) {
    ROW(false),
    COLUMN(false),
    BLOCK(false),
    X(true),
    HYPER(true),
    PERCENT(true),
    CENTERDOT(true),
    ASTERISK(true),
    COLOR(true)
}
