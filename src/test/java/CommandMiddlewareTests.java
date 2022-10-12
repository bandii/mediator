import fakes.command.*;
import fakes.command.middleware.FakeAMiddleware;
import fakes.command.middleware.FakeBMiddleware;
import fakes.command.middleware.FakeVoidMiddleware;
import hu.ajprods.Void;
import hu.ajprods.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Set;

public class CommandMiddlewareTests {

    private Mediator testee;

    @BeforeEach
    public void SetUp() {
        testee = new Mediator();
    }

    @Test
    public void addMiddleware_null_FAIL() {
        Assertions.assertThrows(NullPointerException.class,
                                () -> testee.commandHandler()
                                            .addMiddleware(null));
    }

    @Test
    public void addMiddlewares_null_FAIL() {
        Assertions.assertThrows(NullPointerException.class,
                                () -> testee.commandHandler()
                                            .addMiddlewares(null));
    }

    @Test
    public void handle_null_FAIL() {
        // Given
        var middlewareA = new FakeAMiddleware();
        testee.commandHandler()
              .addMiddleware(middlewareA);

        // When
        Assertions.assertThrows(NullPointerException.class,
                                () -> testee.handle(null));

        // Then
        Assertions.assertSame(0,
                              middlewareA.commandsHandled.size());
    }

    @Test
    public void singleEvent_multiSimilarHandler_FAIL() {
        // Given
        var middlewareA = new FakeAMiddleware();
        testee.commandHandler()
              .addMiddleware(middlewareA);

        var commandAHandler = new FakeACommandHandler();
        var commandAHandler_2 = new FakeACommandHandler();

        // When
        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> testee.commandHandler()
                                            .addHandlers(Set.of(commandAHandler,
                                                                commandAHandler_2)));

