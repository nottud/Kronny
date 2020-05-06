
package editor.unit.selection.view;

import java.util.Set;

import camera.CameraConverter;
import editor.EditorContext;
import editor.GraphicsRedrawHander;
import editor.unit.UnitBoundsFinder;
import editor.unit.selection.BoxSelectionModel;
import editor.unit.selection.SelectedUnitsModel;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import mapmodel.unit.UnitModel;
import utility.graphics.resizeablecanvas.ResizeableCanvas;
import utility.observable.Observer;

public class UnitSelectionView {
   
   private static final Affine IDENTITY = new Affine();
   
   private EditorContext editorContext;
   private CameraConverter cameraConverter;
   private UnitBoundsFinder unitBoundsFinder;
   private SelectedUnitsModel selectedUnitsModel;
   private BoxSelectionModel boxSelectionModel;
   
   private GraphicsRedrawHander graphicsRedrawHander;
   private Observer<Object> requestRedrawObserver;
   
   private ResizeableCanvas canvas;
   
   public UnitSelectionView(EditorContext editorContext, CameraConverter cameraConverter, UnitBoundsFinder unitBoundsFinder,
         SelectedUnitsModel selectedUnitsModel,
         BoxSelectionModel boxSelectionModel) {
      this.editorContext = editorContext;
      this.cameraConverter = cameraConverter;
      this.unitBoundsFinder = unitBoundsFinder;
      this.selectedUnitsModel = selectedUnitsModel;
      this.boxSelectionModel = boxSelectionModel;
      
      graphicsRedrawHander = new GraphicsRedrawHander();
      graphicsRedrawHander.getObservableManager().addObserver(GraphicsRedrawHander.PERFORM_REDRAW, value -> render());
      
      requestRedrawObserver = value -> graphicsRedrawHander.requestRedraw();
      
      cameraConverter.getObservableManager().addObserver(CameraConverter.VIEW_CHANGED, requestRedrawObserver);
      
      canvas = new ResizeableCanvas(1.0, 1.0);
      canvas.setMouseTransparent(true);
      canvas.getObservableManager().addObserver(ResizeableCanvas.RESIZED, requestRedrawObserver);
      
      selectedUnitsModel.getObservableManager().addObserver(SelectedUnitsModel.SELECTION_CHANGED, requestRedrawObserver);
      boxSelectionModel.getObservableManager().addObserver(BoxSelectionModel.UPDATED, requestRedrawObserver);
   }
   
   private void render() {
      GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
      graphicsContext.save();
      graphicsContext.setTransform(IDENTITY);
      graphicsContext.clearRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());
      graphicsContext.setLineWidth(3.0);
      graphicsContext.setStroke(Color.MAGENTA);
      
      Set<Integer> selectedUnits = selectedUnitsModel.getSelectedUnits();
      int index = 0;
      for (UnitModel unitModel : editorContext.getRootModel().getAllUnitsModel().getUnitModels().getChildModels()) {
         if (selectedUnits.contains(index)) {
            BoundingBox screenBounds = unitBoundsFinder.screenBounds(unitModel);
            graphicsContext.strokeRect(screenBounds.getMinX(), screenBounds.getMinY(), screenBounds.getWidth(), screenBounds.getHeight());
         }
         index++;
      }
      
      if (boxSelectionModel.isActive()) {
         graphicsContext.setStroke(Color.WHITE);
         double minX = Math.min(boxSelectionModel.getFromX(), boxSelectionModel.getToX());
         double minY = Math.min(boxSelectionModel.getFromY(), boxSelectionModel.getToY());
         double width = Math.abs(boxSelectionModel.getToX() - boxSelectionModel.getFromX());
         double height = Math.abs(boxSelectionModel.getToY() - boxSelectionModel.getFromY());
         graphicsContext.strokeRect(minX, minY, width, height);
      }
      
      graphicsContext.restore();
   }
   
   public ResizeableCanvas getNode() {
      return canvas;
   }
   
   public void destroy() {
      cameraConverter.getObservableManager().removeObserver(CameraConverter.VIEW_CHANGED, requestRedrawObserver);
      selectedUnitsModel.getObservableManager().removeObserver(SelectedUnitsModel.SELECTION_CHANGED, requestRedrawObserver);
      boxSelectionModel.getObservableManager().removeObserver(BoxSelectionModel.UPDATED, requestRedrawObserver);
   }
   
}
