
package editor.uniteditor;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import command.CommandExecutor;
import datahandler.DataModel;
import datahandler.DataModelHolder;
import datahandler.editor.FilteredComboBoxModelEditor;
import datahandler.editor.FloatModelEditor;
import datahandler.editor.StringModelEditor;
import editor.EditorContext;
import editor.unit.selection.SelectedUnitsModel;
import gameenumeration.unit.UnitEntry;
import gameenumeration.unit.UnitType;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import mapmodel.unit.UnitModel;
import utility.listfactory.IntegerListFactory;
import utility.observable.Observer;

public class UnitEditor {
   
   private static final List<Integer> PLAYER_NUMBERS = Collections.unmodifiableList(IntegerListFactory.generate(13));
   private static final List<Integer> UNIT_TYPES =
         Collections.unmodifiableList(IntegerListFactory.generate(UnitType.getInstance().getUnits().size()));
   
   private EditorContext editorContext;
   private SelectedUnitsModel selectedUnitsModel;
   
   private DataModelHolder<String> unitName;
   private DataModelHolder<Integer> unitType;
   private DataModelHolder<Integer> unitPlayer;
   private DataModelHolder<Float> posX;
   private DataModelHolder<Float> posY;
   private DataModelHolder<Float> posZ;
   private DataModelHolder<Float> scaleX;
   private DataModelHolder<Float> scaleY;
   private DataModelHolder<Float> scaleZ;
   private DataModelHolder<Float> mXX;
   private DataModelHolder<Float> mXY;
   private DataModelHolder<Float> mXZ;
   private DataModelHolder<Float> mYX;
   private DataModelHolder<Float> mYY;
   private DataModelHolder<Float> mYZ;
   private DataModelHolder<Float> mZX;
   private DataModelHolder<Float> mZY;
   private DataModelHolder<Float> mZZ;
   
   private StringModelEditor unitNameEditor;
   private FilteredComboBoxModelEditor<Integer> unitTypeEditor;
   private FilteredComboBoxModelEditor<Integer> unitPlayerEditor;
   private FloatModelEditor posXEditor;
   private FloatModelEditor posYEditor;
   private FloatModelEditor posZEditor;
   private FloatModelEditor scaleXEditor;
   private FloatModelEditor scaleYEditor;
   private FloatModelEditor scaleZEditor;
   private FloatModelEditor mXXEditor;
   private FloatModelEditor mXYEditor;
   private FloatModelEditor mXZEditor;
   private FloatModelEditor mYXEditor;
   private FloatModelEditor mYYEditor;
   private FloatModelEditor mYZEditor;
   private FloatModelEditor mZXEditor;
   private FloatModelEditor mZYEditor;
   private FloatModelEditor mZZEditor;
   
   private VBox box;
   private Observer<Collection<Integer>> selectionObserver;
   
   public UnitEditor(EditorContext editorContext, SelectedUnitsModel selectedUnitsModel) {
      this.editorContext = editorContext;
      this.selectedUnitsModel = selectedUnitsModel;
      unitName = new DataModelHolder<>();
      unitPlayer = new DataModelHolder<>();
      unitType = new DataModelHolder<>();
      posX = new DataModelHolder<>();
      posY = new DataModelHolder<>();
      posZ = new DataModelHolder<>();
      scaleX = new DataModelHolder<>();
      scaleY = new DataModelHolder<>();
      scaleZ = new DataModelHolder<>();
      mXX = new DataModelHolder<>();
      mXY = new DataModelHolder<>();
      mXZ = new DataModelHolder<>();
      mYX = new DataModelHolder<>();
      mYY = new DataModelHolder<>();
      mYZ = new DataModelHolder<>();
      mZX = new DataModelHolder<>();
      mZY = new DataModelHolder<>();
      mZZ = new DataModelHolder<>();
      
      CommandExecutor commandExecutor = editorContext.getCommandExecutor();
      unitNameEditor = new StringModelEditor(commandExecutor, unitName);
      unitTypeEditor = new FilteredComboBoxModelEditor<>(commandExecutor, unitType, () -> UNIT_TYPES, this::getSearchName);
      unitPlayerEditor =
            new FilteredComboBoxModelEditor<>(commandExecutor, unitPlayer, () -> PLAYER_NUMBERS, value -> value == null ? "" : value.toString());
      posXEditor = new FloatModelEditor(commandExecutor, posX);
      posYEditor = new FloatModelEditor(commandExecutor, posY);
      posZEditor = new FloatModelEditor(commandExecutor, posZ);
      scaleXEditor = new FloatModelEditor(commandExecutor, scaleX);
      scaleYEditor = new FloatModelEditor(commandExecutor, scaleY);
      scaleZEditor = new FloatModelEditor(commandExecutor, scaleZ);
      mXXEditor = new FloatModelEditor(commandExecutor, mXX);
      mXYEditor = new FloatModelEditor(commandExecutor, mXY);
      mXZEditor = new FloatModelEditor(commandExecutor, mXZ);
      mYXEditor = new FloatModelEditor(commandExecutor, mYX);
      mYYEditor = new FloatModelEditor(commandExecutor, mYY);
      mYZEditor = new FloatModelEditor(commandExecutor, mYZ);
      mZXEditor = new FloatModelEditor(commandExecutor, mZX);
      mZYEditor = new FloatModelEditor(commandExecutor, mZY);
      mZZEditor = new FloatModelEditor(commandExecutor, mZZ);
      
      box = new VBox(
            createGraphicsRow("Name", unitNameEditor.getEditor()),
            createGraphicsRow("Type", unitTypeEditor.getEditor()),
            createGraphicsRow("Player", unitPlayerEditor.getEditor()),
            createGraphicsRow("Pos X", posXEditor.getEditor()),
            createGraphicsRow("Pos Y", posYEditor.getEditor()),
            createGraphicsRow("Pos Z", posZEditor.getEditor()),
            createGraphicsRow("Scale X", scaleXEditor.getEditor()),
            createGraphicsRow("Scale Y", scaleYEditor.getEditor()),
            createGraphicsRow("Scale Z", scaleZEditor.getEditor()),
            createGraphicsRow("Rot Mxx", mXXEditor.getEditor()),
            createGraphicsRow("Rot Mxy", mXYEditor.getEditor()),
            createGraphicsRow("Rot Mxz", mXZEditor.getEditor()),
            createGraphicsRow("Rot Myx", mYXEditor.getEditor()),
            createGraphicsRow("Rot Myy", mYYEditor.getEditor()),
            createGraphicsRow("Rot Myz", mYZEditor.getEditor()),
            createGraphicsRow("Rot Mzx", mZXEditor.getEditor()),
            createGraphicsRow("Rot Mzy", mZYEditor.getEditor()),
            createGraphicsRow("Rot Mzz", mZZEditor.getEditor()));
      
      selectionObserver = this::updateDataHoldersWithSelection;
      selectedUnitsModel.getObservableManager().addObserver(SelectedUnitsModel.SELECTION_CHANGED, selectionObserver);
      updateDataHoldersWithSelection(selectedUnitsModel.getSelectedUnits());
   }
   
