package com.onegravity.dlx.model

import com.onegravity.dlx.model.Direction.*

/**
 * The base class for the three types of nodes (root node, header nodes and data nodes) implementing basic node
 * operations: get, insert, delete.
 */
abstract class DLXNode(var payload: Any) {

    /**
     * left, right, up and down must be initialized in the subclasses and be set to `this`
     *
     * We cannot initialize the properties with `this` in an `init { }` block here because the node isn't fully
     * initialized yet at that point in time, and it's an open class so subclasses could access the not fully
     * initialized DLXNode through the properties.
     */
    lateinit var left: DLXNode
        protected set

    lateinit var right: DLXNode
        protected set

    lateinit var up: DLXNode
        protected set

    lateinit var down: DLXNode
        protected set

    /**
     * Performs the given action on each node, excluding the starting node.
     *
     * This is a convenience method to make looping over rows/columns easier.
     * Note: since DLXNodes are part of two circular linked lists it will loop over all nodes in th row or column so
     * looping from a DataNode downwards would also loop over the HeaderNode.
     *
     * @param direction the Direction to loop over the nodes.
     * @param action the action to perform on each node taking the node as parameter.
     */
    fun forEach(direction: Direction, action: (node: DLXNode) -> Unit) {
        forEachIndexed(direction) { _, node -> action(node) }
    }

    /**
     * Performs the given action on each node (excluding the starting node), providing sequential index with the node.
     *
     * @param direction the Direction to loop over the nodes.
     * @param action the action to perform on each node taking the index of the node and the node itself as parameters.
     */
    fun forEachIndexed(direction: Direction, action: (index: Int, node: DLXNode) -> Unit) {
        var next = next(direction)
        var index = 0
        while (next != this@DLXNode) {
            action(index++, next)
            next = next.next(direction)
        }
    }

    /**
     * Performs the given action on each node, including the starting node.
     * The action will be called on the starting node first.
     *
     * @param direction the Direction to loop over the nodes.
     * @param action the action to perform on each node taking the node as parameter.
     */
    fun forAll(direction: Direction, action: (node: DLXNode) -> Unit) {
        var next = this
        do {
            action(next)
            next = next.next(direction)
        } while (next != this)
    }

    fun next(direction: Direction) = when (direction) {
        Left -> this.left
        Right -> this.right
        Up -> this.up
        Down -> this.down
    }

    /**
     * Insert this DLXNode at the left, right, top or bottom of another DLXNode and returns the
     * inserted DLXNode (=this).
     *
     * @param node the DLXNode to insert the current node at.
     * @param direction the Direction to insert the node at (left, right, up, down)
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

    /**
     * Remove the node from its neighbors in the indicated directions.
     * While it's theoretically possible to remove a node just from one neighbor, in a double linked list, the node
     * should be removed with up/down or left/right Directions to keep the data structure's integrity.
     *
     * @param directions one or more Directions the node is removed from.
     */
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
