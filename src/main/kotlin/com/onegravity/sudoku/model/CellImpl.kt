package com.onegravity.sudoku.model

import java.util.*

/**
 * The Cell implementation.
 */
class CellImpl(
    val index: Int,
    val block: Int,
    override var isGiven: Boolean = false,
) : Cell {

    val col = index % 9
    val row = index / 9

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

    override fun toString() = "$col/$row: $value"

    override fun equals(other: Any?) = when(other) {
        is CellImpl -> index == other.index
        else -> false
    }

    override fun hashCode() = index.hashCode()

}
