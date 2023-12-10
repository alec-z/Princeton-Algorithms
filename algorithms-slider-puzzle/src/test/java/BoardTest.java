import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class BoardTest {
    private Board board;
    public BoardTest() {
        board = new Board(new int[][]{
            {1, 0, 3},
            {4, 2, 5},
            {7, 8, 6},
        });
    }
    @Test
    public void toStringTest() {
        Board board = new Board(new int[][]{
            {1, 0, 3},
            {4, 2, 5},
            {7, 8, 6},
        });

        
        assertEquals("3" + System.lineSeparator()
        + "1 0 3" + System.lineSeparator()
        + "4 2 5" + System.lineSeparator()
        + "7 8 6",
        board.toString()
        );
    }
    @Test
    public void dimensionTest() {
        assertTrue(board.dimension() == 3);
    }

    @Test
    public void hamming() {
        Board newBoard = new Board(new int[][]{
            {8, 1, 3},
            {4, 0, 2},
            {7, 6, 5},
        });

        assertTrue(newBoard.hamming() == 5);
        assertTrue(board.hamming() == 3);
    }

    @Test
    public void manhattan() {
        Board newBoard = new Board(new int[][]{
            {8, 1, 3},
            {4, 0, 2},
            {7, 6, 5},
        });
        assertTrue(newBoard.manhattan() == 10);
    }

    @Test
    public void isGoalTest() {
        assertFalse(board.isGoal());
        Board goalBoard = new Board(new int[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0},
        });
        assertTrue(goalBoard.isGoal());
    }

    @Test
    public void equalsTest() {
        Board goalBoard = new Board(new int[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0},
        });
        assertFalse(board.equals(goalBoard));
        Board boardCopy = new Board(new int[][]{
            {1, 0, 3},
            {4, 2, 5},
            {7, 8, 6},
        });
        assertTrue(board.equals(boardCopy));
    }

    //@Test
    public void neighborsTest() {
        List<Board> neighbors = (List<Board>)board.neighbors();
        assertTrue(neighbors.contains(new Board(new int[][]{
            {1, 2, 3},
            {4, 0, 5},
            {7, 8, 6},
        })));
        assertTrue(neighbors.contains(new Board(new int[][]{
            {1, 3, 0},
            {4, 2, 5},
            {7, 8, 6},
        })));
        assertTrue(neighbors.contains(new Board(new int[][]{
            {0, 1, 3},
            {4, 2, 5},
            {7, 8, 6},
        })));
        assertFalse(neighbors.contains(new Board(new int[][]{
            {1, 8, 3},
            {4, 2, 5},
            {7, 0, 6},
        })));
    }
    @Test
    public void twinTest() {
        Board twin = board.twin();
        assertFalse(board.equals(board.twin()));

    }



}
