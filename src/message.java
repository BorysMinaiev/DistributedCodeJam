import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

// A program you submit to Distributed Code Jam will be compiled by Google, and
// will run on multiple computers (nodes). This library describes the interface
// needed for the nodes to identify themselves and to communicate.
//
// This is the version of the interface for programs written in Java. Your
// program doesn't need to import it and should always use a class name before
// accessing the static methods, e.g.:
// int n = message.NumberOfNodes();
public class message {

    static int nodes;
    static HashMap<Long, Integer> threadIdToNodeId = new HashMap<>();
    static BlockingQueue<OneMessage>[][] queues;
    static OneMessage[][] currentReadingMessage;
    static OneMessage[][] currentSendMessage;
    static void init(int newNodes) {
        nodes = newNodes;
        threadIdToNodeId = new HashMap<>();
        queues = new BlockingQueue[nodes][nodes];
        for (int i = 0; i < nodes; i++) {
            for (int j = 0; j < nodes; j++) {
                queues[i][j] = new ArrayBlockingQueue<OneMessage>(10);
            }
        }
        currentReadingMessage = new OneMessage[nodes][nodes];
        currentSendMessage = new OneMessage[nodes][nodes];
        for (int i = 0; i < nodes; i++) {
            for (int j = 0; j < nodes; j++) {
                currentSendMessage[i][j] = new OneMessage(i);
            }
        }
    }
    static void registerThread(long threadId, int nodeId) {
        threadIdToNodeId.put(threadId, nodeId);
    }


    // The number of nodes on which the solution is running.
    public static int NumberOfNodes() {
        return nodes;
    }

    // The index (in the range [0 .. NumberOfNodes()-1]) of the node on which this
    // process is running.
    public static int MyNodeId() {
        Integer result = threadIdToNodeId.get(Thread.currentThread().getId());
        if (result == null) {
            throw new AssertionError("Strange! Can't find thread!");
        }
        return result;
    }

    // In all the functions below, if "target" or "source" is not in the valid
    // range, the behaviour is undefined.

    // The library internally has a message buffer for each of the nodes in
    // [0 .. NumberOfNodes()-1]. It accumulates the message in such a buffer through
    // the "Put" methods.

    // Append "value" to the message that is being prepared for the node with id
    // "target". The "Int" in PutInt is interpreted as 32 bits, regardless of
    // whether the actual int type will be 32 or 64 bits.
    public static void PutChar(int target, char value) {
        throw new AssertionError("not supported!");
    }
    public static void PutInt(int target, int value) {
        int me = MyNodeId();
        OneMessage cur = currentSendMessage[me][target];
        cur.addInt(value);
    }
    public static void PutLL(int target, long value) {
        int me = MyNodeId();
        OneMessage cur = currentSendMessage[me][target];
        cur.addLong(value);
    }

    // Send the message that was accumulated in the appropriate buffer to the
    // "target" instance, and clear the buffer for this instance.
    //
    // This method is non-blocking - that is, it does not wait for the receiver to
    // call "Receive", it returns immediately after sending the message.
    public static void Send(int target) {
        int me = MyNodeId();
        OneMessage cur = currentSendMessage[me][target];
        queues[target][me].add(cur);
        currentSendMessage[me][target] = new OneMessage(me);
    }

    // The library also has a receiving buffer for each instance. When you call
    // "Receive" and retrieve a message from an instance, the buffer tied to this
    // instance is overwritten. You can then retrieve individual parts of the
    // message through the Get* methods. You must retrieve the contents of the
    // message in the order in which they were appended.
    //
    // This method is blocking - if there is no message to receive, it will wait for
    // the message to arrive.
    //
    // You can call Receive(-1) to retrieve a message from any source, or with
    // source in [0 .. NumberOfNodes()-1] to retrieve a message from a particular
    // source.
    //
    // It returns the number of the instance which sent the message (which is equal
    // to source, unless source is -1).
    public static int Receive(int source) {
        if (source == -1) {
            throw new AssertionError("not supported!");
        }
        int me = MyNodeId();
        OneMessage cur = currentReadingMessage[me][source];
        if (cur != null && !cur.queue.isEmpty()) {
            throw new AssertionError("Message wan't read fully!");
        }
        try {
            OneMessage msg = queues[me][source].poll(5, TimeUnit.SECONDS);
            if (msg == null) {
                throw new AssertionError("Didn't got message :(");
            }
            currentReadingMessage[me][source] = msg;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return source;
    }

    // Each of these methods returns and consumes one item from the buffer of the
    // appropriate instance. You must call these methods in the order in which the
    // elements were appended to the message (so, for instance, if the message was
    // created with PutChar, PutChar, PutLL, you must call GetChar, GetChar, GetLL
    // in this order).
    // If you call them in different order, or you call a Get* method after
    // consuming all the contents of the buffer, behaviour is undefined.
    // The "Int" in GetInt is interpreted as 32 bits, regardless of whether the
    // actual int type will be 32 or 64 bits.
    public static char GetChar(int source) {
        throw new AssertionError("not supported!");
    }
    public static int GetInt(int source) {
        int me = MyNodeId();
        OneMessage cur = currentReadingMessage[me][source];
        if (cur == null) {
            throw new AssertionError("No message is reading right now from " + source);
        }
        return cur.getInt();
    }
    public static long GetLL(int source) {
        int me = MyNodeId();
        OneMessage cur = currentReadingMessage[me][source];
        if (cur == null) {
            throw new AssertionError("No message is reading right now from " + source);
        }
        return cur.getLong();
    }
}