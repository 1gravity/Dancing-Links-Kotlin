package com.onegravity.dlx

interface PayloadProvider {

    fun getHeaderPayload(index: Int): Any

    fun getDataPayload(col: Int, row: Int): Any

}