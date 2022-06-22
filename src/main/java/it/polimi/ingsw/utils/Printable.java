package it.polimi.ingsw.utils;

/**
 * Interfaces for printing, adds a <code>print</code> method
 */
public interface Printable<T> {
    /**
     * Returns a <code>String</code> or a <code>List</code> of <code>String</code> that represents the object to be
     * print.
     *
     * @param params Params that are needed to print
     *
     * @return <code>String</code> or a <code>List</code> of <code>String</code> that represents the object to be
     * print.
     */
    T print(boolean...params);
}
