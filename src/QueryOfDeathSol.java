import java.util.Arrays;

public class QueryOfDeathSol {
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

    boolean removeNodes() {
        int partSize = 1 + (n - 1) / nodes;
        while (partSize * (nodes - 1) >= n) {
            nodes--;
        }
        return partLen == 0;
    }

    void solve() {
        n = (int) query_of_death.GetLength();
        boolean[] alive = new boolean[nodes];
        Arrays.fill(alive, true);
        alive[0] = false;
        int res = 0;
        int from = 0, to = n;
        if (iAmRoot()) {
            while (from < to) {
                int cntAlive = 0;
                for (boolean z : alive) {
                    cntAlive += z ? 1 : 0;
                }
                int partSize = 1 + (to - from) / cntAlive;
                int it = 0;
                for (int i = 0; i < nodes; i++) {
                    if (alive[i]) {
                        putInt(i, from + it * partSize);
                        putInt(i, Math.min(to, from + (it + 1) * partSize));
                        sendMessage(i);
                        it++;
                    }
                }
                it = 0;
                int nextFr = -1, nextTo = -1;
                for (int i = 0; i < nodes; i++) {
                    if (alive[i]) {
                        receiveMessage(i);
                        int sumCh = readInt(i);
                        if (sumCh < 0) {
                            alive[i] = false;
                            nextFr = from + it * partSize;
                            nextTo = Math.min(to, from + (it + 1) * partSize);
                        } else {
                            res += sumCh;
                        }
                        it++;
                    }
                }
                from = nextFr;
                to = nextTo;
            }
            for (int i = 0; i < nodes; i++) {
                if (alive[i]) {
                    putInt(i, -1);
                    putInt(i, -1);
                    sendMessage(i);
                }
            }
            println(res);
        } else {
            while (true) {
                receiveMessage(rootNode);
                int left = readInt(rootNode);
                int right = readInt(rootNode);
                if (left == -1 && right == -1) {
                    break;
                }
                int s = 0;
                for (int i = left; i < right; i++) {
                    s += query_of_death.GetValue(i);
                }
                if (right - left > 1) {
                  int v = query_of_death.GetValue(left);
                  for (int it = 0; it < 30; it++) {
                      if (query_of_death.GetValue(left) != v) {
                          s = -1;
                      }
                  }
                }
                putInt(rootNode, s);
                sendMessage(rootNode);
                if (s == -1) {
                    break;
                }
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
        new QueryOfDeathSol().start();
    }
}
