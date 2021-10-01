package com.onegravity.sudoku

import com.onegravity.sudoku.solver.solve
import com.onegravity.dlx.solve
import com.onegravity.dlx.toDLX
import com.onegravity.dlx2.solve
import com.onegravity.dlx2.toDLX2
import com.onegravity.dlx3.solve
import com.onegravity.dlx3.toDLX3
import com.onegravity.sudoku.SudokuMatrix.Companion.toSudokuMatrix
import com.onegravity.sudoku.model.getTestGrid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class PerformanceTests : BaseClass4CoroutineTests() {

    @Test
    fun testAlEscargot() {
        testPerformance("Al Escargot - DLX", testSudokuAlEscargot) {
            getTestGrid(it)
                .toSudokuMatrix()
                .toDLX()
                .solve {  }
        }
        testPerformance("Al Escargot - DLX2", testSudokuAlEscargot) {
            getTestGrid(it)
                .toSudokuMatrix()
                .toDLX2()
                .solve { }
        }
        testPerformance("Al Escargot - DLX3", testSudokuAlEscargot) {
            getTestGrid(it)
                .toSudokuMatrix()
                .toDLX3()
                .solve { }
        }
        testPerformance("Al Escargot - Brute Force", testSudokuAlEscargot) {
            runBlocking(Dispatchers.Main) {
                getTestGrid(it).solve(true).collect { }
            }
        }
    }

    @Test
    fun testHardest1() {
        testPerformance("DLX 1", "hardest.csv") {
            getTestGrid(it)
                .toSudokuMatrix()
                .toDLX()
                .solve { }
        }
    }

    @Test
    fun testHardest2() {
        testPerformance("DLX 2", "hardest.csv") {
            getTestGrid(it)
                .toSudokuMatrix()
                .toDLX2()
                .solve { }
        }
    }

    @Test
    fun testHardest3() {
        testPerformance("DLX 3", "hardest.csv") {
            getTestGrid(it)
                .toSudokuMatrix()
                .toDLX3()
                .solve { }
        }
    }

    @Test
    fun testHardestBF() {
        testPerformance("Brute Force", "hardest.csv") {
            runBlocking(Dispatchers.Main) {
                getTestGrid(it).solve(true).collect { }
            }
        }
    }

    private val limit = 10000

    // disabled because you need GIT LFS (https://git-lfs.github.com) to run it
    // @Test
    fun testKaggleReduced1() {
        testPerformance("$limit - DLX 1", "kaggle.csv", limit) {
            getTestGrid(it)
                .toSudokuMatrix()
                .toDLX()
                .solve { }
        }
    }

    // disabled because you need GIT LFS (https://git-lfs.github.com) to run it
    // @Test
    fun testKaggleReduced2() {
        testPerformance("$limit - DLX 2", "kaggle.csv", limit) {
            getTestGrid(it)
                .toSudokuMatrix()
                .toDLX2()
                .solve { }
        }
    }

    // disabled because you need GIT LFS (https://git-lfs.github.com) to run it
    // @Test
    fun testKaggleReduced3() {
        testPerformance("$limit - DLX 3", "kaggle.csv", limit) {
            getTestGrid(it)
                .toSudokuMatrix()
                .toDLX3()
                .solve { }
        }
    }

    // disabled because you need GIT LFS (https://git-lfs.github.com) to run it
    // @Test
    fun testKaggleReducedBF() {
        testPerformance("$limit - Brute Force", "kaggle.csv", limit) {
            runBlocking(Dispatchers.Main) {
                getTestGrid(it).solve(true).collect {  }
            }
        }
    }

    // disabled because you need GIT LFS (https://git-lfs.github.com) to run it
    // @Test
    fun testKaggleFull1() {
        testPerformance("DLX 1", "kaggle.csv") {
            getTestGrid(it)
                .toSudokuMatrix()
                .toDLX()
                .solve { }
        }
    }

    // disabled because you need GIT LFS (https://git-lfs.github.com) to run it
    // @Test
    fun testKaggleFull2() {
        testPerformance("DLX 2", "kaggle.csv") {
            getTestGrid(it)
                .toSudokuMatrix()
                .toDLX2()
                .solve { }
        }
    }


    // disabled because you need GIT LFS (https://git-lfs.github.com) to run it
    // @Test
    fun testKaggleFull3() {
        testPerformance("DLX 3", "kaggle.csv") {
            getTestGrid(it)
                .toSudokuMatrix()
                .toDLX3()
                .solve { }
        }
    }

    // disabled because you need GIT LFS (https://git-lfs.github.com) to run it
    // @Test
    fun testKaggleFullBF() {
        testPerformance("Brute Force", "kaggle.csv") {
            runBlocking(Dispatchers.Main) {
                getTestGrid(it).solve(true).collect { }
            }
        }
    }

}
