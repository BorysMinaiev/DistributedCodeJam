// Sample input 1, in Java.
public class kenneth {
//    static String data = "ABCDEABCDE";
static String data = "ABABABABAA";
//    private static String data = "ABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCA";
    public static long GetPieceLength(long node_index) {
        return 2;
    }

    public static char GetSignalCharacter(long position) {
        int node = message.MyNodeId();
        int num_nodes = message.NumberOfNodes();
        assert (node * 5 / num_nodes <= position);
        assert ((node + 1) * 5 / num_nodes > position);
        return data.charAt((int) position);
    }
}