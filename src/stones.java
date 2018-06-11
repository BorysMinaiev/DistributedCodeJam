// Sample input 3, in Java.
public class stones {
    public stones() {
    }

    static long[] go;

    public static long GetNumberOfStones() {
        return go.length - 1;
    }

    public static long GetJumpLength(long stone) {
        return go[(int) stone];
    }
}