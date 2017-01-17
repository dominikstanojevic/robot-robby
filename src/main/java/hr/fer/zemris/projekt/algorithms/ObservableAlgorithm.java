package hr.fer.zemris.projekt.algorithms;

import hr.fer.zemris.projekt.observer.Observable;
import hr.fer.zemris.projekt.observer.Observer;
import hr.fer.zemris.projekt.observer.observations.TrainingResult;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of the {@link Algorithm} interface, which adds the
 * functionality for this algorithm to be used as the subject in the
 * observer design pattern. The observers will be notified of the
 * best result from the latest complete iteration of the algorithm.
 *
 * @author Leon Luttenberger
 * @version 1.0.1
 */
public abstract class ObservableAlgorithm implements Observable<TrainingResult>, Algorithm {

    /**
     * List of observers.
     */
    private List<Observer<TrainingResult>> observers;

    @Override
    public void addObserver(Observer<TrainingResult> observer) {
        if (observers == null) {
            observers = new ArrayList<>();
        }

        observers = new ArrayList<>(observers);
        observers.add(observer);
    }

    /**
     * Notifies the listeners with a {@link TrainingResult}
     * object only if somebody is observing this object.
     *
     * @param best           best result of the iteration
     * @param averageFitness average fitness of the iteration
     * @param iteration      current iteration
     */
    protected void notifyListeners(Robot best, double averageFitness, int iteration) {
        if (observers == null || observers.isEmpty()) {
            return;
        }

        this.fire(new TrainingResult(best, averageFitness, iteration));
    }

    @Override
    public void removeObserver(Observer<TrainingResult> observer) {
        if (observers != null) {
            observers = new ArrayList<>();
            observers.remove(observer);
        }
    }

    @Override
    public void fire(TrainingResult observation) {
        if (observers != null) {
            for (Observer<TrainingResult> observer : observers) {
                observer.observationMade(this, observation);
            }
        }
    }
}
