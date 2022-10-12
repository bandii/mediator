package fakes.command;

import hu.ajprods.ICommandHandler;

import java.util.ArrayList;
import java.util.List;

public class FakeFaultyACommandHandler
        implements ICommandHandler<FakeACommand, String> {

    public List<FakeACommand> commandsHandled = new ArrayList<>();

    @Override
    public String handle(FakeACommand command) {
        System.out.println(command.message + " got handled");

        commandsHandled.add(command);

        throw new RuntimeException("test");
    }
}
