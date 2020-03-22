
package editor.overlay;

import java.nio.IntBuffer;

import camera.CameraConverter;
import editor.EditorContext;
import editor.GraphicsRedrawHander;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import mapmodel.RootModel;
import mapmodel.map.MapSizeModel;
import utility.graphics.colour.ColourToArgb;
import utility.graphics.resizeablecanvas.ResizeableCanvas;
import utility.observable.Observer;

public class OverlayView {
   
   private static final int TRANSPARENT_ARGB = ColourToArgb.convert(Color.TRANSPARENT);
   
   private EditorContext editorContext;
   private CameraConverter cameraConverter;
   private int pixelsPerTile;
   private boolean vertices;
   
   private GraphicsRedrawHander graphicsRedrawHander;
   private Observer<Object> requestRedrawObserver;
   
   private ResizeableCanvas canvas;
   
   private Observer<Object> mapSizeObserver;
   
   private WritableImage image;
   
   private OverlayView(EditorContext editorContext, CameraConverter cameraConverter, int pixelsPerTile, boolean vertices) {
      this.editorContext = editorContext;
      this.cameraConverter = cameraConverter;
      this.pixelsPerTile = pixelsPerTile;
      this.vertices = vertices;
      
      graphicsRedrawHander = new GraphicsRedrawHander();
      graphicsRedrawHander.getObservableManager().addObserver(GraphicsRedrawHander.PERFORM_REDRAW, value -> render());
      
      requestRedrawObserver = value -> graphicsRedrawHander.requestRedraw();
      
      canvas = new ResizeableCanvas(1.0, 1.0);
      canvas.setMouseTransparent(true);
      canvas.getObservableManager().addObserver(ResizeableCanvas.RESIZED, requestRedrawObserver);
      
      cameraConverter.getObservableManager().addObserver(CameraConverter.VIEW_CHANGED, requestRedrawObserver);
      
      mapSizeObserver = value -> handleMapSizeChange();
      editorContext.getRootModel().getObservableManager().addObserver(RootModel.MODEL_READ, mapSizeObserver);
      handleMapSizeChange();
   }
   
   public static OverlayView tileOverlayView(EditorContext editorContext, CameraConverter cameraConverter, int pixelsPerTile) {
      return new OverlayView(editorContext, cameraConverter, pixelsPerTile, false);
   }
   
   public static OverlayView verticesTileOverlayView(EditorContext editorContext, CameraConverter cameraConverter, int pixelsPerTile) {
      return new OverlayView(editorContext, cameraConverter, pixelsPerTile, true);
   }
   
   private void handleMapSizeChange() {
      MapSizeModel mapSizeModel = editorContext.getRootModel().getMapSizeModel();
      int width;
      int height;
      if (vertices) {
         width = (mapSizeModel.getMapSizeX().getValue() + 1) * pixelsPerTile;
         height = (mapSizeModel.getMapSizeZ().getValue() + 1) * pixelsPerTile;
      } else {
         width = mapSizeModel.getMapSizeX().getValue() * pixelsPerTile;
         height = mapSizeModel.getMapSizeZ().getValue() * pixelsPerTile;
      }
      image = new WritableImage(Math.max(1, width), Math.max(1, height));
      graphicsRedrawHander.requestRedraw();
   }
   
   private void render() {
      GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
      graphicsContext.clearRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());
      if (vertices) {
         graphicsContext.drawImage(image, cameraConverter.toScreenX(-1.0), cameraConverter.toScreenY(-1.0),
               cameraConverter.vectorToScreenX(image.getWidth() * 2.0 / pixelsPerTile),
               cameraConverter.vectorToScreenY(-image.getHeight() * 2.0 / pixelsPerTile));
      } else {
         graphicsContext.drawImage(image, cameraConverter.toScreenX(0.0), cameraConverter.toScreenY(0.0),
               cameraConverter.vectorToScreenX(image.getWidth() * 2.0 / pixelsPerTile),
               cameraConverter.vectorToScreenY(-image.getHeight() * 2.0 / pixelsPerTile));
      }
   }
   
   public boolean isVertices() {
      return vertices;
   }
   
   public int getPixelsPerTile() {
      return pixelsPerTile;
   }
   
   public WritableImage getImage() {
      return image;
   }
   
   public ResizeableCanvas getNode() {
      return canvas;
   }
   
   public void requestRedraw() {
      graphicsRedrawHander.requestRedraw();
   }
   
   public void clear() {
      int width = (int) Math.round(image.getWidth());
      int height = (int) Math.round(image.getHeight());
      
      PixelWriter pixelWriter = image.getPixelWriter();
      IntBuffer intBuffer = IntBuffer.allocate(width * height);
      int[] intValues = new int[width * height];
      int index = 0;
      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            intValues[index] = TRANSPARENT_ARGB;
            index++;
         }
      }
      intBuffer.put(intValues);
      intBuffer.rewind();
      pixelWriter.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), intBuffer, width);
   }
   
   public void destroy() {
      cameraConverter.getObservableManager().removeObserver(CameraConverter.VIEW_CHANGED, requestRedrawObserver);
      editorContext.getRootModel().getObservableManager().removeObserver(RootModel.MODEL_READ, mapSizeObserver);
   }
   
}
