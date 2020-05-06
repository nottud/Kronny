
package editor.unitchooser;

import java.util.Arrays;

import gameenumeration.unit.UnitEntry;
import gameenumeration.unit.UnitType;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import utility.graphics.BlockListener;
import utility.graphics.table.ImageViewGenerator;

public class UnitViewer {
   
   private BlockListener blockListener;
   private ImageViewGenerator imageViewGenerator;
   private TableView<UnitEntry> tableView;
   
   public UnitViewer(UnitSelectionModel unitSelectionModel) {
      blockListener = new BlockListener();
      imageViewGenerator = new ImageViewGenerator();
      
      SortedList<UnitEntry> sortedList = new SortedList<>(FXCollections.observableList(UnitType.getInstance().getUnits()));
      tableView = new TableView<>(sortedList);
      sortedList.comparatorProperty().bind(tableView.comparatorProperty());
      
      TableColumn<UnitEntry, ImageView> imageColumn = new TableColumn<>("Image");
      imageColumn.setCellValueFactory(
            cellData -> new ReadOnlyObjectWrapper<>(imageViewGenerator.createImageView(imageColumn, cellData.getValue().loadOrGetImage())));
      imageColumn.setSortable(false);
      
      TableColumn<UnitEntry, String> nameColumn = new TableColumn<>("Editor Name");
      nameColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getEditorName()));
      nameColumn.setSortable(true);
      
      TableColumn<UnitEntry, String> gameName = new TableColumn<>("Game Name");
      gameName.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getIngameName()));
      gameName.setSortable(true);
      
      tableView.getColumns().setAll(Arrays.asList(imageColumn, nameColumn, gameName));
      tableView.getSortOrder().setAll(Arrays.asList(nameColumn));
      tableView.getSelectionModel().select(unitSelectionModel.getSelectedUnitEntry());
      
      tableView.getSelectionModel().selectedItemProperty().addListener(
            (source, oldValue, newValue) -> blockListener.attemptBlockAndDo(() -> unitSelectionModel.setSelectedUnitType(newValue)));
      unitSelectionModel.getObservableManager().addObserver(UnitSelectionModel.SELECTION_CHANGE,
            value -> blockListener.attemptBlockAndDo(() -> tableView.getSelectionModel().select(value)));
   }
   
   public TableView<UnitEntry> getTableView() {
      return tableView;
   }
   
}
