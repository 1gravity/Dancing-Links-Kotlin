package com.onegravity.dlx.model

import com.onegravity.dlx.model.Direction.Right
import com.onegravity.dlx.getDLX
import com.onegravity.dlx.matrixTest2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HeaderNodeTests {

    @Test
    fun testNrOfNodes() {
        val rootNode = matrixTest2.getDLX()
        rootNode.forEach(Right) {
            val header = it as HeaderNode
            assertEquals(2, header.nrOfNodes)
        }
    }

    @Test
    fun testCoverUncover() {
        val rootNode = matrixTest2.getDLX()

        val headers = ArrayList<HeaderNode>()

        var count = 0
        rootNode.forEachIndexed(Right) { index, _ -> count = index + 1 }
        assertEquals(12, count)

        rootNode.forEachIndexed(Right) { index, node ->
            val header = node as HeaderNode
            headers.add(header)
            header.cover()
            assertEquals(--count, 11 - index)
        }

        headers.reverse()   // we need to uncover in reverse order
        headers.forEachIndexed { index, header ->
            header.uncover()
            assertEquals(++count, index + 1)
        }
    }

}
