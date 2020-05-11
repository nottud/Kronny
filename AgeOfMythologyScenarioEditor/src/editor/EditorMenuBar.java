
package editor;

import command.CommandExecutor;
import editor.tool.EditorToolManager;
import editor.tool.covermap.CoverMapTerrainTool;
import editor.tool.elevation.TerrainElevationTool;
import editor.tool.elevation.WaterElevationTool;
import editor.tool.multiplayeroverride.MultiplayerOverrideTool;
import editor.tool.paintterrain.TerrainTool;
import editor.tool.paintwater.PaintWaterColourTool;
import editor.tool.paintwater.PaintWaterTool;
import editor.tool.player.PlayerTool;
import editor.tool.unit.UnitTool;
import io.ConversionHandler;
import io.IoHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class EditorMenuBar {
   
   private EditorContext editorContext;
   private IoHandler ioHandler;
   private ConversionHandler conversionHandler;
   
   private MenuBar menuBar;
   
   public EditorMenuBar(EditorContext editorContext, IoHandler ioHandler, ConversionHandler conversionHandler) {
      this.editorContext = editorContext;
      this.ioHandler = ioHandler;
      this.conversionHandler = conversionHandler;
      menuBar = new MenuBar(createFileMenu(), createEditMenu(), createToolMenu(), createConvertMenu());
   }
   
   private Menu createFileMenu() {
      MenuItem loadItem = new MenuItem("Load");
      loadItem.setOnAction(event -> ioHandler.loadAs());
      
      MenuItem reloadItem = new MenuItem("Reload");
      reloadItem.setOnAction(event -> ioHandler.load());
      
      MenuItem saveItem = new MenuItem("Save");
      saveItem.setOnAction(event -> ioHandler.save());
      
      MenuItem saveAsItem = new MenuItem("Save as");
      saveAsItem.setOnAction(event -> ioHandler.saveAs());
      
      Menu menu = new Menu("File");
      menu.getItems().setAll(loadItem, reloadItem, saveItem, saveAsItem);
      return menu;
   }
   
   private Menu createEditMenu() {
      CommandExecutor commandExecutor = editorContext.getCommandExecutor();
      
      MenuItem undoItem = new MenuItem("Undo");
      undoItem.setOnAction(event -> commandExecutor.undo());
      commandExecutor.getObservableManager().addObserver(CommandExecutor.UNDO_AVAILABILITY_CHANGED, value -> undoItem.setDisable(!value));
      undoItem.setDisable(!commandExecutor.canUndo());
      
      MenuItem redoItem = new MenuItem("Redo");
      redoItem.setOnAction(event -> commandExecutor.redo());
      commandExecutor.getObservableManager().addObserver(CommandExecutor.REDO_AVAILABILITY_CHANGED, value -> redoItem.setDisable(!value));
      redoItem.setDisable(!commandExecutor.canRedo());
      
      Menu menu = new Menu("Edit");
      menu.getItems().setAll(undoItem, redoItem);
      return menu;
   }
   
   private Menu createToolMenu() {
      EditorToolManager editorToolManager = editorContext.getEditorToolManager();
      
      MenuItem closeActiveTool = new MenuItem("Close active tool");
      closeActiveTool.setOnAction(event -> editorToolManager.closeActiveTool());
      
      MenuItem playerData = new MenuItem("Player data");
      playerData.setOnAction(event -> editorToolManager.startTool(PlayerTool.TOOL_TYPE));
      
      MenuItem multiplayerOverride = new MenuItem("Multiplayer override tool");
      multiplayerOverride.setOnAction(event -> editorToolManager.startTool(MultiplayerOverrideTool.TOOL_TYPE));
      
      MenuItem coverTerrain = new MenuItem("Cover terrain");
      coverTerrain.setOnAction(event -> editorToolManager.startTool(CoverMapTerrainTool.TOOL_TYPE));
      
      MenuItem paintTerrain = new MenuItem("Paint terrain");
      paintTerrain.setOnAction(event -> editorToolManager.startTool(TerrainTool.TOOL_TYPE));
      
      MenuItem colourWater = new MenuItem("Colour water");
      colourWater.setOnAction(event -> editorToolManager.startTool(PaintWaterColourTool.TOOL_TYPE));
      
      MenuItem paintWater = new MenuItem("Paint water");
      paintWater.setOnAction(event -> editorToolManager.startTool(PaintWaterTool.TOOL_TYPE));
      
      MenuItem paintTerrainElevation = new MenuItem("Paint terrain elevation");
      paintTerrainElevation.setOnAction(event -> editorToolManager.startTool(TerrainElevationTool.TOOL_TYPE));
      
      MenuItem paintWaterElevation = new MenuItem("Paint water elevation");
      paintWaterElevation.setOnAction(event -> editorToolManager.startTool(WaterElevationTool.TOOL_TYPE));
      
      MenuItem unitTool = new MenuItem("Unit tool");
      unitTool.setOnAction(event -> editorToolManager.startTool(UnitTool.TOOL_TYPE));
      
      Menu menu = new Menu("Tools");
      menu.getItems().setAll(closeActiveTool, playerData, multiplayerOverride, coverTerrain, paintTerrain, colourWater, paintWater,
            paintTerrainElevation,
            paintWaterElevation, unitTool);
      return menu;
   }
   
   private Menu createConvertMenu() {
      MenuItem extractItem = new MenuItem("Extract");
      extractItem.setOnAction(event -> conversionHandler.extractScenario());
      
      MenuItem compressItem = new MenuItem("Compress");
      compressItem.setOnAction(event -> conversionHandler.compressScenario());
      
      Menu menu = new Menu("Converter");
      menu.getItems().setAll(extractItem, compressItem);
      return menu;
   }
   
   public MenuBar getMenuBar() {
      return menuBar;
   }
   
}
