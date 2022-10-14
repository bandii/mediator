package hu.ajprods;

public class Mediator
        implements ICommandDispatcher,
                   IEventDispatcher {

    private final ICommandMediator Command;

    private final IEventMediator Event;

    public Mediator() {
        Command = new CommandMediator();
        Event = new EventMediator();
    }

    public Mediator(ICommandMediator commandMediator,
                    IEventMediator eventMediator) {
        Command = commandMediator;
        Event = eventMediator;
    }

    public ICommandHandlerRegister commandHandler() {
        return Command;
    }

    public IEventHandlerRegister eventHandler() {
        return Event;
    }

    @Override
    public <TCommand extends ICommand<TResult>, TResult> TResult handle(TCommand command)
            throws NoHandlerFoundException {
        return Command.handle(command);
    }

    @Override
    public <TEvent extends IEvent> void notify(TEvent event) {
        Event.notify(event);
    }
}
