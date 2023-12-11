import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    // find a solution to the initial board (using the A* algorithm)
    private StepBoard goal = null;;
    private Board initial;
    private List<Board> solutions;
    private int moves = -1;
    private boolean solvable = false;

    public Solver(Board initial) {
        this.initial = initial;
        solve();
    }

    private void solve() {
        MinPQ<StepBoard> minPQ = new MinPQ<>(100, (a, b) -> a.priority - b.priority);
        MinPQ<StepBoard> minPQTwin = new MinPQ<>(100, (a, b) -> a.priority - b.priority);
        StepBoard initialStep = new StepBoard(initial, null, 0);
        StepBoard initialTwin = new StepBoard(initial.twin(), null, 0);

        minPQ.insert(initialStep);
        minPQTwin.insert(initialTwin);

        StepBoard currentStep = null;
        StepBoard currentTwin = null;

        while (!minPQ.isEmpty() && !minPQTwin.isEmpty()) {
            currentStep = minPQ.delMin();
            currentTwin = minPQTwin.delMin();
            if (currentStep.isGoal()) {
                solvable = true;
                break;
            }
            if (currentTwin.isGoal()) {
                solvable = false;
                break;
            }

            for (StepBoard newBoard : currentStep.neighbours()) {
                minPQ.insert(newBoard);
            }
            for (StepBoard newBoard : currentTwin.neighbours()) {
                minPQTwin.insert(newBoard);
            }
        }
        if (solvable) {
            goal = currentStep;
            moves = currentStep.step;
            solutions = new ArrayList<Board>(40);
            do {
                solutions.add(currentStep.board);
                currentStep = currentStep.preStep;

            } while (currentStep != null);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;

    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;

    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (solutions == null) {
            return null;
        }
        List<Board> copySoluttions = new ArrayList<>(solutions.size());
        for (int i = solutions.size() - 1; i >= 0; i--) {
            copySoluttions.add(solutions.get(i));
        }
        return copySoluttions;

    }

    private class StepBoard {
        private final Board board;
        private final int priority;
        private final StepBoard preStep;
        private final int step;

        public StepBoard(Board board, StepBoard preStep, int step) {
            this.board = board;
            this.preStep = preStep;
            this.step = step;
            this.priority = board.manhattan() + step;
        }

        public boolean isGoal() {
            return board.isGoal();
        }

        public Iterable<StepBoard> neighbours() {
            List<StepBoard> res = new ArrayList<StepBoard>(3);
            for (Board nb : board.neighbors()) {
                if (preStep == null || !nb.equals(preStep.board)) {
                    res.add(new StepBoard(nb, this, step + 1));
                }
            }
            return res;
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

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
