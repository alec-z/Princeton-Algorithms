import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    private final int[][] tiles;
    private final int n;
    private int sliderRow;
    private int sliderCol;

    public Board(int[][] tiles) {
        this.tiles = deepClone(tiles);
        this.n = tiles.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++ ) {
                if (this.tiles[i][j] == 0) {
                    sliderRow = i;
                    sliderCol = j;
                }
            }
        }
    }

    private int[][] deepClone(int[][] source) {
        int[][] target = source.clone();
        for (int i = 0; i < source.length; i++) {
            target[i] = source[i].clone();
        }
        return target;
    }

    public String toString() {
        StringBuffer res = new StringBuffer();
        res.append(n);
        res.append(System.lineSeparator());
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res.append(tiles[i][j]);
                if (j != n - 1) {
                    res.append(" ");
                }
            }
            if (i != n-1) {
                res.append(System.lineSeparator());
            }
        }
        return res.toString();
    }

    public int dimension() {
        return n;
    }

    private int hammingCached = -1;

    public int hamming() {
        if (hammingCached > -1) return hammingCached;
        int res = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != 0) {
                    if (tiles[i][j] != i * n + j + 1)  {
                        res++;
                    }
                }
            }
        }
        hammingCached = res;
        return hammingCached;
    }
    
    private int manhattanCached = -1;

    public int manhattan() {
        if (manhattanCached > -1) return manhattanCached;
        int res = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j ++) {
                if (tiles[i][j] != 0) {
                    int currentSlide = tiles[i][j];
                    int currentSlideShoudRow = (currentSlide - 1) / n;
                    int currentSlideShoudCol = (currentSlide - 1) % n;
                    res += Math.abs(currentSlideShoudRow - i) + Math.abs(currentSlideShoudCol - j);
                }
            }
        }
        return res;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass().equals(y.getClass())) {
            Board that = (Board)y;
            if (n != that.n) return false;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j <n; j++) {
                    if (tiles[i][j] != that.tiles[i][j]) return false;
                }
            }
            return true;
        }
        return false;
    }

    private void exchange(int[][] aTiles, int i, int j, int row, int col) {
       int tmp = aTiles[i][j];
       aTiles[i][j] = aTiles[row][col];
       aTiles[row][col] = tmp;
    }

    public Iterable<Board> neighbors() {
        List<Board> res = new ArrayList<Board>(4); 
        int[][] newTiles;
        if (sliderRow > 0) {
            newTiles = deepClone(tiles);
            exchange(newTiles, sliderRow,  sliderCol, sliderRow - 1, sliderCol);
            res.add(new Board(newTiles));
        }

        if (sliderCol < n - 1) {
            newTiles = deepClone(tiles);
            exchange(newTiles, sliderRow,  sliderCol, sliderRow, sliderCol + 1);
            res.add(new Board(newTiles));
        }

        if (sliderRow < n - 1) {
            newTiles = deepClone(tiles);
            exchange(newTiles, sliderRow,  sliderCol, sliderRow + 1, sliderCol);
            res.add(new Board(newTiles));
        }

        if (sliderCol > 0) {
            newTiles = deepClone(tiles);
            exchange(newTiles, sliderRow,  sliderCol, sliderRow, sliderCol - 1);
            res.add(new Board(newTiles));
        }
        return res;
    }

    private Board twin = null;
    public Board twin() {
        if (twin != null) return twin;
        Random rand = new Random();
        do {
            int randPosition = rand.nextInt(n);
            int randRow = randPosition / n;
            int randCol = randPosition % n;
            if (tiles[randRow][randCol] != 0 && randRow + 1 < n && tiles[randRow + 1][randCol] != 0) {
                int[][] newTiles = deepClone(tiles);
                exchange(newTiles, randRow, randCol, randRow + 1, randCol);
                twin = new Board(newTiles);
                return twin;
            }
        } while (true);
        
    }

    public static void main(String[] args) {

    }
}
