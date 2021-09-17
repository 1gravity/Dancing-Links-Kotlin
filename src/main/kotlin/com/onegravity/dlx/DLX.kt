package com.onegravity.dlx

import com.onegravity.dlx.model.*
import com.onegravity.dlx.model.Direction.*
import java.util.ArrayList

/**
 * Use this extension function to create the dancing links based on an exact cover matrix.
 *
 * The exact cover matrix is an Array of Boolean Arrays (Array<BooleanArray>).
 */
fun Array<BooleanArray>.getDLX(provider: PayloadProvider = PayloadProviderImpl): RootNode {
    val rootNode = RootNode()

    val nrOfColumns = when {
        isNotEmpty() -> this[0].size
        else -> 0
    }

    // create & insert all Headers
    val headers = ArrayList<HeaderNode>()
    var currentHeader: DLXNode = rootNode
    for (constraint in 0 until nrOfColumns) {
        val newHeader = HeaderNode(provider.getHeaderPayload(constraint))
        currentHeader = newHeader.insertAt(currentHeader, Right)
        headers.add(newHeader)
    }

    // create & insert all Nodes
    forEachIndexed { rowIndex, row ->
        var currentNode: DLXNode? = null
        row.forEachIndexed { colIndex, element ->
            if (element) {
                val header = headers[colIndex]
                val newNode = DataNode(header, provider.getDataPayload(colIndex, rowIndex))
                // inserting Up will append it at the "bottom" of the list (downwards) because it's
                // a circular list
                newNode.insertAt(header, Up)
                header.nrOfNodes++
                currentNode = currentNode?.run { newNode.insertAt(this, Right) } ?: newNode
            }
        }
    }

    return rootNode
}
