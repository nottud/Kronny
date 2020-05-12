
package editor.tool.multiplayeroverride;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import datahandler.DataModelHolder;
import datahandler.editor.DataModelEditorHolder;
import datahandler.editor.FilteredComboBoxModelEditor;
import datahandler.editor.FloatModelEditor;
import datahandler.editor.StringModelEditor;
import editor.EditorContext;
import gameenumeration.majorgod.MajorGod;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import mapmodel.RootModel;
import mapmodel.multiplayeroverride.MultiplayerOverrideDataModel;
import mapmodel.multiplayeroverride.MultiplayerOverrideModel;
import utility.listfactory.ByteListFactory;
import utility.listfactory.IntegerListFactory;
import utility.observable.Observer;

public class MultiplayerOverrideToolPlayerData {
   
   private static final List<MajorGod> MAJOR_GODS = Collections.unmodifiableList(Arrays.asList(MajorGod.values()));
   private static final List<Byte> MAJOR_GOD_INDICES = Collections.unmodifiableList(ByteListFactory.generate(MAJOR_GODS.size()));
   
   private EditorContext editorContext;
   
   private DataModelEditorHolder editorHolder;
   
   private TableView<Integer> tableView;
   
   private MultiplayerOverrideModel multiplayerOverrideModel;
   
   private Observer<Void> modelReadObserver;
   
   private Observer<Boolean> overrideEnabledObserver;
   
   public MultiplayerOverrideToolPlayerData(EditorContext editorContext) {
      this.editorContext = editorContext;
      
      editorHolder = new DataModelEditorHolder();
      
      tableView = new TableView<>();
      
      TableColumn<Integer, Integer> idColumn = new TableColumn<>("Id");
      idColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue() + 1));
      idColumn.setSortable(true);
      multiplayerOverrideModel = editorContext.getRootModel().getMultiplayerOverrideModel();
      MultiplayerOverrideDataModel dataModel = multiplayerOverrideModel.getMultiplayerOverrideDataModel();
      
      List<Byte> possibleTypes = ByteListFactory.generate(5);
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
      
      List<Byte> possibleAssociations = ByteListFactory.generate(MultiplayerOverrideDataModel.SUPPORTED_NON_GAIA_PLAYERS);
      TableColumn<Integer, Region> associatedPlayerColumn = new TableColumn<>("Associated Player");
      associatedPlayerColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
            editorHolder.add(
                  new FilteredComboBoxModelEditor<>(editorContext.getCommandExecutor(),
                        new DataModelHolder<>(dataModel.getPlayerTeams().get(cellData.getValue())),
                        () -> possibleAssociations, Object::toString))
                  .getEditor()));
      
      tableView.getColumns()
            .setAll(Arrays.asList(idColumn, typeColumn, nameColumn, godColumn, ratingColumn, handicapColumn, teamColumn, associatedPlayerColumn));
      
      modelReadObserver = value -> createGraphics();
      editorContext.getRootModel().getObservableManager().addObserver(RootModel.MODEL_READ, modelReadObserver);
      createGraphics();
      
      overrideEnabledObserver = value -> updateAvailabilityFromEnabled();
      multiplayerOverrideModel.getShouldOverride().addValueObserverAndImmediatelyNotify(overrideEnabledObserver);
   }
   
   private String getTypeName(byte type) {
      if (type == 0x00) {
         return "Player";
      } else if (type == 0x01) {
         return "AI";
      } else if (type == 0x02) {
         return "Hidden";
      } else if (type == 0x03) {
         return "Unknown";
      } else {
         return "Observer";
      }
   }
   
   public TableView<Integer> getNode() {
      return tableView;
   }
   
   private void createGraphics() {
      editorHolder.destroyAllEditors();
      tableView.getItems().setAll(IntegerListFactory.generate(multiplayerOverrideModel.getPlayerNumber().getValue()));
   }
   
   private void updateAvailabilityFromEnabled() {
      tableView.setDisable(!multiplayerOverrideModel.getShouldOverride().getValue());
   }
   
   public void destroy() {
      editorContext.getRootModel().getObservableManager().removeObserver(RootModel.MODEL_READ, modelReadObserver);
      editorHolder.destroyAllEditors();
      multiplayerOverrideModel.getShouldOverride().getObservableManager()
            .removeObserver(multiplayerOverrideModel.getShouldOverride().getValueChangedObserverType(), overrideEnabledObserver);
   }
   
}
