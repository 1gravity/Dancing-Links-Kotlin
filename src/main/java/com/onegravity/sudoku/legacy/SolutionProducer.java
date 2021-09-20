package com.onegravity.sudoku.legacy;

import com.onegravity.sudoku.model.Cell;
import com.onegravity.sudoku.model.Grid;
import com.onegravity.sudoku.model.region.Block;

import java.util.BitSet;
import java.util.Random;

public class SolutionProducer {

    private int maxNrOfIterations;
    private int nrOfIterations;
    private int nrOfSolutions;

    private Grid theUnsolvedGrid;
    private Grid theSolvedGrid;

    // defines how many solutions the solver will find before it stops
    private int maxNrOfSolutions;

    /**
     * Get hints that can be found from a brute-force analysis.
     * <p>
     * A warning/information hint is produced in one of the following case:
     * <ul>
     * <li>If the sudoku has no solution
     * <li>If the sudoku has more than one solution. The produced hint
     * contains two different solutions
     * <li>If the sudoku has exactly one solution, and <tt>true</tt> was passed
     * for <tt>includeSoluton</tt> in the constructor, a hint with the solution
     * is produced.
     * </ul>
     */
    public void getHints(Grid grid, Accumulator accu) throws StopHintCollection, InterruptedException  {
        maxNrOfSolutions = 2;
        maxNrOfIterations = Integer.MAX_VALUE;
        nrOfIterations = 0;
        nrOfSolutions = 0;
        theUnsolvedGrid = grid;
        theSolvedGrid = null;

        SolverGrid solverGrid = new SolverGrid(grid);
        solve(solverGrid, new Random());

        Hint hint;
        switch (nrOfSolutions) {
            case 0:
                hint = new NoSolutionHint(grid);
                break;
            case 1:
                hint = new SolutionHint(grid, theSolvedGrid);
                break;
            default:
                hint = new MultipleSolutionsHint(grid);
        }

        accu.add(hint);
    }

    // ****************************************** Private Methods *******************************************

    /**
     * This method is called if a solution has been found
     * @param solution The solution. If you want to keep the solution, use solution.clone() or return true (see below)
     * @return True if the solver should stop looking for more solution, if False the solver will look for more solutions.
     */
    private boolean onSolutionFound(SolverGrid solution) {
        if (++nrOfSolutions==1) {
            theSolvedGrid = new Grid(null, false, Block.Companion.getRegionCodes());
            for (int y=0; y<9; y++) {
                for (int x=0; x<9; x++) {
                    theSolvedGrid.setValue(x, y, solution.getValue(x, y), theUnsolvedGrid.getCell(x, y).isGiven());
                }
            }
        }

        return (nrOfSolutions>=maxNrOfSolutions);
    }

    /**
     * Try to solve the puzzle.
     */
    private boolean solve(SolverGrid solverGrid, Random rand) throws InterruptedException {

        // someone wants us to cancel the operation
        if (Thread.interrupted()) throw new InterruptedException();

        // if maximum nr of iterations reached then stop the process
        nrOfIterations++;
        if (nrOfIterations>maxNrOfIterations) return false;

        // (0) eliminate hidden singles first
        solverGrid.check4HiddenSingles();

        // (1) if puzzle is invalid we're done
        if (!solverGrid.isValid()) return false;

        // (2) if puzzle is solved we're done
        if (solverGrid.isSolved()) return onSolutionFound(solverGrid);

        // (3) find first empty cell
        int pos=0;
        for (; pos<81; pos++) {
            if (solverGrid.isEmpty(pos)) {
                break;
            }
        }

        // (4) try each possible value for the cell
        BitSet candidates = (BitSet) solverGrid.getCandidates(pos).clone();
        int value = (rand==null) ? 1 : rand.nextInt(9)+1;
        for (int i=1; i<=9; i++) {
            if (++value==10) value=1;
            if (candidates.get(value)) {
                SolverGrid savedGrid = solverGrid.clone();
                if (solverGrid.setValue(pos, value)) {
                    if (solve(solverGrid, rand)) return true;
                }
                solverGrid.restore(savedGrid);
            }
        }

        // no solution found
        return false;
    }

    // ****************************************** SolverGrid *******************************************

    static class SolverGrid implements Cloneable {
        // the 81 Sudoku values (1..9), 0=empty cell
        private int[] values;	// int is faster than short or byte

        // the 81 candidates (bits 1..9 used)
        private BitSet[] candidates;

        private boolean isValid;
        private int nrOfSetValues;

        private int[][] neighbors;
        private int[][] neighbors_rows;
        private int[][] neighbors_columns;
        private int[][] neighbors_blocks;

        private SolverGrid(Grid grid) {
            this.candidates = new BitSet[81];
            this.isValid = true;
            this.nrOfSetValues = 0;
            this.neighbors = NeighborManager.getNeighbors(grid);
            this.neighbors_rows = NeighborManager.neighbors_rows();
            this.neighbors_columns = NeighborManager.neighbors_columns();
            this.neighbors_blocks = NeighborManager.neighbors_blocks(Block.Companion.getRegionCodes());
            this.values = new int[81];

            // initialize values
            for (int y=0; y<9; y++) {
                for (int x=0; x<9; x++) {
                    Cell cell = grid.getCell(x, y);
                    if (!cell.isEmpty()) setValue(x, y, cell.getValue());
                }
            }
        }

