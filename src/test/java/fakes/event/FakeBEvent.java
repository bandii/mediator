package fakes.event;

import hu.ajprods.IEvent;

public class FakeBEvent
        implements IEvent {

    public String message;

    public FakeBEvent(String message) {
        this.message = message;
    }
}
