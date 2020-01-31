
package editor.colourchooser;

import javafx.scene.paint.Color;
import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;

public class ColourSelectionModel implements Observable {
   
   public static final ObserverType<Color> COLOUR_CHANGED = new ObserverType<>();
   
   private ObservableManager observableManager;
   
   private Color colour;
   
   public ColourSelectionModel() {
      observableManager = new ObservableManagerImpl();
      colour = Color.BLACK;
   }
   
   public Color getColour() {
      return colour;
   }
   
   public void setColour(Color colour) {
      this.colour = colour;
      observableManager.notifyObservers(COLOUR_CHANGED, colour);
   }
   
   @Override
   public ObservableManager getObservableManager() {
      return observableManager;
   }
   
}
