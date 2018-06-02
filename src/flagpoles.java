import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// Sample input 3, in Java.
public class flagpoles {
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
    public flagpoles() {
    }

    public static long GetNumFlagpoles() {
        return n;
    }

    public static long GetHeight(long i) {
        return a[(int) i];
    }
}