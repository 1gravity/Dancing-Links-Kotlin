package com.onegravity.sudoku.model

data class CellPosition(val col: Int, val row: Int) : Comparable<CellPosition> {

    constructor(index: Int): this(index % 9, index / 9)

    init {
        assert(col in 0..8)
        assert(row in 0..8)
    }

    /**
     * @return the index of the cell from 0 to 80 (left to right then top to bottom):
     * 0 1  2  3  4  5  6  7  8
     * 9 10 11 12 13 14 15 16 17
     * etc.
     */
    fun index() = row * 9 + col

    /**
     * @return the index of the first cell in the row:
     * 0, 9, 18, 27, 36, 45, 54, 63, 72
     */
    fun indexRowStart() = index().div(9).times(9)

    /**
     * @return the index of the first cell in the column:
     * 0, 1, 2, 3, 4, 5, 6, 7, 8
     */
    fun indexColumnStart() = col

    /**
     * Positions are sorted by row the column so it matches the natural index order (0..80)
     */
    override fun compareTo(other: CellPosition) =
        when (val rowCompare = row.compareTo(other.row)) {
            0 -> col.compareTo(other.col)
            else -> rowCompare
        }

}
