package hu.ajprods;

/**
 * @see IEventMediator
 */
public interface IEventHandler<TEvent extends IEvent> {
    void handle(TEvent command);
}
