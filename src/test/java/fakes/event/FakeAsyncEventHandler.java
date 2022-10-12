package fakes.event;

import hu.ajprods.IEventHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class FakeAsyncEventHandler
        implements IEventHandler<FakeAsyncEvent> {

    public static short DELAY = 1000;

    public List<FakeAsyncEvent> eventsHandled = new ArrayList<>();

    public Instant startTime;

    public Instant endTime;

    @Override
    public void handle(FakeAsyncEvent event) {
        startTime = Instant.now();

        System.out.println(event.message + " WILL BE handled");

        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(DELAY);

            eventsHandled.add(event);
            completableFuture.complete(event.message + " got handled");
            endTime = Instant.now();

            System.out.println(event.message + " got handled");
            return null;
        });
    }
}
