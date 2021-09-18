package com.onegravity.sudoku.model

import java.util.*

/**
 * The Cell implementation.
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

    override fun getPotentialValues() = potentialValues

    override fun hasPotentialValue(value: Int) = potentialValues[value]

    override fun addPotentialValue(value: Int) {
        potentialValues[value] = true
    }

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
