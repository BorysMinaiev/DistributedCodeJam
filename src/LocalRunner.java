import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
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

    final long MAX_V = (long) 10;
    final int MAX_CNT = 1000;
    final int MAX_NODES = 10;

    void start() {
        for (int it =0 ; it < 123123; it++) {
            System.err.println("iter = " + it);
            nanobots.range = 1 + Math.abs(rnd.nextLong() % MAX_V);
            int cnt = 1 + rnd.nextInt(MAX_CNT);
            nanobots.sizes = genRandomArray(cnt, nanobots.range);
            nanobots.speeds = genRandomArray(cnt, nanobots.range);
            String ans = run(1 + rnd.nextInt(MAX_NODES));
            String ans2 = run(1 + rnd.nextInt(MAX_NODES));
            if (!ans.equals(ans2)) {
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
