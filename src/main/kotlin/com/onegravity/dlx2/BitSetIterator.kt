package com.onegravity.dlx2

import java.util.*

class BitSetIterator(private val bitset: BitSet) : Iterator<Int> {
    private var pos = 0

    override fun hasNext() = bitset.nextSetBit(pos) >= 0

    override fun next() = bitset.run {
        pos = nextSetBit(pos)
        if (pos < 0) throw NoSuchElementException()
        pos++
    }
}
