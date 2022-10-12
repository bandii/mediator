package hu.ajprods;

import java.util.function.Supplier;

/**
 * The command's type helps to find the proper middleware(s), similar to the ICommandHandler mechanism.
 * The TResult is needed for manipulating the result of each middlewares in the pipeline.
 */
public interface ICommandMiddleware<TCommand extends ICommand<TResult>, TResult> {
    TResult handle(final TCommand command, final Supplier<TResult> next);
}
