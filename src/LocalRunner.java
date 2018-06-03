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

    long[] genRandomArray(int len, int maxV) {
        long[] res = new long[len];
        for (int i = 0; i < len; i++) {
            res[i] = 1 + rnd.nextInt(maxV);
            if (rnd.nextBoolean()) {
                res[i] = 787788;
            }
        }
        return res;
    }

    final int MAX_LEN = 1000;
    final int MAX_V = 10000;
    final int MAX_NODES = 30;

    void start() {
        for (int it =0 ; it < 123123; it++) {
            System.err.println("iter = " + it);
            median.a = genRandomArray(1 + rnd.nextInt(MAX_LEN), 1 + rnd.nextInt(MAX_V));
            median.b = genRandomArray(MAX_NODES + 1, MAX_LEN);
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
