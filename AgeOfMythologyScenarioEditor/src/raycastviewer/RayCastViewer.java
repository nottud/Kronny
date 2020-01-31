
package raycastviewer;

import camera.CameraConverter;
import editor.GraphicsRedrawHander;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import utility.graphics.resizeablecanvas.ResizeableCanvas;

public class RayCastViewer {
   
   private RayCaster rayCaster;
   private GraphicsRedrawHander graphicsRedrawHander;
   private ResizeableCanvas canvas;
   private CameraConverter cameraConverter;
   
   private int width;
   private int height;
   private int[] intValues;
   private PixelWriter pixelWriter;
   
   private double lastDragX;
   private double lastDragY;
   private boolean dragging;
   
   public RayCastViewer(RayCaster rayCaster, CameraConverter cameraConverter,
         GraphicsRedrawHander graphicsRedrawHander) {
      this.rayCaster = rayCaster;
      this.cameraConverter = cameraConverter;
      this.graphicsRedrawHander = graphicsRedrawHander;
      canvas = new ResizeableCanvas(100, 100);
      canvas.addEventFilter(ScrollEvent.SCROLL, this::handleScrollEvent);
      canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, this::handleMousePressed);
      canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, this::handleMouseDragged);
      canvas.addEventFilter(MouseEvent.MOUSE_RELEASED, this::handleMouseReleased);
      
      handleCanvasSizeChange();
      
      canvas.getObservableManager().addObserver(ResizeableCanvas.RESIZED, value -> handleCanvasSizeChange());
      
      graphicsRedrawHander.getObservableManager().addObserver(GraphicsRedrawHander.PERFORM_REDRAW, value -> render());
      render();
   }
   
   private void handleCanvasSizeChange() {
      width = (int) canvas.getWidth();
      height = (int) canvas.getHeight();
      intValues = new int[width * height];
      pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
      graphicsRedrawHander.requestRedraw();
   }
   
   private void render() {
      int index = 0;
      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            intValues[index] = rayCaster.cast(cameraConverter.toWorldX(x), cameraConverter.toWorldZ(y));
            index++;
         }
      }
      pixelWriter.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), intValues, 0, width);
   }
   
   private void handleMousePressed(MouseEvent mouseEvent) {
      if (mouseEvent.getButton() == MouseButton.MIDDLE) {
         dragging = true;
         lastDragX = mouseEvent.getX();
         lastDragY = mouseEvent.getY();
      }
   }
   
   private void handleMouseDragged(MouseEvent mouseEvent) {
      if (dragging) {
         double newMouseX = mouseEvent.getX();
         double newMouseY = mouseEvent.getY();
         cameraConverter.setWorldXAtOrigin(
               cameraConverter.getWorldXAtOrigin() - cameraConverter.vectorToWorldX(newMouseX - lastDragX));
         cameraConverter.setWorldZAtOrigin(
               cameraConverter.getWorldZAtOrigin() - cameraConverter.vectorToWorldZ(newMouseY - lastDragY));
         lastDragX = newMouseX;
         lastDragY = newMouseY;
         graphicsRedrawHander.requestRedraw();
      }
   }
   
   private void handleMouseReleased(MouseEvent mouseEvent) {
      if (mouseEvent.getButton() == MouseButton.MIDDLE) {
         dragging = false;
      }
   }
   
   private void handleScrollEvent(ScrollEvent scrollEvent) {
      double oldZoom = cameraConverter.getZoom();
      double newZoom = oldZoom * Math.pow(1.01, -scrollEvent.getDeltaY());
      
      double oldToCentreX = cameraConverter.vectorToWorldX(canvas.getWidth() / 2.0);
      cameraConverter.setWorldXAtOrigin(cameraConverter.getWorldXAtOrigin() + oldToCentreX);
      double oldToCentreZ = cameraConverter.vectorToWorldZ(canvas.getHeight() / 2.0);
      cameraConverter.setWorldZAtOrigin(cameraConverter.getWorldZAtOrigin() + oldToCentreZ);
      
      cameraConverter.setZoom(newZoom);
      
      double newToCentreX = cameraConverter.vectorToWorldX(canvas.getWidth() / 2.0);
      cameraConverter.setWorldXAtOrigin(cameraConverter.getWorldXAtOrigin() - newToCentreX);
      double newToCentreZ = cameraConverter.vectorToWorldZ(canvas.getHeight() / 2.0);
      cameraConverter.setWorldZAtOrigin(cameraConverter.getWorldZAtOrigin() - newToCentreZ);
      
      graphicsRedrawHander.requestRedraw();
   }
   
   public ResizeableCanvas getNode() {
      return canvas;
   }
   
}
