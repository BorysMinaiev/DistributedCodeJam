import java.util.Arrays;

public class WeirdEditorSol {
    final boolean debug = false;

    // [partFrom .. partTo)
    int partFrom, partTo;
    int partLen;
    int n, m;

    void initPart() {
        int partSize = 1 + (n - 1) / nodes;
        partFrom = Math.min(n, partSize * myId);
        partTo = Math.min(n, partSize * (myId + 1));
        partLen = (int) (partTo - partFrom);
    }

    boolean removeNodes() {
        int partSize = (int) (1 + (n - 1) / nodes);
        while (partSize * (nodes - 1) >= n) {
            nodes--;
        }
        return partLen == 0;
    }

    final int mod = (int) 1e9 + 7;
    int add(int x, int y) {
        x += y;
        return x >= mod ? (x - mod) : x;
    }
    int mul(int x, int y) {
        return (int) (x * 1L * y % mod);
    }


    void add(int[] vals, int index, int cnt) {
        if (cnt == 0) {
            return;
        }
        for (int i = 0; i < index; i++) {
            vals[i] = 0;
        }
        vals[index] += cnt;
    }

    void solve() {
        n = (int) weird_editor.GetNumberLength();
        initPart();
        if (removeNodes()) {
            return;
        }
        int[] r = new int[10];
        for(int i = partFrom; i < partTo; i++) {
            add(r, (int) weird_editor.GetDigit(i),1);
        }
        for (int i = 0; i < 10; i++) {
            putInt(rootNode, r[9 - i]);
        }
        sendMessage(rootNode);
        if (iAmRoot()) {
            Arrays.fill(r, 0);
            for (int i = 0; i < nodes; i++) {
                receiveMessage(i);
                for (int j = 0; j < 10; j++) {
                    add(r, 9 - j, readInt(i));
                }
            }
            int res = 0;
            int zeros = n;
            for (int i = 9; i > 0; i--) {
                res = add(mul(res, pow(10, r[i])), mul(getOnes(r[i]), i));
                zeros -= r[i];
            }
            res = mul(res, pow(10, zeros));
            println(res);
        }
    }

    int getOnes(int len) {
        if (len == 0) {
            return 0;
        }
        if (len == 1) {
            return 1;
        }
        int r = getOnes(len / 2);
        r = add(r, mul(r, pow(10, len / 2)));
        if (len % 2 == 1) {
            r = add(mul(r, 10), 1);
        }
        return r;
    }

    int pow(int x, int y) {
        if (y == 0) {
            return 1;
        }
        int z = pow(x, y / 2);
        z = mul(z, z);
        if (y % 2 == 1) {
            z = mul(z, x);
        }
        return z;
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
        new WeirdEditorSol().start();
    }
}
