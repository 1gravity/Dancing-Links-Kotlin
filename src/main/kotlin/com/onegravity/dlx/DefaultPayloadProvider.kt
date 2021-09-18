package com.onegravity.dlx

/**
 * The DefaultPayloadProvider stores the coordinates of the input matrix elements in the DLX nodes.
 * This is convenient to retrieve the original values when parsing a solution (the DLX node matrix is sparse unlike the
 * original input matrix).
 */
object DefaultPayloadProvider : PayloadProvider {

    data class DefaultPayload(val col: Int, val row: Int)

    override fun getHeaderPayload(index: Int) = "h$index"

    override fun getDataPayload(col: Int, row: Int) = DefaultPayload(col, row)

}
