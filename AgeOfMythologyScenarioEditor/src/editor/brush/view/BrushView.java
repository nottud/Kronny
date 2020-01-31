
package editor.brush.view;

import camera.CameraConverter;
import editor.GraphicsRedrawHander;
import editor.MainView;
import editor.brush.BrushModel;
import editor.brush.BrushModelHolder;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import utility.graphics.resizeablecanvas.ResizeableCanvas;
import utility.observable.Observer;

public class BrushView {
   
   private MainView mainView;
   private BrushModelHolder brushHolderModel;
   private boolean vertices;
   
   private GraphicsRedrawHander graphicsRedrawHander;
   private BrushViewKeyboardShortcuts keyboardShortcuts;
   
   private ResizeableCanvas canvas;
   
   private Observer<Object> requestRedrawObserver;
   
   private EventHandler<MouseEvent> mouseMovedAndDraggedHandler;
   private EventHandler<MouseEvent> mousePressedHandler;
   private EventHandler<MouseEvent> mouseReleasedHandler;
   
   private Observer<Object> cameraMoveObserver;
   
   private double lastMouseScreenX;
   private double lastMouseScreenY;
   
   private BrushView(MainView mainView, BrushModelHolder brushModelHolder, boolean vertices) {
      this.mainView = mainView;
      this.brushHolderModel = brushModelHolder;
      this.vertices = vertices;
      
      graphicsRedrawHander = new GraphicsRedrawHander();
      keyboardShortcuts = new BrushViewKeyboardShortcuts(mainView, brushModelHolder.getBrushModel());
      
      canvas = new ResizeableCanvas(100.0, 100.0);
      canvas.setMouseTransparent(true);
      
      requestRedrawObserver = value -> graphicsRedrawHander.requestRedraw();
      BrushModel brushModel = brushModelHolder.getBrushModel();
      brushModel.getObservableManager().addObserver(BrushModel.POS_X_CHANGED, requestRedrawObserver);
      brushModel.getObservableManager().addObserver(BrushModel.POS_Z_CHANGED, requestRedrawObserver);
      brushModel.getObservableManager().addObserver(BrushModel.WIDTH_CHANGED, requestRedrawObserver);
      brushModel.getObservableManager().addObserver(BrushModel.HEIGHT_CHANGED, requestRedrawObserver);
      
      graphicsRedrawHander.getObservableManager().addObserver(GraphicsRedrawHander.PERFORM_REDRAW, value -> updateBrushGraphics());
      graphicsRedrawHander.requestRedraw();
      
      StackPane stackPane = mainView.getStackPane();
      mouseMovedAndDraggedHandler = this::updateBrushPosition;
      stackPane.addEventFilter(MouseEvent.MOUSE_MOVED, mouseMovedAndDraggedHandler);
      stackPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseMovedAndDraggedHandler);
      mousePressedHandler = this::handleMousePressed;
      stackPane.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedHandler);
      mouseReleasedHandler = this::handleMouseReleased;
      stackPane.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleasedHandler);
      cameraMoveObserver = value -> updateBrushPosition();
      mainView.getCameraConverter().getObservableManager().addObserver(CameraConverter.VIEW_CHANGED, cameraMoveObserver);
   }
   
   public static BrushView tilesBrushView(MainView mainView, BrushModelHolder brushModelHolder) {
      return new BrushView(mainView, brushModelHolder, false);
   }
   
   public static BrushView verticesBrushView(MainView mainView, BrushModelHolder brushModelHolder) {
      return new BrushView(mainView, brushModelHolder, true);
   }
   
   private void updateBrushGraphics() {
      BrushModel brushModel = brushHolderModel.getBrushModel();
      GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
      graphicsContext.clearRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());
      
      CameraConverter cameraConverter = mainView.getCameraConverter();
      double screenX1;
      double screenY1;
      double screenX2;
      double screenY2;
      if (vertices) {
         screenX1 = cameraConverter.toScreenX(brushModel.getPosX() * 2.0 - 1.0);
         screenY1 = cameraConverter.toScreenY(brushModel.getPosZ() * 2.0 - 1.0);
         screenX2 = screenX1 + cameraConverter.vectorToScreenX(brushModel.getWidth() * 2.0);
         screenY2 = screenY1 - cameraConverter.vectorToScreenY(brushModel.getHeight() * 2.0);
      } else {
         screenX1 = cameraConverter.toScreenX(brushModel.getPosX() * 2.0);
         screenY1 = cameraConverter.toScreenY(brushModel.getPosZ() * 2.0);
         screenX2 = screenX1 + cameraConverter.vectorToScreenX(brushModel.getWidth() * 2.0);
         screenY2 = screenY1 - cameraConverter.vectorToScreenY(brushModel.getHeight() * 2.0);
      }
      graphicsContext.setStroke(Color.BLUE);
      graphicsContext.setLineWidth(2.0);
      graphicsContext.beginPath();
      graphicsContext.moveTo(screenX1, screenY1);
      graphicsContext.lineTo(screenX2, screenY1);
      graphicsContext.lineTo(screenX2, screenY2);
      graphicsContext.lineTo(screenX1, screenY2);
      graphicsContext.closePath();
      graphicsContext.stroke();
   }
   
   private void updateBrushPosition(MouseEvent mouseEvent) {
      lastMouseScreenX = mouseEvent.getX();
      lastMouseScreenY = mouseEvent.getY();
      updateBrushPosition();
   }
   
   private void updateBrushPosition() {
      CameraConverter cameraConverter = mainView.getCameraConverter();
      BrushModel brushModel = brushHolderModel.getBrushModel();
      double worldX;
      double worldZ;
      if (vertices) {
         worldX = cameraConverter.toWorldX(lastMouseScreenX) + 1.0;
         worldZ = cameraConverter.toWorldZ(lastMouseScreenY) + 1.0;
      } else {
         worldX = cameraConverter.toWorldX(lastMouseScreenX);
         worldZ = cameraConverter.toWorldZ(lastMouseScreenY);
      }
      brushModel.setPosX((int) Math.round(worldX / 2.0 - brushModel.getWidth() / 2.0));
      brushModel.setPosZ((int) Math.round(worldZ / 2.0 - brushModel.getHeight() / 2.0));
   }
   
   private void handleMousePressed(MouseEvent mouseEvent) {
      if (mouseEvent.getButton() == MouseButton.PRIMARY) {
         brushHolderModel.getBrushMouseStateModel().brushDown();
      }
   }
   
   private void handleMouseReleased(MouseEvent mouseEvent) {
      if (mouseEvent.getButton() == MouseButton.PRIMARY) {
         brushHolderModel.getBrushMouseStateModel().brushUp();
      }
   }
   
   public ResizeableCanvas getNode() {
      return canvas;
   }
   
   public void destroy() {
      BrushModel brushModel = brushHolderModel.getBrushModel();
      brushModel.getObservableManager().removeObserver(BrushModel.POS_X_CHANGED, requestRedrawObserver);
      brushModel.getObservableManager().removeObserver(BrushModel.POS_Z_CHANGED, requestRedrawObserver);
      brushModel.getObservableManager().removeObserver(BrushModel.WIDTH_CHANGED, requestRedrawObserver);
      brushModel.getObservableManager().removeObserver(BrushModel.HEIGHT_CHANGED, requestRedrawObserver);
      
      StackPane stackPane = mainView.getStackPane();
      stackPane.removeEventFilter(MouseEvent.MOUSE_MOVED, mouseMovedAndDraggedHandler);
      stackPane.removeEventFilter(MouseEvent.MOUSE_DRAGGED, mouseMovedAndDraggedHandler);
      stackPane.removeEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedHandler);
      stackPane.removeEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleasedHandler);
      mainView.getCameraConverter().getObservableManager().removeObserver(CameraConverter.VIEW_CHANGED, cameraMoveObserver);
      
      keyboardShortcuts.destroy();
   }
   
}
