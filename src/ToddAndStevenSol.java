public class ToddAndStevenSol {
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

    int cntOdd, cntEven;

    long getEven(int pos) {
        if (pos >= cntEven) {
            return Long.MAX_VALUE;
        }
        if (pos < 0) {
            return -1;
        }
        return todd_and_steven.GetStevenValue(pos);
    }


    long getOdd(int pos) {
        if (pos >= cntOdd) {
            return Long.MAX_VALUE;
        }
        if (pos < 0) {
            return -1;
        }
        return todd_and_steven.GetToddValue(pos);
    }

    void solve() {
        n = (int) (todd_and_steven.GetToddLength() + todd_and_steven.GetStevenLength());
        initPart();
        if (removeNodes()) {
            return;
        }
        cntOdd = (int) todd_and_steven.GetToddLength();
        cntEven = (int) todd_and_steven.GetStevenLength();
        int left = -1, right = Math.min(partFrom, cntOdd);
        while (right - left > 1) {
            int mid = left + ((right - left) >> 1);
            if (getOdd(mid) > getEven(partFrom - mid - 1)) {
                right = mid;
            } else {
                left =  mid;
            }
        }
        int oddIter = right;
        int evenIter = partFrom - right;
        long oddCur = getOdd(oddIter);
        long evenCur = getEven(evenIter);
        int res = 0;
        for (int i = partFrom; i < partTo; i++) {
            if (oddCur < evenCur) {
                res = add(res, (int) ((oddCur ^ i) % mod));
                oddIter++;
                oddCur = getOdd(oddIter);
            } else {
                res = add(res, (int) ((evenCur ^ i) % mod));
                evenIter++;
                evenCur = getEven(evenIter);
            }
        }
//        System.err.println(myId + " " + partFrom + " " + partTo + " " + res);
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
        new ToddAndStevenSol().start();
    }
}
