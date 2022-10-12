package hu.ajprods;

import java.util.Set;

/**
 * @see IEventMediator
 */
public interface IEventHandlerRegister {
    IEventHandlerRegister addHandlers(Set<IEventHandler<? extends IEvent>> handlers);

    <TEvent extends IEvent> IEventHandlerRegister addHandler(IEventHandler<TEvent> handler);
}
