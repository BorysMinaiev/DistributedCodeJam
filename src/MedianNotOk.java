import java.util.*;

public class MedianNotOk {
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

    final int p = 239017;
    final int len = (1 << 6);
    int[] pow = new int[len + 1];

    class MiddleFinder {
        final int MAX = (int) 1e6;
        int[] values = new int[MAX];
        int minElement = Integer.MIN_VALUE, maxElement = Integer.MAX_VALUE;
        int cntMin = 0, cntMax = 0;
        final int SHIFT = 2000;
        int sz;
        int total;


        MiddleFinder() {

        }

        void addElement(int x) {
            total++;
            if (x <= minElement) {
                cntMin++;
                return;
            }
            if (x >= maxElement) {
                cntMax++;
                return;
            }
            if (sz < MAX) {
                values[sz++] = x;
            } else {
                shrink();
                addElement(x);
            }
        }

        void shrink() {
            if (sz <= SHIFT * 2) {
                return;
            }
            int sum = sz + cntMin + cntMax;
            Arrays.sort(values, 0, sz);
//            System.err.println("shrink! " + sz + " " + total + " " + cntMin + " " + cntMax + ", sum = " + sum + ",mid = " + values[midPosReal]);
            int midPos = sum / 2 - cntMin;
            int from = midPos - SHIFT;
            int to = midPos + SHIFT;
            if (from >= 0) {
                minElement = values[from];
            }
            if (to < sz) {
                maxElement = values[to];
            }
            int nsz = 0;
            for (int i = 0; i < sz; i++) {
                if (values[i] <= minElement) {
                    cntMin++;
                } else if (values[i] >= maxElement) {
                    cntMax++;
                } else {
                    values[nsz++] = values[i];
                }
            }
//            System.err.println(minElement + " " + maxElement);
            sz = nsz;
        }
    }
    MiddleFinder finder = new MiddleFinder();

    class Solver {
        long startPos;
        int curHash;
        int[] circle = new int[len];
        int circleIt;
        int totalLen;

        Solver(long startPos) {
            this.startPos = startPos;
            for (int i = 0; i < len; i++) {
                getNewValue(false);
            }
        }

        void getNewValue(boolean addToCounter) {
            int value = median.GetData(startPos + totalLen);
            addToCircle(value);
            if (addToCounter) {
                finder.addElement(value);
            }
        }

        void addToCircle(int value) {
            if (totalLen >= circle.length) {
                curHash -= pow[len - 1] * circle[circleIt];
            }
            totalLen++;
            circle[circleIt] = value;
            circleIt = (circleIt + 1) & (len - 1);
            curHash = curHash * p + value;
        }
    }

    void solve() {
        pow[0] = 1;
        for (int i = 1; i < pow.length; i++) {
            pow[i] = pow[i - 1] * p;
        }
        Random rnd = new Random(7877881 * (myId + 1));
        final int SOLVERS = 1;
        Solver[] solvers = new Solver[SOLVERS];
        HashSet<Integer> hashes = new HashSet<>();
        n = median.GetN();
        initPart();
        for (int i = 0; i < SOLVERS; i++) {
            solvers[i] = new Solver(partFrom);
            if (hashes.contains(solvers[i].curHash)) {
                solvers[i] = null;
            } else {
                hashes.add(solvers[i].curHash);
            }
        }
        for (int i = 0; i < nodes; i++) {
            if (i == myId) {
                continue;
            }
            putInt(i, hashes.size());
            for (int x : hashes) {
                putInt(i, x);
            }
            sendMessage(i);
        }
        HashSet<Integer> otherHashes = new HashSet<>();
        for (int i = 0; i < nodes; i++) {
            if (i == myId) {
                for (int j = 0; j < solvers.length; j++) {
                    if (solvers[j] != null && otherHashes.contains(solvers[j].curHash)) {
                        solvers[j] = null;
                    }
                }
            } else {
                receiveMessage(i);
                int sz = readInt(i);
                for (int j = 0; j < sz; j++) {
                    otherHashes.add(readInt(i));
                }
            }
        }
        hashes.addAll(otherHashes);
        int[] hashesArray = new int[hashes.size()];
        int it = 0;
        for (int i : hashes) {
            hashesArray[it++] = i;
        }
        Arrays.sort(hashesArray);
        for (Solver s : solvers) {
            if (s == null) {
                continue;
            }
            while (true) {
                s.getNewValue(true);
                if (Arrays.binarySearch(hashesArray, s.curHash) >= 0) {
                    break;
                }
                if (s.totalLen > 2e7) {
                    throw new AssertionError();
                }
            }
        }
        finder.shrink();
        putInt(rootNode, finder.minElement);
        putInt(rootNode, finder.cntMin);
        putInt(rootNode, finder.maxElement);
        putInt(rootNode, finder.cntMax);
        putInt(rootNode, finder.sz);
        for (int i = 0; i < finder.sz; i++) {
            putInt(rootNode, finder.values[i]);
        }
        sendMessage(rootNode);
        if (iAmRoot()) {
            List<Element> all = new ArrayList<>();
            for (int i = 0; i < nodes; i++) {
                receiveMessage(i);
                all.add(new Element(readInt(i), readInt(i)));
                all.add(new Element(readInt(i), readInt(i)));
                int cnt = readInt(i);
                for (int j = 0; j < cnt; j++) {
                    all.add(new Element(readInt(i), 1));
                }
            }
            Collections.sort(all);
            int totalCnt = 0;
            int rMin = Integer.MAX_VALUE, rMAx =Integer.MIN_VALUE;
            for (Element e : all) {
                if (e.cnt != 0) {
                    rMAx = Math.max(rMAx, e.value);
                    rMin =Math.min(rMin, e.value);
                }
                totalCnt += e.cnt;
            }
            if (median.GetN() % totalCnt != 0 && rMAx != rMin) {
                throw new AssertionError();
            }
//            System.err.println("totalCnt = " + totalCnt);
            int needPos = totalCnt / 2;
            for (Element e : all) {
                needPos -= e.cnt;
                if (needPos < 0) {
                    println(e.value);
                    return;
                }
            }
        }
    }

    class Element implements Comparable<Element> {
        int value, cnt;

        public Element(int value, int cnt) {
            this.value = value;
            this.cnt = cnt;
        }

        @Override
        public int compareTo(Element o) {
            return Integer.compare(value, o.value);
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
        new MedianNotOk().start();
    }
}
