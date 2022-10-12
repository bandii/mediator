package hu.ajprods;

import java.util.LinkedList;
import java.util.Set;

/**
 * @see ICommandMediator
 */
public interface ICommandHandlerRegister {
    ICommandHandlerRegister addHandlers(Set<ICommandHandler<? extends ICommand<?>, ?>> handlers);

    <TCommand extends ICommand<TResult>, TResult> ICommandHandlerRegister addHandler(ICommandHandler<TCommand, TResult> commandHandler);

    ICommandHandlerRegister addMiddlewares(LinkedList<ICommandMiddleware> middlewares);

    ICommandHandlerRegister addMiddleware(ICommandMiddleware middleware);
}
