
package editor.unit.selection;

import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;

public class BoxSelectionModel implements Observable {
   
   public static final ObserverType<Void> UPDATED = new ObserverType<>();
   
   private boolean active;
   private double fromX;
   private double fromY;
   private double toX;
   private double toY;
   
   private ObservableManager observableManager;
   
   public BoxSelectionModel() {
      observableManager = new ObservableManagerImpl();
   }
   
   public void setActive(boolean active) {
      this.active = active;
      observableManager.notifyObservers(UPDATED, null);
   }
   
   public boolean isActive() {
      return active;
   }
   
   public void setFromX(double fromX) {
      this.fromX = fromX;
      observableManager.notifyObservers(UPDATED, null);
   }
   
   public double getFromX() {
      return fromX;
   }
   
   public void setFromY(double fromY) {
      this.fromY = fromY;
      observableManager.notifyObservers(UPDATED, null);
   }
   
   public double getFromY() {
      return fromY;
   }
   
   public void setToX(double toX) {
      this.toX = toX;
      observableManager.notifyObservers(UPDATED, null);
   }
   
   public double getToX() {
      return toX;
   }
   
   public void setToY(double toY) {
      this.toY = toY;
      observableManager.notifyObservers(UPDATED, null);
   }
   
   public double getToY() {
      return toY;
   }
   
   @Override
   public ObservableManager getObservableManager() {
      return observableManager;
   }
   
}
