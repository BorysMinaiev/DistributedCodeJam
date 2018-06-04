// Sample input 3, in Java.
public class todd_and_steven {
    static long[] a = {15, 23};
    static long[] b = {4, 8, 16, 42};

    public todd_and_steven() {
    }

    public static long GetToddLength() {
        return a.length;
    }

    public static long GetStevenLength() {
        return b.length;
    }

    public static long GetToddValue(long i) {
        return a[(int) i];
    }

    public static long GetStevenValue(long i) {
        return b[(int) i];
    }
}