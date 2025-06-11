package org.example;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

import java.util.Comparator;

public class Solver {
    // find a solution to the initial board (using the A* algorithm)
    private boolean isSolvable;
    private Queue<Board> solution;
    private int moves;

    public Solver(Board initial) {
        if (initial==null) throw new IllegalArgumentException("The board can not be a null object.");
        MinPQ<SearchNode> boardsQueue = new MinPQ<>();
        MinPQ<SearchNode> twinBoardsQueue = new MinPQ<>();
//        MinPQ<SearchNode> visitedBoardsQueue = new MinPQ<>(new Comparator<SearchNode>() {
//            @Override
//            public int compare(SearchNode o1, SearchNode o2) {
//                if (o1.nodeManhattanDistance
//                        > o2.nodeManhattanDistance) return 1;
//                else if (o2.nodeManhattanDistance >
//                        o1.nodeManhattanDistance) return -1;
//                else return 0;
//            }
//        });
//        MinPQ<SearchNode> visitedTwinBoardsQueue = new MinPQ<SearchNode>(new Comparator<SearchNode>() {
//            @Override
//            public int compare(SearchNode o1, SearchNode o2) {
//                if (o1.nodeManhattanDistance
//                        > o2.nodeManhattanDistance) return 1;
//                else if (o2.nodeManhattanDistance >
//                        o1.nodeManhattanDistance) return -1;
//                else return 0;
//            }
//        });
        solution = new Queue<>();
        SearchNode currentSearchNode = new SearchNode(initial, null, initial.hamming(), initial.manhattan(), 0);
        SearchNode currentTwinSearchNode = new SearchNode(initial.twin(), null, initial.twin().hamming(), initial.twin().manhattan(), 0);
        while (!currentSearchNode.board.isGoal() && !currentTwinSearchNode.board.isGoal()) {
            for (Board neighbor : currentSearchNode.board.neighbors()) {
                if (currentSearchNode.parentNode == null || !currentSearchNode.parentNode.board.equals(neighbor)) {
                    boardsQueue.insert(new SearchNode(neighbor, currentSearchNode, neighbor.hamming(), neighbor.manhattan(), currentSearchNode.moves + 1));
                }
            }
            currentSearchNode = boardsQueue.delMin();
            for (Board neighbor : currentTwinSearchNode.board.neighbors()) {
                if (currentTwinSearchNode.parentNode == null || !currentTwinSearchNode.parentNode.board.equals(neighbor)) {
                    twinBoardsQueue.insert(new SearchNode(neighbor, currentTwinSearchNode, neighbor.hamming(), neighbor.manhattan(), currentTwinSearchNode.moves + 1));
                }
            }
            currentTwinSearchNode = twinBoardsQueue.delMin();
        }
        if (currentSearchNode.board.isGoal()) {
            isSolvable = true;
            moves = currentSearchNode.moves;
            while (currentSearchNode.parentNode != null) {
                solution.enqueue(currentSearchNode.board);
                currentSearchNode = currentSearchNode.parentNode;
            }
        }
        if (currentTwinSearchNode.board.isGoal()) {
            isSolvable = false;
        }
    }


    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable) return -1;
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable) return null;
        return solution;
    }

    private static class SearchNode implements Comparable<SearchNode> {
        SearchNode parentNode;
        Board board;
        int nodeManhattanDistance;
        int nodeHammingDistance;
        int priority;
        int moves;

        private SearchNode(Board board, SearchNode parentNode, int nodeHammingDistance, int nodeManhattanDistance, int moves) {
            this.board = board;
            this.nodeHammingDistance = board.hamming();
            this.nodeManhattanDistance = board.manhattan();
            this.priority = nodeHammingDistance + nodeManhattanDistance + moves;
            this.priority = nodeManhattanDistance + moves;
            this.parentNode = parentNode;
            this.moves = moves;
        }

        @Override
        public int compareTo(SearchNode that) {
            if (this.priority < that.priority) return -1;
            if (this.priority > that.priority) return 1;
            return 0;
        }
    }

    // test client (see below)
    public static void main(String[] args) {

    }
}
