package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.exceptions.EriantysRuntimeException;

/**
 * Interface for commands, adds an <code>execute</code> method.
 */
public interface Command {
    /**
     * Asks the controller to execute the requested command.
     *
     * @throws EriantysException
     * @throws EriantysRuntimeException
     */
    void execute() throws EriantysException, EriantysRuntimeException;
}
