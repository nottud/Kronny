
package datahandler.lengthdependency;

import datahandler.DataModel;
import utility.observable.Observer;

public class LengthDependency<T> {
   
   private DataModel<Integer> lengthModel;
   private DataModel<T> dependentModel;
   
   private Observer<Object> aboutToChangeObserver;
   private Observer<Object> changedObserver;
   
   private boolean active;
   
   private int oldLength;
   
   public LengthDependency(DataModel<Integer> lengthModel, DataModel<T> dependentModel) {
      this.lengthModel = lengthModel;
      this.dependentModel = dependentModel;
      
      aboutToChangeObserver = value -> aboutToChange();
      changedObserver = value -> changed();
      
      setActive(true);
   }
   
   public void setActive(boolean newActive) {
      if (!active && newActive) {
         active = true;
         dependentModel.getObservableManager().addObserver(dependentModel.getValueAboutToChangeObserverType(), aboutToChangeObserver);
         dependentModel.getObservableManager().addObserver(dependentModel.getValueChangedObserverType(), changedObserver);
      } else if (active && !newActive) {
         active = false;
         dependentModel.getObservableManager().removeObserver(dependentModel.getValueAboutToChangeObserverType(), aboutToChangeObserver);
         dependentModel.getObservableManager().removeObserver(dependentModel.getValueChangedObserverType(), changedObserver);
      }
   }
   
   private void aboutToChange() {
      oldLength = dependentModel.getDataConverter().toBytes(dependentModel.getValue()).size();
   }
   
   private void changed() {
      int newLength = dependentModel.getDataConverter().toBytes(dependentModel.getValue()).size();
      lengthModel.setValue(lengthModel.getValue() + newLength - oldLength);
   }
   
}
