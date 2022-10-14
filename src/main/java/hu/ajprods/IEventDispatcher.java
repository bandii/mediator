package hu.ajprods;

/**
 * @see IEventMediator
 */
public interface IEventDispatcher {

    /**
     * Notifying the handlers about the event happened
     */
    <TEvent extends IEvent> void notify(TEvent event);
}
