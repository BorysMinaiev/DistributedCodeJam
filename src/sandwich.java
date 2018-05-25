import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class sandwich {
    static int n;
    static long[] a;

    static {
        try {
            Scanner sc = new Scanner(new File("test.in"));
            n = sc.nextInt();
            a = new long[n];
            for (int i = 0; i < n; i++) {
                a[i] = sc.nextLong();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public sandwich() {

    }

    public static long GetN() {
        return n;
    }

    public static long GetTaste(long index) {
        return a[(int) index];
    }
}