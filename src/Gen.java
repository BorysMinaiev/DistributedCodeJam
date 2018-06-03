import java.io.*;
import java.util.Random;

public class Gen {
    PrintWriter out;

    Random rnd = new Random();

    void genArrayDist(int MAXN, int vals) {
        vals = 1 + rnd.nextInt(vals);
        double[] p = new double[vals];
        int[] exactVals = new int[vals];
        for (int i = 0; i < vals; i++) {
            exactVals[i] = 1 + rnd.nextInt(12345);
        }
        double s = 0;
        for (int i = 0; i < p.length; i++) {
            p[i] = rnd.nextDouble();
            s += p[i];
        }
        for (int i = 0; i < p.length; i++) {
            p[i] /= s;
        }
        int n = 1 + rnd.nextInt(MAXN);
        out.println(n);
        for (int i = 0; i < n; i++) {
            double xx = rnd.nextDouble();
            for (int j = 0; j < vals; j++) {
                xx -= p[j];
                if (xx < 0){
                    out.print(exactVals[j]+" ");
                    break;
                }
            }
        }
        out.println();
    }

    void genArray(int MAXN, int MAXV) {
        int n = 1 + rnd.nextInt(MAXN);
        out.println(n);
        for (int i = 0; i < n; i++) {
            int value =  rnd.nextDouble() < 1e-4 ? 1 : 2;
            long x = 1 + value;
            out.print(x + " ");
        }
        out.println();
    }

    void genGrid() {
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
        genArrayDist(100000, 10);
//        genArray(10000, 1000000);
        genArray(1000, 1000);
//        genGrid();
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