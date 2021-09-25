package com.onegravity.dlx

import com.onegravity.dlx.model.*
import com.onegravity.dlx.model.Direction.*
import java.util.ArrayList

/**
 * This creates the dancing links data structure based on an exact cover matrix.
 * The exact cover matrix is an Array of Boolean Arrays (Array<BooleanArray>).
 *
 * @param provider The PayloadProvider to attach arbitrary information to DLX nodes, defaults to DefaultPayloadProvider.
 *
 * @return the RootNode of the DLX data structure.
 */
fun Array<BooleanArray>.toDLX(provider: PayloadProvider = DefaultPayloadProvider) = RootNode().apply {
    val nrOfColumns = when {
        isNotEmpty() -> this@toDLX[0].size
        else -> 0
    }

    // create & insert all Headers
    val headers = ArrayList<HeaderNode>()
    var currentHeader: DLXNode = this@apply
    for (constraint in 0 until nrOfColumns) {
        val newHeader = HeaderNode(provider.getHeaderPayload(constraint))
        currentHeader = newHeader.insertAt(currentHeader, Right)
        headers.add(newHeader)
    }

    // create & insert all Nodes
    this@toDLX.forEachIndexed { rowIndex, row ->
        var currentNode: DLXNode? = null
        row.forEachIndexed { colIndex, isSet ->
            if (isSet) {
                val header = headers[colIndex]
                val newNode = DataNode(header, rowIndex, provider.getDataPayload(colIndex, rowIndex))
                // inserting Up will append it at the "bottom" of the list (downwards) because it's
                // a circular list
                newNode.insertAt(header, Up)
                header.nrOfNodes++
                currentNode = currentNode?.run { newNode.insertAt(this, Right) } ?: newNode
            }
        }
    }
}
