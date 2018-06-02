// Sample input 3, in Java.
public class broken_memory {
    public broken_memory() {
    }

    public static long GetLength() {
        return 16L;
    }

    public static long GetValue(long i) {
        if (i < 0L || i >= GetLength())
            throw new IllegalArgumentException("Invalid argument");
        if ((12L ^ message.MyNodeId()) == i)
            return ((i * i) ^ 21L ^ (i + message.MyNodeId() + 1)) + 1L;
        return ((i * i) ^ 21L) + 1L;
    }
}