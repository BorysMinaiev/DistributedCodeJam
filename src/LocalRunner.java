import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

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

    final int MAX_N = 1000;
    final long MAX_V = 1000;
    final int MAX_NODES = 10;

    void start() {
        for (int it =0 ; it < 123123; it++) {
            System.err.println("iter = " + it);
            todd_and_steven.a = genRandomArrayEven(1 + rnd.nextInt(MAX_N), MAX_V,  false);
            todd_and_steven.b =genRandomArrayEven(1 + rnd.nextInt(MAX_N), MAX_V, true);
            Arrays.sort(todd_and_steven.a);
            Arrays.sort(todd_and_steven.b);
            String ans = run(1 + rnd.nextInt(MAX_NODES));
            String ans2 = run(1 + rnd.nextInt(MAX_NODES));
            if (!ans.equals(ans2)) {
                System.err.println(Arrays.toString(todd_and_steven.a));
                System.err.println(Arrays.toString(todd_and_steven.b));
                System.err.println("Ans = " + ans + ", ans2 = " + ans2);
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
