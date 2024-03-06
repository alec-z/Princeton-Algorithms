
public class CircularSuffixArray {
    private String s;
    private int len;
    private int[] index;
    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        this.s = s;
        this.len = s.length();
        if (this.len == 0) return;
        index = new int[len];
        for (int i = 0; i < len; i++) {
            index[i] = i;
        }
        int L = 1;
        int[] posToChar = new int[len];
        for (int i = 0; i < len; i++) posToChar[i] = this.s.charAt(i);
        int[] newIndex = new int[len];
        int[] counts = new int[Math.max(len + 1, 257)];
        coutingOrder(index, newIndex, posToChar, 256, counts);
        int[] tmpI = index;
        index = newIndex;
        newIndex = tmpI;

        int[] toClass = new int[len];
        int[] newToClass = new int[len];
        int classR = 0;
        toClass[index[0]] = 0;
        for (int i = 1; i < index.length; i++) {
            if (s.charAt(index[i]) != s.charAt(index[i - 1])) {
                classR ++;
            }
            toClass[index[i]] = classR;
        }
        classR++;
        while (L < len) {
            for (int i = 0; i < index.length; i++) {
                index[i] = (index[i] - L + len) % len;
            }
            coutingOrder(index, newIndex, toClass, classR, counts);
            tmpI = index;
            index = newIndex;
            newIndex = tmpI;

            classR = 0;
            newToClass[index[0]] = 0;
            for (int i = 1; i < index.length; i++) {
                if (toClass[index[i]] != toClass[index[i - 1]] || toClass[(index[i] + L) % len]  != toClass[(index[i - 1] + L) % len]) {
                    classR ++;
                }
                newToClass[index[i]] = classR;
            }
            classR++;
            int[] tmp = toClass;
            toClass = newToClass;
            newToClass = tmp;
            L = 2 * L;
        }
    }

    private void coutingOrder(int[] order, int[] aux, int[] toClass, int classR, int[] counts) {
        for (int i = 0; i < classR + 1; i++) counts[i] = 0;
        for (int i = 0; i < order.length; i++) {
            int classID = toClass[order[i]];
            counts[classID + 1]++;
        }
        for (int i = 1; i < classR; i++) {
            counts[i] += counts[i - 1]; 
        }

        for (int i = 0; i < len; i++) {
            int classID = toClass[order[i]];
            aux[counts[classID]++] = order[i];
        }
        return ;
    }

    // length of s
    public int length() {
        return len;

    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i >= this.len || i < 0) throw new IllegalArgumentException();

        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray solution = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < solution.length(); i++) {
            System.out.println(solution.index(i));
        }
    }

}
