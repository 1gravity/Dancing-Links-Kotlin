package com.onegravity.sudoku.model

import com.onegravity.sudoku.model.region.*
import java.lang.IllegalArgumentException
import java.lang.StringBuilder
import kotlin.collections.ArrayList

/**
 * The Grid class is the Puzzle implementation.
 *
 * @param extraRegionType if not Null defines the extra regions this puzzle holds (X, Hyper,
 * Centerdot etc.
 * @param isJigsaw True if the puzzle is a Jigsaw puzzle in which case the blockCodes need to be
 * passed in
 * @param blockCodes the block codes define to which block a cell belongs to.
 * The codes go from 0 to 8.
 * For a regular puzzle they are (not jigsaw):
 * 0, 0, 0, 3, 3, 3, 6, 6, 6
 * 0, 0, 0, 3, 3, 3, 6, 6, 6
 * 0, 0, 0, 3, 3, 3, 6, 6, 6
 * 1, 1, 1, 4, 4, 4, 7, 7, 7
 * 1, 1, 1, 4, 4, 4, 7, 7, 7
 * 1, 1, 1, 4, 4, 4, 7, 7, 7
 * 2, 2, 2, 5, 5, 5, 8, 8, 8
 * 2, 2, 2, 5, 5, 5, 8, 8, 8
 * 2, 2, 2, 5, 5, 5, 8, 8, 8
 */
class Grid(
    override val extraRegionType: RegionType?,
    override val isJigsaw: Boolean,
    private val blockCodes: Array<IntArray> = Block.regionCodes
): Puzzle<CellImpl> {

    init {
        blockCodes.forEach { it.forEach { code -> assert(code in 0..8) } }
        assert(extraRegionType?.isExtraRegion ?: true)
    }

    /*
     * The Cells.
     * The first array index is the vertical index (from top to bottom) -> y/row,
     * the second index is the horizontal index (from left to right) -> x/column.
     */
    private val cellList = ArrayList<CellImpl>().apply {
        for (row in 0 until 9) {
            for (col in 0 until 9) {
                add(CellImpl(CellPosition(col, row), blockCodes[col][row]))
            }
        }
    }

    private val cells = Array(9) { col ->
        Array(9) { row ->
            cellList[row * 9 + col]
        }
    }

    private val rows by lazy { create(9) { Row(this@Grid, it) } }

    private val columns by lazy { create(9) { Column(this@Grid, it) } }

    private val blocks by lazy {
        ArrayList<Block<CellImpl>>().apply {
            val blockMapping = cellList.groupBy { it.blockCode }
            for (blockCode in 0..8) {
                val block = Block(blockMapping[blockCode] ?: emptyList(), blockCode)
                add(block)
            }
        }
    }
    private val blockNeighbors by lazy { computeNeighbors(blockCodes) }
    private val blockIndices by lazy { computeRegionIndices(blockCodes) }

    private val xRegions by lazy { create(X.nrOfRegions) { X(this@Grid, it) } }

    private val hyperRegions by lazy { create(Hyper.nrOfRegions) { Hyper(this@Grid, it) } }

    private val percentRegions by lazy { create(Percent.nrOfRegions) { Percent(this@Grid, it) } }

    private val centerdotRegions by lazy { create(Centerdot.nrOfRegions) { Centerdot(this@Grid, it) } }

    private val asteriskRegions by lazy { create(Asterisk.nrOfRegions) { Asterisk(this@Grid, it) } }

    private val colorRegions by lazy { create(Color.nrOfRegions) { Color(this@Grid, it) } }

    private fun <R: Region<CellImpl>>create(
        nrOfRegions: Int,
        instance: (regionNr: Int) -> R
    ) = ArrayList<R>().apply {
        for (regionNr in 0 until nrOfRegions) {
            add(instance(regionNr))
        }
    }

    override fun getCells() = cells

    override fun getCell(col: Int, row: Int) = cells[col][row]

    override fun getCell(index: Int) = CellPosition(index).run { cells[col][row] }

    override fun getRegions(type: RegionType?): List<Region<CellImpl>> {
        val hasExtraRegion = extraRegionType != null
        return when(type) {
            RegionType.ROW -> rows
            RegionType.COLUMN -> columns
            RegionType.BLOCK -> blocks
            RegionType.X ->  xRegions
            RegionType.HYPER -> hyperRegions
            RegionType.PERCENT -> percentRegions
            RegionType.CENTERDOT -> centerdotRegions
            RegionType.ASTERISK -> if (hasExtraRegion) asteriskRegions else emptyList()
            RegionType.COLOR -> if (hasExtraRegion) colorRegions else emptyList()
            else -> emptyList()
        }
    }

    override fun getRegionAtOrThrow(col: Int, row: Int, type: RegionType?) =
        getRegionAtOrNull(col, row, type) ?: throw NoSuchElementException("No region of type $type at $col/$row")

    override fun getRegionAtOrNull(col: Int, row: Int, type: RegionType?): Region<CellImpl>? {
        if (type == null) return null
        val cell = getCell(col, row)
        getRegions(type).forEach { region ->
            if (region.contains(cell)) return region
        }
        return null
    }

    override fun getIndices(type: RegionType) =
        when(type) {
            RegionType.ROW -> Row.indices
            RegionType.COLUMN -> Column.indices
            RegionType.BLOCK -> blockIndices
            RegionType.X -> handleExtraRegion(type, X.indices)
            RegionType.HYPER -> handleExtraRegion(type, Hyper.indices)
            RegionType.PERCENT -> handleExtraRegion(type, Percent.indices)
            RegionType.CENTERDOT -> handleExtraRegion(type, Centerdot.indices)
            RegionType.ASTERISK -> handleExtraRegion(type, Asterisk.indices)
            RegionType.COLOR -> handleExtraRegion(type, Color.indices)
        }

    override fun getNeighbors(type: RegionType) =
        when(type) {
            RegionType.ROW -> Row.neighbors
            RegionType.COLUMN -> Column.neighbors
            RegionType.BLOCK -> blockNeighbors
            RegionType.X -> handleExtraRegion(type, X.neighbors)
            RegionType.HYPER -> handleExtraRegion(type, Hyper.neighbors)
            RegionType.PERCENT -> handleExtraRegion(type, Percent.neighbors)
            RegionType.CENTERDOT -> handleExtraRegion(type, Centerdot.neighbors)
            RegionType.ASTERISK -> handleExtraRegion(type, Asterisk.neighbors)
            RegionType.COLOR -> handleExtraRegion(type, Color.neighbors)
        }

    private fun handleExtraRegion(type: RegionType, values: Array<IntArray>) =
        when (extraRegionType) {
            null -> throw IllegalArgumentException("puzzle has no ${type.name} region")
            else -> values
        }

    fun setValue(x: Int, y: Int, value: Int, isGiven: Boolean) {
        getCell(x, y).run {
            this.isGiven = isGiven
            this.value = value
        }
    }

    fun setValue(index: Int, value: Int, isGiven: Boolean) {
        getCell(index).run {
            this.isGiven = isGiven
            this.value = value
        }
    }

    override fun toString() = StringBuilder().apply {
        for (row in 0..8) {
            for (col in 0..8) {
                append("${cells[col][row].value}, ")
            }
            append("\n\r")
        }
    }.toString()

}
