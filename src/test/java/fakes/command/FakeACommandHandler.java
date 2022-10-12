package fakes.command;

import hu.ajprods.ICommandHandler;

import java.util.ArrayList;
import java.util.List;

public class FakeACommandHandler
        implements ICommandHandler<FakeACommand, String> {

    public List<FakeACommand> commandsHandled = new ArrayList<>();

    @Override
    public String handle(FakeACommand command) {
        System.out.println(command.message + " got handled");

        commandsHandled.add(command);

        return command.message + " got handled";
    }
}
