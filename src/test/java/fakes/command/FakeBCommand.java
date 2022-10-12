package fakes.command;

import hu.ajprods.ICommand;
import hu.ajprods.ICommandMiddleware;

import java.util.LinkedList;

public class FakeBCommand
        implements ICommand<String> {

    public String message;

    public LinkedList<ICommandMiddleware> middlewaresVisited = new LinkedList<>();

    public FakeBCommand(String message) {
        this.message = message;
    }
}
