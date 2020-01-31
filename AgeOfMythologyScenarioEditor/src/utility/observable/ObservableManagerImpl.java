package utility.observable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of {@link ObservableManager}.
 */
public class ObservableManagerImpl implements ObservableManager {

	private final Map<ObserverType<?>, Set<Observer<?>>> observerTypeToObservers;

	/**
	 * Constructor creating the internal {@link Map}.
	 */
	public ObservableManagerImpl() {
		observerTypeToObservers = new LinkedHashMap<>();
	}

	@Override
	public <T> void addObserver(ObserverType<T> observerType, Observer<? super T> observer) {
		Set<Observer<?>> observers = observerTypeToObservers.get(observerType);
		if (observers == null) {
			observers = new LinkedHashSet<>();
			observerTypeToObservers.put(observerType, observers);
		}
		observers.add(observer);
	}

	@Override
	public <T> void removeObserver(ObserverType<T> observerType, Observer<? super T> observer) {
		Set<Observer<?>> observers = observerTypeToObservers.get(observerType);
		if(observers != null) {
			observers.remove(observer);
		}
	}

	@Override
	public <T> void notifyObservers(ObserverType<T> observerType, T value) {
		Set<Observer<?>> observers = observerTypeToObservers.get(observerType);
		if (observers != null) {
			List<Observer<? super T>> typedObserversCopy = new ArrayList<>();
			for (Observer<?> observer : observers) {
				Observer<? super T> castObserver = observerType.castObserver(observer);
				typedObserversCopy.add(castObserver);
			}
			for (Observer<? super T> observer : typedObserversCopy) {
				observer.notify(value);
			}
		}
	}

}
