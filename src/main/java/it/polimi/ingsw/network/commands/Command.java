package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.exceptions.EriantysRuntimeException;

public interface Command {
    void execute() throws EriantysException, EriantysRuntimeException;
}
