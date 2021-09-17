package com.onegravity.dlx.model

import com.onegravity.dlx.model.Direction.*

class HeaderNode(payload: Any): DLXNode(payload) {

    init {
        left = this
        right = this
        up = this
        down = this
    }

    var nrOfNodes = 0

    /**
     * Covering a column means removing the Column, it's DataNodes and all siblings in the same
     * row.
     *
     * E.g. removing the first column in this matrix:
     * 1001000
     * 1001000
     * 0001101
     * 0010110
     * 0110011
     * 0100001
     *
     * leaves us with (note: the zeros aren't there in a DLX matrix):
     * -------
     * -------
     * 0001101
     * 0010110
     * 0110011
     * 0100001
     */
    fun cover() {
        // remove the header
        remove(Left, Right)

        // iterate down the column
        forEach(Down) {
            // remove all nodes in the row
            it.forEach(Right) { node ->
                node.remove(Up, Down)
                (node as DataNode).header.nrOfNodes--
            }
        }
    }

    /**
     * Add the column back into the matrix.
     */
    fun uncover() {
        // iterate up the column
        forEach(Up) {
            // re-insert all nodes in the row
            it.forEach(Left) { node ->
                node.insertAt(node.up, Down)
                (node as DataNode).header.nrOfNodes++
            }
        }

        // re-insert the column
        insertAt(left, Right)
    }

    override fun toString() = "header: $payload, #data_nodes: $nrOfNodes"

}

