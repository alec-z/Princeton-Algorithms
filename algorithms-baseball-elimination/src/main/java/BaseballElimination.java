import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class BaseballElimination {
    private int teamSize = 0;
    private String[] teamNames = null;
    private int[] wins, losses, remains;
    private int[][] pairRemains;
    private Map<String, Integer> nameToId;
    // caculated
    private boolean[] isEliminated;
    private ArrayList<ArrayList<Integer>> coes = null;
    private Map<Integer, String> verticeNameToID;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        teamSize = in.readInt();
        teamNames = new String[teamSize];
        wins = new int[teamSize];
        losses = new int[teamSize];
        remains = new int[teamSize];
        pairRemains = new int[teamSize][];

        for (int i = 0; i < teamSize; i++) {
            teamNames[i] = in.readString();
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remains[i] = in.readInt();
            pairRemains[i] = new int[teamSize];
            for (int j = 0; j < teamSize; j++) {
                pairRemains[i][j] = in.readInt();
            }
        }
        nameToId = new HashMap<>();
        for (int i = 0; i < teamSize; i++) {
            nameToId.put(teamNames[i], i);
        }

        // caculate
        init();

    }

    private void init() {
        isEliminated = new boolean[teamSize];
        coes = new ArrayList<>();
        for (int i = 0; i < teamSize; i++) {
            isEliminated[i] = false;
            coes.add(new ArrayList<Integer>());
        }
        int matchVers = teamSize * (teamSize - 1) / 2;
        int sVer = 0;
        int tVer = matchVers + teamSize + 1;

        for (int v = 0; v < teamSize; v++) {
            FlowNetwork fn = new FlowNetwork(matchVers + teamSize + 2);
            int verID = 0;
            double totalR = 0;
            for (int i = 0; i < teamSize; i++) {
                for (int j = i + 1; j < teamSize; j++) {
                    verID++;
                    if (i == v || j == v)
                        continue;
                    fn.addEdge(new FlowEdge(sVer, verID, pairRemains[i][j]));
                    fn.addEdge(new FlowEdge(verID, matchVers + i + 1, Double.POSITIVE_INFINITY));
                    fn.addEdge(new FlowEdge(verID, matchVers + j + 1, Double.POSITIVE_INFINITY));
                    totalR += pairRemains[i][j];
                }
            }

            for (int i = 0; i < teamSize; i++) {
                if (i == v)
                    continue;
                int c = wins[v] + remains[v] - wins[i];
                if (c < 0) {
                    isEliminated[v] = true;
                    coes.get(v).add(i);
                } else {
                    fn.addEdge(new FlowEdge(matchVers + i + 1, tVer, c));
                }

            }
            FordFulkerson ff = new FordFulkerson(fn, sVer, tVer);
            if (!isEliminated[v] && ff.value() < totalR) {
                isEliminated[v] = true;
                for (int i = 0; i < teamSize; i++) {
                    if (i == v)
                        continue;
                    if (ff.inCut(matchVers + i + 1)) {
                        coes.get(v).add(i);
                    }
                }
            }
        }

    }

    // number of teams
    public int numberOfTeams() {
        return teamSize;
    }

    // all teams
    public Iterable<String> teams() {
        return Arrays.asList(teamNames);
    }

    // number of wins for given team
    public int wins(String team) {
        if (team == null || team.length() == 0)
            throw new IllegalArgumentException();
        Integer id = nameToId.get(team);
        if (id == null)
            throw new IllegalArgumentException();
        return wins[id];
    }

    // number of losses for given team
    public int losses(String team) {
        if (team == null || team.length() == 0)
            throw new IllegalArgumentException();
        Integer id = nameToId.get(team);
        if (id == null)
            throw new IllegalArgumentException();
        return losses[id];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (team == null || team.length() == 0)
            throw new IllegalArgumentException();
        Integer id = nameToId.get(team);
        if (id == null)
            throw new IllegalArgumentException();
        return remains[id];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (team1 == null || team2 == null)
            throw new IllegalArgumentException();
        Integer id1 = nameToId.get(team1);
        Integer id2 = nameToId.get(team2);
        if (id1 == null || id2 == null)
            throw new IllegalArgumentException();
        return pairRemains[id1][id2];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (team == null || team.length() == 0)
            throw new IllegalArgumentException();
        Integer id = nameToId.get(team);
        if (id == null)
            throw new IllegalArgumentException();
        return isEliminated[id];

    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (team == null || team.length() == 0)
            throw new IllegalArgumentException();
        Integer id = nameToId.get(team);
        if (id == null)
            throw new IllegalArgumentException();
        ArrayList<String> res = new ArrayList<>();
        for (int i : coes.get(id)) {
            res.add(teamNames[i]);
        }
        if (res.isEmpty()) return null;
        return res;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

}
