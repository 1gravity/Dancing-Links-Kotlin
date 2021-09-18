package com.onegravity.dlx

/**
 * When creating the dancing links data structure the PayloadProvider is used to attach arbitrary information to nodes
 * as a way to translate the DLX solution back to the original problem.
 * For Sudoku puzzles we would e.g. attach the cell position (col/row or cell index).
 */
interface PayloadProvider {

    fun getHeaderPayload(index: Int): Any

    fun getDataPayload(col: Int, row: Int): Any

}