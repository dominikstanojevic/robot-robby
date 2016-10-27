package hr.fer.zemris.projekt.observer;

/**
 * This interface represents an observable object.
 * It can be implemented to represent an object that the application wants to have observed.
 * @param <T> type representing the observation
 *
 * @author Leon Luttenberger
 */
public interface Observable<T> {

    /**
     * Adds the specified observer to the list of observers for this object.
     * @param observer observer to be added
     */
    void addObserver(Observer<T> observer);

    /**
     * Removes the specified observer from the list of observers.
     * @param observer observer to remove
     */
    void removeObserver(Observer<T> observer);

    /**
     * Informs the observers that on action worth observing has taken place.
     *
     * @param observation object representing the observation
     */
    void fire(T observation);
}
