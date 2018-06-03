import java.util.HashMap;

public class LemmingSol {
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

    void solve() {
        int[] dx = new int[]{-1, 0, 0, 1};
        int[] dy = new int[]{0, -1, 1, 0};
        String dirs = "^<>v";
        n = (int) lemming.GetRows();
        m = (int) lemming.GetColumns();
        initPart();
        if (partLen == 0) {
            return;
        }
        removeNodes();
        final int SHIFT = (int) 1e6;
        // 0 - wan't here
        // 1 - just was here
        // 2 - go ok
        // SHIFT + pos - end in [-1][pos]
        // 2 * SHIFT + pos - end in [partLen][pos]
        int[][] go = new int[partLen][m];
        int[] qX = new int[partLen * m];
        int[] qY = new int[partLen * m];
        int res = 0;
        putInt(rootNode, partLen == 1 ? 1 : 2);
        for (int i = 0; i < partLen; i++) {
            for (int j = 0; j < m; j++) {
//                System.err.println("check " + i + " " + j + " " + go[i][j]);
                if (go[i][j] == 0) {
                    go[i][j] = 1;
                    int qIt = 0;
                    qX[0] = i;
                    qY[0] = j;
                    int newRes = -1;
                    while (newRes == -1) {
                        int x = qX[qIt];
                        int y = qY[qIt];
                        int dir = dirs.indexOf(lemming.GetDirection(x + partFrom, y));
                        int nx = x + dx[dir];
                        int ny = y + dy[dir];
                        if (nx >= 0 && nx < partLen && ny >= 0 && ny < m) {
                            if (go[nx][ny] == 0) {
                                qIt++;
                                qX[qIt] = nx;
                                qY[qIt] = ny;
                                go[nx][ny] = 1;
                            } else {
                                newRes = go[nx][ny];
                            }
                        } else {
                            if (nx < 0) {
                                newRes = SHIFT + ny;
                            } else if (nx >= partLen) {
                                newRes = SHIFT * 2 + ny;
                            } else {
                                newRes = 2;
                                res++;
                            }
                        }
                    }
                    if (newRes == 1) {
                        newRes = 2;
                        res++;
                    }
                    if (newRes < 2) {
                        throw new AssertionError();
                    }
                    for (int z = 0; z <= qIt; z++) {
                        go[qX[z]][qY[z]] = newRes;
                    }
                }
                if (i == 0 || i == partLen - 1) {
                    if (go[i][j] <= 1) {
                        throw new AssertionError();
                    }
                    putInt(rootNode, go[i][j]);
                }
            }
        }
        putInt(rootNode, res);
        sendMessage(rootNode);
        if (iAmRoot()) {
            int[] next = new int[nodes * 2 * m];
            int sz = 0;
            res = 0;
            for (int i = 0; i < nodes; i++) {
                receiveMessage(i);
                int len = readInt(i);
                int nextSz = sz + len * m;
                for (int j = 0; j < len; j++) {
                    for (int k = 0; k < m; k++) {
                        int to = readInt(i);
                        int myId = sz + j * m + k;
                        if (to >= 2 * SHIFT) {
                            to -= 2 * SHIFT;
                            to += nextSz;
                            if (to != myId + m) {
                                to -= m;
                            }
                        } else if (to >= SHIFT) {
                            to -= SHIFT;
                            to += sz - m;
                            if (to != myId - m) {
                                to += m;
                            }
                        } else {
                            to = Integer.MIN_VALUE;
                        }
                        next[myId] = to;
                    }
                }
                res += readInt(i);
                sz = nextSz;
            }
            int[] q = new int[sz];
            HashMap<Integer, Integer> outer = new HashMap<>();
            for (int i = 0; i < sz; i++) {
                if (next[i] == Integer.MIN_VALUE) {
                    continue;
                }
                int qIt = 0;
                q[0] = i;
                while (true) {
                    int v = q[qIt];
                    int ne = next[v];
                    if (ne < 0 || ne >= sz) {
//                        if (outer.get(ne) != null) {
//                            throw new AssertionError(v + " " + ne + " " + outer.get(ne));
//                        }
//                        System.err.println("!!! " + ne);
//                        outer.put(ne, v);
                        res++;
                        break;
                    } else if (next[ne] == Integer.MIN_VALUE) {
                        break;
                    } else if (next[ne] == Integer.MIN_VALUE + 1) {
                        res++;
                        break;
                    } else {
                        qIt++;
                        q[qIt] = ne;
                        next[v] = Integer.MIN_VALUE + 1;
                    }
                }
                for (int z = 0; z <= qIt; z++) {
                    next[q[z]] = Integer.MIN_VALUE;
                }
            }
            println(res + outer.size());
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
        new LemmingSol().start();
    }
}
