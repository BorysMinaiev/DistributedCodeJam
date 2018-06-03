import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// Sample input 1, in Java.
public class lemming {
    static int n, m;
    static char[][] a;

    static {
        try {
            Scanner sc = new Scanner(new File("test.in"));
            n = sc.nextInt();
            m =sc.nextInt();
            a = new char[n][];
            for (int i = 0; i < n; i++) {
                a[i] = sc.next().toCharArray();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public lemming() {
    }

    public static long GetRows() {
        return n;
    }

    public static long GetColumns() {
        return m;
    }

    public static char GetDirection(long i, long j) {
        return a[(int)i][(int)j];
    }
}