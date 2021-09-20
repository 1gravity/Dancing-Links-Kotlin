package com.onegravity.sudoku.legacy

import com.onegravity.sudoku.model.Grid

class NoSolutionHint(val grid: Grid) : Hint {

    override fun apply() {
        /* NOP */
    }

}
