
package editor.unit;

import camera.CameraConverter;
import editor.EditorContext;
import editor.GraphicsRedrawHander;
import gameenumeration.unit.UnitType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import mapmodel.unit.UnitModel;
import utility.graphics.resizeablecanvas.ResizeableCanvas;
import utility.observable.Observer;

public class UnitView {
   
   private static final Affine IDENTITY = new Affine();
   
   private EditorContext editorContext;
   private CameraConverter cameraConverter;
   
   private GraphicsRedrawHander graphicsRedrawHander;
   private Observer<Object> requestRedrawObserver;
   
   private ResizeableCanvas canvas;
   
   public UnitView(EditorContext editorContext, CameraConverter cameraConverter) {
      this.editorContext = editorContext;
      this.cameraConverter = cameraConverter;
      
      graphicsRedrawHander = new GraphicsRedrawHander();
      graphicsRedrawHander.getObservableManager().addObserver(GraphicsRedrawHander.PERFORM_REDRAW, value -> render());
      
      requestRedrawObserver = value -> graphicsRedrawHander.requestRedraw();
      
      cameraConverter.getObservableManager().addObserver(CameraConverter.VIEW_CHANGED, requestRedrawObserver);
      
      canvas = new ResizeableCanvas(1.0, 1.0);
      canvas.setMouseTransparent(true);
      canvas.getObservableManager().addObserver(ResizeableCanvas.RESIZED, requestRedrawObserver);
      
      for (UnitModel unitModel : editorContext.getRootModel().getAllUnitsModel().getUnitModels().getChildModels()) {
         unitModel.getUnitType().getObservableManager().addObserver(unitModel.getUnitType().getValueChangedObserverType(), requestRedrawObserver);
         unitModel.getPosX().getObservableManager().addObserver(unitModel.getPosX().getValueChangedObserverType(), requestRedrawObserver);
         unitModel.getPosZ().getObservableManager().addObserver(unitModel.getPosZ().getValueChangedObserverType(), requestRedrawObserver);
      }
   }
   
   private void render() {
      GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
      graphicsContext.setTransform(IDENTITY);
      graphicsContext.clearRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());
      
      Transform transform = Transform.scale(1.0 / cameraConverter.getZoom(), 1.0 / cameraConverter.getZoom());
      transform = transform.createConcatenation(Transform.translate(-cameraConverter.getWorldXAtOrigin(), cameraConverter.getWorldZAtOrigin()));
      
      for (UnitModel unitModel : editorContext.getRootModel().getAllUnitsModel().getUnitModels().getChildModels()) {
         Transform localTransform =
               transform.createConcatenation(Transform.translate(unitModel.getPosX().getValue(), -unitModel.getPosZ().getValue()));
         
         graphicsContext.setTransform(new Affine(localTransform));
         graphicsContext.drawImage(UnitType.getInstance().getUnits().get(unitModel.getUnitType().getValue()).loadOrGetImage(),
               -UnitBoundsFinder.UNIT_RADIUS, -UnitBoundsFinder.UNIT_RADIUS, UnitBoundsFinder.UNIT_DIAMETRE, UnitBoundsFinder.UNIT_DIAMETRE);
      }
   }
   
   public ResizeableCanvas getNode() {
      return canvas;
   }
   
   public void destroy() {
      cameraConverter.getObservableManager().removeObserver(CameraConverter.VIEW_CHANGED, requestRedrawObserver);
      
      for (UnitModel unitModel : editorContext.getRootModel().getAllUnitsModel().getUnitModels().getChildModels()) {
         unitModel.getUnitType().getObservableManager().removeObserver(unitModel.getUnitType().getValueChangedObserverType(), requestRedrawObserver);
         unitModel.getPosX().getObservableManager().removeObserver(unitModel.getPosX().getValueChangedObserverType(), requestRedrawObserver);
         unitModel.getPosZ().getObservableManager().removeObserver(unitModel.getPosZ().getValueChangedObserverType(), requestRedrawObserver);
      }
   }
   
}
