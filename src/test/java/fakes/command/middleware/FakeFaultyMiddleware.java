package fakes.command.middleware;

import fakes.command.FakeACommand;
import hu.ajprods.ICommandMiddleware;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FakeFaultyMiddleware
        implements ICommandMiddleware<FakeACommand, String> {

    public List<FakeACommand> commandsHandled = new ArrayList<>();

    @Override
    public String handle(FakeACommand command, Supplier<String> next) {
        System.out.println("*** Begin" + command.message);

        command.middlewaresVisited.add(this);
        commandsHandled.add(command);

        throw new RuntimeException("test");
    }
}
