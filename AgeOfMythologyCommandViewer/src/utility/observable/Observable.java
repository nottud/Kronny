
package utility.observable;

/**
 * Defines an object containing an {@link ObservableManager}.
 */
public interface Observable {
   
   /**
    * Returns the {@link ObservableManager} being used by this object.
    * 
    * @return {@link ObservableManager} being used.
    */
   public ObservableManager getObservableManager();
   
}
