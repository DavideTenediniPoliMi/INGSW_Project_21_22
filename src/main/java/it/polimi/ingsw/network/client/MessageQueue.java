package it.polimi.ingsw.network.client;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Class implementing a thread-safe version of a queue.
 *
 * @param <T> the type of the elements.
 */
public class MessageQueue<T> {
    private final Queue<T> queue = new LinkedList<>();
    private final Object EMPTY_QUEUE = new Object();

    /**
     * Pauses the thread until an element is added to this queue.
     *
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    public void waitOnEmpty() throws InterruptedException {
        synchronized (EMPTY_QUEUE) {
            EMPTY_QUEUE.wait();
        }
    }

    /**
     * Notifies the consumer waiting for a new element.
     */
    public void notifyAllForEmpty() {
        synchronized (EMPTY_QUEUE) {
            EMPTY_QUEUE.notifyAll();
        }
    }

    /**
     * Adds a new element to this queue.
     *
     * @param t the new element.
     */
    public void add(T t) {
        synchronized (queue) {
            queue.add(t);
        }
    }

    /**
     * Removes the first element of this queue, returns null if it's empty.
     *
     * @return the first element of this queue.
     */
    public T remove() {
        synchronized (queue) {
            return queue.poll();
        }
    }

    /**
     * Checks if this queue is empty.
     *
     * @return true if this queue is empty, false otherwise.
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
