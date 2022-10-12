package fakes.command;

import hu.ajprods.ICommand;
import hu.ajprods.ICommandMiddleware;

import java.util.LinkedList;

public class FakeACommand
        implements ICommand<String> {

    public String message;

    public LinkedList<ICommandMiddleware> middlewaresVisited = new LinkedList<>();

    public FakeACommand(String message) {
        this.message = message;
    }
}
