
package editor.tool.multiplayeroverride;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import datahandler.DataModelHolder;
import datahandler.editor.DataModelEditorHolder;
import datahandler.editor.FilteredComboBoxModelEditor;
import datahandler.editor.FloatModelEditor;
import datahandler.editor.NamedCheckboxBooleanModelEditor;
import datahandler.editor.StringModelEditor;
import editor.EditorContext;
import editor.tool.EditorTool;
import editor.tool.EditorToolType;
import gameenumeration.majorgod.MajorGod;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import mapmodel.RootModel;
import mapmodel.multiplayeroverride.MultiplayerOverrideDataModel;
import mapmodel.multiplayeroverride.MultiplayerOverrideModel;
import utility.listfactory.ByteListFactory;
import utility.listfactory.IntegerListFactory;
import utility.observable.Observer;

public class MultiplayerOverrideTool implements EditorTool {
   
   public static final EditorToolType TOOL_TYPE = new EditorToolType();
   
   private static final List<MajorGod> MAJOR_GODS = Collections.unmodifiableList(Arrays.asList(MajorGod.values()));
   private static final List<Byte> MAJOR_GOD_INDICES = Collections.unmodifiableList(ByteListFactory.generate(MAJOR_GODS.size()));
   
   private EditorContext editorContext;
   private DataModelEditorHolder editorHolder;
   
   private NamedCheckboxBooleanModelEditor enableOverrideEditor;
   
   private TableView<Integer> tableView;
   
   private Observer<Void> modelReadObserver;
   
   private HBox bottomBox;
   
   private FilteredComboBoxModelEditor<Byte> gameTypeEditor;
   private StringModelEditor lobbyNameEditor;
   private FilteredComboBoxModelEditor<Byte> pauseLimitEditor;
   
   private BorderPane borderPane;
   
