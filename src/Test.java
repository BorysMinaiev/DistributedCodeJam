import java.util.*;

public class Test {
    void test1() {
        int n = (int) 1e9;
        int m = 100;
        int[] a = new int[m + 2];
        Random rnd = new Random();
        for (int i = 0; i < m; i++) {
            a[i + 1] = rnd.nextInt(n);
        }
        a[m + 1] = n;
        Arrays.sort(a);
        int max = 0;
        for (int i = 0; i + 1 < a.length; i++) {
            max = Math.max(max, a[i + 1] - a[i]);
        }
        System.err.println("max len = " + max);
    }

    static void test2() {
        Random rnd = new Random();
        int n = 100;
        int m = 346782;
        int[] cnt = new int[n];
        for (int i = 0; i < n * m; i++) {
            cnt[rnd.nextInt(n)]++;
        }
        int max = 0;
        for (int x : cnt) {
            max = Math.max(max, x);
        }
        System.err.println("max = " + max);
    }

    static void test3() {
        int n = (int) 1e9;
        int m = 100;
        int k = 100;
        int[] a = new int[m * k + 2];
        Random rnd = new Random();
        for (int i = 0; i < m * k; i++) {
            a[i + 1] = rnd.nextInt(n);
        }
        a[m + 1] = n;
        Arrays.sort(a);
        List<Integer> diffs = new ArrayList();
        for (int i = 0; i + 1 < a.length; i++) {
            diffs.add(a[i + 1] - a[i]);
        }
        Collections.shuffle(diffs);
        int max = 0;
        for (int i = 0; i < diffs.size(); i += k) {
            int s = 0;
            for (int j = i; j < diffs.size() && j < i + k; j++) {
                s += diffs.get(j);
            }
            max = Math.max(max, s);
        }
        System.err.println("max len = " + max);
    }

    public static void main(String[] args) {
        test3();
    }
}
