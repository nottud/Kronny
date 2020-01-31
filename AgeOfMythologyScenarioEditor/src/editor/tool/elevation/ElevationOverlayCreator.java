
package editor.tool.elevation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import datahandler.DataModel;
import editor.EditorContext;
import editor.overlay.OverlayView;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import mapmodel.map.MapSizeModel;
import utility.observable.Observer;

public class ElevationOverlayCreator {
   
   private EditorContext editorContext;
   private List<DataModel<Float>> dataModelList;
   private ElevationRangeModel model;
   
   private ElevationRangeColourCoverter colourCoverter;
   private Map<DataModel<Float>, Observer<Float>> observers;
   private OverlayView overlayView;
   
   public ElevationOverlayCreator(EditorContext editorContext, List<DataModel<Float>> dataModelList, ElevationRangeModel model) {
      this.editorContext = editorContext;
      this.dataModelList = dataModelList;
      this.model = model;
      
      colourCoverter = new ElevationRangeColourCoverter(model);
      observers = new LinkedHashMap<>();
      overlayView = OverlayView.verticesTileOverlayView(editorContext, editorContext.getMainView().getCameraConverter(), 1);
      editorContext.getMainView().getStackPane().getChildren().add(overlayView.getNode());
      
      MapSizeModel mapSizeModel = editorContext.getRootModel().getMapSizeModel();
      int index = 0;
      int width = mapSizeModel.getMapSizeX().getValue();
      int height = mapSizeModel.getMapSizeZ().getValue();
      if (width <= 0 || height <= 0) {
         return;
      }
      for (int x = 0; x <= width; x++) {
         for (int y = 0; y <= height; y++) {
            final int posX = x;
            final int posY = y;
            DataModel<Float> dataModel = dataModelList.get(index);
            observers.put(dataModel, dataModel.addValueObserverAndImmediatelyNotify(foundHeight -> updatePreview(posX, posY, foundHeight)));
            index++;
         }
      }
      
      model.getObservableManager().addObserver(ElevationRangeModel.BOTTOM_RANGE_CHANGE, value -> updateEntirePreview());
      model.getObservableManager().addObserver(ElevationRangeModel.TOP_RANGE_CHANGE, value -> updateEntirePreview());
      model.getObservableManager().addObserver(ElevationRangeModel.OPACITY_CHANGE, value -> updateEntirePreview());
      
      overlayView.requestRedraw();
   }
   
   private void updatePreview(int x, int y, float height) {
      Color colour = colourCoverter.getColour(height);
      PixelWriter pixelWriter = overlayView.getImage().getPixelWriter();
      pixelWriter.setColor(x, y, new Color(colour.getRed(), colour.getGreen(), colour.getBlue(), model.getOpacity()));
      overlayView.requestRedraw();
   }
   
   private void updateEntirePreview() {
      MapSizeModel mapSizeModel = editorContext.getRootModel().getMapSizeModel();
      int width = mapSizeModel.getMapSizeX().getValue();
      int height = mapSizeModel.getMapSizeZ().getValue();
      PixelWriter pixelWriter = overlayView.getImage().getPixelWriter();
      int index = 0;
      for (int x = 0; x <= width; x++) {
         for (int y = 0; y <= height; y++) {
            Color colour = colourCoverter.getColour(dataModelList.get(index).getValue());
            pixelWriter.setColor(x, y, new Color(colour.getRed(), colour.getGreen(), colour.getBlue(), model.getOpacity()));
            index++;
         }
      }
      overlayView.requestRedraw();
   }
   
   public void destroy() {
      overlayView.destroy();
      editorContext.getMainView().getStackPane().getChildren().remove(overlayView.getNode());
      
      for (Entry<DataModel<Float>, Observer<Float>> entry : observers.entrySet()) {
         entry.getKey().getObservableManager().removeObserver(entry.getKey().getValueChangedObserverType(), entry.getValue());
      }
      observers.clear();
   }
   
}
