package com.onegravity.dlx.model

import com.onegravity.dlx.model.Direction.*

abstract class DLXNode(var payload: Any) {

    /**
     * left, right, up and down must be initialized in the sub classes and be set to `this`
     *
     * We cannot initialize the properties with `this` in an `init { }` block here because
     * the node isn't fully initialized yet at that point in time and it's an open class so sub
     * classes could access the not fully initialized BaseNode through the properties.
     */
    lateinit var left: DLXNode
        protected set

    lateinit var right: DLXNode
        protected set

    lateinit var up: DLXNode
        protected set

    lateinit var down: DLXNode
        protected set

    suspend fun forEachSuspended(direction: Direction, block: suspend (node: DLXNode) -> Unit) {
        forEachIndexedSuspended(direction) { _, node -> block(node) }
    }

    suspend fun forEachIndexedSuspended(direction: Direction, block: suspend (index: Int, node: DLXNode) -> Unit) {
        val thisNode = this@DLXNode
        var next = getNext(thisNode, direction)
        var index = 0
        while (next != thisNode) {
            block(index++, next)
            next = getNext(next, direction)
        }
    }

    fun forEach(direction: Direction, block: (node: DLXNode) -> Unit) {
        forEachIndexed(direction) { _, node -> block(node) }
    }

    fun forEachIndexed(direction: Direction, block: (index: Int, node: DLXNode) -> Unit) {
        val thisNode = this@DLXNode
        var next = getNext(thisNode, direction)
        var index = 0
        while (next != thisNode) {
            block(index++, next)
            next = getNext(next, direction)
        }
    }

    private fun getNext(node: DLXNode, direction: Direction) =
        when (direction) {
            Left -> node.left
            Right -> node.right
            Up -> node.up
            Down -> node.down
        }

    /**
     * Insert this DLXNode at the left, right, top or bottom of another DLXNode and returns the
     * inserted DLXNode (=this).
     *
     * @param node the DLXNode at which the current node is inserted.
     * @param direction defines where the node is inserted (left, right, up, down)
     *
     * @return the inserted DLXNode = this
     */
    fun insertAt(node: DLXNode, direction: Direction): DLXNode {
        when (direction) {
            Left -> {
                right = node
                left = node.left
                node.left.right = this
                node.left = this
            }
            Right -> {
                left = node
                right = node.right
                node.right.left = this
                node.right = this
            }
            Up -> {
                down = node
                up = node.up
                node.up.down = this
                node.up = this
            }
            Down -> {
                up = node
                down = node.down
                node.down.up = this
                node.down = this
            }
        }

        return this
    }

    fun remove(vararg directions: Direction) {
        directions.forEach { remove(it) }
    }

    private fun remove(direction: Direction) {
        when (direction) {
            Left -> left.right = right
            Right -> right.left = left
            Up -> up.down = down
            Down -> down.up = up
        }
    }

}
