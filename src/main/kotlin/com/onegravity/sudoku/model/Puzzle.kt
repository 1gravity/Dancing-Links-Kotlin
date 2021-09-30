package com.onegravity.sudoku.model

import com.onegravity.sudoku.model.region.*

/**
 * Puzzle is the interface for a Sudoku puzzle.
 * It supports jigsaw/squiggly puzzles and also puzzles with extra regions (X-Sudoku, Hyper-Sudoku etc.).
 */
interface Puzzle {

    val extraRegionType: RegionType?

    val isJigsaw: Boolean

    /**
     * Get all cells as a one-dimensional array of Cells.
     *
     * @return all cells of this Puzzle as a one-dimensional array of Cells.
     */
    fun getCells(): Array<Cell>

    /**
     * Get the cell at the given coordinates
     *
     * @param col the x coordinate (0=leftmost, 8=rightmost)
     * @param row the y coordinate (0=topmost, 8=bottommost)
     *
     * @return the cell at the given coordinates
     */
    fun getCell(col: Int, row: Int): Cell

    /**
     * Get the cell at the given index
     *
     * @param index the index of the cell from left to right then top to bottom (0=topmost, 80=bottommost)
     * 0  1  2  3  4  5  6  7  8
     * 9 10 11 12 13 14 15 16 17
     * etc.
     *
     * @return the cell at the given index
     */
    fun getCell(index: Int): Cell

    /**
     * Returns a list of regions by RegionType.
     *
     * @param type the RegionType to retrieve
     *
     * @return A list of Regions, they will be returned in their natural order (row 0, row 1 etc.)
     */
    fun getRegions(type: RegionType?): List<Region>

    /**
     * Returns the region at a specific position.
     * There can be only one region of a specific type at a specific position (no overlaps).
     *
     * @param col the x coordinate (0=leftmost, 8=rightmost)
     * @param row the y coordinate (0=topmost, 8=bottommost)
     * @param type the RegionType to retrieve
     *
     * @return The regions that contain the Cell at the specified position
     *
     * @throws NoSuchElementException if no region can be found at col/row
     */
    fun getRegionAtOrThrow(col: Int, row: Int, type: RegionType?): Region

    /**
     * Returns the region at a specific position or Null of no such region exists.
     */
    fun getRegionAtOrNull(col: Int, row: Int, type: RegionType?): Region?

    /**
     * Returns the region at a specific position or Null of no such region exists.
     */
    fun getRegionAtOrNull(index: Int, type: RegionType?): Region?

    /**
     * Returns the indices of all cells of a certain RegionType as an Array[IntArray[9]].
     *
     * The Array Index matches the region code/index (used internally to identify the region).
     * The IntArray elements are the cell indices from 0 to 80.
     *
     * Example for rows:
     * [0] -> [0, 1, 2, 3, 4, 5, 6, 7, 8]
     * [1] -> [9,10,11,12,13,14,15,16,17]
     *  etc.
     *
     * @return an Array[9] of IntArrays[9] or throws an IllegalArgumentException if the type is an
     * ExtraRegion not matching the Puzzle's extraRegionType.
     */
    fun getIndices(type: RegionType): Array<IntArray>

    /**
     * Returns the neighbors for each cell of a specific RegionType as an Array[81]IntArray[].
     *
     * The Array holds 81 IntArrays, the Array indices matching the cell indices (0..80).
     * Each IntArray holds the indices for its neighbors (in most cases that's 8 neighbors, the
     * X-Region's central cell [4,4] has more than 16 neighbors).
     *
     * Example for blocks:
     * [0] -> [1,2,9,10,11,18,19,20]
     * [1] -> [0,2,9,10,11,18,19,20]
     *  etc.
     *
     * @return an Array [81] of IntArrays [8..] or throws an IllegalArgumentException if the type is an
     * ExtraRegion not matching the Puzzle's extraRegionType.
     */
    fun getNeighbors(type: RegionType): Array<IntArray>

}
