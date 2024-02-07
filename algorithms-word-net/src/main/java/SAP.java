import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;

public class SAP {

    // constructor takes a digraph (not necessarily a DAG)
    private Digraph g = null;
    private Queue<Integer> queue = new LinkedList<>();
    private int[] markR;
    private int[] markB;

    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        g = new Digraph(G);
        int vNum = g.V();
        markR = new int[vNum];
        Arrays.fill(markR, -1);
        markB = new int[vNum];
        Arrays.fill(markB, -1);
        queue.clear();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        List<Integer> vs = new ArrayList<>(1);
        vs.add(v);
        List<Integer> ws = new ArrayList<>(1);
        ws.add(w);
        return length(vs, ws);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {
        List<Integer> vs = new ArrayList<>(1);
        vs.add(v);
        List<Integer> ws = new ArrayList<>(1);
        ws.add(w);
        return ancestor(vs, ws);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in
    // w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        for (Integer i: v) if (i == null || i < 0 || i >= g.V()) throw new IllegalArgumentException();
        for (Integer i: w) if (i == null || i < 0 || i >= g.V()) throw new IllegalArgumentException();

        Result r = ancestorBFS(v, w);
        return r.length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such
    // path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        for (Integer i: v) if (i == null || i < 0 || i >= g.V()) throw new IllegalArgumentException();
        for (Integer i: w) if (i == null || i < 0 || i >= g.V()) throw new IllegalArgumentException();
        Result r = ancestorBFS(v, w);
        return r.ancestor;
    }

    private Result ancestorBFS(Iterable<Integer> v, Iterable<Integer> w) {
        Arrays.fill(markR, -1);
        Arrays.fill(markB, -1);
        queue.clear();
        for (int r : v) {
            queue.add(r);
            markR[r] = 0;
            if (markB[r] > -1) {
                return new Result(r, 0);
            }
        }
        for (int b : w) {
            queue.add(b);
            markB[b] = 0;
            if (markR[b] > -1) {
                return new Result(b, 0);
            }
        }
        Result result = new Result(-1, Integer.MAX_VALUE);
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            if (markR[cur] > -1 && markR[cur] < result.length) {
                for (int next : g.adj(cur)) {
                    if (markB[next] > -1) {
                        int newLength =  markR[cur] + 1 + markB[next];
                        if (newLength < result.length) {
                            result.length = newLength;
                            result.ancestor = next;
                        }
                    }
                    if (markR[next] == -1) {
                        markR[next] = markR[cur] + 1;
                        queue.add(next);
                    }
                }
            }
            if (markB[cur] > -1 && markB[cur] < result.length ){
                for (int next : g.adj(cur)) {
                    if (markR[next] > -1) {
                        int newLength =  markB[cur] + 1 + markR[next];
                        if (newLength < result.length) {
                            result.length = newLength;
                            result.ancestor = next;
                        }
                    }
                    if (markB[next] == -1) {
                        markB[next] = markB[cur] + 1;
                        queue.add(next);
                    }
                }
            }
        }
        if (result.ancestor == -1) {
            result.length = -1;
        }
        return result;
    }

    private class Result {
        int ancestor;
        int length;

        Result(int ancestor, int length) {
            this.ancestor = ancestor;
            this.length = length;
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String inputFile = "digraph1.txt";
        In in = new In(inputFile);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}