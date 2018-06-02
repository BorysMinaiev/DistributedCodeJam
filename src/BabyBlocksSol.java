public class BabyBlocksSol {
    final boolean debug = false;

    // [partFrom .. partTo)
    int partFrom, partTo;
    int partLen;

    int[] cachedValues;

    int get(int pos) {
        if (pos >= partFrom && pos < partTo) {
            return cachedValues[pos - partFrom];
        }
        if (pos < 0 || pos >= n) {
            return -1;
        }
        return (int) baby_blocks.GetBlockWeight(pos);
    }

    int getFrom(int nodeId) {
        return Math.min(n, nodeId * partSize);
    }

    int getTo(int nodeId) {
        return Math.min(n, (nodeId + 1) * partSize);
    }

    int n, partSize;

    boolean[] taskSent;
    void sendTaskToNode(int leftFr, int leftTo, long leftSum, int rightFr, int rightTo, long rightSum, int nodeId) {
        if (taskSent[nodeId]) {
            throw new AssertionError();
        }
        taskSent[nodeId] = true;
        putInt(nodeId, leftFr);
        putInt(nodeId, leftTo);
        putLong(nodeId, leftSum);
        putInt(nodeId, rightFr);
        putInt(nodeId, rightTo);
        putLong(nodeId, rightSum);
        sendMessage(nodeId);
    }

    void solve() {
        n = (int) baby_blocks.GetNumberOfBlocks();
        partSize = 1 + (n - 1) / nodes;
        partFrom = getFrom(myId);
        partTo = getTo(myId);
        partLen = partTo - partFrom;
        if (partLen == 0) {
            return;
        }
        while (getFrom(nodes - 1) >= n) {
            nodes--;
        }
        cachedValues = new int[partLen];
        long sum = 0;
        for (int i = 0; i < partLen; i++) {
            cachedValues[i] = (int) baby_blocks.GetBlockWeight(i + partFrom);
            sum += cachedValues[i];
        }
        long[] partSum = joinLongArray(sum);
        if (iAmRoot()) {
            taskSent = new boolean[nodes];
            long sumLeft = 0, sumRight = 0;
            int i = 0, j = nodes - 1;
            while (i <= j) {
                long nextSumLeft = sumLeft + partSum[i];
                long nextSumRight = sumRight + partSum[j];
                if (nextSumLeft <= sumRight) {
                    sumLeft= nextSumLeft;
                    i++;
                    continue;
                }
                if (nextSumRight <= sumLeft) {
                    sumRight = nextSumRight;
                    j--;
                    continue;
                }
                int sendTo = (nextSumLeft <= nextSumRight) ? i : j;
                sendTaskToNode(getFrom(i), getTo(i), sumLeft, getFrom(j), getTo(j), sumRight, sendTo);
                if (sendTo == i) {
                    sumLeft = nextSumLeft;
                    i++;
                } else {
                    sumRight = nextSumRight;
                    j--;
                }
            }
            for (int k = 0; k < nodes; k++) {
                if (!taskSent[k]) {
                    putInt(k, -1);
                    sendMessage(k);
                }
            }
        }
        receiveMessage(rootNode);
        int leftFr = readInt(rootNode);
        if (leftFr != -1) {
            int leftTo = readInt(rootNode);
            long leftSum = readLong(rootNode);
            int rightFr = readInt(rootNode);
            int rightTo = readInt(rootNode);
            long rightSum = readLong(rootNode);
            putInt(rootNode, solveSubTask(leftFr, leftTo, leftSum, rightTo - 1, rightFr - 1, rightSum));
            sendMessage(rootNode);
        }
        if (iAmRoot()) {
            int res= 0;
            for (int i = 0; i < nodes; i++) {
                if (taskSent[i]) {
                    receiveMessage(i);
                    res += readInt(i);
                }
            }
            println(res);
        }
    }

    int solveSubTask(int lFr, int lTo, long sumL, int rFr, int rTo, long sumR) {
        int res = 0;
        sumL += get(lFr);
        sumR += get(rFr);
        while (lFr < rFr && lFr < lTo && rFr > rTo) {
            if (sumL == sumR) {
                res++;
            }
            if (sumL <= sumR) {
                lFr++;
                sumL += get(lFr);
            } else {
                rFr--;
                sumR += get(rFr);
            }
        }
        return res;
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
        new BabyBlocksSol().start();
    }
}
