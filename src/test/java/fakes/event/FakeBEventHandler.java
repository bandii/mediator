package fakes.event;

import hu.ajprods.IEventHandler;

import java.util.ArrayList;
import java.util.List;

public class FakeBEventHandler
        implements IEventHandler<FakeBEvent> {

    public List<FakeBEvent> eventsHandled = new ArrayList<>();

    @Override
    public void handle(FakeBEvent command) {
        System.out.println(command.message + " got handled");

        eventsHandled.add(command);
    }
}
