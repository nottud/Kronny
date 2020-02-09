
package editor.tool.player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import datahandler.editor.DataModelEditorHolder;
import datahandler.editor.FilteredComboBoxModelEditor;
import datahandler.editor.FloatModelEditor;
import datahandler.editor.StringModelEditor;
import editor.EditorContext;
import editor.tool.EditorTool;
import editor.tool.EditorToolType;
import gameenumeration.majorgod.MajorGod;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import mapmodel.ListModel;
import mapmodel.RootModel;
import mapmodel.player.AllPlayersModel;
import mapmodel.player.PlayerModel;
import utility.listfactory.IntegerListFactory;
import utility.observable.Observer;

public class PlayerTool implements EditorTool {
   
   public static final EditorToolType TOOL_TYPE = new EditorToolType();
   
   private static final List<MajorGod> MAJOR_GODS = Collections.unmodifiableList(Arrays.asList(MajorGod.values()));
   private static final List<Integer> MAJOR_GOD_INDICES = Collections.unmodifiableList(IntegerListFactory.generate(MAJOR_GODS.size()));
   
   private EditorContext editorContext;
   
   private DataModelEditorHolder editorHolder;
   
   private TableView<PlayerModel> tableView;
   
   private Observer<Void> modelReadObserver;
   
   public PlayerTool(EditorContext editorContext) {
      this.editorContext = editorContext;
      
      editorHolder = new DataModelEditorHolder();
      
      tableView = new TableView<>();
      
      TableColumn<PlayerModel, Integer> idColumn = new TableColumn<>("Id");
      idColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPlayerId().getValue()));
      idColumn.setSortable(true);
      
      TableColumn<PlayerModel, Region> nameColumn = new TableColumn<>("Name");
      nameColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
            editorHolder.add(new StringModelEditor(editorContext.getCommandExecutor(), cellData.getValue().getPlayerName())).getEditor()));
      
      TableColumn<PlayerModel, Region> colourColumn = new TableColumn<>("Colour");
      colourColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
            editorHolder.add(new PlayerColourEditor(editorContext.getCommandExecutor(), cellData.getValue().getPlayerColour())).getEditor()));
      
      TableColumn<PlayerModel, Region> godColumn = new TableColumn<>("Major God");
      godColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
            editorHolder.add(new FilteredComboBoxModelEditor<>(editorContext.getCommandExecutor(), cellData.getValue().getPlayerGod(),
                  () -> MAJOR_GOD_INDICES, index -> MAJOR_GODS.get(index).getName())).getEditor()));
      
      TableColumn<PlayerModel, Region> foodColumn = new TableColumn<>("Food");
      foodColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
            editorHolder.add(new FloatModelEditor(editorContext.getCommandExecutor(), cellData.getValue().getPlayerFood())).getEditor()));
      
      TableColumn<PlayerModel, Region> woodColumn = new TableColumn<>("Wood");
      woodColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
            editorHolder.add(new FloatModelEditor(editorContext.getCommandExecutor(), cellData.getValue().getPlayerWood())).getEditor()));
      
      TableColumn<PlayerModel, Region> goldColumn = new TableColumn<>("Gold");
      goldColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
            editorHolder.add(new FloatModelEditor(editorContext.getCommandExecutor(), cellData.getValue().getPlayerGold())).getEditor()));
      
      TableColumn<PlayerModel, Region> favourColumn = new TableColumn<>("Favour");
      favourColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
            editorHolder.add(new FloatModelEditor(editorContext.getCommandExecutor(), cellData.getValue().getPlayerFavour())).getEditor()));
      
      tableView.getColumns().setAll(Arrays.asList(idColumn, nameColumn, colourColumn, godColumn, foodColumn, woodColumn, goldColumn, favourColumn));
      
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
