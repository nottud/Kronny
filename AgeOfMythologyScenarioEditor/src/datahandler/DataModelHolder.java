
package datahandler;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.Observer;
import utility.observable.ObserverType;

public class DataModelHolder<T> implements Observable {
   
   private Collection<DataModel<T>> dataModels;
   
   private ObservableManager observableManager;
   private ObserverType<T> valueChangedObserverType;
   private ObserverType<T> userChangedValueObserverType;
   
   private Observer<T> internalValueObserver;
   
   private T value;
   
   public DataModelHolder() {
      this(Collections.emptyList());
   }
   
   public DataModelHolder(DataModel<T> initialDataModel) {
      this(Collections.singleton(initialDataModel));
   }
   
   public DataModelHolder(Collection<DataModel<T>> initialDataModels) {
      dataModels = Collections.emptyList();
      
      observableManager = new ObservableManagerImpl();
      valueChangedObserverType = new ObserverType<>();
      userChangedValueObserverType = new ObserverType<>();
      
      internalValueObserver = value -> calculateValue(true);
      setDataModels(initialDataModels);
   }
   
   public void setDataModels(Collection<DataModel<T>> newDataModels) {
      for (DataModel<T> model : dataModels) {
         model.getObservableManager().removeObserver(model.getValueChangedObserverType(), internalValueObserver);
      }
      dataModels = newDataModels;
      for (DataModel<T> model : newDataModels) {
         model.getObservableManager().addObserver(model.getValueChangedObserverType(), internalValueObserver);
      }
      calculateValue(false);
   }
   
   public Collection<DataModel<T>> getDataModels() {
      return dataModels;
   }
   
   private void calculateValue(boolean userChanged) {
      Set<T> values = dataModels.stream().map(DataModel::getValue).collect(Collectors.toCollection(LinkedHashSet::new));
      T newValue;
      if (values.size() == 1) {
         newValue = values.iterator().next();
      } else {
         newValue = null;
      }
      if (!Objects.equals(newValue, value)) {
         value = newValue;
         observableManager.notifyObservers(valueChangedObserverType, value);
         if (userChanged) {
            observableManager.notifyObservers(userChangedValueObserverType, value);
         }
      }
   }
   
   @Override
   public ObservableManager getObservableManager() {
      return observableManager;
   }
   
   public ObserverType<T> getValueChangedObserverType() {
      return valueChangedObserverType;
   }
   
   public Observer<T> addValueObserverAndImmediatelyNotify(Observer<T> observer) {
      observableManager.addObserver(valueChangedObserverType, observer);
      observer.notify(value);
      return observer;
   }
   
   public T getValue() {
      return value;
   }
   
}
