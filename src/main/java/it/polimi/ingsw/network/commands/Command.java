package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.exceptions.EriantysRuntimeException;

/**
 * Interface for commands, adds an <code>execute</code> method.
 */
public interface Command {
    void execute() throws EriantysException, EriantysRuntimeException;
}
