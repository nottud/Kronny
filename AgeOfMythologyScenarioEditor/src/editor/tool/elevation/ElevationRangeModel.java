
package editor.tool.elevation;

import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;

public class ElevationRangeModel implements Observable {
   
   public static final ObserverType<Float> BOTTOM_RANGE_CHANGE = new ObserverType<>();
   public static final ObserverType<Float> TOP_RANGE_CHANGE = new ObserverType<>();
   public static final ObserverType<Double> OPACITY_CHANGE = new ObserverType<>();
   
   private ObservableManager observableManager;
   
   private float bottomRange;
   private float topRange;
   private double opacity;
   
   public ElevationRangeModel() {
      observableManager = new ObservableManagerImpl();
      
      bottomRange = -5.0F;
      topRange = 15.0F;
      opacity = 0.5;
   }
   
   public float getBottomRange() {
      return bottomRange;
   }
   
   public void setBottomRange(float bottomRange) {
      this.bottomRange = bottomRange;
      observableManager.notifyObservers(BOTTOM_RANGE_CHANGE, bottomRange);
   }
   
   public float getTopRange() {
      return topRange;
   }
   
   public void setTopRange(float topRange) {
      this.topRange = topRange;
      observableManager.notifyObservers(TOP_RANGE_CHANGE, topRange);
   }
   
   public double getOpacity() {
      return opacity;
   }
   
   public void setOpacity(double opacity) {
      this.opacity = opacity;
      observableManager.notifyObservers(OPACITY_CHANGE, opacity);
   }
   
   @Override
   public ObservableManager getObservableManager() {
      return observableManager;
   }
   
}
