package byog.Core;

import java.util.Iterator;

public class StringArrayDeque implements Iterable<String> {

    private int first; // index of first item (for dequeue)
    private int last; // index of last item (for enqueue)
    private int size = 4;
    private String[] deque = new String[4];

    public Iterator<String> iterator() {
        return new StringArrayDequeIterator();
    }

    /**
     * Code inspired from HW1. This ring deque is not a full implementation, but the
     * bare minimum to support message scrolling for the HUD. Essentially, it just
     * keeps track of the last 4 messages, and when a new message is added, it overwrites
     * the oldest message by replacing it with the enqueued message.
     *
     * Has iterable functionality to produce messages in order from oldest to newest.
     *
     * @source my HW1 code for ArrayRingBuffer.
     * */
    private class StringArrayDequeIterator implements Iterator<String> {
        private int ptr;
        private int count;

        private StringArrayDequeIterator() {
            ptr = first;
        }

        @Override
        public boolean hasNext() {
            return count < size; // by construction, deque always full.
        }

        @Override
        public String next() {
            String res = deque[ptr];
            ptr = (ptr + 1) % size;
            count++;
            return res;
        }
    }

    public StringArrayDeque() {
        first = 0;
        last = 0;
        enqueue("");
        enqueue("");
        enqueue("");
        enqueue("You awaken in a dark place...");
    }

    public void enqueue(String str) {
        if (last == first) {
            first = (first + 1) % size;
        }
        deque[last] = str;
        last = (last + 1) % size;
    }



}
