
package editor;

import camera.CameraConverter;
import editor.shortcut.GlobalShortcuts;
import editor.tool.EditorTool;
import editor.tool.EditorToolManager;
import io.ConversionHandler;
import io.IoHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import raycastviewer.RayCastViewer;
import raycastviewer.terrain.ShowMapOnLoad;
import raycastviewer.terrain.TerrainRayCaster;
import utility.image.ImageLoaderEntryType;

public class EditorPane {
   
   private MainView mainView;
   
   private BorderPane outerBorderPane;
   private BorderPane innerBorderPane;
   
   private TerrainRayCaster terrainRayCaster;
   private RayCastViewer terrainRayCastViewer;
   
   private double horizontalSplitFraction;
   private double verticalSplitFraction;
   
   private SplitPane horizontalSplitPane;
   private SplitPane verticalSplitPane;
   
   public EditorPane(EditorContext editorContext) {
      mainView = editorContext.getMainView();
      
      IoHandler ioHandler = new IoHandler(editorContext);
      
      outerBorderPane = new BorderPane();
      outerBorderPane.setFocusTraversable(true);
      outerBorderPane.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> innerBorderPane.requestFocus());
      outerBorderPane.setTop(new EditorMenuBar(editorContext, ioHandler, new ConversionHandler(editorContext)).getMenuBar());
      
      innerBorderPane = new BorderPane();
      outerBorderPane.setCenter(innerBorderPane);
      innerBorderPane.setTop(new EditorIconBar(editorContext, ioHandler).getNode());
      
      terrainRayCaster = new TerrainRayCaster(editorContext.getRootModel().getMapSizeModel());
      CameraConverter cameraConverter = editorContext.getMainView().getCameraConverter();
      cameraConverter.getObservableManager().addObserver(CameraConverter.VIEW_CHANGED, value -> updateTerrainRayCastViewerImageType(cameraConverter));
      terrainRayCastViewer =
            new RayCastViewer(terrainRayCaster, editorContext.getMainView().getCameraConverter(), editorContext.getRedrawTerrainHander());
      editorContext.getMainView().getStackPane().getChildren().add(terrainRayCastViewer.getNode());
      new ShowMapOnLoad(editorContext.getRootModel(), editorContext.getMainView().getCameraConverter(), editorContext.getRedrawTerrainHander(),
            () -> terrainRayCastViewer.getNode().getWidth(), () -> terrainRayCastViewer.getNode().getHeight());
      
      editorContext.getEditorToolManager().getObservableManager().addObserver(EditorToolManager.TOOL_STARTED, this::handleToolStarted);
      handleToolStarted(editorContext.getEditorToolManager().getActiveTool());
      
      new GlobalShortcuts(editorContext, outerBorderPane);
      
      horizontalSplitFraction = 0.5;
      verticalSplitFraction = 0.5;
   }
   
   private void updateTerrainRayCastViewerImageType(CameraConverter cameraConverter) {
      if (cameraConverter.getZoom() >= 1.0) {
         terrainRayCaster.setImageLoaderEntryType(ImageLoaderEntryType.TINY);
      } else if (cameraConverter.getZoom() >= 0.25) {
         terrainRayCaster.setImageLoaderEntryType(ImageLoaderEntryType.SMALL);
      } else if (cameraConverter.getZoom() >= 0.0625) {
         terrainRayCaster.setImageLoaderEntryType(ImageLoaderEntryType.MEDIUM);
      } else {
         terrainRayCaster.setImageLoaderEntryType(ImageLoaderEntryType.LARGE);
      }
   }
   
   private void handleToolStarted(EditorTool editorTool) {
      horizontalSplitPane = null;
      verticalSplitPane = null;
      
      Node leftNode = editorTool.getLeftGraphics();
      Node bottomNode = editorTool.getBottomGraphics();
      
      Node topNode;
      if (leftNode == null) {
         topNode = mainView.getStackPane();
      } else {
         horizontalSplitPane = new SplitPane(leftNode, mainView.getStackPane());
         topNode = horizontalSplitPane;
      }
      
      if (bottomNode == null) {
         innerBorderPane.setCenter(topNode);
      } else {
         verticalSplitPane = new SplitPane(topNode, bottomNode);
         verticalSplitPane.setOrientation(Orientation.VERTICAL);
         innerBorderPane.setCenter(verticalSplitPane);
      }
      
      if (horizontalSplitPane != null) {
         horizontalSplitPane.getDividers().get(0).setPosition(horizontalSplitFraction);
         horizontalSplitPane.getDividers().get(0).positionProperty()
               .addListener((source, oldValue, newValue) -> horizontalSplitFraction = newValue.doubleValue());
      }
      if (verticalSplitPane != null) {
         verticalSplitPane.getDividers().get(0).setPosition(verticalSplitFraction);
         verticalSplitPane.getDividers().get(0).positionProperty()
               .addListener((source, oldValue, newValue) -> verticalSplitFraction = newValue.doubleValue());
      }
   }
   
   public BorderPane getNode() {
      return outerBorderPane;
   }
   
}
