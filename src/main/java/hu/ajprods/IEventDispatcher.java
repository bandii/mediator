package hu.ajprods;

/**
 * @see IEventMediator
 */
public interface IEventDispatcher {
    <TEvent extends IEvent> void notify(TEvent event)
            throws MediatorException;
}
