package fakes.command;

import hu.ajprods.ICommandHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * It has 1 sec delay in the handle!
 */
public class FakeAsyncCommandHandler
        implements ICommandHandler<FakeAsyncCommand, Future<String>> {

    public static short DELAY = 1000;

    public List<FakeAsyncCommand> commandsHandled = new ArrayList<>();

    public Instant startTime;

    public Instant endTime;

    @Override
    public Future<String> handle(FakeAsyncCommand command) {
        startTime = Instant.now();

        System.out.println(command.message + " WILL BE handled in Fakes.fakes.command.FakeACommandHandler");

        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(DELAY);

            commandsHandled.add(command);
            completableFuture.complete(command.message + " got handled");
            endTime = Instant.now();

            System.out.println(command.message + " got handled");
            return null;
        });

        return completableFuture;
    }
}
