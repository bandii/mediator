package hu.ajprods;

/**
 * 1-n mapping between events and handlers. Inheritance of the events are not supported,
 * therefore one type of event will be handled only by those handlers, that are subscribed only to
 * that type of event
 */
public interface IEventMediator
        extends IEventHandlerRegister,
                IEventDispatcher {

}
