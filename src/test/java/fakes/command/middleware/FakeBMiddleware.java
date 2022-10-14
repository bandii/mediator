package fakes.command.middleware;

import fakes.command.FakeBCommand;
import hu.ajprods.ICommandMiddleware;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FakeBMiddleware
        implements ICommandMiddleware<FakeBCommand, String> {

    public List<FakeBCommand> commandsHandled = new ArrayList<>();

    @Override
    public String handle(FakeBCommand command, Supplier<String> next) {
        System.out.println("*** Begin - FakeFaultyMiddleware" + command.message);

        commandsHandled.add(command);
        command.middlewaresVisited.add(this);

        String ret = next.get();

        System.out.println("*** END - FakeFaultyMiddleware" + command.message);

        return ret;
    }
}
