
package editor.tool.paintwater;

import editor.EditorContext;
import editor.brush.BrushModelHolder;
import editor.brush.handler.GeneralBrushHandler;
import editor.brush.view.BrushView;
import editor.colourchooser.ColourSelectionModel;
import editor.colourchooser.ColourSelectionView;
import editor.tool.EditorTool;
import editor.tool.EditorToolType;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class PaintWaterColourTool implements EditorTool {
   
   public static final EditorToolType TOOL_TYPE = new EditorToolType();
   
   private EditorContext editorContext;
   
   private BrushModelHolder brushModelHolder;
   
   private WaterColourOverlayCreator overlayViewCreator;
   private BrushView brushView;
   
   private ColourSelectionModel colourSelectionModel;
   private ColourSelectionView colourSelectionView;
   
   private GeneralBrushHandler<Color> generalBrushHandler;
   
   public PaintWaterColourTool(EditorContext editorContext) {
      this.editorContext = editorContext;
      
      brushModelHolder = new BrushModelHolder();
      
      overlayViewCreator = new WaterColourOverlayCreator(editorContext);
      
      brushView = BrushView.verticesBrushView(editorContext.getMainView(), brushModelHolder);
      editorContext.getMainView().getStackPane().getChildren().add(brushView.getNode());
      
      colourSelectionModel = new ColourSelectionModel();
      colourSelectionView = new ColourSelectionView(colourSelectionModel);
      
      generalBrushHandler = new GeneralBrushHandler<>(editorContext, brushModelHolder,
            editorContext.getRootModel().getMapSizeModel().getWaterColour().getChildModels(), value -> colourSelectionModel.getColour(), true);
   }
   
   @Override
   public Node getLeftGraphics() {
      return colourSelectionView.getNode();
   }
   
   @Override
   public void destroy() {
      brushView.destroy();
      editorContext.getMainView().getStackPane().getChildren().remove(brushView.getNode());
      
      overlayViewCreator.destroy();
      
      colourSelectionView.destroy();
      
      generalBrushHandler.destroy();
   }
   
}
