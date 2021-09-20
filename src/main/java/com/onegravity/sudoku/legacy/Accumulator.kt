package com.onegravity.sudoku.legacy

/**
 * An Accumulator collects hints till "something" stops the collection.
 * Different implementations of the Accumulator have different stop criteria.
 */
interface Accumulator {

    /**
     * Add a hint to this accumulator.
     *
     * @param hint the hint to add
     *
     * @throws StopHintCollection if this accumulator wants to stop the gathering of hints.
     */
    @Throws(StopHintCollection::class)
    fun add(hint: Hint?)

    fun getHints(): List<Hint>

}
