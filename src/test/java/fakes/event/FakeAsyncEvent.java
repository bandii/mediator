package fakes.event;

import hu.ajprods.IEvent;

public class FakeAsyncEvent
        implements IEvent {

    public String message;

    public FakeAsyncEvent(String message) {
        this.message = message;
    }
}
