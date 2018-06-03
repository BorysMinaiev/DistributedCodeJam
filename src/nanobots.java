// Sample input 3, in Java.
public class nanobots {
    public nanobots() {
    }

    static long range = 4;

    public static long GetRange() {
        return range;
    }

    public static long GetNumNanobots() {
        return sizes.length;
    }

    static long sizes[] = {2,1,1,1};
    static long speeds[] = {1,2,1,1};

    public static char Experiment(long size, long speed) {
        if (size < 1 || size > GetRange() || speed < 1 || speed > GetRange())
            throw new IllegalArgumentException("Invalid argument");
        for (int i = 0; i < sizes.length; ++i)
            if (sizes[i] > size && speeds[i] > speed) return 'T';
        return 'E';
    }
}
