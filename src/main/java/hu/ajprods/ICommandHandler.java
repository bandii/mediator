package hu.ajprods;

/**
 * @see ICommandMediator
 */
public interface ICommandHandler<TCommand extends ICommand<TResult>, TResult> {
    TResult handle(TCommand command);
}
