package fakes.command;

import fakes.IFakeAdvancedBusinessLogic;
import fakes.IFakeBusinessLogic;
import hu.ajprods.ICommandHandler;

import java.util.ArrayList;
import java.util.List;

public class FakeMultiImplCommandHandler
        implements IFakeBusinessLogic<String>,
                   ICommandHandler<FakeACommand, String>,
                   IFakeAdvancedBusinessLogic<String> {

    public List<FakeACommand> commandsHandled = new ArrayList<>();

    @Override
    public String handle(FakeACommand command) {
        System.out.println(command.message + " got handled by FakeACommandHandler");

        commandsHandled.add(command);

        return command.message + " got handled";
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
