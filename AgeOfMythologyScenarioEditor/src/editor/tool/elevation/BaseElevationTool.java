
package editor.tool.elevation;

import java.util.List;

import datahandler.DataModel;
import editor.EditorContext;
import editor.brush.BrushModelHolder;
import editor.brush.handler.GeneralBrushHandler;
import editor.brush.view.BrushView;
import editor.tool.EditorTool;
import javafx.scene.Node;

public class BaseElevationTool implements EditorTool {
   
   private EditorContext editorContext;
   
   private ElevationRangeModel rangeModel;
   private ElevationHeightModel heightModel;
   private ElevationOverlayCreator overlayCreator;
   
   private BrushModelHolder brushModelHolder;
   
   private ElevationRangeViewer elevationRangeViewer;
   private BrushView brushView;
   
   private GeneralBrushHandler<Float> generalBrushHandler;
   
   public BaseElevationTool(EditorContext editorContext, List<DataModel<Float>> dataModel) {
      this.editorContext = editorContext;
      rangeModel = new ElevationRangeModel();
      heightModel = new ElevationHeightModel();
      overlayCreator = new ElevationOverlayCreator(editorContext, dataModel, rangeModel);
      
      brushModelHolder = new BrushModelHolder();
      
      brushView = BrushView.verticesBrushView(editorContext.getMainView(), brushModelHolder);
      editorContext.getMainView().getStackPane().getChildren().add(brushView.getNode());
      
      elevationRangeViewer = new ElevationRangeViewer(rangeModel, heightModel);
      
      generalBrushHandler = new GeneralBrushHandler<>(editorContext, brushModelHolder, dataModel, value -> heightModel.getHeight(), true);
   }
   
   @Override
   public Node getLeftGraphics() {
      return elevationRangeViewer.getNode();
   }
   
   @Override
   public void destroy() {
      brushView.destroy();
      editorContext.getMainView().getStackPane().getChildren().remove(brushView.getNode());
      
      overlayCreator.destroy();
      
      generalBrushHandler.destroy();
   }
   
}