   public MultiplayerOverrideTool(EditorContext editorContext) {
      this.editorContext = editorContext;
      editorHolder = new DataModelEditorHolder();
      
      enableOverrideEditor = new NamedCheckboxBooleanModelEditor(editorContext.getCommandExecutor(),
            new DataModelHolder<>(editorContext.getRootModel().getMultiplayerOverrideModel().getShouldOverride()),
            "Enable lobby setting override (Lost if saved with ingame editor)");
      
      tableView = new TableView<>();
      
      TableColumn<Integer, Integer> idColumn = new TableColumn<>("Id");
      idColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue() + 1));
      idColumn.setSortable(true);
      
      MultiplayerOverrideDataModel dataModel = editorContext.getRootModel().getMultiplayerOverrideModel().getMultiplayerOverrideDataModel();
      
      List<Byte> possibleTypes = Arrays.asList((byte) 0x00, (byte) 0x01, (byte) 0x02);
      TableColumn<Integer, Region> typeColumn = new TableColumn<>("Type");
      typeColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
            editorHolder.add(
                  new FilteredComboBoxModelEditor<>(editorContext.getCommandExecutor(),
                        new DataModelHolder<>(dataModel.getPlayerTypes().get(cellData.getValue() + 1)),
                        () -> possibleTypes, this::getTypeName))
                  .getEditor()));
      
      TableColumn<Integer, Region> nameColumn = new TableColumn<>("Name");
      nameColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
            editorHolder
                  .add(new StringModelEditor(editorContext.getCommandExecutor(),
                        new DataModelHolder<>(dataModel.getPlayerNames().get(cellData.getValue()))))
                  .getEditor()));
      
      TableColumn<Integer, Region> godColumn = new TableColumn<>("God");
      godColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
            editorHolder.add(
                  new FilteredComboBoxModelEditor<>(editorContext.getCommandExecutor(),
                        new DataModelHolder<>(dataModel.getPlayerGods().get(cellData.getValue() + 1)),
                        () -> MAJOR_GOD_INDICES, index -> MAJOR_GODS.get(index).getName()))
                  .getEditor()));
      
      TableColumn<Integer, Region> ratingColumn = new TableColumn<>("Rating");
      ratingColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
            editorHolder
                  .add(new FloatModelEditor(editorContext.getCommandExecutor(),
                        new DataModelHolder<>(dataModel.getPlayerRatings().get(cellData.getValue()))))
                  .getEditor()));
      
      TableColumn<Integer, Region> handicapColumn = new TableColumn<>("Handicap");
      handicapColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
            editorHolder
                  .add(new FloatModelEditor(editorContext.getCommandExecutor(),
                        new DataModelHolder<>(dataModel.getPlayerHandicaps().get(cellData.getValue()))))
                  .getEditor()));
      
      List<Byte> possibleTeams = ByteListFactory.generate(MultiplayerOverrideDataModel.SUPPORTED_NON_GAIA_PLAYERS);
      possibleTeams.add((byte) 0xFF);
      TableColumn<Integer, Region> teamColumn = new TableColumn<>("Team");
      teamColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
            editorHolder.add(
                  new FilteredComboBoxModelEditor<>(editorContext.getCommandExecutor(),
                        new DataModelHolder<>(dataModel.getPlayerTeams().get(cellData.getValue())),
                        () -> possibleTeams, value -> value == (byte) 0xFF ? "?" : Integer.toString(value + 1)))
                  .getEditor()));
      
      tableView.getColumns()
            .setAll(Arrays.asList(idColumn, typeColumn, nameColumn, godColumn, ratingColumn, handicapColumn, teamColumn));
      
      modelReadObserver = value -> createGraphics();
      editorContext.getRootModel().getObservableManager().addObserver(RootModel.MODEL_READ, modelReadObserver);
      createGraphics();
      
      List<Byte> possibleGameTypes = ByteListFactory.generate(7);
      gameTypeEditor = new FilteredComboBoxModelEditor<>(editorContext.getCommandExecutor(), new DataModelHolder<>(dataModel.getGameType()),
            () -> possibleGameTypes, this::getGameType);
      
      lobbyNameEditor = new StringModelEditor(editorContext.getCommandExecutor(), new DataModelHolder<>(dataModel.getLobbyName()));
      
      List<Byte> possiblePauseLimits = ByteListFactory.generate(256);
      pauseLimitEditor = new FilteredComboBoxModelEditor<>(editorContext.getCommandExecutor(), new DataModelHolder<>(dataModel.getPauseLimit()),
            () -> possiblePauseLimits, this::getPauseLimit);
      
      bottomBox = new HBox(
            new Label("Game Type:"), gameTypeEditor.getEditor(),
            new Label("Lobby Name:"), lobbyNameEditor.getEditor(),
            new Label("Pause Limit:"), pauseLimitEditor.getEditor());
      
      borderPane = new BorderPane();
      borderPane.setTop(enableOverrideEditor.getEditor());
      borderPane.setCenter(tableView);
      borderPane.setBottom(bottomBox);
   }
   
   private void createGraphics() {
      editorHolder.destroyAllEditors();
      MultiplayerOverrideModel multiplayerOverrideModel = editorContext.getRootModel().getMultiplayerOverrideModel();
      
      tableView.getItems().setAll(IntegerListFactory.generate(multiplayerOverrideModel.getPlayerNumber().getValue()));
   }
   
   private String getTypeName(byte type) {
      if (type == 0x00) {
         return "Player";
      } else if (type == 0x01) {
         return "AI";
      } else {
         return "Hidden";
      }
   }
   
   private String getGameType(byte type) {
      if (type == 0x00) {
         return "Supremecy";
      } else if (type == 0x01) {
         return "Conquest";
      } else if (type == 0x02) {
         return "Lightning";
      } else if (type == 0x03) {
         return "Deathmatch";
      } else if (type == 0x04) {
         return "Treaty";
      } else if (type == 0x05) {
         return "Restore Game";
      } else {
         return "Scenario";
      }
   }
   
   private String getPauseLimit(byte type) {
      if (type == 0x00) {
         return "No Limit";
      } else {
         return Integer.toString(Byte.toUnsignedInt(type));
      }
   }
   
   @Override
   public Node getBottomGraphics() {
      return borderPane;
   }
   
   @Override
   public void destroy() {
      enableOverrideEditor.destroy();
      editorContext.getRootModel().getObservableManager().removeObserver(RootModel.MODEL_READ, modelReadObserver);
      editorHolder.destroyAllEditors();
      gameTypeEditor.destroy();
      lobbyNameEditor.destroy();
      pauseLimitEditor.destroy();
   }
   
}
