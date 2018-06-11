public class StonesSol {
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

    int addToStack(int[] myPos, int[] cntMoves, int[] lastPos, int stackSize, int curMoves, int curNextPos, int curPos) {
        while (stackSize > 0 && (cntMoves[stackSize - 1] > curMoves || (cntMoves[stackSize - 1] == curMoves && lastPos[stackSize - 1] <= curNextPos))) {
            stackSize--;
        }
        cntMoves[stackSize] = curMoves;
        myPos[stackSize] = curPos;
        lastPos[stackSize++] = curNextPos;
        return stackSize;
    }

    int findBest(int maxPos, int[] myPos, int stackSize) {
        int right = stackSize;
        int left = -1;
        while (right - left > 1) {
            int mid = (left + right) >> 1;
            if (myPos[mid] <= maxPos) {
                right = mid;
            } else {
                left = mid;
            }
        }
        return right;
    }

    int movesNext = Integer.MAX_VALUE / 2;
    int rightPos = 0;
    int rightMaxPos = 0;

    void update(int pos, int cntMoves) {
        if (cntMoves < movesNext - 1) {
            movesNext = cntMoves;
            rightPos = 0;
            rightMaxPos = 0;
        } else if (cntMoves < movesNext) {
            movesNext = cntMoves;
            rightMaxPos = rightPos;
            rightPos = 0;
        }
        if (cntMoves == movesNext) {
            rightPos =Math.max(rightPos, pos);
        } else if(cntMoves == movesNext + 1) {
            rightMaxPos =Math.max(rightMaxPos, pos);
        }
    }

    void solve() {
        n = (int) stones.GetNumberOfStones() + 1;
        initPart();
        if (removeNodes()) {
            return;
        }
        int[] myPos = new int[partLen];
        int[] cntMoves = new int[partLen];
        int[] lastPos = new int[partLen];
        int stackSize = 0;
        int maxGOTO= -1;
//        System.err.println(partFrom + " " + partTo);
        for (int pos = partTo - 1; pos >= partFrom; pos--) {
            int goTo = (int) (stones.GetJumpLength(pos) + pos);
            maxGOTO = Math.max(maxGOTO, goTo);
            if (goTo >= partTo) {
                stackSize = addToStack(myPos, cntMoves, lastPos, stackSize, 1, goTo, pos);
            } else {
                int stPos = findBest(goTo, myPos, stackSize);
                stackSize = addToStack(myPos, cntMoves, lastPos, stackSize, cntMoves[stPos] + 1, lastPos[stPos], pos);
            }
        }
        int movesFromLeft = 0;
        int leftPos = 0;
        int leftMaxPos = 0;
        if (partFrom != 0) {
            receiveMessage(myId - 1);
            movesFromLeft = readInt(myId - 1);
            leftPos = readInt(myId - 1);
            leftMaxPos = readInt(myId - 1);
        }
//        System.err.println(movesFromLeft + " "  +  leftPos + " " + leftMaxPos);
        if (leftPos >= partTo) {
            update(leftPos, movesFromLeft);
        }
        if (leftMaxPos >= partTo) {
            update(leftMaxPos, movesFromLeft + 1);
        }
        {
            int stPos = findBest(leftPos, myPos, stackSize);
            if (stPos != -1) {
                update(lastPos[stPos], cntMoves[stPos] + movesFromLeft);
            }
        }
        {
            int stPos = findBest(leftMaxPos, myPos, stackSize);
            if (stPos != -1) {
                update(lastPos[stPos], cntMoves[stPos] + movesFromLeft + 1);
            }
        }
        rightMaxPos = Math.max(rightMaxPos, rightPos);
        rightMaxPos= Math.max(rightMaxPos, maxGOTO);
//        System.err.println(movesNext + " " + rightPos + "  " + rightMaxPos);
        if (partTo != n) {
            putInt(myId + 1, movesNext);
            putInt(myId + 1, rightPos);
            putInt(myId + 1, rightMaxPos);
            sendMessage(myId + 1);
        } else {
            println(movesNext);
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
        new StonesSol().start();
    }
}
