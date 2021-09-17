package com.onegravity.dlx.model

import com.onegravity.dlx.model.Direction.*

class RootNode: DLXNode("Root Node") {

    init {
        left = this
        right = this
        up = this
        down = this
    }

    override fun toString() = StringBuilder("$payload")
        .apply {
            forEach(Right) { header ->
                append("$header | ")
            }
        }.toString()

}
