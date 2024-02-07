
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class WordNet {
    private SortedMap<String, TreeSet<Integer>> nounToSynset = new TreeMap<>();
    private Digraph g = null;;
    private SAP sap = null;
    private List<String> vertices = new ArrayList<>();
    public WordNet(String synsets, String hypernyms){
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        In inSynSets = new In(synsets);
        In inHypernyms = new In(hypernyms);
        
        int vNum = 0;
        
        while (inSynSets.hasNextLine()) {
            String line = inSynSets.readLine();
            String[] lineArray = line.split(",");
            String setStr = lineArray[1];
            String[] words = setStr.split("\\s");
            Integer setID = Integer.valueOf(lineArray[0]);
            vertices.add(setStr);
            for (String word: words) {
                if (nounToSynset.get(word) == null) {
                    nounToSynset.put(word, new TreeSet<>());
                }
                nounToSynset.get(word).add(setID);
            }
            vNum++;
        }
        g = new Digraph(vNum);

        while (inHypernyms.hasNextLine()) {
            String line = inHypernyms.readLine();
            String[] lineArray = line.split(",");
            int v = Integer.parseInt(lineArray[0]);
            for (int i = 1; i < lineArray.length; i++) {
               g.addEdge(v, Integer.parseInt(lineArray[i]));
            }
        }
        sap = new SAP(g);
        
    }

    public Iterable<String> nouns(){        
        return nounToSynset.keySet();
    }
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nounToSynset.get(word) != null;
    }
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        Set<Integer> setA = nounToSynset.get(nounA);
        Set<Integer> setB = nounToSynset.get(nounB);
        if (setA == null || setB == null) throw new IllegalArgumentException();
        return sap.length(setA, setB);
    }
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        Set<Integer> setA = nounToSynset.get(nounA);
        Set<Integer> setB = nounToSynset.get(nounB);
        if (setA == null || setB == null) throw new IllegalArgumentException();
        int ancestor = sap.ancestor(setA, setB);
        return vertices.get(ancestor);
    }

    public static void main(String[] args) {
        return;
    }

}
