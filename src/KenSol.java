import java.util.ArrayList;
import java.util.List;

public class KenSol {
    final boolean debug = false;

    // [partFrom .. partTo)
    int partFrom, partTo;
    int partLen;
    int n, m;

    void initPart() {
        int partSize = 1 + (n - 1) / nodes;
        partFrom = Math.min(n, partSize * myId);
        partTo = Math.min(n, partSize * (myId + 1));
        partLen = (partTo - partFrom);
    }

    boolean removeNodes() {
        int partSize = (int) (1 + (n - 1) / nodes);
        while (partSize * (nodes - 1) >= n) {
            nodes--;
        }
        return partLen == 0;
    }

    List<Integer> getAllDivs(int x) {
        List<Integer> res = new ArrayList<>();
        for (int i = 1; i * 1L * i <= x; i++) {
            if (x % i == 0) {
                res.add(i);
                if (i * i != x) {
                    res.add(x / i);
                }
            }
        }
        return res;
    }

    final int p = 239017;
    int[] pw;

    void solve() {
//        n = (int) kenneth.
//        initPart();
//        if (removeNodes()) {
//            return;
//        }
        int[] lengths = new int[nodes];
        for (int i = 0; i < nodes; i++) {
            lengths[i] = (int) kenneth.GetPieceLength(i);
        }
        int[] sumL = new int[nodes + 1];
        for (int i = 0; i < nodes; i++) {
            sumL[i + 1] = sumL[i] + lengths[i];
        }
        partFrom = sumL[myId];
        partTo = sumL[myId + 1];
        int n = sumL[nodes];
        partLen = partTo - partFrom;
        pw = new int[partLen + 2];
        pw[0] = 1;
        for (int i = 1; i < pw.length; i++) {
            pw[i] = mul(pw[i - 1], p);
        }
        int[] hashes = new int[partLen + 1];
        for (int i = 0; i < partLen; i++) {
            hashes[i + 1] = add(mul(hashes[i], p), kenneth.GetSignalCharacter(partFrom + i));
        }
//        System.err.println("hashes =" + Arrays.toString(hashes));
        List<Integer> allDis = getAllDivs(n);
        for (int strLen : allDis) {
            int h1 = calcStringHash(0, n - strLen, hashes);
            int h2 = calcStringHash(strLen, n, hashes);
//            System.err.println(strLen + " "+  h1 + " "+  h2);
            putInt(rootNode, h1);
            putInt(rootNode, h2);
        }
        sendMessage(rootNode);
        if (iAmRoot()) {
            int[] h1 = new int[allDis.size()];
            int[] h2 = new int[allDis.size()];
            for (int i = 0; i < nodes; i++) {
                receiveMessage(i);
                for (int j = 0; j < h1.length; j++) {
                    h1[j] = add(h1[j], readInt(i));
                    h2[j] = add(h2[j], readInt(i));
                }
            }
//            System.err.println(Arrays.toString(h1));
//            System.err.println(Arrays.toString(h2));
            int res = n;
            for (int i = 0; i < h1.length; i++) {
                if (h1[i] == h2[i] && allDis.get(i) < res) {
                    res = allDis.get(i);
                }
            }
            println(res);
        }
    }

    int calcMyHash(int[] myHash, int from, int to) {
        return sub(myHash[to], mul(myHash[from], pw[to - from]));
    }

    int calcStringHash(int segFrom, int segTo, int[] myHashes) {
        int fr = Math.max(segFrom, partFrom);
        int to = Math.min(segTo, partTo);
//        System.err.println(fr + " ---> " + to);
        if (to <= fr) {
            return 0;
        }
        int myHash = calcMyHash(myHashes, fr - partFrom, to - partFrom);
//        System.err.println("mHash = " + myHash);
        return mul(pow(p, segTo - to), myHash);
    }

    int pow(int x, int y) {
        if (y == 0) {
            return 1;
        }
        int m = pow(x, y / 2);
        m = mul(m, m);
        if (y % 2 == 1) {
            m = mul(m, x);
        }
        return m;
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
            for (int i = 0; i < nodes; i++) {
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
            for (int i = 0; i < nodes; i++) {
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
            for (int i = 0; i < nodes; i++) {
                receiveMessage(i);
                res += readLong(i);
            }
            return res;
        } else {
            return 0;
        }
    }

    final int mod = (int) 1e9 + 7;

    int add(int x, int y) {
        x += y;
        return x >= mod ? (x - mod) : x;
    }

    int sub(int x, int y) {
        x -= y;
        return x < 0 ? (x + mod) : x;
    }

    int mul(int x, int y) {
        return (int) (x * 1L * y % mod);
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
        new KenSol().start();
    }
}
