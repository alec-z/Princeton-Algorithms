import java.util.ArrayList;
import java.util.HashSet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.lengthScoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

    // Initializes the data structure using the given array of strings as the
    // dictionary.
    // (You can assume each word in the dictionary contains only the uppercase
    // letters A through Z.)

    private TrieST trieST;

    public BoggleSolver(String[] dictionary) {
        trieST = new TrieST();
        for (String d : dictionary)
            trieST.put(d, lengthScoreOf(d));
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    private long score = 0;
    private boolean[][] marker;
    private int m, n;
    private BoggleBoard board;
    private HashSet<String> resultWords;

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        score = 0;
        m = board.rows();
        n = board.cols();
        marker = new boolean[m][n];
        this.board = board;
        resultWords = new HashSet<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                resetMarker(marker);
                travelTrieST(i, j, trieST.root);
            }
        }
        return resultWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero
    // otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)

    public int scoreOf(String word) {
        Integer r =  trieST.get(word);
        return r == null ? 0 : r;
    }

    
    private int lengthScoreOf(String word) {
        int l = word.length();
        if (l < 3)
            return 0;
        else if (l <= 4)
            return 1;
        else if (l <= 5)
            return 2;
        else if (l <= 6)
            return 3;
        else if (l <= 7)
            return 5;
        else
            return 11;
    }

    private void resetMarker(boolean[][] marker) {
        int m = marker.length;
        n = marker[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                marker[i][j] = false;
            }
        }
    }

    private void travelTrieST(int i, int j, Node x) {
        if (x == null)
            return;
        if (marker[i][j])
            return;
        marker[i][j] = true;
        Node next = trieST.getNext(x, board.getLetter(i, j));
        if (next != null) {
            if (next.value != null && next.value > 0) {
                if (!resultWords.contains(next.relatedStr)) {
                    score += (next.value);
                    resultWords.add(next.relatedStr);
                }
            }
            if (i - 1 >= 0)
                travelTrieST(i - 1, j, next);
            if (i - 1 >= 0 && j + 1 < n)
                travelTrieST(i - 1, j + 1, next);
            if (j + 1 < n)
                travelTrieST(i, j + 1, next);
            if (i + 1 < m && j + 1 < n)
                travelTrieST(i + 1, j + 1, next);

            if (i + 1 < m)
                travelTrieST(i + 1, j, next);
            if (i + 1 < m && j - 1 >= 0)
                travelTrieST(i + 1, j - 1, next);

            if (j - 1 >= 0)
                travelTrieST(i, j - 1, next);
            if (i - 1 >= 0 && j - 1 >= 0)
                travelTrieST(i - 1, j - 1, next);
        }
        marker[i][j] = false;

    }

    private static final int R = 26;

    private class Node {
        private Integer value;
        private String relatedStr;
        private Node[] next = new Node[R];
    }

    private class TrieST {

        public Node root = new Node();

        public void put(String key, Integer v) {
            put(root, key, v, 0);

        }

        private Node put(Node x, String key, Integer v, int d) {
            if (x == null)
                x = new Node();
            if (d == key.length()) {
                x.value = v;
                x.relatedStr = key;
            } else {
                int c = key.charAt(d) - 'A';
                x.next[c] = put(x.next[c], key, v, d + 1);
            }
            return x;
        }

        public Integer get(String key) {
            return get(root, key, 0);
        }

        private Integer get(Node x, String key, int d) {
            if (x == null)
                return null;
            if (d == key.length())
                return x.value;
            else {
                int c = key.charAt(d) - 'A';
                return get(x.next[c], key, d + 1);
            }
        }

        public Node getNext(Node x, char c) {
            int ci = c - 'A';
            if (c != 'Q') {
                return x.next[ci];
            } else {
                Node next = x.next[ci];
                if (next == null)
                    return null;
                return next.next['U' - 'A'];
            }
        }
    }

}
