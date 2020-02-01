
package datahandler;

import java.util.List;

import datahandler.converter.DataConverter;
import datahandler.location.DataLocationFinder;
import datahandler.location.LocationNotFoundException;
import mapmodel.ChildModel;
import mapmodel.ParentModel;
import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.Observer;
import utility.observable.ObserverType;

public class DataModel<T> implements Observable, ChildModel {
   
   private ParentModel parent;
   
   private DataConverter<T> dataConverter;
   private DataHandler<T> dataHandler;
   private ObserverType<T> valueChangedObserverType;
   private ObservableManager observableManager;
   
   private T value;
   
   public DataModel(ParentModel parent, DataLocationFinder dataLocationFinder, DataConverter<T> dataConverter) {
      this.parent = parent;
      this.dataConverter = dataConverter;
      dataHandler = new DataHandler<>(dataLocationFinder, dataConverter);
      valueChangedObserverType = new ObserverType<>();
      observableManager = new ObservableManagerImpl();
      value = dataConverter.createDefaultValue();
   }
   
   @Override
   public void readAllModels(List<Byte> data, int offsetHint) throws LocationNotFoundException {
      value = dataHandler.readValue(data, offsetHint);
      observableManager.notifyObservers(valueChangedObserverType, value);
   }
   
   @Override
   public void writeAllModels(List<Byte> data, int offsetHint) throws LocationNotFoundException {
      dataHandler.writeValue(data, offsetHint, value);
   }
   
   public T getValue() {
      return value;
   }
   
   public void setValue(T value) {
      this.value = value;
      observableManager.notifyObservers(valueChangedObserverType, value);
   }
   
   public ObserverType<T> getValueChangedObserverType() {
      return valueChangedObserverType;
   }
   
   public Observer<T> addValueObserverAndImmediatelyNotify(Observer<T> observer) {
      observableManager.addObserver(valueChangedObserverType, observer);
      observer.notify(value);
      return observer;
   }
   
   public DataLocationFinder getDataLocationFinder() {
      return dataHandler.getDataLocationFinder();
   }
   
   public DataConverter<T> getDataConverter() {
      return dataConverter;
   }
   
   @Override
   public ObservableManager getObservableManager() {
      return observableManager;
   }
   
   @Override
   public ParentModel getParentModel() {
      return parent;
   }
   
}
