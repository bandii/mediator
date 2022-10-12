package hu.ajprods;

/**
 * 1-1 mapping between commands and handlers, because one command is one initiative!
 *
 * @see Void the empty or 'void' result of a command
 */
public interface ICommandMediator
        extends ICommandHandlerRegister,
                ICommandDispatcher {

}
