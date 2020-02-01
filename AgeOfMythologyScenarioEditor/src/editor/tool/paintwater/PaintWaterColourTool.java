
package editor.tool.paintwater;

import java.util.List;
import java.util.OptionalInt;

import datahandler.DataModel;
import editor.EditorContext;
import editor.brush.BrushModelHolder;
import editor.brush.handler.GeneralBrushHandler;
import editor.brush.view.BrushView;
import editor.colourchooser.ColourSelectionModel;
import editor.colourchooser.ColourSelectionView;
import editor.sample.SampleFinder;
import editor.tool.EditorTool;
import editor.tool.EditorToolType;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import mapmodel.map.MapSizeModel;

public class PaintWaterColourTool implements EditorTool {
   
   public static final EditorToolType TOOL_TYPE = new EditorToolType();
   
   private EditorContext editorContext;
   
   private BrushModelHolder brushModelHolder;
   
   private WaterColourOverlayCreator overlayViewCreator;
   private BrushView brushView;
   
   private ColourSelectionModel colourSelectionModel;
   private ColourSelectionView colourSelectionView;
   
   private GeneralBrushHandler<Color> generalBrushHandler;
   
   private SampleFinder sampleFinder;
   private EventHandler<MouseEvent> mouseClickedHandler;
   
   public PaintWaterColourTool(EditorContext editorContext) {
      this.editorContext = editorContext;
      
      brushModelHolder = editorContext.getToolModels().getBrushModelHolder();
      
      overlayViewCreator = new WaterColourOverlayCreator(editorContext);
      
      brushView = BrushView.verticesBrushView(editorContext.getMainView(), brushModelHolder);
      editorContext.getMainView().getStackPane().getChildren().add(brushView.getNode());
      
      colourSelectionModel = editorContext.getToolModels().getWaterModels().getColourSelectionModel();
      colourSelectionView = new ColourSelectionView(colourSelectionModel);
      
      generalBrushHandler = new GeneralBrushHandler<>(editorContext, brushModelHolder,
            editorContext.getRootModel().getMapSizeModel().getWaterColour().getChildModels(), value -> colourSelectionModel.getColour(), true);
      
      sampleFinder = new SampleFinder(editorContext);
      mouseClickedHandler = this::handleMouseClicked;
      editorContext.getMainView().getStackPane().addEventFilter(MouseEvent.MOUSE_CLICKED, mouseClickedHandler);
   }
   
   private void handleMouseClicked(MouseEvent mouseEvent) {
      if (MouseButton.SECONDARY.equals(mouseEvent.getButton())) {
         OptionalInt index = sampleFinder.findValue(mouseEvent.getX(), mouseEvent.getY(), false);
         if (!index.isPresent()) {
            return;
         }
         
         MapSizeModel mapSizeModel = editorContext.getRootModel().getMapSizeModel();
         List<DataModel<Color>> colourModels = mapSizeModel.getWaterColour().getChildModels();
         
         colourSelectionModel.setColour(colourModels.get(index.getAsInt()).getValue());
      }
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
      
      editorContext.getMainView().getStackPane().removeEventFilter(MouseEvent.MOUSE_CLICKED, mouseClickedHandler);
   }
   
}
