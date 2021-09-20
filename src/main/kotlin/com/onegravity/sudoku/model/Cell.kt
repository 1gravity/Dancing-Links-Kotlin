package com.onegravity.sudoku.model

import java.util.*

/**
 * A cell of a sudoku grid.
 * <p>
 * Holds:
 * <ul>
 * <li>The x and y coordinates (column/row) within the grid
 * <li>The current value, or <code>0</code> if the cell is empty
 * <li>The bitset of potential values for this cell (the candidates).
 * </ul>
 */
interface Cell {

    val position: CellPosition

    var value: Int

    var isGiven: Boolean

    /**
     * Returns true if no value has been set yet
     */
    fun isEmpty() = value == 0

    /**
     * Get the potential values for this cell.
     *
     * The result is returned as a bitset. Each of the bit number 1 to 9 is set if the corresponding value is a
     * potential value for this cell. Bit number <tt>0</tt> is not used and ignored.
     *
     * @return the potential values for this cell
     */
    fun getPotentialValues(): BitSet

    /**
     * Test whether the given value is a potential value for this cell.
     *
     * @param value the potential value to test, between 1 and 9, inclusive
     *
     * @return whether the given value is a potential value for this cell
     */
    fun hasPotentialValue(value: Int): Boolean

    /**
     * Add the given value as a potential value for this cell.
     *
     * @param value the value to add, between 1 and 9, inclusive
     */
    fun addPotentialValue(value: Int)

    /**
     * Remove the given value from the potential values of this cell.
     *
     * @param value the value to remove, between 1 and 9, inclusive
     */
    fun removePotentialValue(value: Int)

    /**
     * Remove the given valueS from the potential values of this cell.
     *
     * @param valuesToRemove the valueS to remove, between 1 and 9, inclusive
     */
    fun removePotentialValues(valuesToRemove: BitSet)

    /**
     * Delete all potential values of this cell.
     */
    fun clearPotentialValues()

}
