package hu.ajprods;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.Supplier;

public class CommandMediator
        implements ICommandMediator {

    private final HashMap<String, ICommandHandler<ICommand<?>, ?>> commandHandlers = new HashMap<>();

    @Override
    public ICommandHandlerRegister addHandlers(Set<ICommandHandler<? extends ICommand<?>, ?>> handlers) {
        if (handlers == null) {
            throw new NullPointerException("Cannot register no handlers");
        }

        handlers.forEach(this::addHandler);

        return this;
    }

    @Override
    public <TCommand extends ICommand<TResult>, TResult> ICommandHandlerRegister addHandler(ICommandHandler<TCommand, TResult> commandHandler) {
        if (commandHandler == null) {
            throw new NullPointerException("Cannot register null handler");
        }

        var commandTypeName = ((ParameterizedType) commandHandler.getClass()
                                                                 .getGenericInterfaces()[0])
                .getActualTypeArguments()[0]
                .getTypeName();

        if (commandHandlers.containsKey(commandTypeName)) {
            throw new UnsupportedOperationException("One command can have only one handler!");
        }

        commandHandlers.put(commandTypeName,
                            (ICommandHandler<ICommand<?>, ?>) commandHandler);

        return this;
    }

    @Override
    public <TCommand extends ICommand<TResult>, TResult> TResult handle(TCommand command)
            throws NoHandlerFoundException {
        if (command == null) {
            throw new NullPointerException("Cannot process null command!");
        }

        var commandClass = command.getClass()
                                  .getName();

        var handler = commandHandlers.get(commandClass);
        if (handler == null) {
            throw new NoHandlerFoundException(String.format("There are no handlers for command %s",
                                                            commandClass));
        }

        // The conversions must be made, so the client will get what it is expecting.
        // The type-safety is ensured via the ICommand's and the ICommandHandler's generic params.

        return (TResult) applyMiddlewares(command,
                                          0,
                                          () -> handler.handle(command));
    }

    private final Map<String, List<ICommandMiddleware>> middlewares = new HashMap<>();

    @Override
    public ICommandHandlerRegister addMiddlewares(LinkedList<ICommandMiddleware> middlewares) {
        if (middlewares == null) {
            throw new NullPointerException("Cannot register no middlewares");
        }

        middlewares.forEach(this::addMiddleware);

        return this;
    }

    @Override
    public ICommandHandlerRegister addMiddleware(ICommandMiddleware middleware) {
        if (middleware == null) {
            throw new NullPointerException("Cannot register no middleware");
        }

        var commandTypeName = ((ParameterizedType) middleware.getClass()
                                                             .getGenericInterfaces()[0])
                .getActualTypeArguments()[0]
                .getTypeName();

        var handlers = middlewares.get(commandTypeName);
        if (handlers == null) {
            var newSet = new LinkedList<ICommandMiddleware>();
            newSet.add(middleware);

            middlewares.put(commandTypeName,
                            newSet);
        }
        else {
            handlers.add(middleware);
        }

        return this;
    }

    protected <TCommand extends ICommand<?>> Object applyMiddlewares(final TCommand command,
                                                                     final int index,
                                                                     final Supplier<Object> handle) {
        List<ICommandMiddleware> middlewares = this.middlewares.get(command.getClass()
                                                                           .getName());
        if (middlewares == null) {
            return handle.get();
        }

        var middleware = middlewares.get(index);
        if (middleware == null) {
            return handle.get();
        }

        return middleware.handle(command,
                                 () -> {
                                     if (index == middlewares.size() - 1) {
                                         return handle.get();
                                     }
                                     else {
                                         return applyMiddlewares(command,
                                                                 index + 1,
                                                                 handle);
                                     }
                                 });
    }
}
