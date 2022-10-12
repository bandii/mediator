package hu.ajprods;

/**
 * An empty object to be used in the framework.
 *
 * @apiNote Use the VOID static member
 */
public final class Void {
    /**
     * A single Void object
     */
    public static Void VOID = new Void();

    private Void() {

    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Void;
    }

    @Override
    public int hashCode() {
        return this.getClass()
                   .getName()
                   .hashCode();
    }

    @Override
    public String toString() {
        return this.getClass()
                   .getSimpleName();
    }
}
