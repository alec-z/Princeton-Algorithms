import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
    private static final int R = 256;

    public static void encode() {
        char[] seq = new char[R];
        for (int i = 0; i < R; i++) seq[i] = (char)i;
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int i = 0;
            for (; i < R; i++) {
                if (seq[i] == c) break;
            }
            BinaryStdOut.write((char)i);
            char tmp = seq[i];
            for (;i > 0; i--) {
                seq[i] = seq[i - 1];
            }
            seq[0] = tmp;
        }
        BinaryStdOut.close();

    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] seq = new char[R];
        for (int i = 0; i < R; i++) seq[i] = (char)i;
        while (!BinaryStdIn.isEmpty()) {
            char i = BinaryStdIn.readChar();
            BinaryStdOut.write(seq[i]);
            char tmp = seq[i];
            for (;i > 0; i--) {
                seq[i] = seq[i - 1];
            }
            seq[0] = tmp;
        }
        BinaryStdOut.close();

    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        }
    }
}
