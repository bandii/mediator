package fakes.command;

import hu.ajprods.ICommand;
import hu.ajprods.ICommandMiddleware;
import hu.ajprods.Void;

import java.util.LinkedList;

public class FakeVoidCommand
        implements ICommand<Void> {

    public String message;

    public LinkedList<ICommandMiddleware> middlewaresVisited = new LinkedList<>();

    public FakeVoidCommand(String message) {
        this.message = message;
    }
}
