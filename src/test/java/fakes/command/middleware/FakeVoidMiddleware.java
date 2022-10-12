package fakes.command.middleware;

import fakes.command.FakeVoidCommand;
import hu.ajprods.ICommandMiddleware;
import hu.ajprods.Void;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FakeVoidMiddleware
        implements ICommandMiddleware<FakeVoidCommand, Void> {

    public List<FakeVoidCommand> commandsHandled = new ArrayList<>();

    @Override
    public Void handle(FakeVoidCommand command, Supplier<Void> next) {
        System.out.println("*** Begin" + command.message);

        commandsHandled.add(command);
        command.middlewaresVisited.add(this);

        Void ret = next.get();

        System.out.println("*** END" + command.message);

        return ret;
    }
}
