package com.onegravity.dlx.model

class DataNode(val header: HeaderNode, payload: Any): DLXNode(payload) {

    init {
        left = this
        right = this
        up = this
        down = this
    }

    override fun toString() = "data: $payload"
}
