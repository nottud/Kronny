
package editor;

import command.CommandExecutor;
import editor.tool.EditorToolManager;
import editor.tool.elevation.TerrainElevationTool;
import editor.tool.elevation.WaterElevationTool;
import editor.tool.paintterrain.TerrainTool;
import editor.tool.paintwater.PaintWaterColourTool;
import editor.tool.paintwater.PaintWaterTool;
import icon.IconType;
import io.IoHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class EditorIconBar {
   
   private HBox hBox;
   private EditorContext editorContext;
   private IoHandler ioHandler;
   
   public EditorIconBar(EditorContext editorContext, IoHandler ioHandler) {
      this.editorContext = editorContext;
      this.ioHandler = ioHandler;
      CommandExecutor commandExecutor = editorContext.getCommandExecutor();
      EditorToolManager toolManager = editorContext.getEditorToolManager();
      hBox = new HBox(
            createButton(IconType.LOAD, value -> ioHandler.loadAs()),
            createButton(IconType.SAVE, value -> ioHandler.save()),
            new Separator(),
            createButton(IconType.UNDO, value -> commandExecutor.undo()),
            createButton(IconType.REDO, event -> commandExecutor.redo()),
            new Separator(),
            createButton(IconType.PAINT_TERRAIN, event -> toolManager.startTool(TerrainTool.TOOL_TYPE)),
            createButton(IconType.PAINT_ELEVATION, event -> toolManager.startTool(TerrainElevationTool.TOOL_TYPE)),
            createButton(IconType.PAINT_WATER, event -> toolManager.startTool(PaintWaterTool.TOOL_TYPE)),
            createButton(IconType.PAINT_WATER_COLOUR, event -> toolManager.startTool(PaintWaterColourTool.TOOL_TYPE)),
            createButton(IconType.PAINT_WATER_HEIGHT, event -> toolManager.startTool(WaterElevationTool.TOOL_TYPE)));
   }
   
   public HBox getNode() {
      return hBox;
   }
   
   private Button createButton(IconType iconType, EventHandler<ActionEvent> eventHandler) {
      Button button = new Button();
      button.setPadding(new Insets(4.0));
      Image image = iconType.loadOrGetImage();
      ImageView imageView = new ImageView(image);
      button.setGraphic(imageView);
      button.setOnAction(eventHandler);
      button.setFocusTraversable(false);
      return button;
   }
   
}
