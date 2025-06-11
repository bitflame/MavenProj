package org.example;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;


public final class Board {
    private int N, hammingDistance = 0, manhattanDistance = 0, zeroIndex =0, lengthOfOneDimensionalArray =0, zeroRow = 0, zeroCol =0;
    private char [] tiles;
    private char [] goalBoardOneDimensionTiles = new char[0];
    private Board twin;
    public Board(int[][] tiles) {
        int itemCount = 1, row=0, col=0, desiredRow = 0, desiredCol =0;
        N =  tiles[0].length;
        lengthOfOneDimensionalArray = N*N;
        goalBoardOneDimensionTiles = new char [lengthOfOneDimensionalArray];
        this.tiles = new char [lengthOfOneDimensionalArray];
        for (int i = 0; i < lengthOfOneDimensionalArray; i++) {
            if (col == N) {
                row++;
                col = 0;
            }
            goalBoardOneDimensionTiles[i] = (char) itemCount++;
            this.tiles[i] = (char) tiles[row][col];
            if (this.tiles[i]!=0) {
                if (i < lengthOfOneDimensionalArray -1 && this.tiles[i]!= goalBoardOneDimensionTiles[i]) hammingDistance++;
                desiredRow = (tiles[row][col] -1) / N;
                if (tiles[row][col]% N==0) {
                    desiredCol = N -1;
                } else desiredCol = (tiles[row][col] % N) -1;
                manhattanDistance += Math.abs(row - (desiredRow)) + Math.abs(col - (desiredCol));
            } else {
                zeroIndex = i;
                zeroRow = row;
                zeroCol = col;
                if (zeroIndex!=lengthOfOneDimensionalArray-1) hammingDistance++;
            }
            col++;
        }
        goalBoardOneDimensionTiles[lengthOfOneDimensionalArray - 1] = 0;
    }
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        int col = 0;
        for (int i = 0; i < N*N; i++) {
                s.append(String.format("%2d ", (int) tiles[i]));
                col++;
                if (col==N) {
                    col =0;
                    s.append("\n");
                }
        }
        return s.toString();
    }

    // board dimension n
    public int dimension(){
        return N;
    }

    // number of tiles out of place
    public int hamming() {
        return hammingDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal(){
        return Arrays.equals(this.tiles, goalBoardOneDimensionTiles);
    }

    // does this board equal y?
    public boolean equals(Object y){
        if (this==y) return true;
        if (y==null) return false;
        if (this.getClass()!=y.getClass()) return false;
        Board that = (Board) y;
        return Arrays.equals(this.tiles,((Board) y).tiles);
    }
    private int[][] convertToTwoDimensionalArray(char [] tiles, int N) {
        int[][] modifiedTiles = new int[N][N];
        int oneDimensionalCounter = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                modifiedTiles[i][j]= tiles[oneDimensionalCounter];
                oneDimensionalCounter++;
            }
        }
        return modifiedTiles;
    }
    public Iterable<Board> neighbors() {
        char temp = ' ';
        int tempIntegerValue = 0, actualRowBeforeMove, actualRowAfterMov, actualColBeforeMove, actualColAfterMov,
                manhattanContributionBeforeMove, manhattanContributionAfterMove, desiredRow, desiredCol;
        Queue<Board> neighbors = new Queue<>();
        int [][] modifiedTiles = convertToTwoDimensionalArray(tiles, N);
        if (((zeroIndex % N)) < N - 1) {  // create right neighbor if zero is not in the last column
            Board rightNeighbor = new Board(modifiedTiles);
            rightNeighbor.N = N;
            rightNeighbor.tiles = Arrays.copyOf(tiles, lengthOfOneDimensionalArray);
            temp = rightNeighbor.tiles[zeroIndex + 1];
            rightNeighbor.manhattanDistance = manhattanDistance;
            rightNeighbor.hammingDistance = hammingDistance;
            if (goalBoardOneDimensionTiles[zeroIndex+1]==rightNeighbor.tiles[zeroIndex+1]) {
                // if the right neighbor value was in the right place, we increment hamming distance by 1
                rightNeighbor.hammingDistance++;
            }
            rightNeighbor.tiles[zeroIndex+1] = 0;
            rightNeighbor.tiles[zeroIndex] = temp;
            rightNeighbor.zeroIndex = zeroIndex+1;
            rightNeighbor.zeroCol = zeroCol + 1;
            rightNeighbor.zeroRow = zeroRow;
            if (goalBoardOneDimensionTiles[zeroIndex]==rightNeighbor.tiles[zeroIndex]) {
                // if the value was moved to where it was supposed to be decrement hamming and manhattan dist
                rightNeighbor.hammingDistance--;
                rightNeighbor.manhattanDistance--;
            } else {
                // update manhattan by comparing desired row, col, vs. actual row, col of value
                tempIntegerValue = (int) temp;
                desiredRow = (tempIntegerValue -1) / N;
                if (tempIntegerValue % N == 0 ) {
                    desiredCol = N -1;
                } else desiredCol = ( tempIntegerValue % N) -1;
                actualRowBeforeMove = (zeroIndex + 1) / N;
                actualRowAfterMov = (zeroIndex) / N;
                actualColBeforeMove = (zeroIndex + 1) % N;
                actualColAfterMov = (zeroIndex) % N;
                manhattanContributionBeforeMove = Math.abs(actualRowBeforeMove - desiredRow) + Math.abs(actualColBeforeMove - desiredCol);
                manhattanContributionAfterMove = Math.abs(actualRowAfterMov - desiredRow) + Math.abs(actualColAfterMov - desiredCol);
                rightNeighbor.manhattanDistance += manhattanContributionAfterMove - manhattanContributionBeforeMove;
            }
            neighbors.enqueue(rightNeighbor);
        }
        if ( (zeroIndex % N) > 0 ) { // create left neighbor if zero is not in the first column
            Board leftNeighbor = new Board(modifiedTiles);
            leftNeighbor.N = N;
            leftNeighbor.tiles = Arrays.copyOf(tiles, lengthOfOneDimensionalArray);
            temp = leftNeighbor.tiles[zeroIndex - 1];
            leftNeighbor.hammingDistance = hammingDistance;
            leftNeighbor.manhattanDistance = manhattanDistance;
            if (goalBoardOneDimensionTiles[zeroIndex-1]==leftNeighbor.tiles[zeroIndex-1]) {
                // if the left neighbor was in the right place, this move should increment the hamming distance
                leftNeighbor.hammingDistance++;
            }
            leftNeighbor.tiles[zeroIndex - 1] = 0;
            leftNeighbor.tiles[zeroIndex] = temp;
            leftNeighbor.zeroIndex = zeroIndex -1;
            leftNeighbor.zeroRow = zeroRow;
            leftNeighbor.zeroCol = zeroCol - 1;
            if (goalBoardOneDimensionTiles[zeroIndex]==leftNeighbor.tiles[zeroIndex]) {
                // if the value was moved to where it was supposed to be decrement hamming and manhattan dist
                leftNeighbor.hammingDistance--;
                leftNeighbor.manhattanDistance--;
            } else {
                // update manhattan by comparing desired row, col, vs. actual row, col of value
                tempIntegerValue = (int) temp;
                desiredRow = (tempIntegerValue -1) / N;
                if (tempIntegerValue % N == 0 ) {
                    desiredCol = N -1;
                } else desiredCol = ( tempIntegerValue % N) -1;
                actualRowBeforeMove = (zeroIndex - 1) / N;
                actualRowAfterMov = (zeroIndex) / N;
                actualColBeforeMove = (zeroIndex - 1) % N;
                actualColAfterMov = (zeroIndex) % N;
                manhattanContributionBeforeMove = Math.abs(actualRowBeforeMove - desiredRow) + Math.abs(actualColBeforeMove - desiredCol);
                manhattanContributionAfterMove = Math.abs(actualRowAfterMov - desiredRow) + Math.abs(actualColAfterMov - desiredCol);
                // todo - test this thoroughly to make sure manhattanDistance increases and decreases as it should
                leftNeighbor.manhattanDistance += manhattanContributionAfterMove - manhattanContributionBeforeMove;
            }
            neighbors.enqueue(leftNeighbor);
        }
        if (((zeroIndex / N)) < (N -1 )) { // create a bottom neighbor if zero is not in the last row
            Board bottomNeighbor = new Board(modifiedTiles);
            bottomNeighbor.N = N;
            bottomNeighbor.tiles = Arrays.copyOf(tiles, lengthOfOneDimensionalArray);
            temp = bottomNeighbor.tiles[zeroIndex + N];
            bottomNeighbor.hammingDistance = hammingDistance;
            bottomNeighbor.manhattanDistance = manhattanDistance;
            if (goalBoardOneDimensionTiles[zeroIndex+N]==bottomNeighbor.tiles[zeroIndex+N]){
                // if bottomNeighbor was in the right place, this move should increment the distances by 1
                bottomNeighbor.hammingDistance++;
            }
            bottomNeighbor.tiles[zeroIndex + N ] = 0;
            bottomNeighbor.tiles[zeroIndex] = temp;
            bottomNeighbor.zeroIndex = zeroIndex + N;
            bottomNeighbor.zeroRow = zeroRow + 1;
            bottomNeighbor.zeroCol = zeroCol;
            if (goalBoardOneDimensionTiles[zeroIndex]==bottomNeighbor.tiles[zeroIndex]) {
                // if the value was moved to where it was supposed to be decrement hamming and manhattan dist
                bottomNeighbor.hammingDistance--;
                bottomNeighbor.manhattanDistance--;
            } else {
                // update manhattan by comparing desired row, col, vs. actual row, col of value
                tempIntegerValue = (int) temp;
                desiredRow = (tempIntegerValue -1 ) / N;
                if ((tempIntegerValue)  % N == 0 ) {
                    desiredCol = N -1;
                } else desiredCol = ( tempIntegerValue % N) -1;
                actualRowBeforeMove = zeroRow + 1;
                actualRowAfterMov = zeroRow;
                actualColBeforeMove = zeroCol;
                actualColAfterMov = zeroCol;
                manhattanContributionBeforeMove = Math.abs(actualRowBeforeMove - desiredRow) + Math.abs(actualColBeforeMove - desiredCol);
                manhattanContributionAfterMove = Math.abs(actualRowAfterMov - desiredRow) + Math.abs(actualColAfterMov - desiredCol);
                bottomNeighbor.manhattanDistance += manhattanContributionAfterMove - manhattanContributionBeforeMove;
            }
            neighbors.enqueue(bottomNeighbor);
        }
        if ((zeroIndex / N) > 0) { // create a top neighbor only if zero is not on the first row
            Board topNeighbor = new Board(modifiedTiles);
            topNeighbor.N = N;
            topNeighbor.tiles = Arrays.copyOf(tiles, lengthOfOneDimensionalArray);
            temp = topNeighbor.tiles[zeroIndex-N];
            topNeighbor.hammingDistance = hammingDistance;
            topNeighbor.manhattanDistance = manhattanDistance;
            if (goalBoardOneDimensionTiles[zeroIndex-N]==topNeighbor.tiles[zeroIndex-N]) {
                // if the value is in the right place, this move should increment distances
                topNeighbor.hammingDistance++;
            }
            topNeighbor.tiles[zeroIndex] = temp;
            topNeighbor.tiles[zeroIndex-N] = 0;
            topNeighbor.zeroIndex = zeroIndex - N;
            topNeighbor.zeroRow = zeroRow - 1;
            topNeighbor.zeroCol = zeroCol;
            if (goalBoardOneDimensionTiles[zeroIndex]==topNeighbor.tiles[topNeighbor.zeroIndex]) {
                // if the value was moved to where it was supposed to be decrement hamming and manhattan dist
                topNeighbor.hammingDistance--;
                // topNeighbor.manhattanDistance--;
            } else {
                // update manhattan by comparing desired row, col, vs. actual row, col of value
                tempIntegerValue = (int) temp;
                desiredRow = (tempIntegerValue -1) / N;
                if (tempIntegerValue % N == 0 ) {
                    desiredCol = N -1;
                } else desiredCol = ( tempIntegerValue % N) -1;
                actualRowBeforeMove = zeroRow - 1;
                actualRowAfterMov = zeroRow;
                actualColBeforeMove = zeroCol;
                actualColAfterMov = zeroCol;
                manhattanContributionBeforeMove = Math.abs(actualRowBeforeMove - desiredRow) + Math.abs(actualColBeforeMove - desiredCol);
                manhattanContributionAfterMove = Math.abs(actualRowAfterMov - desiredRow) + Math.abs(actualColAfterMov - desiredCol);
                topNeighbor.manhattanDistance += manhattanContributionAfterMove - manhattanContributionBeforeMove;
            }
            neighbors.enqueue(topNeighbor);
        }
        return neighbors;
    }
    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        char temp;
        int [][] modifiedTiles = convertToTwoDimensionalArray(tiles, N);
        twin = new Board(modifiedTiles);
        int firstTwinCellAddress;
        int secondTwinCellAddress;
        char firstTwinValue;
        char secondTwinValue;
        int desiredRow, actualRowBeforeMove, actualRowAfterMov, actualColBeforeMove, actualColAfterMov,desiredCol, manhattanContributionBeforeMove, manhattanContributionAfterMove;
        if (zeroIndex == 0) {
            // if zero is in the first cell exchange the next two values
            firstTwinCellAddress = 1;
            secondTwinCellAddress = 2;
        } else if (zeroIndex == lengthOfOneDimensionalArray-1) {
            // if zero is in the last cell exchange the two previous ones
            firstTwinCellAddress = zeroIndex - 1;
            secondTwinCellAddress = zeroIndex - 2;
        } else {
            firstTwinCellAddress = zeroIndex - 1;
            secondTwinCellAddress = zeroIndex + 1;
        }
        firstTwinValue = twin.tiles[firstTwinCellAddress];
        int valueOfFirstTwin = (int) firstTwinValue;
        secondTwinValue = twin.tiles[secondTwinCellAddress];
        int valueOfSecondTwin = (int) secondTwinValue;
        // before the exchange if the value is in the right place, increment hamming distance
        if (tiles[firstTwinCellAddress]==goalBoardOneDimensionTiles[firstTwinCellAddress]) {
            twin.hammingDistance++;
        }
        if (twin.tiles[secondTwinCellAddress]==goalBoardOneDimensionTiles[secondTwinCellAddress]) {
            twin.hammingDistance++;
        }
        temp = twin.tiles[firstTwinCellAddress];
        twin.tiles[firstTwinCellAddress] = twin.tiles[secondTwinCellAddress];
        twin.tiles[secondTwinCellAddress] = temp;
        // After the exchange if the value is in the right place, decrement hamming distance
        if (twin.tiles[firstTwinCellAddress]==goalBoardOneDimensionTiles[firstTwinCellAddress]) {
            twin.hammingDistance--;
        }
        if (twin.tiles[secondTwinCellAddress]==goalBoardOneDimensionTiles[secondTwinCellAddress]) {
            twin.hammingDistance--;
        }
        // first cell

        desiredRow = (firstTwinValue - 1) / N;
        actualRowBeforeMove = firstTwinCellAddress / N;
        actualRowAfterMov = secondTwinCellAddress / N;
        if (firstTwinValue % N == 0) {
            desiredCol = N -1;
        } else {
            desiredCol = (firstTwinValue % N) - 1;
        }
        // calculate manhattan contribution of the first cell
        actualColBeforeMove = firstTwinCellAddress % N;
        actualColAfterMov = secondTwinCellAddress % N;
        manhattanContributionBeforeMove = Math.abs(desiredRow - actualRowBeforeMove) + Math.abs(desiredCol- actualColBeforeMove);
        manhattanContributionAfterMove = Math.abs(desiredRow - actualRowAfterMov) + Math.abs(desiredCol - actualColAfterMov);
        twin.manhattanDistance += manhattanContributionAfterMove - manhattanContributionBeforeMove;
        desiredRow = (secondTwinValue - 1) / N;
        actualRowBeforeMove = secondTwinCellAddress / N;
        actualRowAfterMov = firstTwinCellAddress / N;
        if (secondTwinValue % N == 0) {
            desiredCol = N - 1;
        }  else {
            desiredCol = (secondTwinValue % N) - 1;
        }
        actualColBeforeMove = secondTwinCellAddress % N;
        actualColAfterMov = firstTwinCellAddress % N;
        // calculate the manhattan contribution of the second cell
        manhattanContributionBeforeMove = Math.abs(desiredRow - actualRowBeforeMove) + Math.abs(desiredCol- actualColBeforeMove);
        manhattanContributionAfterMove = Math.abs(desiredRow - actualRowAfterMov) + Math.abs(desiredCol - actualColAfterMov);
        twin.manhattanDistance += manhattanContributionAfterMove - manhattanContributionBeforeMove;
        return twin; // for now
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // todo -- create spock unit tests for testing equals
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        int[][] copyOfTiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++){
                tiles[i][j] = in.readInt();
                copyOfTiles[i][j] = tiles[i][j];
            }
        Board initial = new Board(tiles);
        Board copyOfInitial = new Board(copyOfTiles);
        StdOut.println("Current puzzle layout is as follows: ");
        System.out.println(initial.toString());
        System.out.println("The exact copy of it is this one: ");
        System.out.println(copyOfInitial.toString());
        System.out.println("Is the board equal to its copy? Should be true: "+initial.equals(copyOfInitial));
        System.out.println("Is initial board == to copyOfInitial? "+ (initial == copyOfInitial));
        System.out.println("Now we are going to change cells (1, 2) and (1, 3)");
        int temp = copyOfTiles[1][2];
        copyOfTiles[1][2]=copyOfTiles[1][1];
        copyOfTiles[1][1]= temp;
        copyOfInitial = new Board(copyOfTiles);
        System.out.println("Is the board equal to its copy after swaping a couple of cells? Should be false: "+initial.equals(copyOfInitial));
        System.out.println("Is initial board == to copyOfInitial? Expecting false:  "+ (initial == copyOfInitial));
        StdOut.printf("The dimension of it is: %d\n", initial.dimension());
        StdOut.printf("The hamming distance of current board is: %d\n", initial.hamming());
        StdOut.printf("The manhattan distance of the current board is: %d\n", initial.manhattan());
        StdOut.printf("Board is equal to its copy: %b\n",initial.equals(copyOfInitial));
        System.out.println("Here are the neighbors: ");
        for(Board board: initial.neighbors()) {
            System.out.println(board.toString());
            StdOut.printf("The hamming distance: %d, Manhattan Distance: %d\n", board.hamming(), board.manhattan());
        }
        System.out.println("Here is the twin");
        Board twin = initial.twin();
        System.out.println(twin.toString());
        System.out.printf("hamming distance of it: %d manhattan distance of it: %d\n", twin.hamming(), twin.manhattan());
        System.out.println("Here are the neighbors of the twin: ");
        for(Board board: twin.neighbors()) {
            System.out.println(board.toString());
            System.out.printf("The hamming distance: %d, Manhattan Distance: %d\n", board.hamming(), board.manhattan());
        }
        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }

    }
}
