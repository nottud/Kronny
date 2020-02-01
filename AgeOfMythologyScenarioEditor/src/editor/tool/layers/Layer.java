
package editor.tool.layers;

import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObserverType;

public enum Layer implements Observable {
   
   TERRAIN_ELEVATION(false),
   WATER(false),
   WATER_COLOUR(false),
   WATER_ELEVATION(false);
   
   public static final ObserverType<Void> VISIBLE = new ObserverType<>();
   public static final ObserverType<Void> INVISIBLE = new ObserverType<>();
   
   private boolean visibleFromUser;
   private boolean visibleFromTool;
   
   private ObservableManager observableManager;
   
   private boolean visible;
   
   private Layer(boolean defaultState) {
      visibleFromUser = defaultState;
      visibleFromTool = false;
      
      updateVisibility();
   }
   
   private void updateVisibility() {
      boolean newVisible = visibleFromUser || visibleFromTool;
      if (newVisible && !visible) {
         visible = true;
         observableManager.notifyObservers(VISIBLE, null);
      } else if (!newVisible && visible) {
         visible = false;
         observableManager.notifyObservers(INVISIBLE, null);
      }
   }
   
   @Override
   public ObservableManager getObservableManager() {
      return observableManager;
   }
   
}
