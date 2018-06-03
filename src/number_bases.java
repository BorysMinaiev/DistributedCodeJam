// Sample input 3, in Java.
public class number_bases {
    public number_bases() {
    }

    public static long GetLength() {
        return 4L;
    }

    public static long GetDigitX(long i) {
        switch ((int)i) {
            case 0: return 5L;
            case 1: return 3L;
            case 2: return 0L;
            case 3: return 0L;
            default: throw new IllegalArgumentException("Invalid argument");
        }
    }

    public static long GetDigitY(long i) {
        switch ((int)i) {
            case 0: return 5L;
            case 1: return 3L;
            case 2: return 0L;
            case 3: return 0L;
            default: throw new IllegalArgumentException("Invalid argument");
        }
    }

    public static long GetDigitZ(long i) {
        switch ((int)i) {
            case 0: return 0L;
            case 1: return 6L;
            case 2: return 0L;
            case 3: return 0L;
            default: throw new IllegalArgumentException("Invalid argument");
        }
    }
}