        /**
         * Use this constructor only in the clone method
         */
        private SolverGrid() {}

        @SuppressWarnings("MethodDoesntCallSuperMethod")
        @Override
        public SolverGrid clone() {
            SolverGrid clone = new SolverGrid();
            clone.values = this.values.clone();
            int counter = 0;
            clone.candidates = new BitSet[81];
            for (BitSet tmp:this.candidates) {
                clone.candidates[counter++] = (tmp==null) ? null : (BitSet) tmp.clone();
            }
            clone.isValid = this.isValid;
            clone.nrOfSetValues = this.nrOfSetValues;
            clone.neighbors = this.neighbors;
            clone.neighbors_rows = this.neighbors_rows;
            clone.neighbors_columns = this.neighbors_columns;
            clone.neighbors_blocks = this.neighbors_blocks;
            return clone;
        }

        private void restore(SolverGrid other) {
            this.values = other.values.clone();
            int counter = 0;
            for (BitSet tmp:other.candidates) {
                this.candidates[counter++] = (tmp==null) ? null : (BitSet) tmp.clone();
            }
            this.isValid = other.isValid;
            this.nrOfSetValues = other.nrOfSetValues;
        }

        /**
         * Sets the value at a certain position
         * @param x x-coordinate of the value to set (0..8)
         * @param y y-coordinate of the value to set (0..8)
         * @param value the value to set (1..9)
         */
        private void setValue(int x, int y, int value) {
            setValue(getPos(x, y), value);
        }

        /**
         * Sets the value at a certain position
         * @param pos position of the value in the values array (0..80)
         * @param value the value to set (1..9)
         */
        private boolean setValue(int pos, int value) {
            // set new value
            if (values[pos]==0 && value>0) nrOfSetValues++;
            values[pos] = value;
            BitSet candidates = getCandidates(pos);
            candidates.clear();

            // remove the candidate in neighboring cells (row, column, block)
            for (int m:neighbors[pos]) {
                removeCandidates(m, value);
            }
            return isValid();
        }
        private void removeCandidates(int pos, int value) {
            if (isEmpty(pos)) {
                BitSet candidates = getCandidates(pos);
                candidates.clear(value);
                int card = candidates.cardinality();
                if (card==0) {
                    // no candidates left --> invalid
                    isValid=false;
                    return;
                }
                if (card==1) {
                    // naked single
                    setValue(pos, candidates.nextSetBit(0));
                }
            }
            else if (values[pos]==value) {
                // if one of the neighboring cells is set to value
                // then the puzzle isn't valid
                isValid=false;
            }
        }

        /**
         * Get the value of a certain position
         * @param x x-coordinate of the value to set (0..8)
         * @param y y-coordinate of the value to set (0..8)
         * @return the value (1..9)
         */
        private int getValue(int x, int y) {
            return values[getPos(x, y)];
        }

        private void check4HiddenSingles() {
            for (int pos=0; pos<81; pos++) {
                if (isEmpty(pos)) {
                    BitSet candidates = getCandidates(pos);
                    for (int value=candidates.nextSetBit(0); value>0; value=candidates.nextSetBit(value+1)) {
                        // check for hidden singles in rows
                        boolean hiddenSingleRow = true;
                        for (int r:neighbors_rows[pos]) {
                            if (isEmpty(r) && getCandidates(r).get(value)) {
                                hiddenSingleRow = false;
                                break;
                            }
                        }
                        if (hiddenSingleRow) {
                            setValue(pos, value);
                            check4HiddenSingles();
                            return;
                        }
                        else {
                            // check for hidden singles in columns
                            boolean hiddenSingleColumn = true;
                            for (int c:neighbors_columns[pos]) {
                                if (isEmpty(c) && getCandidates(c).get(value)) {
                                    hiddenSingleColumn = false;
                                    break;
                                }
                            }
                            if (hiddenSingleColumn) {
                                setValue(pos, value);
                                check4HiddenSingles();
                                return;
                            }
                            else {
                                // check for hidden singles in blocks
                                boolean hiddenSingleBlock = true;
                                for (int b:neighbors_blocks[pos]) {
                                    if (isEmpty(b) && getCandidates(b).get(value)) {
                                        hiddenSingleBlock = false;
                                        break;
                                    }
                                }
                                if (hiddenSingleBlock) {
                                    setValue(pos, value);
                                    check4HiddenSingles();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }

        private boolean isEmpty(int pos) {
            return (values[pos]==0);
        }

        private boolean isSolved() {
            return isFull() && isValid();
        }

        private boolean isValid() {
            return isValid;
        }

        private boolean isFull() {
            return (nrOfSetValues==81);
        }

        /**
         * Return the candidates as BitSet
         * @return The candidates as BitSet.
         */
        private BitSet getCandidates(int pos) {
            if (candidates[pos]==null) {
                candidates[pos] = new BitSet();
                candidates[pos].set(1, 10);
            }
            return candidates[pos];
        }

        private int getPos(int x, int y) {
            return y*9 + x;
        }

    }

}

