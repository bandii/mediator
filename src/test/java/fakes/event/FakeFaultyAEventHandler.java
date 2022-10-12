package fakes.event;

import hu.ajprods.IEventHandler;

import java.util.ArrayList;
import java.util.List;

public class FakeFaultyAEventHandler
        implements IEventHandler<FakeAEvent> {

    public List<FakeAEvent> eventsHandled = new ArrayList<>();

    @Override
    public void handle(FakeAEvent command) {
        System.out.println(command.message + " got handled");

        eventsHandled.add(command);

        throw new RuntimeException("test");
    }
}
