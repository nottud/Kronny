
package utility.observable;

/**
 * Manages {@link Observer}s and notifications allowing observers to be added
 * using {@link #addObserver(ObserverType, Observer)} and removed using
 * {@link #removeObserver(ObserverType, Observer)}. The
 * {@link #notifyObservers(ObserverType, Object)} notifies all observers with
 * the provided value.
 */
public interface ObservableManager {
   
   /**
    * Adds the provided {@link Observer} for the provided {@link ObserverType}
    * object.
    * 
    * @param <T>
    *           Type of value notifications will use.
    * @param observerType
    *           {@link ObserverType} gives what the {@link Observer} is listening
    *           on and provides type safety.
    * @param observer
    *           {@link Observer} to add.
    */
   public <T> void addObserver(ObserverType<T> observerType, Observer<? super T> observer);
   
   /**
    * Removes the provided {@link Observer} for the provided {@link ObserverType}
    * object.
    * 
    * @param <T>
    *           Type of value notifications will use.
    * @param observerType
    *           {@link ObserverType} gives what the {@link Observer} is listening
    *           on and provides type safety.
    * @param observer
    *           {@link Observer} to remove.
    */
   public <T> void removeObserver(ObserverType<T> observerType, Observer<? super T> observer);
   
   /**
    * Notifies all observers of the {@link ObserverType} a change with the new
    * provided value.
    * 
    * @param <T>
    *           Type of value notifications will use.
    * @param callType
    *           {@link ObserverType} provides what the {@link Observer}s are
    *           listening on.
    * @param value
    *           The new value associated with the change.
    */
   public <T> void notifyObservers(ObserverType<T> callType, T value);
   
}
