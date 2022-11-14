package fakes.command.middleware;

import fakes.IFakeAdvancedBusinessLogic;
import fakes.IFakeBusinessLogic;
import fakes.command.FakeACommand;
import hu.ajprods.ICommandMiddleware;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FakeMultiImplMiddleware
        implements IFakeBusinessLogic<String>,
                   ICommandMiddleware<FakeACommand, String>,
                   IFakeAdvancedBusinessLogic<String> {

    public List<FakeACommand> commandsHandled = new ArrayList<>();

    @Override
    public String handle(FakeACommand command, Supplier<String> next) {
        System.out.println("*** Begin - FakeAMiddleware" + command.message);

        commandsHandled.add(command);
        command.middlewaresVisited.add(this);

        String ret = next.get();

        System.out.println("*** END - FakeAMiddleware" + command.message);

        return ret;
    }

    @Override
    public String doPrint(String param) {
        return null;
    }

    @Override
    public boolean shouldPrint(String param) {
        return false;
    }
}

