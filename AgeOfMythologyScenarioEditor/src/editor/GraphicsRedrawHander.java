
package editor;

import javafx.application.Platform;
import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;

public class GraphicsRedrawHander implements Observable {
   
   public static final ObserverType<Void> PERFORM_REDRAW = new ObserverType<>();
   
   private boolean redrawRequested;
   
   private ObservableManager observableManager;
   
   public GraphicsRedrawHander() {
      observableManager = new ObservableManagerImpl();
   }
   
   public void requestRedraw() {
      if (!redrawRequested) {
         redrawRequested = true;
         Platform.runLater(this::performRedraw);
      }
   }
   
   private void performRedraw() {
      redrawRequested = false;
      observableManager.notifyObservers(PERFORM_REDRAW, null);
   }
   
   @Override
   public ObservableManager getObservableManager() {
      return observableManager;
   }
   
}
