
package editor.tool.player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import command.CommandExecutor;
import datahandler.DataModelHolder;
import datahandler.editor.DataModelEditor;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import utility.listfactory.IntegerListFactory;
import utility.observable.Observer;

public class PlayerColourEditor extends DataModelEditor<Integer> {
   
   private static final List<Integer> PLAYER_NUMBERS = Collections.unmodifiableList(IntegerListFactory.generate(14));
   
   private static final double COLOUR_PREF_WIDTH = 30.0;
   private static final double COLOUR_PREF_HEIGHT = 20.0;
   
   private static final List<Color> COLOURS = Arrays.asList(
         Color.rgb(153, 102, 0),
         Color.rgb(25, 25, 255),
         Color.rgb(255, 25, 25),
         Color.rgb(0, 150, 0),
         Color.rgb(50, 253, 255),
         Color.rgb(223, 52, 238),
         Color.rgb(200, 200, 0),
         Color.rgb(255, 60, 0),
         Color.rgb(128, 0, 64),
         Color.rgb(50, 255, 50),
         Color.rgb(179, 251, 186),
         Color.rgb(80, 80, 80),
         Color.rgb(255, 0, 102),
         Color.rgb(255, 255, 255));
   
   private DataModelHolder<Integer> dataModel;
   private ComboBox<Integer> comboBox;
   private Observer<Integer> observer;
   
   public PlayerColourEditor(CommandExecutor commandExecutor, DataModelHolder<Integer> dataModel) {
      super(commandExecutor, dataModel);
      this.dataModel = dataModel;
      comboBox = new ComboBox<>(FXCollections.observableArrayList(PLAYER_NUMBERS));
      Supplier<ListCell<Integer>> listCellSupplier = () -> new ListCell<Integer>() {
         
         private BorderPane borderPane;
         
         {
            borderPane = new BorderPane();
            borderPane.setPrefWidth(COLOUR_PREF_WIDTH);
            borderPane.setPrefHeight(COLOUR_PREF_HEIGHT);
         }
         
         @Override
         protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);
            
            if (empty || item == null) {
               setText(null);
               setGraphic(null);
            } else {
               setText(Integer.toString(item));
               borderPane.setBackground(new Background(new BackgroundFill(COLOURS.get(item), null, null)));
               setGraphic(borderPane);
            }
         }
      };
      comboBox.setCellFactory(value -> listCellSupplier.get());
      comboBox.setButtonCell(listCellSupplier.get());
      comboBox.setOnAction(event -> notifyValueChangedAndDone(comboBox.getValue()));
      comboBox.setMinWidth(0.0);
      comboBox.setMaxWidth(Double.MAX_VALUE);
      observer = dataModel.addValueObserverAndImmediatelyNotify(value -> updateFromModel());
   }
   
   @Override
   protected void updateValueInternal(Integer newValue) {
      comboBox.setValue(newValue);
   }
   
   @Override
   public ComboBox<Integer> getEditor() {
      return comboBox;
   }
   
   @Override
   public void destroy() {
      dataModel.getObservableManager().removeObserver(dataModel.getValueChangedObserverType(), observer);
   }
   
}
