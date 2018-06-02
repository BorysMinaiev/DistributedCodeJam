import java.util.Arrays;
import java.util.Random;

public class Test {
    public static void main(String[] args) {
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
}
