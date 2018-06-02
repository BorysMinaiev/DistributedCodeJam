import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// Sample input 3, in Java.
public class baby_blocks {
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


    public baby_blocks() {
    }

    public static long GetNumberOfBlocks() {
        return n;
    }

    public static long GetBlockWeight(long i) {
        return a[(int) i];
    }
}