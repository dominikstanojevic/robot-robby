package hr.fer.zemris.projekt.observer;

/**
 * Represents an {@code Observer} in the
 * <a href="https://en.wikipedia.org/wiki/Observer_pattern">observer design pattern.</a>.
 * A class implementing this method will receive information about relevant changed to the object
 * being observed.
 *
 * @param <T> type in which observations will be received
 * @author Leon Luttenberger
 */
public interface Observer<T> {

    /**
     * This method is called whenever the {@code subject} being observed performs the observable operation.
     *
     * @param sender method being observer
     * @param observation object representing the observation
     */
    void observationMade(Observable sender, T observation);
}
