package com.onegravity.sudoku.legacy;

import com.onegravity.sudoku.model.CellImpl;
import com.onegravity.sudoku.model.CellPosition;
import com.onegravity.sudoku.model.Puzzle;
import com.onegravity.sudoku.model.region.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

@SuppressWarnings("unchecked")
public class NeighborManager {

	/**
	 * Get neighbors for a specific cell.
	 * The method can deal with normal & jigsaw puzzles and works with extra regions
	 */
	public static int[] getNeighbors(Puzzle<CellImpl> puzzle, int x, int y) {
		return getNeighbors(puzzle)[getPos(x, y)];
	}
	public static int[] getNeighbors(Puzzle<CellImpl> puzzle, int index) {
		return getNeighbors(puzzle)[index];
	}

	/**
	 * Get neighbors for a puzzle.
	 * The method can deal with normal & jigsaw puzzles and works with extra regions
	 */
    public static int[][] getNeighbors(Puzzle<CellImpl> puzzle) {
		if (puzzle.isJigsaw()) {
			int[][] blockCodes = Block.Companion.getRegionCodes();
			// jigsaw puzzle without extra regions
			return neighbors_jigsaw_all(blockCodes);
		}
		// normal puzzle without extra regions
		return neighbors_normal_all();
	}

    // ****************************************** Static Initializer *******************************************

    /*
     * The static initializer computes the different elements needed to "assemble" neighbors for different puzzle types:
     * rows, columns, standard blocks, rows & columns, rows & columns & standard blocks
     * These lists (List<Set<Integer>>) are later converted into neighbor arrays for different puzzle types (int[][]) 
     */

    private static final List<Set<Integer>> neighbors_row = new Vector<>();
	private static final List<Set<Integer>> neighbors_column = new Vector<>();
	private static final List<Set<Integer>> neighbors_block = new Vector<>();
	private static final List<Set<Integer>> neighbors_row_column = new Vector<>();
	private static final List<Set<Integer>> neighbors_row_column_block = new Vector<>();

	static {
		// compute rows, columns and standard block's neighbors
		for (int pos=0; pos<81; pos++) {
			// initialize entries for this position
			Set<Integer> standard_row = new TreeSet<>();
			Set<Integer> standard_column = new TreeSet<>();
			Set<Integer> standard_block = new TreeSet<>();
			Set<Integer> standard_row_column = new TreeSet<>();
			Set<Integer> standard_row_column_block = new TreeSet<>();
			neighbors_row.add(standard_row);
			neighbors_column.add(standard_column);
			neighbors_block.add(standard_block);
			neighbors_row_column.add(standard_row_column);
			neighbors_row_column_block.add(standard_row_column_block);
			
			// find row and column neighbors
			int rowStart = pos/9*9;		// the index of the first cell in the row
			int columnStart = pos%9;	// the index of the first cell in the column
			for (int i=0, r=rowStart, c=columnStart; i<9; i++, r++, c+=9) {
				if (r!=pos) {
					standard_row.add(r);
					standard_row_column.add(r);
					standard_row_column_block.add(r);
				}
				if (c!=pos) {
					standard_column.add(c);
					standard_row_column.add(c);
					standard_row_column_block.add(c);
				}
			}

			// find block neighbors
			int blockStart = (pos/9/3*27) + (columnStart/3*3);
			processBlock(blockStart, pos, standard_block, standard_row_column_block);
			processBlock(blockStart+1, pos, standard_block, standard_row_column_block);
			processBlock(blockStart+2, pos, standard_block, standard_row_column_block);
			processBlock(blockStart+9, pos, standard_block, standard_row_column_block);
			processBlock(blockStart+10, pos, standard_block, standard_row_column_block);
			processBlock(blockStart+11, pos, standard_block, standard_row_column_block);
			processBlock(blockStart+18, pos, standard_block, standard_row_column_block);
			processBlock(blockStart+19, pos, standard_block, standard_row_column_block);
			processBlock(blockStart+20, pos, standard_block, standard_row_column_block);
		}
		((Vector<Set<Integer>>)neighbors_row).trimToSize();
		((Vector<Set<Integer>>)neighbors_column).trimToSize();
		((Vector<Set<Integer>>)neighbors_block).trimToSize();
		((Vector<Set<Integer>>)neighbors_row_column).trimToSize();
		((Vector<Set<Integer>>)neighbors_row_column_block).trimToSize();
	}
	private static void processBlock(int blockPos, int pos, Set<Integer> neighborSetAll, Set<Integer> neighborSetNoIntersection) {
		neighborSetAll.add(blockPos);
		if (pos/9!=blockPos/9 && pos%9!=blockPos%9) {
			neighborSetNoIntersection.add(blockPos);
		}
	}

