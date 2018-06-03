import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// Sample input 3, in Java.
public class median {
    static int n, m;
    static long[] a =new long[]{5,9,4,8,11,7,10,6,1,2,3}
            , b;

//    static {
//        try {
//            Scanner sc = new Scanner(new File("test.in"));
//            n = sc.nextInt();
//            a = new long[n];
//            for (int i = 0; i < n; i++) {
//                a[i] = sc.nextLong();
//            }
//            m = sc.nextInt();
//            b = new long[m];
//            for (int i = 0; i < m; i++) {
//                b[i] = sc.nextLong();
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
    public median() {
    }

    public static int GetN() {
        return a.length;
    }

    public static int GetData(long i) {
        if (i < 0L || i > 1000000000000000000L)
            throw new IllegalArgumentException("Invalid argument");
        return (int) a[(int) ((i + b[message.MyNodeId()]) % a.length)];
//        return (int) a[(int)((i + b[message.MyNodeId() % b.length]) % n)];
    }
}
