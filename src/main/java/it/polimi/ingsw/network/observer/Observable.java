package it.polimi.ingsw.network.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for observable entities. Adds methods to handle:
 * <ul>
 *     <li>Adding a new observer</li>
 *     <li>Removing an existing observer</li>
 *     <li>Notifying all observers attached to this class</li>
 * </ul>
 */
public class Observable<T> {
    private final List<Observer<T>> observers = new ArrayList<>();

    /**
     * Adds a new class of type <code>Observer</code> to the observers.
     *
     * @param observer the <code>Observer</code> to add.
     */
    public void addObserver(Observer<T> observer) {
        if(observer == null) return;

        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * Return all the <Code>Observer<Code/> attached to this <Code>Object<Code/>
     *
     * @return the list of observers
     */
    public List<Observer<T>> getObservers() {
        return new ArrayList<>(observers);
    }

    /**
     * Removes the specified <code>Observer</code> from the ones attached to this class.
     *
     * @param observer the <code>Observer</code> to remove.
     */
    public void removeObserver(Observer<T> observer) {
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * Clears the list of observers for this class.
     */
    public void clearObservers() {
        observers.clear();
    }

    /**
     * Notifies every <code>Observer</code> attached to this class with the message specified.
     *
     * @param message the message to send to the Observers.
     */
    protected void notify(T message) {
        synchronized(observers) {
            for(Observer<T> observer : observers) {
                observer.update(message);
            }
        }
    }
}
