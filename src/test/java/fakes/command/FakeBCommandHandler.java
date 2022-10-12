package fakes.command;

import hu.ajprods.ICommandHandler;

import java.util.ArrayList;
import java.util.List;

public class FakeBCommandHandler
        implements ICommandHandler<FakeBCommand, String> {

    public List<FakeBCommand> commandsHandled = new ArrayList<>();

    @Override
    public String handle(FakeBCommand command) {
        System.out.println(command.message + " got handled");

        commandsHandled.add(command);

        return command.message + " got handled";
    }
}
