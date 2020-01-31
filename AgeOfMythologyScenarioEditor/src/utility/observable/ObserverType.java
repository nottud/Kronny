package utility.observable;

/**
 * Defines a type of object an {@link Observer} can {@link Observer} on.
 * 
 * @param <T>
 *            Type of value to observe on.
 */
public class ObserverType<T> {

	/**
	 * Hard casts the provided {@link Object} to the correct type. This does not
	 * check the type and should only be used internally.
	 * 
	 * @param observer
	 *            {@link Object} to cast.
	 * @return The {@link Object} casted.
	 */
	@SuppressWarnings("unchecked") // Type not checked - external should check the type.
	Observer<? super T> castObserver(Observer<?> observer) {
		return (Observer<? super T>) observer;
	}

}
