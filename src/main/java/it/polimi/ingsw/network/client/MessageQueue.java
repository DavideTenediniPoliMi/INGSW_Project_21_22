package it.polimi.ingsw.network.client;

import java.util.LinkedList;
import java.util.Queue;

public class MessageQueue<T> {
    private final Queue<T> queue = new LinkedList<>();
    private final Object EMPTY_QUEUE = new Object();

    public void waitOnEmpty() throws InterruptedException {
        synchronized (EMPTY_QUEUE) {
            EMPTY_QUEUE.wait();
        }
    }

    public void notifyAllForEmpty() {
        synchronized (EMPTY_QUEUE) {
            EMPTY_QUEUE.notifyAll();
        }
    }

    public void add(T t) {
        synchronized (queue) {
            queue.add(t);
        }
    }

    public T remove() {
        synchronized (queue) {
            return queue.poll();
        }
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
