
package editor.tool.paintwater;

import editor.EditorContext;
import editor.overlay.GeneralOverlayHandler;
import editor.overlay.OverlayView;
import javafx.scene.paint.Color;

public class WaterColourOverlayCreator {
   
   private EditorContext editorContext;
   private OverlayView overlayView;
   private GeneralOverlayHandler<Color> generalOverlayHandler;
   
   public WaterColourOverlayCreator(EditorContext editorContext) {
      this.editorContext = editorContext;
      overlayView = OverlayView.verticesTileOverlayView(editorContext, editorContext.getMainView().getCameraConverter(), 1);
      generalOverlayHandler = new GeneralOverlayHandler<>(editorContext, overlayView,
            editorContext.getRootModel().getMapSizeModel().getWaterColour().getChildModels(),
            colour -> new Color(colour.getRed(), colour.getGreen(), colour.getBlue(), 0.5));
      
      editorContext.getMainView().getStackPane().getChildren().add(overlayView.getNode());
   }
   
   public void destroy() {
      generalOverlayHandler.destroy();
      editorContext.getMainView().getStackPane().getChildren().remove(overlayView.getNode());
   }
   
}
