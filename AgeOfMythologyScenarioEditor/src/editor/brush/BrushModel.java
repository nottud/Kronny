
package editor.brush;

import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;

public class BrushModel implements Observable {
   
   public static final ObserverType<Integer> POS_X_CHANGED = new ObserverType<>();
   public static final ObserverType<Integer> POS_Z_CHANGED = new ObserverType<>();
   public static final ObserverType<Integer> WIDTH_CHANGED = new ObserverType<>();
   public static final ObserverType<Integer> HEIGHT_CHANGED = new ObserverType<>();
   
   private int posX;
   private int posZ;
   
   private int width;
   private int height;
   
   private ObservableManager observableManager;
   
   public BrushModel() {
      posX = 0;
      posZ = 0;
      
      width = 1;
      height = 1;
      
      observableManager = new ObservableManagerImpl();
   }
   
   public int getPosX() {
      return posX;
   }
   
   public void setPosX(int posX) {
      if (this.posX != posX) {
         this.posX = posX;
         observableManager.notifyObservers(POS_X_CHANGED, posX);
      }
   }
   
   public int getPosZ() {
      return posZ;
   }
   
   public void setPosZ(int posZ) {
      if (this.posZ != posZ) {
         this.posZ = posZ;
         observableManager.notifyObservers(POS_Z_CHANGED, posZ);
      }
   }
   
   public int getWidth() {
      return width;
   }
   
   public void setWidth(int width) {
      if (this.width != width) {
         this.width = width;
         observableManager.notifyObservers(WIDTH_CHANGED, width);
      }
   }
   
   public int getHeight() {
      return height;
   }
   
   public void setHeight(int height) {
      if (this.height != height) {
         this.height = height;
         observableManager.notifyObservers(HEIGHT_CHANGED, height);
      }
   }
   
   @Override
   public ObservableManager getObservableManager() {
      return observableManager;
   }
   
}
