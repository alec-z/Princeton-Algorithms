
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.List;
import java.util.ArrayList;

import java.util.Queue;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordnet;
    public Outcast(WordNet wordnet) {        // constructor takes a WordNet object
        this.wordnet = wordnet;
    }
    public String outcast(String[] nouns)  { // given an array of WordNet nouns, return an outcast
        
        Map<String, Integer> f = new HashMap<>();
        int maxDisSum = Integer.MIN_VALUE;
        int out = -1;
        for (int i = 0; i < nouns.length; i++) {
            int disSum = 0;
            for (int j = 0; j < nouns.length; j++) {
                if (i != j) {
                    int max = Math.max(i, j);
                    int min = Math.min(i, j);
                    if (f.get(min + ":" + max) == null) {
                        int length = wordnet.distance(nouns[min], nouns[max]);
                        f.put(min + ":" + max, length);
                    }
                    disSum += f.get(min + ":" + max);
                }
            }
            if (disSum > maxDisSum) {
                maxDisSum = disSum;
                out = i;
            }
        }
        return nouns[out];
    }

    public static void main(String[] args)  { // see test client below
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
 }
