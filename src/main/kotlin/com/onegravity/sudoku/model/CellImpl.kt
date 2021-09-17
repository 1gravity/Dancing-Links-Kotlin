package com.onegravity.sudoku.model

import java.util.*

/**
 * A cell of a sudoku grid.
 * <p>
 * Holds:
 * <ul>
 * <li>The x and y coordinates (column/row) within the grid
 * <li>The block code of the block the cell is part of
 * <li>The current value, or <code>0</code> if the cell is empty
 * <li>The bitset of potential values for this cell (the candidates).
 * </ul>
 */
class CellImpl(
    override val position: CellPosition,
    val blockCode: Int,
    override var isGiven: Boolean = false,
) : Cell {

    override var value: Int = 0
        set(newValue) {
            if (newValue in 0..9) field = newValue else throw IllegalArgumentException("value must be between 0 and 9")
            potentialValues.clear()
        }

    private val potentialValues = BitSet(9)

    /**
     * Get the potential values for this cell.
     *
     *
     * The result is returned as a bitset. Each of the
     * bit number 1 to 9 is set if the corresponding
     * value is a potential value for this cell. Bit number
     * <tt>0</tt> is not used and ignored.
     * @return the potential values for this cell
     */
    override fun getPotentialValues() = potentialValues

    /**
     * Test whether the given value is a potential
     * value for this cell.
     * @param value the potential value to test, between 1 and 9, inclusive
     * @return whether the given value is a potential value for this cell
     */
    override fun hasPotentialValue(value: Int) = potentialValues[value]

    /**
     * Add the given value as a potential value for this cell
     * @param value the value to add, between 1 and 9, inclusive
     */
    override fun addPotentialValue(value: Int) {
        potentialValues[value] = true
    }

    /**
     * Remove the given value from the potential values of this cell.
     * @param value the value to remove, between 1 and 9, inclusive
     */
    override fun removePotentialValue(value: Int) {
        potentialValues[value] = false
    }

    override fun removePotentialValues(valuesToRemove: BitSet) {
        potentialValues.andNot(valuesToRemove)
    }

    override fun clearPotentialValues() {
        potentialValues.clear()
    }

    override fun toString() = "${position.col}/${position.row}: $value"

    override fun equals(other: Any?) = when(other) {
        is CellImpl -> position == other.position
        else -> false
    }

    override fun hashCode() = position.hashCode()

}
