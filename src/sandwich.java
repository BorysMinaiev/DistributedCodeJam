// Sample input 1, in Java.
public class sandwich {
    public sandwich() {
    }

    public static long GetN() {
        return 7L;
    }

    public static long GetTaste(long index) {
        switch ((int)index) {
            case 0: return 10L;
            case 1: return -2L;
            case 2: return 5L;
            case 3: return -4L;
            case 4: return 3L;
            case 5: return -5L;
            case 6: return 1L;
            default: throw new IllegalArgumentException("Invalid argument");
        }
    }
}