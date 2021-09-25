package com.onegravity.dlx.model

/**
 * DataNodes make up the actual DLX matrix.
 * On top of the four direct links they also link to the HeaderNode directly.
 */
class DataNode(val header: HeaderNode, val row: Int, payload: Any): DLXNode(payload) {

    init {
        left = this
        right = this
        up = this
        down = this
    }

    override fun toString() = "data: $payload"
}
