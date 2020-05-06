
package camera;

import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;

public class CameraConverter implements Observable {
   
   public static final ObserverType<Void> VIEW_CHANGED = new ObserverType<>();
   
   private double worldXAtOrigin;
   private double worldZAtOrigin;
   private double zoom;
   
   private ObservableManager observableManager;
   
   public CameraConverter() {
      worldXAtOrigin = 0.0;
      worldZAtOrigin = 0.0;
      zoom = 1.0;
      
      observableManager = new ObservableManagerImpl();
   }
   
   public double toWorldX(double screenX) {
      return screenX * zoom + worldXAtOrigin;
   }
   
   public double vectorToWorldX(double vectorScreenX) {
      return zoom * vectorScreenX;
   }
   
   public double toWorldZ(double screenY) {
      return -screenY * zoom + worldZAtOrigin;
   }
   
   public double vectorToWorldZ(double vectorScreenY) {
      return -zoom * vectorScreenY;
   }
   
   public double toScreenX(double worldX) {
      return (worldX - worldXAtOrigin) / zoom;
   }
   
   public double vectorToScreenX(double vectorWorldX) {
      return vectorWorldX / zoom;
   }
   
   public double toScreenY(double worldZ) {
      return -(worldZ - worldZAtOrigin) / zoom;
   }
   
   public double vectorToScreenY(double vectorWorldZ) {
      //This should negate - TODO fix
      return vectorWorldZ / zoom;
   }
   
   public void setWorldXAtOrigin(double worldXAtOrigin) {
      this.worldXAtOrigin = worldXAtOrigin;
      observableManager.notifyObservers(VIEW_CHANGED, null);
   }
   
   public double getWorldXAtOrigin() {
      return worldXAtOrigin;
   }
   
   public void setWorldZAtOrigin(double worldZAtOrigin) {
      this.worldZAtOrigin = worldZAtOrigin;
      observableManager.notifyObservers(VIEW_CHANGED, null);
   }
   
   public double getWorldZAtOrigin() {
      return worldZAtOrigin;
   }
   
   public void setZoom(double zoom) {
      this.zoom = zoom;
      observableManager.notifyObservers(VIEW_CHANGED, null);
   }
   
   public double getZoom() {
      return zoom;
   }
   
   @Override
   public ObservableManager getObservableManager() {
      return observableManager;
   }
   
}
