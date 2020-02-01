
package editor.tool.elevation;

import java.util.List;
import java.util.OptionalInt;

import datahandler.DataModel;
import editor.EditorContext;
import editor.brush.BrushModelHolder;
import editor.brush.handler.GeneralBrushHandler;
import editor.brush.view.BrushView;
import editor.sample.SampleFinder;
import editor.tool.EditorTool;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class BaseElevationTool implements EditorTool {
   
   private EditorContext editorContext;
   private List<DataModel<Float>> dataModel;
   private ElevationHeightModel heightModel;
   
   private ElevationOverlayCreator overlayCreator;
   
   private BrushModelHolder brushModelHolder;
   
   private ElevationRangeViewer elevationRangeViewer;
   private BrushView brushView;
   
   private GeneralBrushHandler<Float> generalBrushHandler;
   
   private SampleFinder sampleFinder;
   private EventHandler<MouseEvent> mouseClickedHandler;
   
   public BaseElevationTool(EditorContext editorContext, List<DataModel<Float>> dataModel, ElevationRangeModel rangeModel,
         ElevationHeightModel heightModel) {
      this.editorContext = editorContext;
      this.dataModel = dataModel;
      this.heightModel = heightModel;
      
      overlayCreator = new ElevationOverlayCreator(editorContext, dataModel, rangeModel);
      
      brushModelHolder = new BrushModelHolder();
      
      brushView = BrushView.verticesBrushView(editorContext.getMainView(), brushModelHolder);
      editorContext.getMainView().getStackPane().getChildren().add(brushView.getNode());
      
      elevationRangeViewer = new ElevationRangeViewer(rangeModel, heightModel);
      
      generalBrushHandler = new GeneralBrushHandler<>(editorContext, brushModelHolder, dataModel, value -> heightModel.getHeight(), true);
      
      sampleFinder = new SampleFinder(editorContext);
      mouseClickedHandler = this::handleMouseClicked;
      editorContext.getMainView().getStackPane().addEventFilter(MouseEvent.MOUSE_CLICKED, mouseClickedHandler);
   }
   
   private void handleMouseClicked(MouseEvent mouseEvent) {
      if (MouseButton.SECONDARY.equals(mouseEvent.getButton())) {
         OptionalInt index = sampleFinder.findValue(mouseEvent.getX(), mouseEvent.getY(), true);
         if (!index.isPresent()) {
            return;
         }
         
         heightModel.setHeight(dataModel.get(index.getAsInt()).getValue());
      }
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
      
      editorContext.getMainView().getStackPane().removeEventFilter(MouseEvent.MOUSE_CLICKED, mouseClickedHandler);
      
      elevationRangeViewer.destroy();
   }
   
}
