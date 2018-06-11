import java.util.Arrays;

public class TowelsSol {
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

    int[] data;

    int get(int stack, int pos) {
        return data[stack * m + pos];
    }

    final int PARTS = 1013;

//    final int PARTS = 5;

    void solve() {
        n = (int) towels.GetNumberOfStacks();
        initPart();
        if (removeNodes()) {
            return;
        }
        m = (int) towels.GetNumberOfTowelsInStack();
//        if (nodes == 1) {
//            if (iAmRoot()) {
//                int[] pos =new int[n];
//                Arrays.fill(pos, m - 1);
//                int need = (int) towels.GetNumberOfGymUsersBeforeYou();
//                int bestId = -1, bestValue = Integer.MAX_VALUE;
//                for (int i =0; i < n; i++) {
//                    if (pos[i] == -1) {
//                        continue;
//                    }
//                    int val = (int) towels.GetTowelCleanlinessRank(i, pos[i]);
//                    if (val < bestValue) {
//                        bestId =i;
//                        bestValue = val;
//                    }
//                }
//                need--;
//                if (need < 0) {
//                    println(bestValue);
//                    return;
//                }
//                pos[bestId]--;
//            } else {
//                return;
//            }
//        }
        data = new int[partLen * m];
        for (int i = 0; i < partLen; i++) {
            for (int j = 0; j < m; j++) {
                data[i * m + j] = (int) towels.GetTowelCleanlinessRank(partFrom + i, j);
            }
        }
        int before = (int) towels.GetNumberOfGymUsersBeforeYou();
        int left = 0, right = n * m + 2;
        int curSum = -1;
        for (int it = 0; it < 3; it++) {
            int[] cnt = new int[PARTS + 1];
            int size = 1 + (right - left) / PARTS;
//            System.err.println("size = " + size  + ", fr =" + left + ", to = " + right);
            for (int i = 0; i < partLen; i++) {
                int curPartSize = -1;
                for (int j = m - 1; j >=0 ;j--) {
                    int value = get(i, j);
                    int partId = (value < left ? -1 : (value - left) / size);
                    if (partId >= PARTS) {
                        break;
                    }
                    curPartSize = Math.max(curPartSize, partId);
                    cnt[curPartSize + 1]++;
                }
            }
            int s = 0;
            for (int i = 0; i < PARTS + 1; i++) {
                s += cnt[i];
                putInt(rootNode, s);
            }
            sendMessage(rootNode);
            if (iAmRoot()) {
                Arrays.fill(cnt, 0);
                for (int i = 0; i < nodes; i++) {
                    receiveMessage(i);
                    for (int j = 0; j < PARTS + 1; j++) {
                        cnt[j] += readInt(i);
                    }
                }
//                System.err.println(Arrays.toString(cnt));
                int nextLeft = left, nextRight = left + size, nextSum = 0;
                s = 0;
                for (int i = 0; i < PARTS + 1; i++) {
                    s = cnt[i];
                    if (s <= before) {
                        nextLeft = left + i * size - 1;
                        nextRight = left + (i + 1) * size - 1;
                        nextSum = s;
                    }
                }
//                System.err.println("next = " + nextLeft + " " + nextRight);
                for (int i = 0; i < nodes; i++) {
                    putInt(i, nextLeft);
                    putInt(i, nextRight);
                    putInt(i, nextSum);
                    sendMessage(i);
                }
            }
            receiveMessage(rootNode);
            left = readInt(rootNode);
            right = readInt(rootNode);
            curSum = readInt(rootNode);
        }
        if (right - left > 1) {
            throw new AssertionError();
        }
        int needMore = before - curSum + 1;
        int expectValue = right;
//        System.err.println("expect " + expectValue + ", needMore = " + needMore);
        int res = -1;
        for (int i = 0; i < partLen; i++) {
            boolean found = false;
            for (int j = m - 1; j >=0 ;j--) {
                int value = get(i, j);
                if (value > expectValue) {
                    break;
                }
                if (value == expectValue) {
                    found = true;
                }
                if (found) {
                    needMore--;
                    if (needMore == 0) {
                        res = value;
                        break;
                    }
                }
            }
        }
        if (res != -1) {
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
        new TowelsSol().start();
    }
}
