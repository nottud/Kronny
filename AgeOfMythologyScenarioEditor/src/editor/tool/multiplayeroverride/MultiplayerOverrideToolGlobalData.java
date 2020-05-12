
package editor.tool.multiplayeroverride;

import java.util.Arrays;
import java.util.List;

import datahandler.DataModelHolder;
import datahandler.editor.ByteFlagsModelEditor;
import datahandler.editor.FilteredComboBoxModelEditor;
import datahandler.editor.NamedCheckboxBooleanModelEditor;
import editor.EditorContext;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import mapmodel.multiplayeroverride.MultiplayerOverrideDataModel;
import mapmodel.multiplayeroverride.MultiplayerOverrideModel;
import utility.listfactory.ByteListFactory;
import utility.observable.Observer;

public class MultiplayerOverrideToolGlobalData {
   
   private MultiplayerOverrideModel multiplayerOverrideModel;
   
   private NamedCheckboxBooleanModelEditor enableOverrideEditor;
   
   private FilteredComboBoxModelEditor<Byte> gameTypeEditor;
   private FilteredComboBoxModelEditor<Byte> difficultyEditor;
   private FilteredComboBoxModelEditor<Byte> pauseLimitEditor;
   private ByteFlagsModelEditor flagEditor;
   
   private VBox box;
   private BorderPane borderPane;
   private ScrollPane scrollPane;
   
   private Observer<Boolean> overrideEnabledObserver;
   
   public MultiplayerOverrideToolGlobalData(EditorContext editorContext) {
      multiplayerOverrideModel = editorContext.getRootModel().getMultiplayerOverrideModel();
      MultiplayerOverrideDataModel dataModel = multiplayerOverrideModel.getMultiplayerOverrideDataModel();
      
      enableOverrideEditor = new NamedCheckboxBooleanModelEditor(editorContext.getCommandExecutor(),
            new DataModelHolder<>(editorContext.getRootModel().getMultiplayerOverrideModel().getShouldOverride()),
            "Enable lobby override");
      
      List<Byte> possibleGameTypes = ByteListFactory.generate(7);
      gameTypeEditor = new FilteredComboBoxModelEditor<>(editorContext.getCommandExecutor(), new DataModelHolder<>(dataModel.getGameType()),
            () -> possibleGameTypes, this::getGameType);
      
      List<Byte> possibleDifficulties = ByteListFactory.generate(4);
      difficultyEditor = new FilteredComboBoxModelEditor<>(editorContext.getCommandExecutor(), new DataModelHolder<>(dataModel.getDifficulty()),
            () -> possibleDifficulties, this::getDifficulty);
      
      List<Byte> possiblePauseLimits = ByteListFactory.generate(256);
      pauseLimitEditor = new FilteredComboBoxModelEditor<>(editorContext.getCommandExecutor(), new DataModelHolder<>(dataModel.getPauseLimit()),
            () -> possiblePauseLimits, this::getPauseLimit);
      
      flagEditor = new ByteFlagsModelEditor(editorContext.getCommandExecutor(), new DataModelHolder<>(dataModel.getFlags()),
            Arrays.asList("Shared Resources", "Shared Population", "Locked Teams", "Enable Cheats"));
      
      box = new VBox(
            createRow("Game Type", gameTypeEditor.getEditor()),
            createRow("Difficulty", difficultyEditor.getEditor()),
            createRow("Pause Limit", pauseLimitEditor.getEditor()),
            flagEditor.getEditor());
      
      borderPane = new BorderPane();
      borderPane.setTop(enableOverrideEditor.getEditor());
      borderPane.setCenter(box);
      
      scrollPane = new ScrollPane(borderPane);
      scrollPane.setFitToWidth(true);
      
      overrideEnabledObserver = value -> updateAvailabilityFromEnabled();
      multiplayerOverrideModel.getShouldOverride().addValueObserverAndImmediatelyNotify(overrideEnabledObserver);
   }
   
   public ScrollPane getNode() {
      return scrollPane;
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
   
   private String getDifficulty(byte type) {
      if (type == 0x00) {
         return "Easy";
      } else if (type == 0x01) {
         return "Moderate";
      } else if (type == 0x02) {
         return "Hard";
      } else {
         return "Titan";
      }
   }
   
   private String getPauseLimit(byte type) {
      if (type == 0x00) {
         return "No Limit";
      } else {
         return Integer.toString(Byte.toUnsignedInt(type));
      }
   }
   
   private BorderPane createRow(String field, Node editor) {
      BorderPane rowBorderPane = new BorderPane();
      Label label = new Label(field);
      BorderPane.setAlignment(label, Pos.CENTER_LEFT);
      rowBorderPane.setLeft(label);
      rowBorderPane.setCenter(editor);
      return rowBorderPane;
   }
   
   private void updateAvailabilityFromEnabled() {
      box.setDisable(!multiplayerOverrideModel.getShouldOverride().getValue());
   }
   
   public void destroy() {
      enableOverrideEditor.destroy();
      gameTypeEditor.destroy();
      difficultyEditor.destroy();
      pauseLimitEditor.destroy();
      flagEditor.destroy();
   }
   
}