    // ****************************************** Normal Sudoku (no extra regions) *******************************************

	/**
	 * In this part the neighbors for puzzles without extra regions are computed (including jigsaw puzzles)
	 */
	
	private static int[][] neighbors_rows;
	private static int[][] neighbors_columns;
	private static int[][] neighbors_blocks;		// regular blocks
	private static int[][] neighbors_normal_all;	// rows, columns, regular blocks

	public static int[][] neighbors_rows() {
		if (neighbors_rows==null) {
			neighbors_rows = convert2Array(neighbors_row);
		}
		return neighbors_rows;
	}

	public static int[][] neighbors_columns() {
		if (neighbors_columns==null) {
			neighbors_columns = convert2Array(neighbors_column);
		}
		return neighbors_columns;
	}

	public static int[][] neighbors_blocks(int[][] blockCodes) {
		if (blockCodes!=null) {
			return computeJigsawNeighbors(blockCodes);
		}
		if (neighbors_blocks==null) {
			neighbors_blocks = convert2Array(neighbors_block);
		}
		return neighbors_blocks;
	}

	private static int[][] neighbors_jigsaw_all(int[][] blockCodes) {
		return computeJigsawNeighbors(blockCodes, neighbors_row_column);
	}

	private static int[][] neighbors_normal_all() {
		if (neighbors_normal_all==null) {
			neighbors_normal_all = convert2Array(neighbors_row_column_block);
		}
		return neighbors_normal_all;
	}
	
	// ****************************************** Common Stuff Jigsaw Puzzles *******************************************

	private static final Map<String, int[][]> jigsawMapBlocks = new HashMap<>();
	/**
	 * Computes only the Jigsaw neighbors (blocks)
	 * @param blockCodes The block codes describing the Jigsaw blocks
	 */
	private static int[][] computeJigsawNeighbors(int[][] blockCodes) {
		return computeJigsawNeighbors(jigsawMapBlocks, blockCodes);
	}

	// jigsawMap This map is used to cache pre-computed neighbors
	private static final Map<String, int[][]> jigsawMap = new HashMap<>();
	/**
	 * Computes neighbors for a Jigsaw puzzle
	 * @param blockCodes The block codes describing the Jigsaw blocks
	 * @param neighborSets These pre-computed neighbor sets (row and column neighbors normally) will be merged with the computed jigsaw neighbors
	 */
	private static int[][] computeJigsawNeighbors(int[][] blockCodes, List<Set<Integer>>...neighborSets) {
		return computeJigsawNeighbors(jigsawMap, blockCodes, neighborSets);
	}
	
