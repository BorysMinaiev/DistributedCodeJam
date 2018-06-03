import java.io.*;
import java.util.Random;

public class Gen {
    PrintWriter out;

    void genArray() {
        Random rnd = new Random();
        int n = 1 + rnd.nextInt(10000);
        out.println(n);
        for (int i = 0; i < n; i++) {
            long x = 1 + rnd.nextInt((int) 10);
            out.print(x + " ");
        }
        out.println();
    }

    void genGrid() {
        Random rnd = new Random();
        final int MAX = 50000;
        int n = 1 + rnd.nextInt(20);
        int m = 1 + rnd.nextInt(MAX);
        out.println(n + " " + m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                char c = "<>^v".charAt(rnd.nextInt(4));
                out.print(c);
            }
            out.println();
        }
    }

    void solve() {
        // genArray();
        genGrid();
    }

    void run() {
        out = new PrintWriter(System.out);

        solve();

        out.close();
    }


    public static void main(String[] args) {
        new Gen().run();
    }
}