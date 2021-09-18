package com.onegravity.dlx.model

import com.onegravity.dlx.DefaultPayloadProvider.DefaultPayload
import com.onegravity.dlx.model.Direction.*
import com.onegravity.dlx.PayloadProvider
import com.onegravity.dlx.getDLX
import com.onegravity.dlx.matrixTest2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DLXNodeTests {

    @Test
    fun testForEach() {
        val rootNode = matrixTest2.getDLX()

        val nodesIndexed = ArrayList<DLXNode>()
        rootNode.forEachIndexed(Right) { _, header ->
            nodesIndexed.add(header)
            (header as HeaderNode).forEachIndexed(Down) { _, node ->
                nodesIndexed.add(node)
                node.forEachIndexed(Right) { _, neighbor ->
                    nodesIndexed.add(neighbor)
                }
            }
        }

        val nodes = ArrayList<DLXNode>()
        rootNode.forEach(Right) { header ->
            nodes.add(header)
            (header as HeaderNode).forEach(Down) { node ->
                nodes.add(node)
                node.forEach(Right) { neighbor ->
                    nodes.add(neighbor)
                }
            }
        }

        assertEquals(nodes, nodesIndexed)
    }

    @Test
    fun testDefaultPayloads() {
        val payloads = arrayOf(
            arrayOf(DefaultPayload(0, 0), DefaultPayload(0, 1)),
            arrayOf(DefaultPayload(1, 2), DefaultPayload(1, 3)),
            arrayOf(DefaultPayload(2, 4), DefaultPayload(2, 5)),
            arrayOf(DefaultPayload(3, 6), DefaultPayload(3, 7)),

            arrayOf(DefaultPayload(4, 0), DefaultPayload(4, 4)),
            arrayOf(DefaultPayload(5, 2), DefaultPayload(5, 6)),
            arrayOf(DefaultPayload(6, 1), DefaultPayload(6, 5)),
            arrayOf(DefaultPayload(7, 3), DefaultPayload(7, 7)),

            arrayOf(DefaultPayload(8, 0), DefaultPayload(8, 2)),
            arrayOf(DefaultPayload(9, 4), DefaultPayload(9, 6)),
            arrayOf(DefaultPayload(10, 1), DefaultPayload(10, 3)),
            arrayOf(DefaultPayload(11, 5), DefaultPayload(11, 7)),
        )
        val rootNode = matrixTest2.getDLX()
        rootNode.forEachIndexed(Right) { column, header ->
            (header as HeaderNode).forEachIndexed(Down) { row, node ->
                assertEquals(payloads[column][row], node.payload)
            }
        }
    }

    @Test
    fun testCustomPayloads() {
        val payloads = arrayOf(
            arrayOf("0/0","","","","4/0","","","","8/0","","",""),
            arrayOf("0/1","","","","","","6/1","","","","10/1",""),
            arrayOf("","1/2","","","","5/2","","","8/2","","",""),
            arrayOf("","1/3","","","","","","7/3","","","10/3",""),
            arrayOf("","","2/4","","4/4","","","","","9/4","",""),
            arrayOf("","","2/5","","","","6/5","","","","","11/5"),
            arrayOf("","","","3/6","","5/6","","","","9/6","",""),
            arrayOf("","","","3/7","","","","7/7","","","","11/7")
        )

        data class TestPayload(val row: Int, val col: Int, val s: String) : Comparable<Any> {
            override fun compareTo(other: Any) = when(other) {
                is TestPayload -> when (val colCompare = col.compareTo(other.col)) {
                    0 -> row.compareTo(other.row)
                    else -> colCompare
                }
                else -> 0
            }
        }

        val rootNode = matrixTest2.getDLX(object: PayloadProvider {
            override fun getHeaderPayload(index: Int) = "TESTHEADER$index"
            override fun getDataPayload(col: Int, row: Int) = TestPayload(row, col, payloads[row][col])
        })

        rootNode.forEachIndexed(Right) { col, header ->
            assertEquals("TESTHEADER$col", header.payload)
            (header as HeaderNode).forEachIndexed(Down) { _, node ->
                val payload = (node.payload as TestPayload)
                assertEquals(payloads[payload.row][payload.col], payload.s)
            }
        }
    }

    @Test
    fun testInsert() {
        val rootNode = matrixTest2.getDLX()
        assertEquals("h0", rootNode.right.payload)
        assertEquals(DefaultPayload(0,0), rootNode.right.down.payload)
        assertEquals(DefaultPayload(0,1), rootNode.right.up.payload)
        assertEquals("h11", rootNode.left.payload)
        assertEquals(DefaultPayload(11,5), rootNode.left.down.payload)
        assertEquals(DefaultPayload(11,7), rootNode.left.up.payload)

        val hRight = HeaderNode("New Header 1")
        hRight.insertAt(rootNode, Right)
        assertEquals("New Header 1", rootNode.right.payload)
        assertEquals("h0", rootNode.right.right.payload)
        assertEquals("h0", hRight.right.payload)

        val hLeft = HeaderNode("New Header 2")
        hLeft.insertAt(rootNode, Left)
        assertEquals("New Header 2", rootNode.left.payload)
        assertEquals("h11", rootNode.left.left.payload)
        assertEquals("h11", hLeft.left.payload)

        val nDown = DataNode(hRight, "New Data Node 1")
        nDown.insertAt(hRight, Down)
        assertEquals("New Data Node 1", rootNode.right.down.payload)
        assertEquals("New Data Node 1", rootNode.right.up.payload)
        assertEquals("New Header 1", (rootNode.right.down as DataNode).header.payload)

        val nUp = DataNode(hRight, "New Data Node 2")
        nUp.insertAt(hRight, Up)
        assertEquals("New Data Node 2", rootNode.right.up.payload)
        assertEquals("New Data Node 1", rootNode.right.up.up.payload)
        assertEquals("New Data Node 2", rootNode.right.down.down.payload)
        assertEquals("New Header 1", (rootNode.right.up as DataNode).header.payload)
    }

    @Test
    fun testRemove() {
        val rootNode = matrixTest2.getDLX()

        val h1 = HeaderNode("New Header 1")
        val h2 = HeaderNode("New Header 2")
        h1.insertAt(rootNode, Right)
        h2.insertAt(h1, Right)
        assertEquals("New Header 1", rootNode.right.payload)
        assertEquals("New Header 2", rootNode.right.right.payload)

        h1.remove(Left, Right)
        assertEquals("New Header 2", rootNode.right.payload)
        h2.remove(Left, Right)
        assertEquals("h0", rootNode.right.payload)

        h1.insertAt(rootNode, Right)
        h2.insertAt(h1, Right)

        h1.remove(Up, Down)
        assertEquals("New Header 1", rootNode.right.payload)

        val node1 = DataNode(h1, "New Data Node 1")
        val node2 = DataNode(h1, "New Data Node 2")
        node1.insertAt(h1, Up)
        node2.insertAt(h1, Up)
        node1.remove(Up, Down)
        assertEquals("New Header 1", rootNode.right.payload)
        assertEquals("New Data Node 2", rootNode.right.down.payload)
        assertEquals("New Data Node 2", rootNode.right.up.payload)
        node2.remove(Up, Down)
        assertEquals("New Header 1", rootNode.right.payload)
        assertEquals("New Header 1", rootNode.right.down.payload)
        assertEquals("New Header 1", rootNode.right.up.payload)
    }
}
