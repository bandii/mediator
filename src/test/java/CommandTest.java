import fakes.command.*;
import hu.ajprods.*;
import hu.ajprods.Void;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class CommandTest {

    private Mediator testee;

    @BeforeEach
    public void SetUp() {
        testee = new Mediator();
    }

    @Test
    public void addCommand_null_FAIL() {
        Assertions.assertThrows(NullPointerException.class,
                                () -> testee.commandHandler()
                                            .addHandler((ICommandHandler<ICommand<Object>, Object>) null));
    }

    @Test
    public void addCommands_null_FAIL() {
        Assertions.assertThrows(NullPointerException.class,
                                () -> testee.commandHandler()
                                            .addHandlers((Set<ICommandHandler<? extends ICommand<?>, ?>>) null));
    }

    @Test
    public void handle_null_FAIL() {
        Assertions.assertThrows(NullPointerException.class,
                                () -> testee.handle(null));
    }

    @Test
    public void singleEvent_multiSimilarHandler_FAIL() {
        var commandAHandler = new FakeACommandHandler();
        var commandAHandler_2 = new FakeACommandHandler();

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> testee.commandHandler()
                                            .addHandlers(Set.of(commandAHandler,
                                                                commandAHandler_2)));
    }

    @Test
    public void noHandler_FAIL() {
        Assertions.assertThrows(NoHandlerFoundException.class,
                                () -> testee.handle(new FakeACommand("fail")));
    }

    @Test
    public void singleCommand_OK()
            throws NoHandlerFoundException {
        // Given
        var commandHandler = new FakeACommandHandler();
        testee.commandHandler()
              .addHandler(commandHandler);

        var command = new FakeACommand("singleCommand_OK");

        // When
        var result = testee.handle(command);

        // Then
        Assertions.assertEquals(command.message + " got handled",
                                result);
        Assertions.assertSame(1,
                              commandHandler.commandsHandled.size());
        Assertions.assertSame(command,
                              commandHandler.commandsHandled.get(0));
    }

    @Test
    public void singleAsyncCommand_OK()
            throws NoHandlerFoundException,
                   ExecutionException,
                   InterruptedException {
        // Given
        var commandHandler = new FakeAsyncCommandHandler();
        testee.commandHandler()
              .addHandler(commandHandler);

        var command = new FakeAsyncCommand("singleAsyncCommand_OK");

        // When
        var result = testee.handle(command);

        // Then
        // Future did not run, yet
        Assertions.assertSame(0,
                              commandHandler.commandsHandled.size());

        // Let's await and evaluate the FutureTask
        Assertions.assertEquals(command.message + " got handled",
                                result.get());
        Assertions.assertTrue(Duration.between(commandHandler.startTime,
                                               commandHandler.endTime)
                                      .toMillis() >= FakeAsyncCommandHandler.DELAY);
        Assertions.assertSame(1,
                              commandHandler.commandsHandled.size());
        Assertions.assertSame(command,
                              commandHandler.commandsHandled.get(0));
    }

    @Test
    public void singleVoidCommand_OK()
            throws NoHandlerFoundException,
                   MediatorException {
        // Given
        var commandHandler = new FakeVoidCommandHandler();
        testee.commandHandler()
              .addHandler(commandHandler);

        var command = new FakeVoidCommand("singleVoidCommand_OK");

        // When
        var result = testee.handle(command);

        // Then
        Assertions.assertSame(Void.VOID,
                              result);

        // Checking Void's overrides
        Assertions.assertTrue(Void.VOID.equals(result));
        Assertions.assertEquals(Void.VOID.hashCode(),
                                result.hashCode());
        Assertions.assertEquals(Void.VOID.toString(),
                                result.toString());

        Assertions.assertSame(1,
                              commandHandler.commandsHandled.size());
        Assertions.assertSame(command,
                              commandHandler.commandsHandled.get(0));
    }

    @Test
    public void singleCommand_faultyHandler_OK() {
        // Given
        var commandHandler = new FakeFaultyACommandHandler();
        testee.commandHandler()
              .addHandler(commandHandler);

        var command = new FakeACommand("singleCommand_faultyHandler_OK");

        // When
        Assertions.assertThrows(RuntimeException.class,
                                () -> testee.handle(command),
                                "test");

        // Then
        Assertions.assertSame(1,
                              commandHandler.commandsHandled.size());
        Assertions.assertSame(command,
                              commandHandler.commandsHandled.get(0));
    }

    @Test
    public void singleCommand_multiHandler_OK()
            throws NoHandlerFoundException {
        // Given
        var commandAHandler = new FakeACommandHandler();
        var commandBHandler = new FakeBCommandHandler();
        testee.commandHandler()
              .addHandlers(Set.of(commandAHandler,
                                  commandBHandler));

        var command = new FakeACommand("singleCommand_multiHandler_OK");

        // When
        var result = testee.handle(command);

        // Then
        Assertions.assertEquals(command.message + " got handled",
                                result);
        Assertions.assertSame(1,
                              commandAHandler.commandsHandled.size());
        Assertions.assertSame(command,
                              commandAHandler.commandsHandled.get(0));

        Assertions.assertSame(0,
                              commandBHandler.commandsHandled.size());
    }

    @Test
    public void MultiCommand_multiHandler_OK()
            throws NoHandlerFoundException {
        // Given
        var commandAHandler = new FakeACommandHandler();
        var commandBHandler = new FakeBCommandHandler();
        testee.commandHandler()
              .addHandlers(Set.of(commandAHandler,
                                  commandBHandler));

        var command = new FakeACommand("MultiCommand_multiHandler_OK");

        // When
        var result = testee.handle(command);

        // Then
        Assertions.assertEquals(command.message + " got handled",
                                result);
        Assertions.assertSame(1,
                              commandAHandler.commandsHandled.size());
        Assertions.assertSame(command,
                              commandAHandler.commandsHandled.get(0));

        Assertions.assertSame(0,
                              commandBHandler.commandsHandled.size());

        // When
        result = testee.handle(command);

        // Then
        Assertions.assertEquals(command.message + " got handled",
                                result);
        Assertions.assertSame(2,
                              commandAHandler.commandsHandled.size());
        Assertions.assertSame(command,
                              commandAHandler.commandsHandled.get(1));

        Assertions.assertSame(0,
                              commandBHandler.commandsHandled.size());
    }
}

