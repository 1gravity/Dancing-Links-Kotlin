package com.onegravity.dlx.model

import com.onegravity.dlx.model.Direction.*

/**
 * The root node is the root of the DLX data structure.
 * It's a special node to the `left` of the first header node.
 */
class RootNode: DLXNode("Root Node") {

    init {
        left = this
        right = this
        up = this
        down = this
    }

    /**
     * Returns a Collection of all HeaderNodes
     */
    fun getHeaders(): Collection<HeaderNode> = ArrayList<HeaderNode>().apply {
        forEach(Right) { add(it as HeaderNode) }
    }

    override fun toString() = StringBuilder("$payload").apply {
        forEach(Right) { header ->
            append(" | $header")
        }
    }.toString()

}
