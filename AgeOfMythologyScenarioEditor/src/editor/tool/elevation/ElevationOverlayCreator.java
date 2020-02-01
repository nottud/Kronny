
package editor.tool.elevation;

import java.util.List;

import datahandler.DataModel;
import editor.EditorContext;
import editor.overlay.GeneralOverlayHandler;
import editor.overlay.OverlayView;
import javafx.scene.paint.Color;
import utility.observable.Observer;

public class ElevationOverlayCreator {
   
   private EditorContext editorContext;
   private ElevationRangeModel model;
   
   private ElevationRangeColourCoverter colourCoverter;
   private GeneralOverlayHandler<Float> generalOverlayHandler;
   
   private Observer<Object> observer;
   
   private OverlayView overlayView;
   
   public ElevationOverlayCreator(EditorContext editorContext, List<DataModel<Float>> dataModelList, ElevationRangeModel model) {
      this.editorContext = editorContext;
      this.model = model;
      
      colourCoverter = new ElevationRangeColourCoverter(model);
      overlayView = OverlayView.verticesTileOverlayView(editorContext, editorContext.getMainView().getCameraConverter(), 1);
      generalOverlayHandler = new GeneralOverlayHandler<>(editorContext, overlayView, dataModelList, this::calculateColour);
      
      editorContext.getMainView().getStackPane().getChildren().add(overlayView.getNode());
      
      observer = value -> generalOverlayHandler.updateEntirePreview();
      model.getObservableManager().addObserver(ElevationRangeModel.BOTTOM_RANGE_CHANGE, observer);
      model.getObservableManager().addObserver(ElevationRangeModel.TOP_RANGE_CHANGE, observer);
      model.getObservableManager().addObserver(ElevationRangeModel.OPACITY_CHANGE, observer);
   }
   
   private Color calculateColour(Float value) {
      Color colour = colourCoverter.getColour(value);
      return new Color(colour.getRed(), colour.getGreen(), colour.getBlue(), model.getOpacity());
   }
   
   public void destroy() {
      generalOverlayHandler.destroy();
      editorContext.getMainView().getStackPane().getChildren().remove(overlayView.getNode());
      
      model.getObservableManager().removeObserver(ElevationRangeModel.BOTTOM_RANGE_CHANGE, observer);
      model.getObservableManager().removeObserver(ElevationRangeModel.TOP_RANGE_CHANGE, observer);
      model.getObservableManager().removeObserver(ElevationRangeModel.OPACITY_CHANGE, observer);
   }
   
}