	/**
	 * Computes neighbors for a Jigsaw puzzle 
	 * @param jigsawMap This map is used to cache pre-computed neighbors
	 * @param blockCodes The block codes describing the Jigsaw blocks
	 * @param neighborSets These pre-computed neighbor sets (row and column neighbors normally) will be merged with the computed jigsaw neighbors
	 */
	private static int[][] computeJigsawNeighbors(Map<String, int[][]> jigsawMap, int[][] blockCodes, List<Set<Integer>>...neighborSets) {
		// generate key
		StringBuilder tmp = new StringBuilder();
    	for (int y=0; y<9; y++) {
        	for (int x=0; x<9; x++) {
        		tmp.append( blockCodes[x][y] );
        	}
    	}
		String key = tmp.toString();
		// retrieve neighbors
		int[][] neighbors = jigsawMap.get(key);
		if (neighbors==null) {
			// compute neighbors and cache the last ten results in a Map
			neighbors = convert2ArrayPlus(computeBlockNeighbors(blockCodes), neighborSets);
			if (jigsawMap.size()>=10) jigsawMap.clear();
			jigsawMap.put(key, neighbors);
		}
		return neighbors;
	}
	
	/**
	 * Compute the neighbor list for a jigsaw puzzle
	 * @param blockCodes The block codes describing the Jigsaw blocks
	 */
	private static List<Set<Integer>> computeBlockNeighbors(int[][] blockCodes) {
    	final List<Set<Integer>> neighborsList = new Vector<>();
    	// compute blockCode to CellPosition mapping
		final Vector<Set<CellPosition>> blockCodes2Positions = new Vector<>();
		for (int i=0; i<9; i++) {
			blockCodes2Positions.add(new TreeSet<>());
		}
    	for (int y=0; y<9; y++) {
        	for (int x=0; x<9; x++) {
        		int blockCode = blockCodes[x][y];
        		Set<CellPosition> positions = blockCodes2Positions.elementAt(blockCode);
        		positions.add(new CellPosition(x, y));
        	}
    	}
    	// now fill the neighbors list
		for (int i=0; i<81; i++) {
			neighborsList.add(new TreeSet<>());
		}
    	for (int y=0; y<9; y++) {
        	for (int x=0; x<9; x++) {
        		int pos = getPos(x, y);
        		Set<Integer> neighbors = neighborsList.get(pos);
        		int blockCode = blockCodes[x][y];
        		for (CellPosition position : blockCodes2Positions.elementAt(blockCode)) {
        			int posNeighbor = getPos(position.getCol(), position.getRow());
        			if (pos!=posNeighbor) {
        				neighbors.add(posNeighbor);
        			}
        		}
        	}
    	}
    	return neighborsList;
    }

	// ****************************************** Common Stuff *******************************************

	/**
	 * Take some neighbor lists and convert them to a neighbor array (int[][])
	 * @param positionSets The lists of neighbors to process
	 */
	private static int[][] convert2Array(List<Set<Integer>>...positionSets) {
		return convert2ArrayPlus(null, positionSets);
	}

	/**
	 * Take some neighbor lists and convert them to a neighbor array (int[][]).
	 * The plusSet is needed because a convert2ArrayPlus(List<Set<Integer>> plusSet, List<Set<Integer>>...positionSets)
	 * doesn't compile with convert2Array(List<Set<Integer>>...positionSets).
	 * @param plusSet A list of neighbors to process
	 * @param positionSets The lists of neighbors to process
	 */
	private static int[][] convert2ArrayPlus(List<Set<Integer>> plusSet, List<Set<Integer>>...positionSets) {
		int[][] result = new int[81][];
		Set<Integer> neighbors = new TreeSet<>();
		Set<Integer> tmp;
		for (int pos=0; pos<81; pos++) {
			if (plusSet!=null) {
				tmp = plusSet.get(pos);
				if (tmp!=null) neighbors.addAll(tmp);
			}
			for (List<Set<Integer>> positionSet:positionSets) {
				tmp = positionSet.get(pos);
				if (tmp!=null) neighbors.addAll(tmp);
			}
			result[pos] = new int[neighbors.size()];
			int index = 0;
			for(int n:neighbors) {
				result[pos][index++] = n;
			}
			neighbors.clear();
		}
		return result;
	}

	private static int getPos(int x, int y) {
		return y*9 + x;
	}

}