
package editor.tool.player;

import java.util.Arrays;

import datahandler.editor.DataModelEditorHolder;
import datahandler.editor.StringModelEditor;
import editor.EditorContext;
import editor.tool.EditorTool;
import editor.tool.EditorToolType;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import mapmodel.ListModel;
import mapmodel.RootModel;
import mapmodel.player.AllPlayersModel;
import mapmodel.player.PlayerModel;
import utility.observable.Observer;

public class PlayerDisplayView implements EditorTool {
   
   public static final EditorToolType TOOL_TYPE = new EditorToolType();
   
   private EditorContext editorContext;
   
   private DataModelEditorHolder editorHolder;
   
   private TableView<PlayerModel> tableView;
   
   private Observer<Void> modelReadObserver;
   
   public PlayerDisplayView(EditorContext editorContext) {
      this.editorContext = editorContext;
      
      editorHolder = new DataModelEditorHolder();
      
      tableView = new TableView<>();
      
      TableColumn<PlayerModel, Integer> idColumn = new TableColumn<>("Id");
      idColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPlayerId().getValue()));
      idColumn.setSortable(true);
      
      TableColumn<PlayerModel, Region> nameColumn = new TableColumn<>("Name");
      nameColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
            editorHolder.add(new StringModelEditor(editorContext.getCommandExecutor(), cellData.getValue().getPlayerName())).getEditor()));
      
      tableView.getColumns().setAll(Arrays.asList(idColumn, nameColumn));
      
      modelReadObserver = value -> createGraphics();
      editorContext.getRootModel().getObservableManager().addObserver(RootModel.MODEL_READ, modelReadObserver);
      createGraphics();
   }
   
   private void createGraphics() {
      editorHolder.destroyAllEditors();
      AllPlayersModel allPlayersModel = editorContext.getRootModel().getAllPlayersModel();
      ListModel<PlayerModel> listModel = allPlayersModel.getPlayerModels();
      
      tableView.getItems().setAll(listModel.getChildModels());
   }
   
   @Override
   public Node getBottomGraphics() {
      return tableView;
   }
   
   @Override
   public void destroy() {
      editorContext.getRootModel().getObservableManager().removeObserver(RootModel.MODEL_READ, modelReadObserver);
      editorHolder.destroyAllEditors();
   }
   
}
