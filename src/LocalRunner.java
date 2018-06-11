import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class LocalRunner {

    String run(int nodes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PrintStream ps = new PrintStream(baos, true, "UTF-8");
            System.setOut(ps);

            Thread[] threads = new Thread[nodes];
            SolutionRunner[] runners = new SolutionRunner[nodes];
            message.init(nodes);
            for (int i = 0; i < runners.length; i++) {
                runners[i] = new SolutionRunner();
                threads[i] = new Thread(runners[i]);
                threads[i].setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        e.printStackTrace();
                    }
                });
                message.registerThread(threads[i].getId(), i);
            }
            for (int i = 0; i < nodes; i++) {
                threads[i].start();
            }
            for (int i = 0; i < nodes; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return new String(baos.toByteArray(), StandardCharsets.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    Random rnd = new Random(123);

    long[] genRandomArray(int len, long maxV) {
        long[] res = new long[len];
        for (int i = 0; i < len; i++) {
            res[i] = 1 + Math.abs(rnd.nextLong() % maxV);
        }
        return res;
    }

    long[] genRandomArrayEven(int len, long maxV, boolean even) {
        long[] res = new long[len];
        for (int i = 0; i < len; i++) {
            res[i] = 1 + Math.abs(rnd.nextLong() % maxV);
            if (even && res[i] % 2 == 1) {
                res[i]++;
            }
            if (!even && res[i] % 2 == 0) {
                res[i]++;
            }
        }
        return res;
    }

    final int MAX_N = 100;
    final int MAX_V = 30;
    final int MAX_NODES = 10;

    void printArray(int[][]r) {
        System.err.println("{");
        for (int[] x : r) {
            System.err.print("{");
            for (int y : x) {
                System.err.print(y + ", ");
            }
            System.err.println("},");
        }
        System.err.println("}");
    }

    void start() {
        for (int it =0 ; it < 123123; it++) {
            System.err.println("iter = " + it);
            int n = 1 + rnd.nextInt(MAX_N);
            int m = 1 + rnd.nextInt(MAX_N);
            List<Integer> all = new ArrayList<Integer>();
            for (int i = 1; i <= n *m; i++) {
                all.add(i);
            }
            Collections.shuffle(all);
            int[][] r = new int[n][m];
            int itt = 0;
            for (int i = 0; i <n ; i++) {
                for (int j = 0; j < m; j++) {
                   r[i][j] = all.get(itt++);
                }
            }
            towels.before =rnd.nextInt(n * m);
            towels.r =r;
            int nodes = 1 + rnd.nextInt(MAX_NODES);
            String ans = run(nodes);
            String ans2 = run(1 + rnd.nextInt(MAX_NODES));
//            String ans2 = run(1 );
            if (ans.length() == 0) {
                System.err.println("Ans = " + ans + ", ans2 = " + ans2);
                System.err.println("r = " + Arrays.deepToString(r));
                printArray(r);
                System.err.println("before = " + towels.before + ", nodes = " + nodes);
                throw new AssertionError();
            }
            if (!ans.equals(ans2)) {
                System.err.println("Ans = " + ans + ", ans2 = " + ans2);
                System.err.println("r = " + Arrays.deepToString(r));
                printArray(r);
                System.err.println("before = " + towels.before);
                throw new AssertionError();
            } else {
                System.err.print("ok, ans = " + ans);
            }
        }
    }

    class SolutionRunner implements Runnable{

        @Override
        public void run() {
            new Main().start();
        }
    }

    public static void main(String[] args) {
        new LocalRunner().start();
    }
}
