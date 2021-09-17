package com.onegravity.dlx

object PayloadProviderImpl : PayloadProvider {

    /**
     * The default payload stores the coordinates of the input matrix elements in the DLX nodes.
     * This is convenient so that the original values can be retrieved when parsing a solution
     * (the DLX node matrix is sparse unlike the original input matrix).
     */
    data class DefaultPayload(val col: Int, val row: Int)

    override fun getHeaderPayload(index: Int) = "h$index"

    override fun getDataPayload(col: Int, row: Int) = DefaultPayload(col, row)

}
