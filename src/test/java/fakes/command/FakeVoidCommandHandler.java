package fakes.command;

import hu.ajprods.ICommandHandler;
import hu.ajprods.Void;

import java.util.ArrayList;
import java.util.List;

public class FakeVoidCommandHandler
        implements ICommandHandler<FakeVoidCommand, Void> {

    public List<FakeVoidCommand> commandsHandled = new ArrayList<>();

    @Override
    public Void handle(FakeVoidCommand command) {
        System.out.println(command.message + " got handled by FakeVoidCommandHandler");

        commandsHandled.add(command);

        return Void.VOID;
    }
}
