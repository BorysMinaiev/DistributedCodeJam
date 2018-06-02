import java.util.*;

public class Main {
    final boolean debug = false;

    // [partFrom .. partTo)
    int partFrom, partTo;
    int partLen;
    int n;

    long getMult(Random rnd) {
        long res = rnd.nextLong();
        return res % 2 == 0 ? (res + 1) : res;
    }

    class BinarySearch {
        int nodeTo;
        int from, to;
        long[] s;

        public BinarySearch(int nodeTo, int from, int to, long[] s) {
            this.nodeTo = nodeTo;
            this.from = from;
            this.to = to;
            this.s = s;
        }

        void ask() {
            if (to - from > 1) {
                int mid = (to + from) >> 1;
                putLong(nodeTo, s[mid + 1] - s[from + 1]);
                sendMessage(nodeTo);
            }
        }

        void got() {
            if (to - from > 1) {
                int mid = (to + from) >> 1;
                receiveMessage(nodeTo);
                if (s[mid + 1] - s[from + 1] != readLong(nodeTo)) {
                    to = mid;
                } else {
                    from = mid;
                }
            }
        }

        void reset(List<Integer> answer, int n) {
            answer.add(to);
            from = to;
            to = n - 1;
        }
    }

    void solve() {
        n = (int) broken_memory.GetLength();
        Random rnd = new Random(123);
        long[] prefSum = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefSum[i + 1] = prefSum[i] + getMult(rnd) * broken_memory.GetValue(i);
        }
        int leftNode = (myId -1 + nodes) % nodes;
        int rightNode = (myId + 1) % nodes;
        BinarySearch leftBS = new BinarySearch(leftNode, -1, n - 1, prefSum);
        BinarySearch rightBS = new BinarySearch(rightNode, -1, n - 1, prefSum);
        List<Integer> broken = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int it = 0; it < 30; it++) {
                leftBS.ask();
                rightBS.ask();
                leftBS.got();
                rightBS.got();
            }
            leftBS.reset(broken, n);
            rightBS.reset(broken, n);
        }
        int brokenPos = -1;
        Collections.sort(broken);
        for (int i = 0; i + 1 < broken.size(); i++) {
            if (broken.get(i).equals(broken.get(i + 1))) {
                brokenPos = broken.get(i);
            }
        }
        if (brokenPos == -1) {
            throw new AssertionError();
        }
        int[] res = joinIntArray(brokenPos);
        if (iAmRoot()) {
            for (int i : res) {
                System.out.print(i + " ");
            }
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
        new Main().start();
    }
}
