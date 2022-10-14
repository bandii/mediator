package hu.ajprods;

/**
 * @see ICommandMediator
 */
public interface ICommandDispatcher {
    <TCommand extends ICommand<TResult>, TResult> TResult handle(TCommand command)
            throws NoHandlerFoundException;
}
