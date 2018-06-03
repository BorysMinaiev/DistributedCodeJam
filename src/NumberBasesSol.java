public class NumberBasesSol {
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
        n = (int) number_bases.GetLength();
        initPart();
        if (removeNodes()) {
            return;
        }
        int[] a =new int[partLen];
        int[] b =new int[partLen];
        int[] c = new int[partLen];
        int max = 0;
        int check = -1;
        for (int i = 0; i < partLen; i++) {
            a[i] = (int) number_bases.GetDigitX(i + partFrom);
            b[i] = (int) number_bases.GetDigitY(i + partFrom);
            c[i] = (int) number_bases.GetDigitZ(i + partFrom);
            max = Math.max(max, a[i]);
            max = Math.max(max, b[i]);
            max = Math.max(max, c[i]);
            if (a[i] + b[i] != c[i] && check == -1) {
                check = a[i] + b[i] - c[i];
                if (check < 0) {
                    check = -2;
                }
            }
        }
        putInt(rootNode, max);
        putInt(rootNode, check);
        sendMessage(rootNode);
        if (iAmRoot()) {
            check = -1;
            for (int i = 0; i < nodes; i++) {
                receiveMessage(i);
                max = Math.max(max, readInt(i));
                int nextCheck = readInt(i);
                if (nextCheck != -1 && check == -1) {
                    check = nextCheck;
                }
            }
            if (check <= max && check != -1) {
                check = -2;
            }
            for (int i = 0; i < nodes; i++) {
                putInt(i, check);
                sendMessage(i);
            }
        }
        receiveMessage(rootNode);
        check = readInt(rootNode);
        if (check < 0) {
            if (iAmRoot()) {
                println(check == -1 ? "NON-UNIQUE" : "IMPOSSIBLE");
            }
            return;
        }
        int more0 = 0, more1 = 1;
        for (int i = 0; i < partLen; i++) {
            more0 = check(more0, a[i], b[i], c[i], check);
            more1 = check(more1, a[i], b[i], c[i], check);
        }
        putInt(rootNode, more0);
        putInt(rootNode, more1);
        sendMessage(rootNode);
        if (iAmRoot()) {
            int cur = 0;
            for (int i = 0; i < nodes; i++) {
                receiveMessage(i);
                int ne0 = readInt(i);
                int ne1 = readInt(i);
                if (cur == 0 ) {
                    cur = ne0;
                } else if (cur == 1) {
                    cur = ne1;
                }
            }
            if (cur == 0) {
                println(check);
            } else {
                println("IMPOSSIBLE");
            }
        }
    }

    int check(int plus, int a, int b, int c, int check) {
        if (plus == -1) {
            return -1;
        }
        int go = a + b + plus;
        if (go % check != c) {
            return -1;
        }
        return go / check;
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
        new NumberBasesSol().start();
    }
}
