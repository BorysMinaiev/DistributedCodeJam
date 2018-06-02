public class FlagpolesSol {
    final boolean debug = false;

    // [partFrom .. partTo)
    int partFrom, partTo;
    int partLen;
    int n;

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

    void solve() {
        n = (int) flagpoles.GetNumFlagpoles() - 1;
        if (n == 0) {
            if (iAmRoot()) {
                println(1);
            }
            return;
        }
        initPart();
        if (partLen == 0) {
            return;
        }
        removeNodes();
        long[] diff = new long[partLen];
        long start =  flagpoles.GetHeight(partFrom);
        for (int i = 0; i < partLen; i++) {
            long cur = flagpoles.GetHeight(partFrom + i + 1);
            diff[i] = cur - start;
            start = cur;
        }
        int firstLen = 1;
        for (int i = 1; i < partLen; i++) {
            if (diff[i] == diff[0]) {
                firstLen = i + 1;
            } else {
                break;
            }
        }
        int lastLen = 1;
        for (int i = 1; i < partLen; i++) {
            if (diff[diff.length - 1] == diff[diff.length - i - 1]) {
                lastLen = i + 1;
            } else {
                break;
            }
        }
        int maxLen = 0;
        for (int i = 0; i < diff.length; ) {
            int j = i;
            while (j != diff.length && diff[i] == diff[j]) {
                j++;
            }
            maxLen = Math.max(maxLen, j - i);
            i =j;
        }
        putInt(rootNode, firstLen);
        putLong(rootNode, diff[0]);
        if (firstLen == partLen) {
            putInt(rootNode, 1);
        } else {
            putInt(rootNode, 0);
            putInt(rootNode, maxLen);
            putInt(rootNode, lastLen);
            putLong(rootNode, diff[diff.length - 1]);
        }
        sendMessage(rootNode);
        if (iAmRoot()) {
            for (int i = 0; i < nodes; i++) {
                receiveMessage(i);
                join(readInt(i), readLong(i));
                int all = readInt(i);
                if (all == 0) {
                    join(readInt(i), Integer.MAX_VALUE);
                    join(readInt(i), readLong(i));
                }
            }
            println(res + 1);
        }
    }

    int res = 0;
    long curValue = Long.MAX_VALUE;
    int curLen = 0;

    void join(int len, long value) {
        if (value == curValue) {
            curLen += len;
        } else {
            curValue = value;
            curLen = len;
        }
        res = Math.max(res, curLen);
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
        new FlagpolesSol().start();
    }
}
