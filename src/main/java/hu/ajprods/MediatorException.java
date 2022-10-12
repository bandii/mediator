package hu.ajprods;

/**
 * Wrapper around your handlers' exceptions
 */
public class MediatorException
        extends RuntimeException {
    MediatorException(Throwable e) {
        super(e);
    }
}
