import fakes.event.*;
import hu.ajprods.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Set;

public class EventTest {

    private Mediator testee;

    @BeforeEach
    public void SetUp() {
        testee = new Mediator();
    }

    @Test
    public void addEvent_null_FAIL() {
        Assertions.assertThrows(NullPointerException.class,
                                () -> testee.eventHandler()
                                            .addHandler((IEventHandler<IEvent>) null));
    }

    @Test
    public void addEvents_null_FAIL() {
        Assertions.assertThrows(NullPointerException.class,
                                () -> testee.notify(null));
    }

    @Test
    public void notify_null_FAIL() {
        Assertions.assertThrows(NullPointerException.class,
                                () -> testee.eventHandler()
                                            .addHandlers((Set<IEventHandler<? extends IEvent>>) null));
    }

    @Test
    public void singleEvent_OK() {
        // Given
        var eventHandler = new FakeAEventHandler();
        testee.eventHandler()
              .addHandler(eventHandler);

        var event = new FakeAEvent("singleEvent_OK");

        // When
        testee.notify(event);

        // Then
        Assertions.assertSame(1,
                              eventHandler.eventsHandled.size());
        Assertions.assertSame(event,
                              eventHandler.eventsHandled.get(0));
    }

    @Test
    public void singleAsyncEvent_OK()
            throws InterruptedException {
        // Given
        var EventHandler = new FakeAsyncEventHandler();
        testee.eventHandler()
              .addHandler(EventHandler);

        var event = new FakeAsyncEvent("singleAsyncEvent_OK");

        // When
        testee.notify(event);

        // Then
        // Future did not run, yet
        Assertions.assertSame(0,
                              EventHandler.eventsHandled.size());

        // Let's await and evaluate the FutureTask
        Thread.sleep(FakeAsyncEventHandler.DELAY * 2);

        Assertions.assertTrue(Duration.between(EventHandler.startTime,
                                               EventHandler.endTime)
                                      .toMillis() >= FakeAsyncEventHandler.DELAY);
        Assertions.assertSame(1,
                              EventHandler.eventsHandled.size());
        Assertions.assertSame(event,
                              EventHandler.eventsHandled.get(0));
    }

    @Test
    public void singleEvent_faultyHandler_OK() {
        // Given
        var eventHandler = new FakeFaultyAEventHandler();
        testee.eventHandler()
              .addHandler(eventHandler);

        var event = new FakeAEvent("singleEvent_faultyHandler_OK");

        // When
        Assertions.assertThrows(RuntimeException.class,
                                () -> testee.notify(event),
                                "test");

        // Then
        Assertions.assertSame(1,
                              eventHandler.eventsHandled.size());
        Assertions.assertSame(event,
                              eventHandler.eventsHandled.get(0));
    }

    @Test
    public void singleEvent_multiHandler_OK() {
        // Given
        var eventAHandler = new FakeAEventHandler();
        var eventBHandler = new FakeBEventHandler();
        testee.eventHandler()
              .addHandlers(Set.of(eventAHandler,
                                  eventBHandler));

        var event = new FakeAEvent("singleEvent_multiHandler_OK");

        // When
        testee.notify(event);

        // Then
        Assertions.assertSame(1,
                              eventAHandler.eventsHandled.size());
        Assertions.assertSame(event,
                              eventAHandler.eventsHandled.get(0));

        Assertions.assertSame(0,
                              eventBHandler.eventsHandled.size());
    }

    @Test
    public void singleEvent_multiSimilarHandler_OK() {
        // Given
        var eventAHandler = new FakeAEventHandler();
        var eventAHandler_2 = new FakeAEventHandler();
        testee.eventHandler()
              .addHandlers(Set.of(eventAHandler,
                                  eventAHandler_2));

        var event = new FakeAEvent("singleEvent_multiSimilarHandler_OK");

        // When
        testee.notify(event);

        // Then
        Assertions.assertSame(1,
                              eventAHandler.eventsHandled.size());
        Assertions.assertSame(event,
                              eventAHandler.eventsHandled.get(0));

        Assertions.assertSame(1,
                              eventAHandler_2.eventsHandled.size());
        Assertions.assertSame(event,
                              eventAHandler.eventsHandled.get(0));
    }

    @Test
    public void MultiEvent_multiHandler_OK() {
        // Given
        var eventAHandler = new FakeAEventHandler();
        var eventBHandler = new FakeBEventHandler();
        testee.eventHandler()
              .addHandlers(Set.of(eventAHandler,
                                  eventBHandler));

        var event = new FakeAEvent("MultiEvent_multiHandler_OK");

        // When
        testee.notify(event);

        // Then
        Assertions.assertSame(1,
                              eventAHandler.eventsHandled.size());
        Assertions.assertSame(event,
                              eventAHandler.eventsHandled.get(0));

        Assertions.assertSame(0,
                              eventBHandler.eventsHandled.size());

        // When
        testee.notify(event);

        // Then
        Assertions.assertSame(2,
                              eventAHandler.eventsHandled.size());
        Assertions.assertSame(event,
                              eventAHandler.eventsHandled.get(1));

        Assertions.assertSame(0,
                              eventBHandler.eventsHandled.size());
    }

    @Test
    public void singleEvent_complexHandler_OK() {
        // Given
        var eventHandler = new FakeAEventHandler();
        testee.eventHandler()
              .addHandler(eventHandler);

        var event = new FakeAEvent("singleEvent_OK");

        // When
        testee.notify(event);

        // Then
        Assertions.assertSame(1,
                              eventHandler.eventsHandled.size());
        Assertions.assertSame(event,
                              eventHandler.eventsHandled.get(0));
    }
}

