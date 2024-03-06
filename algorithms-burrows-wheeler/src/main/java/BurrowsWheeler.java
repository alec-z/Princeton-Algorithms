import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output 
    public static void transform() {
        StringBuilder sb = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            sb.append(BinaryStdIn.readChar());
        }
        String str = sb.toString();
        CircularSuffixArray csa = new CircularSuffixArray(str);
        int len = csa.length();
        for (int i = 0; i < len; i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < len; i++) {
            int lastPos = (csa.index(i) + len - 1) % len;
            BinaryStdOut.write(str.charAt(lastPos));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int starter = BinaryStdIn.readInt();

        StringBuilder sb = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            sb.append(BinaryStdIn.readChar());
        }
        String btw = sb.toString();
        int len = btw.length();
        final int R = 256;
        int[] count = new int[R + 1];
        for (int i = 0; i < len; i++)
            count[btw.charAt(i) + 1] ++;

        for (int i = 1; i < R; i++) {
            count[i] += count[i - 1];
        }
        int[] lastToFirst = new int[len];
        for (int i = 0; i < len; i++) {
            lastToFirst[i] = count[btw.charAt(i)]++;
        }
        StringBuilder res = new StringBuilder();
        
        for (int i = starter, size = 0; size < len; size++, i = lastToFirst[i]) {
            res.append(btw.charAt(i));
        }
        
        for (int i = res.length() - 1; i >= 0; i--)
            BinaryStdOut.write(res.charAt(i));
        BinaryStdOut.close();


    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
       } else if (args[0].equals("+")) {
            inverseTransform();
        }
    }
}
