import java.io.*;
import java.util.Random;

public class Gen {
    PrintWriter out;

    void solve() {
        Random rnd = new Random();
        int n = 1 + rnd.nextInt(10000);
        out.println(n);
        for (int i = 0; i < n; i++) {
            long x = 1 + rnd.nextInt((int) 10);
            out.print(x + " ");
        }
        out.println();
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