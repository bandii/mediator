package fakes.event;

import hu.ajprods.IEvent;

public class FakeAEvent
        implements IEvent {

    public String message;

    public FakeAEvent(String message) {
        this.message = message;
    }
}