        // Then
        Assertions.assertSame(0,
                              middlewareA.commandsHandled.size());
    }

    @Test
    public void noHandler_FAIL() {
        // Given
        var middlewareA = new FakeAMiddleware();
        testee.commandHandler()
              .addMiddleware(middlewareA);

        // When
        Assertions.assertThrows(NoHandlerFoundException.class,
                                () -> testee.handle(new FakeACommand("fail")));

        // Then
        Assertions.assertSame(0,
                              middlewareA.commandsHandled.size());
    }

    @Test
    public void singleCommand_singleMiddleware_OK()
            throws NoHandlerFoundException {
        // Given
        var middlewareA = new FakeAMiddleware();
        testee.commandHandler()
              .addMiddleware(middlewareA);

        var commandHandler = new FakeACommandHandler();
        testee.commandHandler()
              .addHandler(commandHandler);

        var command = new FakeACommand("singleCommand_OK");

        // When
        var result = testee.handle(command);

        // Then
        Assertions.assertSame(1,
                              middlewareA.commandsHandled.size());
        Assertions.assertSame(command,
                              middlewareA.commandsHandled.get(0));

        Assertions.assertSame(1,
                              command.middlewaresVisited.size());
        Assertions.assertSame(middlewareA,
                              command.middlewaresVisited.get(0));

        Assertions.assertEquals(command.message + " got handled",
                                result);
        Assertions.assertSame(1,
                              commandHandler.commandsHandled.size());
        Assertions.assertSame(command,
                              commandHandler.commandsHandled.get(0));
    }

    @Test
    public void singleCommand_multipleMiddlewares_orderCheck_OK()
            throws NoHandlerFoundException {
        // Given
        // Let's say, these two middlewares are somewhere different, but listen to the same Command
        var middlewareA_1 = new FakeAMiddleware();
        var middlewareA_2 = new FakeAMiddleware();
        var orderedList = new LinkedList<ICommandMiddleware>();

        orderedList.add(middlewareA_1);
        orderedList.add(middlewareA_2);

        testee.commandHandler()
              .addMiddlewares(orderedList);

        var commandHandler = new FakeACommandHandler();
        testee.commandHandler()
              .addHandler(commandHandler);

        var command = new FakeACommand("singleCommand_OK");

        // When
        var result = testee.handle(command);

        // Then
        Assertions.assertSame(1,
                              middlewareA_1.commandsHandled.size());
        Assertions.assertSame(command,
                              middlewareA_1.commandsHandled.get(0));
        Assertions.assertSame(1,
                              middlewareA_2.commandsHandled.size());
        Assertions.assertSame(command,
                              middlewareA_2.commandsHandled.get(0));

        Assertions.assertSame(2,
                              command.middlewaresVisited.size());
        Assertions.assertSame(middlewareA_1,
                              command.middlewaresVisited.get(0));
        Assertions.assertSame(middlewareA_2,
                              command.middlewaresVisited.get(1));

        Assertions.assertEquals(command.message + " got handled",
                                result);
        Assertions.assertSame(1,
                              commandHandler.commandsHandled.size());
        Assertions.assertSame(command,
                              commandHandler.commandsHandled.get(0));
    }

    @Test
    public void singleVoidCommand_singleMiddleware_OK()
            throws NoHandlerFoundException {
        // Given
        var middlewareVoid = new FakeVoidMiddleware();
        testee.commandHandler()
              .addMiddleware(middlewareVoid);

        var commandHandler = new FakeVoidCommandHandler();
        testee.commandHandler()
              .addHandler(commandHandler);

        var command = new FakeVoidCommand("singleVoidCommand_OK");

        // When
        var result = testee.handle(command);

        // Then
        Assertions.assertSame(1,
                              middlewareVoid.commandsHandled.size());
        Assertions.assertSame(command,
                              middlewareVoid.commandsHandled.get(0));

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
    public void singleCommand_singleMiddleware_faultyHandler_OK() {
        // Given
        var middlewareA = new FakeAMiddleware();
        testee.commandHandler()
              .addMiddleware(middlewareA);

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
                              middlewareA.commandsHandled.size());
        Assertions.assertSame(command,
                              middlewareA.commandsHandled.get(0));

        Assertions.assertSame(1,
                              command.middlewaresVisited.size());
        Assertions.assertSame(middlewareA,
                              command.middlewaresVisited.get(0));

        Assertions.assertSame(1,
                              commandHandler.commandsHandled.size());
        Assertions.assertSame(command,
                              commandHandler.commandsHandled.get(0));
    }

    @Test
    public void singleCommand_multipleKindMiddleware_OK()
            throws NoHandlerFoundException {
        // Given
        var middlewareA = new FakeAMiddleware();
        var middlewareB = new FakeBMiddleware();
        var orderedList = new LinkedList<ICommandMiddleware>();

        orderedList.add(middlewareA);
        orderedList.add(middlewareB);

        testee.commandHandler()
              .addMiddlewares(orderedList);

        var commandAHandler = new FakeACommandHandler();
        var commandBHandler = new FakeBCommandHandler();
        testee.commandHandler()
              .addHandlers(Set.of(commandAHandler,
                                  commandBHandler));

        var command = new FakeACommand("singleCommand_multiHandler_OK");

        // When
        var result = testee.handle(command);

        // Then
        Assertions.assertSame(1,
                              middlewareA.commandsHandled.size());
        Assertions.assertSame(command,
                              middlewareA.commandsHandled.get(0));
        Assertions.assertSame(0,
                              middlewareB.commandsHandled.size());

        Assertions.assertSame(1,
                              command.middlewaresVisited.size());
        Assertions.assertSame(middlewareA,
                              command.middlewaresVisited.get(0));

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
        var middlewareA = new FakeAMiddleware();
        var middlewareB = new FakeBMiddleware();
        var orderedList = new LinkedList<ICommandMiddleware>();

        orderedList.add(middlewareA);
        orderedList.add(middlewareB);

        testee.commandHandler()
              .addMiddlewares(orderedList);

        var commandAHandler = new FakeACommandHandler();
        var commandBHandler = new FakeBCommandHandler();
        testee.commandHandler()
              .addHandlers(Set.of(commandAHandler,
                                  commandBHandler));

        var command = new FakeACommand("MultiCommand_multiHandler_OK");

        // When
        var result = testee.handle(command);

        // Then
        Assertions.assertSame(1,
                              middlewareA.commandsHandled.size());
        Assertions.assertSame(command,
                              middlewareA.commandsHandled.get(0));

        Assertions.assertSame(1,
                              command.middlewaresVisited.size());
        Assertions.assertSame(middlewareA,
                              command.middlewaresVisited.get(0));

        Assertions.assertEquals(command.message + " got handled",
                                result);
        Assertions.assertSame(1,
                              commandAHandler.commandsHandled.size());
        Assertions.assertSame(command,
                              commandAHandler.commandsHandled.get(0));

        Assertions.assertSame(0,
                              commandBHandler.commandsHandled.size());

        // When
        var commandB = new FakeBCommand("MultiCommand_multiHandler_OK");
        result = testee.handle(commandB);

        // Then
        Assertions.assertSame(1,
                              middlewareB.commandsHandled.size());
        Assertions.assertSame(commandB,
                              middlewareB.commandsHandled.get(0));

        Assertions.assertSame(1,
                              commandB.middlewaresVisited.size());
        Assertions.assertSame(middlewareB,
                              commandB.middlewaresVisited.get(0));

        Assertions.assertEquals(commandB.message + " got handled",
                                result);
        Assertions.assertSame(1,
                              commandBHandler.commandsHandled.size());
        Assertions.assertSame(commandB,
                              commandBHandler.commandsHandled.get(0));

        Assertions.assertSame(1,
                              commandAHandler.commandsHandled.size());
    }
}