   private void updateDataHoldersWithSelection(Collection<Integer> selection) {
      List<UnitModel> allUnitModels = editorContext.getRootModel().getAllUnitsModel().getUnitModels().getChildModels();
      Set<UnitModel> unitsModels = selection.stream().map(allUnitModels::get).collect(Collectors.toCollection(LinkedHashSet::new));
      
      unitName.setDataModels(getDataModels(unitsModels, UnitModel::getUnitName));
      unitType.setDataModels(getDataModels(unitsModels, UnitModel::getUnitType));
      unitPlayer.setDataModels(getDataModels(unitsModels, UnitModel::getUnitPlayer));
      posX.setDataModels(getDataModels(unitsModels, UnitModel::getPosX));
      posY.setDataModels(getDataModels(unitsModels, UnitModel::getPosY));
      posZ.setDataModels(getDataModels(unitsModels, UnitModel::getPosZ));
      scaleX.setDataModels(getDataModels(unitsModels, UnitModel::getScaleX));
      scaleY.setDataModels(getDataModels(unitsModels, UnitModel::getScaleY));
      scaleZ.setDataModels(getDataModels(unitsModels, UnitModel::getScaleZ));
      mXX.setDataModels(getDataModels(unitsModels, UnitModel::getmXX));
      mXY.setDataModels(getDataModels(unitsModels, UnitModel::getmXY));
      mXZ.setDataModels(getDataModels(unitsModels, UnitModel::getmXZ));
      mYX.setDataModels(getDataModels(unitsModels, UnitModel::getmYX));
      mYY.setDataModels(getDataModels(unitsModels, UnitModel::getmYY));
      mYZ.setDataModels(getDataModels(unitsModels, UnitModel::getmYZ));
      mZX.setDataModels(getDataModels(unitsModels, UnitModel::getmZX));
      mZY.setDataModels(getDataModels(unitsModels, UnitModel::getmZY));
      mZZ.setDataModels(getDataModels(unitsModels, UnitModel::getmZZ));
   }
   
   private <T> Collection<DataModel<T>> getDataModels(Collection<UnitModel> unitsModels, Function<UnitModel, DataModel<T>> dataExtractor) {
      return unitsModels.stream().map(dataExtractor::apply).collect(Collectors.toList());
   }
   
   private String getSearchName(Integer unitIndex) {
      if (unitIndex == null) {
         return "";
      }
      UnitEntry unitEntry = UnitType.getInstance().getUnits().get(unitIndex);
      if (unitEntry.getIngameName() != null) {
         return unitEntry.getEditorName() + " | " + unitEntry.getIngameName();
      } else {
         return unitEntry.getEditorName();
      }
   }
   
   private BorderPane createGraphicsRow(String name, Node node) {
      BorderPane borderPane = new BorderPane();
      Label label = new Label(name);
      BorderPane.setAlignment(label, Pos.CENTER_LEFT);
      borderPane.setLeft(label);
      borderPane.setCenter(node);
      return borderPane;
   }
   
   public VBox getNode() {
      return box;
   }
   
   public void destroy() {
      updateDataHoldersWithSelection(Collections.emptyList());
      selectedUnitsModel.getObservableManager().removeObserver(SelectedUnitsModel.SELECTION_CHANGED, selectionObserver);
   }
   
}
