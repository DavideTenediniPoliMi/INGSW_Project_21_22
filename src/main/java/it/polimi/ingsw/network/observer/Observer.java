package it.polimi.ingsw.network.observer;

/**
 * Interface for Observers, adds an <code>update</code> method.
 */
public interface Observer<T> {
    /**
     * Elaborates the message receives by the Observable objects.
     *
     * @param message the message sent to the observer.
     */
    void update(T message);
}
