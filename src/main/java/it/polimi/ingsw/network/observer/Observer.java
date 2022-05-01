package it.polimi.ingsw.network.observer;

/**
 * Interface for Observers, adds an <code>update</code> method.
 */
public interface Observer<T> {
    void update(T message);
}
