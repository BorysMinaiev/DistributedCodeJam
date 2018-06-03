import java.util.ArrayDeque;
import java.util.Queue;

public class OneMessage {

    int from;
    Queue<OneMessagePart> queue;

    OneMessage(int from) {
        this.from = from;
        queue = new ArrayDeque<>();
    }

    void addLong(long value) {
        queue.add(new MyLong(value));
    }

    void addInt(int value) {
        queue.add(new MyInt(value));
    }

    int getInt() {
        OneMessagePart elem = queue.poll();
        if (elem == null) {
            throw new AssertionError("message is empty!");
        }
        if (elem instanceof MyInt) {
            return ((MyInt) elem).value;
        }
        throw new AssertionError("Not int!");
    }


    long getLong() {
        OneMessagePart elem = queue.poll();
        if (elem == null) {
            throw new AssertionError("message is empty!");
        }
        if (elem instanceof MyLong) {
            return ((MyLong) elem).value;
        }
        throw new AssertionError("Not long!");
    }

    interface OneMessagePart {

    }

    class MyLong implements OneMessagePart {
        long value;

        public MyLong(long value) {
            this.value = value;
        }
    }

    class MyInt implements  OneMessagePart {
        int value;

        public MyInt(int value) {
            this.value = value;
        }
    }
}
