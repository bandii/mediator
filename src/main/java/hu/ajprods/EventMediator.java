package hu.ajprods;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class EventMediator
        implements IEventMediator {

    private final HashMap<String, Set<IEventHandler<IEvent>>> eventHandlers = new HashMap<>();

    @Override
    public IEventHandlerRegister addHandlers(Set<IEventHandler<? extends IEvent>> handlers) {
        if (handlers == null) {
            throw new NullPointerException("Cannot register no handlers");
        }

        handlers.forEach(this::addHandler);

        return this;
    }

    @Override
    public <TEvent extends IEvent> IEventHandlerRegister addHandler(IEventHandler<TEvent> handler) {
        if (handler == null) {
            throw new NullPointerException("Cannot register no handler");
        }

        var eventTypeName = ((ParameterizedType) handler.getClass().getGenericInterfaces()[0])
                .getActualTypeArguments()[0]
                .getTypeName();

        var handlers = eventHandlers.get(eventTypeName);
        if (handlers == null) {
            var newSet = new HashSet<IEventHandler<IEvent>>(1);
            newSet.add((IEventHandler<IEvent>) handler);

            eventHandlers.put(eventTypeName,
                              newSet);
        }
        else {
            handlers.add((IEventHandler<IEvent>) handler);
        }

        return this;
    }

    @Override
    public <TEvent extends IEvent> void notify(TEvent event)
        throws MediatorException {
        if (event == null) {
            throw new NullPointerException("Cannot process null event!");
        }

        var key = event.getClass()
                       .getName();

        var handlers = eventHandlers.get(key);
        if (handlers == null || handlers.size() == 0) {
            return;
        }

        handlers.forEach(handler -> {
            try {
                handler.handle(event);
            }
            catch (Throwable e) {
                throw new MediatorException(e);
            }
        });
    }
}
