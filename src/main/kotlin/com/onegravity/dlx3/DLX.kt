package com.onegravity.dlx3

import com.onegravity.dlx.DefaultPayloadProvider
import com.onegravity.dlx.PayloadProvider
import java.util.ArrayList

/**
 * This creates the dancing links data structure based on an exact cover matrix.
 * The exact cover matrix is an Array of Boolean Arrays (Array<BooleanArray>).
 *
 * @param provider The PayloadProvider to attach arbitrary information to DLX nodes, defaults to DefaultPayloadProvider.
 *
 * @return the DLXNode/root node of the DLX data structure.
 */
fun Array<BooleanArray>.toDLX3(provider: PayloadProvider = DefaultPayloadProvider) = DLXNode(payload = "Root Node").apply {
    val nrOfColumns = when {
        isNotEmpty() -> this@toDLX3[0].size
        else -> 0
    }

    // create & insert all Headers
    val headers = ArrayList<DLXNode>()
    var currentHeader = this@apply
    for (constraint in 0 until nrOfColumns) {
        val newHeader = DLXNode(payload = provider.getHeaderPayload(constraint))
        newHeader.right= currentHeader.right
        newHeader.left = currentHeader
        currentHeader.right.left = newHeader
        currentHeader.right = newHeader
        currentHeader = newHeader
        headers.add(newHeader)
    }

    // create & insert all Nodes
    this@toDLX3.forEachIndexed { rowIndex, row ->
        var currentNode: DLXNode? = null
        row.forEachIndexed { colIndex, isSet ->
            if (isSet) {
                val header = headers[colIndex]
                val newNode = DLXNode(header, payload = provider.getDataPayload(colIndex, rowIndex), row = rowIndex)
                // inserting Up will append it at the "bottom" of the list (downwards) because it's
                // a circular list
                newNode.down = header
                newNode.up = header.up
                header.up.down = newNode
                header.up = newNode
                header.nrOfNodes++

                newNode.right = currentNode?.right ?: newNode
                newNode.left = currentNode ?: newNode
                currentNode?.right?.left = newNode
                currentNode?.right = newNode

                currentNode = newNode
            }
        }
    }
}
