package com.onegravity.sudoku

import com.onegravity.bruteforce.solve
import com.onegravity.dlx.solve
import com.onegravity.dlx.toDLX
import com.onegravity.dlx2.solve
import com.onegravity.dlx2.toDLX2
import com.onegravity.dlx3.solve
import com.onegravity.dlx3.toDLX3
import com.onegravity.sudoku.SudokuMatrix.Companion.toSudokuMatrix
import org.junit.jupiter.api.Test

class PerformanceTests {

    @Test
    fun testAlEscargot() {
        testPerformance("Al Escargot - DLX", testSudokuAlEscargot) {
            getTestGrid(it, null)
                .toSudokuMatrix()
                .toDLX()
                .solve {  }
        }
        testPerformance("Al Escargot - DLX2", testSudokuAlEscargot) {
            getTestGrid(it, null)
                .toSudokuMatrix()
                .toDLX2()
                .solve { }
        }
        testPerformance("Al Escargot - DLX3", testSudokuAlEscargot) {
            getTestGrid(it, null)
                .toSudokuMatrix()
                .toDLX3()
                .solve { }
        }
        testPerformance("Al Escargot - Brute Force", testSudokuAlEscargot) {
            it.solve()
        }
        testPerformance("Al Escargot - Brute Force 2", testSudokuAlEscargot) {
            getTestGrid(it, null).solve()
        }
    }

    @Test
    fun testHardest() {
        testPerformance("DLX", "hardest.csv") {
            getTestGrid(it, null)
                .toSudokuMatrix()
                .toDLX()
                .solve { }
        }
        testPerformance("DLX2", "hardest.csv") {
            getTestGrid(it, null)
                .toSudokuMatrix()
                .toDLX2()
                .solve { }
        }
        testPerformance("DLX3", "hardest.csv") {
            getTestGrid(it, null)
                .toSudokuMatrix()
                .toDLX3()
                .solve { }
        }
        testPerformance("Brute Force", "hardest.csv") {
            it.solve()
        }
        testPerformance("Brute Force 2", "hardest.csv") {
            getTestGrid(it, null).solve()
        }
    }

    @Test
    fun testKaggleReduced() {
        val limit = 10000

        testPerformance("$limit - DLX", "kaggle.csv", limit) {
            getTestGrid(it, null)
                .toSudokuMatrix()
                .toDLX()
                .solve { }
        }
        testPerformance("$limit - DLX2", "kaggle.csv", limit) {
            getTestGrid(it, null)
                .toSudokuMatrix()
                .toDLX2()
                .solve { }
        }
        testPerformance("$limit - DLX3", "kaggle.csv", limit) {
            getTestGrid(it, null)
                .toSudokuMatrix()
                .toDLX3()
                .solve { }
        }
        testPerformance("$limit - Brute Force", "kaggle.csv", limit) {
            it.solve()
        }
        testPerformance("$limit - Brute Force 2", "kaggle.csv", limit) {
            getTestGrid(it, null).solve()
        }
    }

//    @Test
    fun testKaggleFull() {
//        testPerformance("DLX", "kaggle.csv") {
//            getTestGrid(it, null)
//                .toSudokuMatrix()
//                .toDLX()
//                .solve { }
//        }
//        testPerformance("DLX2", "kaggle.csv") {
//            getTestGrid(it, null)
//                .toSudokuMatrix()
//                .toDLX2()
//                .solve { }
//        }
//        testPerformance("DLX3", "kaggle.csv") {
//            getTestGrid(it, null)
//                .toSudokuMatrix()
//                .toDLX3()
//                .solve { }
//        }
        testPerformance("Brute Force", "kaggle.csv") {
            it.solve()
        }
        testPerformance("Brute Force 2", "kaggle.csv") {
            getTestGrid(it, null).solve()
        }
    }

}
