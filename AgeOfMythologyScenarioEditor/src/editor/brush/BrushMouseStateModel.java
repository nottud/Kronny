
package editor.brush;

import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.Observer;
import utility.observable.ObserverType;

public class BrushMouseStateModel implements Observable {
   
   public static final ObserverType<Void> BRUSH_DOWN = new ObserverType<>();
   public static final ObserverType<Void> BRUSH_DRAGGED = new ObserverType<>();
   public static final ObserverType<Void> BRUSH_UP = new ObserverType<>();
   
   private boolean brushDown;
   private ObservableManager observableManager;
   
   public BrushMouseStateModel(BrushModel brushModel) {
      observableManager = new ObservableManagerImpl();
      
      Observer<Object> observer = value -> handleBrushChanged();
      brushModel.getObservableManager().addObserver(BrushModel.POS_X_CHANGED, observer);
      brushModel.getObservableManager().addObserver(BrushModel.POS_Z_CHANGED, observer);
      brushModel.getObservableManager().addObserver(BrushModel.WIDTH_CHANGED, observer);
      brushModel.getObservableManager().addObserver(BrushModel.HEIGHT_CHANGED, observer);
   }
   
   public void brushDown() {
      if (!brushDown) {
         brushDown = true;
         observableManager.notifyObservers(BRUSH_DOWN, null);
      }
   }
   
   public void brushUp() {
      if (brushDown) {
         brushDown = false;
         observableManager.notifyObservers(BRUSH_UP, null);
      }
   }
   
   private void handleBrushChanged() {
      if (brushDown) {
         observableManager.notifyObservers(BRUSH_DRAGGED, null);
      }
   }
   
   @Override
   public ObservableManager getObservableManager() {
      return observableManager;
   }
   
}
