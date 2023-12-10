import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SolverTest {
    @Test
    public void solveTest() {
        Board initBoard = new Board(new int[][]{
            {0, 1, 3},
            {4, 2, 5},
            {7, 8, 6},
        });
        Solver solver = new Solver(initBoard);
        assertEquals(4,solver.moves());
        int step = 0;
        for (Board sb: solver.solution()) {
            System.out.println(sb.toString());
            System.out.println(">>>>>" + "  S:"  + step++);
            
        }
    }
    
}
