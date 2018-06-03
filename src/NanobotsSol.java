import java.util.Random;

public class NanobotsSol {
    final boolean debug = false;

    // [partFrom .. partTo)
    int partFrom, partTo;
    int partLen;
    int n, m;

    void initPart() {
        int partSize = 1 + (n - 1) / nodes;
        partFrom = Math.min(n, partSize * myId);
        partTo = Math.min(n, partSize * (myId + 1));
        partLen = partTo - partFrom;
    }

    void removeNodes() {
        int partSize = 1 + (n - 1) / nodes;
        while (partSize * (nodes - 1) >= n) {
            nodes--;
        }
    }

    final int mod = (int) 1e9 + 7;
    int add(int x, int y) {
        x += y;
        return x >= mod ? (x - mod) : x;
    }

    int mul(int x, int y) {
        return (int)((x*1L*y)% mod);
    }

    boolean inside(long x, long y) {
        if (x >= size || y >= size) {
            return false;
        }
        if (x< 0 || y < 0) {
            return true;
        }
        return nanobots.Experiment(x + 1, y + 1) == 'T';
    }

    long getH(long x) {
        long left = -1, right = size;
        while (right - left > 1) {
            long mid = (left + right) >> 1;
            if (inside(x, mid)) {
                left = mid;
            } else {
                right = mid;
            }
        }
        return right;
    }

    long size;
    // [from, to)
    int solve(long from, long to) {
        int res = 0;
        while (true) {
            if (to <= from) {
                return res;
            }
            long h = getH(from);
            if (h == 0) {
                return res;
            }
            long left = from, right = to;
            while (right - left > 1) {
                long mid = (left + right) >> 1;
                if (inside(mid, h - 1)) {
                    left = mid;
                } else {
                    right = mid;
                }
            }
            res = add(res, mul((int)((right - from) % mod), (int) (h % mod)));
            from = right;
        }
    }

    void solve() {
        size = nanobots.GetRange();
        long parts = Math.min(size, nodes * 346782L);
        long partLen = 1 + (size - 1) / parts;
        Random rnd = new Random(123);
        int res = 0;
        for (int i = 0; i < parts; i++) {
            if (rnd.nextInt(nodes) == myId) {
                res = add(res, solve(partLen * i, Math.min(size, partLen * (i + 1))));
            }
        }
        res = (int) (sumLongs(res) % mod);
        if (iAmRoot()) {
            println(res);
        }
    }

    void putInt(int to, int value) {
        if (debug) {
            System.err.println("send int " + value + " to " + to);
        }
        message.PutInt(to, value);
    }

    void putLong(int to, long value) {
        if (debug) {
            System.err.println("send long " + value + " to " + to);
        }
        message.PutLL(to, value);
    }

    void sendMessage(int to) {
        if (debug) {
            System.err.println("send message to " + to);
        }
        message.Send(to);
    }

    int readInt(int from) {
        if (debug) {
            System.err.println("receive int from " + from);
        }
        return message.GetInt(from);
    }

    long readLong(int from) {
        if (debug) {
            System.err.println("receive long from " + from);
        }
        return message.GetLL(from);
    }

    void receiveMessage(int from) {
        if (debug) {
            System.err.println("receive message from " + from);
        }
        message.Receive(from);
    }

    void println(String s) {
        System.out.println(s);
    }

    void println(long val) {
        System.out.println(val);
    }

    int[] joinIntArray(int value) {
        putInt(rootNode, value);
        sendMessage(rootNode);
        if (iAmRoot()) {
            int[] res = new int[nodes];
            for (int i = 0; i < nodes; i++ ){
                receiveMessage(i);
                res[i] = readInt(i);
            }
            return res;
        } else {
            return null;
        }
    }

    long[] joinLongArray(long value) {
        putLong(rootNode, value);
        sendMessage(rootNode);
        if (iAmRoot()) {
            long[] res = new long[nodes];
            for (int i = 0; i < nodes; i++ ){
                receiveMessage(i);
                res[i] = readLong(i);
            }
            return res;
        } else {
            return null;
        }
    }

    long sumLongs(long value) {
        putLong(rootNode, value);
        sendMessage(rootNode);
        if (iAmRoot()) {
            long res = 0;
            for (int i = 0; i < nodes; i++ ){
                receiveMessage(i);
                res += readLong(i);
            }
            return res;
        } else {
            return 0;
        }
    }

    boolean iAmRoot() {
        return myId == rootNode;
    }
    int myId;
    int nodes;
    int rootNode = 0;

    void start() {
        myId = message.MyNodeId();
        nodes = message.NumberOfNodes();
        solve();
    }

    public static void main(String[] args) {
        new NanobotsSol().start();
    }
}
