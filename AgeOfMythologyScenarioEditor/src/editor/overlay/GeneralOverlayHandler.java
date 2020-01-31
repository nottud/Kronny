
package editor.overlay;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import datahandler.DataModel;
import editor.EditorContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import mapmodel.map.MapSizeModel;
import utility.observable.Observer;

public class GeneralOverlayHandler<T> {
   
   private EditorContext editorContext;
   private OverlayView overlayView;
   private List<DataModel<T>> dataModelList;
   private Function<T, Color> valueToColour;
   
   private Map<DataModel<T>, Observer<T>> observers;
   
   public GeneralOverlayHandler(EditorContext editorContext, OverlayView overlayView, List<DataModel<T>> dataModelList,
         Function<T, Color> valueToColour) {
      this.editorContext = editorContext;
      this.overlayView = overlayView;
      this.dataModelList = dataModelList;
      this.valueToColour = valueToColour;
      
      observers = new LinkedHashMap<>();
      
      MapSizeModel mapSizeModel = editorContext.getRootModel().getMapSizeModel();
      int index = 0;
      int width = mapSizeModel.getMapSizeX().getValue();
      int height = mapSizeModel.getMapSizeZ().getValue();
      if (width <= 0 || height <= 0) {
         return;
      }
      if (overlayView.isVertices()) {
         width++;
         height++;
      }
      for (int x = 0; x < width; x++) {
         for (int y = 0; y < height; y++) {
            final int posX = x;
            final int posY = y;
            DataModel<T> dataModel = dataModelList.get(index);
            observers.put(dataModel, dataModel.addValueObserverAndImmediatelyNotify(foundValue -> updatePreview(posX, posY, foundValue)));
            index++;
         }
      }
      
      overlayView.requestRedraw();
   }
   
   private void updatePreview(int x, int y, T value) {
      int pixelSize = overlayView.getPixelsPerTile();
      int startX = x * pixelSize;
      int startY = y * pixelSize;
      Color colour = valueToColour.apply(value);
      PixelWriter pixelWriter = overlayView.getImage().getPixelWriter();
      for (int xx = startX; xx < startX + pixelSize; xx++) {
         for (int yy = startY; yy < startY + pixelSize; yy++) {
            pixelWriter.setColor(xx, yy, colour);
         }
      }
      overlayView.requestRedraw();
   }
   
   public void updateEntirePreview() {
      MapSizeModel mapSizeModel = editorContext.getRootModel().getMapSizeModel();
      int width = mapSizeModel.getMapSizeX().getValue();
      int height = mapSizeModel.getMapSizeZ().getValue();
      if (overlayView.isVertices()) {
         width++;
         height++;
      }
      int index = 0;
      for (int x = 0; x < width; x++) {
         for (int y = 0; y < height; y++) {
            updatePreview(x, y, dataModelList.get(index).getValue());
            index++;
         }
      }
   }
   
   public void destroy() {
      overlayView.destroy();
      editorContext.getMainView().getStackPane().getChildren().remove(overlayView.getNode());
      
      for (Entry<DataModel<T>, Observer<T>> entry : observers.entrySet()) {
         entry.getKey().getObservableManager().removeObserver(entry.getKey().getValueChangedObserverType(), entry.getValue());
      }
      observers.clear();
   }
   
}
