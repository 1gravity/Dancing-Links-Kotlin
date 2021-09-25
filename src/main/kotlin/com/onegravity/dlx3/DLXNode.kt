package com.onegravity.dlx3

data class DLXNode(
    var header: DLXNode? = null,
    var payload: Any? = null,
    var row: Int = 0
) {

    var left: DLXNode = this
    var right: DLXNode = this
    var up: DLXNode = this
    var down: DLXNode = this

    var nrOfNodes = 0

    fun cover() {
        var downNode = down
        while (downNode != this) {
            var rightNode = downNode.right
            while (rightNode != downNode) {
                rightNode.up.down = rightNode.down
                rightNode.down.up = rightNode.up
                rightNode.header!!.nrOfNodes--
                rightNode = rightNode.right
            }
            downNode = downNode.down
        }

        left.right = right
        right.left = left
    }

    fun uncover() {
        left.right = this
        right.left = this

        var upNode = up
        while (upNode != this) {
            var leftNode = upNode.left
            while (leftNode != upNode) {
                leftNode.up.down = leftNode
                leftNode.down.up = leftNode
                leftNode.header!!.nrOfNodes++
                leftNode = leftNode.left
            }
            upNode = upNode.up
        }
    }

    override fun toString() = "${payload.toString()}/$nrOfNodes"

}
