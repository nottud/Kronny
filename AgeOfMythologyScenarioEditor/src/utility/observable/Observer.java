package utility.observable;

/**
 * Interface allowing listening to notifications of a given type.
 * 
 * @param <T>
 *            Type of value the observer is listening on.
 */
@FunctionalInterface
public interface Observer<T> {

	/**
	 * Notification on change with the provided associated value.
	 * 
	 * @param value
	 *            Value to notify with.
	 */
	public void notify(T value);

}
