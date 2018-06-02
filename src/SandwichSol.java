public class SandwichSol {

    // [partFrom .. partTo)
    int partFrom, partTo;
    int partLen;

    void solve() {
        int n = (int) sandwich.GetN();
        int partSize = 1 + (n - 1) / nodes;
        partFrom = Math.min(n, myId * partSize);
        partTo = Math.min(n, (myId + 1) * partSize);
        partLen = partTo - partFrom;
        long[] values = new long[partLen];
        for (int i = 0; i < partLen; i++) {
            values[i] = sandwich.GetTaste(i + partFrom);
        }
        SandwichPart part = new SandwichPart(values);
        SandwichPart[] sum = getAllNodes(part);
        if (sum != null) {
            long ans = 0;
            for (int fr = 0; fr < nodes; fr++) {
                for (int to = fr; to < nodes; to++) {
                    long curAns = 0;
                    for (int i = 0; i < fr; i++) {
                        curAns += sum[i].sum;
                    }
                    if (fr == to) {
                        curAns += sum[fr].maxAns;
                    } else {
                        curAns += sum[fr].maxPref;
                        curAns += sum[to].maxSuf;
                    }
                    for (int i = to + 1; i < nodes; i++) {
                        curAns += sum[i].sum;
                    }
                    ans = Math.max(ans, curAns);
                }
            }
            println(ans);
        }
    }

    class SandwichPart {
        long sum;
        long maxPref;
        long maxSuf;
        long maxAns;

        SandwichPart(long[] values) {
            long suf = 0;
            long[] bestSuf = new long[values.length];
            for (int i = values.length - 1; i >= 0; i--) {
                suf += values[i];
                maxSuf = Math.max(maxSuf, suf);
                bestSuf[i] = maxSuf;
            }
            for (int i = 0; i < values.length; i++) {
                maxAns = Math.max(maxAns, maxPref + bestSuf[i]);
                long x = values[i];
                sum += x;
                maxPref = Math.max(maxPref, sum);
            }
        }

        public SandwichPart(long sum, long maxPref, long maxSuf, long maxAns) {
            this.sum = sum;
            this.maxPref = maxPref;
            this.maxSuf = maxSuf;
            this.maxAns = maxAns;
        }

        public void sendTo(int to) {
//            System.err.println("send to " + to + " " + this);
            putLong(to, sum);
            putLong(to, maxPref);
            putLong(to, maxSuf);
            putLong(to, maxAns);
            sendMessage(to);
        }

        public SandwichPart receiveFrom(int from) {
            receiveMessage(from);
            SandwichPart res = new SandwichPart(readLong(from), readLong(from), readLong(from), readLong(from));
//            System.err.println("received " + res +" from " + from);
            return res;
        }

        @Override
        public String toString() {
            return "SandwichPart{" +
                    "sum=" + sum +
                    ", maxPref=" + maxPref +
                    ", maxSuf=" + maxSuf +
                    ", maxAns=" + maxAns +
                    '}';
        }
    }

    SandwichPart[] getAllNodes(SandwichPart curInfo) {
        curInfo.sendTo(rootNode);
        if (myId == rootNode) {
            SandwichPart[] res = new SandwichPart[nodes];
            for (int i = 0; i < nodes; i++) {
                res[i] = curInfo.receiveFrom(i);
            }
            return res;
        } else {
            return null;
        }
    }

    void putInt(int to, int value) {
        message.PutInt(to, value);
    }

    void putLong(int to, long value) {
        message.PutLL(to, value);
    }

    void sendMessage(int to) {
        message.Send(to);
    }

    int readInt(int from) {
        return message.GetInt(from);
    }

    long readLong(int from) {
        return message.GetLL(from);
    }

    void receiveMessage(int from) {
        message.Receive(from);
    }

    void println(String s) {
        System.out.println(s);
    }

    void println(long val) {
        System.out.println(val);
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
        new SandwichSol().start();
    }
}
