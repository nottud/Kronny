
package graphics;

import model.command.AomMethod;
import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;

public class SelectedMethodModel implements Observable {
   
   public static final ObserverType<AomMethod> SELECTION_CHANGED = new ObserverType<>();
   
   private AomMethod selectedMethod;
   private ObservableManager observableManager;
   
   public SelectedMethodModel() {
      observableManager = new ObservableManagerImpl();
   }
   
   public void setSelectedMethod(AomMethod selectedMethod) {
      this.selectedMethod = selectedMethod;
      observableManager.notifyObservers(SELECTION_CHANGED, selectedMethod);
   }
   
   public AomMethod getSelectedMethod() {
      return selectedMethod;
   }
   
   @Override
   public ObservableManager getObservableManager() {
      return observableManager;
   }
   
}
