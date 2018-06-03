// Sample input 3, in Java.
public class pancakes {
    public pancakes() {
    }

    public static long GetStackSize() {
        return 7L;
    }

    public static long GetNumDiners() {
        return 5L;
    }

    public static long GetStackItem(long i) {
        switch ((int)i) {
            case 0: return 0L;
            case 1: return 1L;
            case 2: return 3L;
            case 3: return 2L;
            case 4: return 1L;
            case 5: return 3L;
            case 6: return 0L;
            default: throw new IllegalArgumentException("Invalid argument");
        }
    }
}