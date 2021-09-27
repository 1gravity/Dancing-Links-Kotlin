package com.onegravity.sudoku

import com.onegravity.bruteforce.solve
import com.onegravity.dlx.solve
import com.onegravity.dlx.toDLX
import com.onegravity.dlx2.solve
import com.onegravity.dlx2.toDLX2
import com.onegravity.dlx3.solve
import com.onegravity.dlx3.toDLX3
import com.onegravity.sudoku.SudokuMatrix.Companion.toSudokuMatrix
import com.onegravity.sudoku.legacy.Accumulator
import com.onegravity.sudoku.legacy.Hint
import com.onegravity.sudoku.legacy.SolutionProducer
import org.junit.jupiter.api.Test

class PerformanceTests {

    @Test
    fun testSudoku1() {
        testPerformance("DLX", testSudoku1) {
            getTestGrid(it, null).toSudokuMatrix().toDLX().solve {  }
        }
        testPerformance("DLX2", testSudoku1) {
            getTestGrid(it, null).toSudokuMatrix().toDLX2().solve {  }
        }
        testPerformance("DLX3", testSudoku1) {
            getTestGrid(it, null).toSudokuMatrix().toDLX3().solve {  }
        }
        testPerformance("Brute Force", testSudoku1) {
            it.solve()
        }
        testPerformance("Legacy", testSudoku1) {
            SolutionProducer().getHints(getTestGrid(it, null), object : Accumulator {
                override fun add(hint: Hint?) {}
                override fun getHints() = emptyList<Hint>()
            })
        }
    }

    @Test
    fun testSudoku2() {
        testPerformance("DLX", testSudoku2) {
            getTestGrid(it, null).toSudokuMatrix().toDLX().solve {  }
        }
        testPerformance("DLX2", testSudoku2) {
            getTestGrid(it, null).toSudokuMatrix().toDLX2().solve {  }
        }
        testPerformance("DLX3", testSudoku2) {
            getTestGrid(it, null).toSudokuMatrix().toDLX3().solve {  }
        }
        testPerformance("Brute Force", testSudoku2) {
            it.solve()
        }
        testPerformance("Legacy", testSudoku2) {
            SolutionProducer().getHints(getTestGrid(it, null), object : Accumulator {
                override fun add(hint: Hint?) {}
                override fun getHints() = emptyList<Hint>()
            })
        }
    }

    @Test
    fun testSudoku3() {
        testPerformance("DLX", testSudokuAlEscargot) {
            getTestGrid(it, null).toSudokuMatrix().toDLX().solve {  }
        }
        testPerformance("DLX2", testSudokuAlEscargot) {
            getTestGrid(it, null).toSudokuMatrix().toDLX2().solve {  }
        }
        testPerformance("DLX3", testSudokuAlEscargot) {
            getTestGrid(it, null).toSudokuMatrix().toDLX3().solve {  }
        }
        testPerformance("Brute Force", testSudokuAlEscargot) {
            it.solve()
        }
        testPerformance("Legacy", testSudokuAlEscargot) {
            SolutionProducer().getHints(getTestGrid(it, null), object : Accumulator {
                override fun add(hint: Hint?) {}
                override fun getHints() = emptyList<Hint>()
            })
        }
    }

    @Test
    fun testHardest() {
        testPerformance("DLX", "hardest.csv") {
            val grid = getTestGrid(it, null)
            val matrix = grid.toSudokuMatrix()
            matrix.toDLX().solve { }
        }
        testPerformance("DLX2", "hardest.csv") {
            val grid = getTestGrid(it, null)
            val matrix = grid.toSudokuMatrix()
            matrix.toDLX2().solve { }
        }
        testPerformance("DLX3", "hardest.csv") {
            val grid = getTestGrid(it, null)
            val matrix = grid.toSudokuMatrix()
            matrix.toDLX3().solve { }
        }
        testPerformance("Brute Force", "hardest.csv") {
            it.solve()
        }
        testPerformance("Legacy", "hardest.csv") {
            val grid = getTestGrid(it, null)
            SolutionProducer().getHints(grid, object : Accumulator {
                override fun add(hint: Hint?) {}
                override fun getHints() = emptyList<Hint>()
            })
        }
    }

//    @Test
    fun testKaggle() {
        testPerformance("DLX", "kaggle.csv") {
            val grid = getTestGrid(it, null)
            val matrix = grid.toSudokuMatrix()
            matrix.toDLX().solve { }
        }
        testPerformance("DLX2", "kaggle.csv") {
            val grid = getTestGrid(it, null)
            val matrix = grid.toSudokuMatrix()
            matrix.toDLX2().solve { }
        }
        testPerformance("DLX3", "kaggle.csv") {
            val grid = getTestGrid(it, null)
            val matrix = grid.toSudokuMatrix()
            matrix.toDLX3().solve { }
        }
        testPerformance("Brute Force", "kaggle.csv") {
            it.solve()
        }
        testPerformance("Legacy", "kaggle.csv") {
            val grid = getTestGrid(it, null)
            SolutionProducer().getHints(grid, object : Accumulator {
                override fun add(hint: Hint?) {}
                override fun getHints() = emptyList<Hint>()
            })
        }
    }

}
