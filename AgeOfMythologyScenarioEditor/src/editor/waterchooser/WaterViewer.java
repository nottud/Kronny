
package editor.waterchooser;

import java.util.Arrays;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import utility.graphics.BlockListener;
import utility.graphics.table.ImageViewGenerator;
import water.WaterEntry;
import water.WaterType;

public class WaterViewer {
   
   private BlockListener blockListener;
   private ImageViewGenerator imageViewGenerator;
   private TableView<WaterEntry> tableView;
   
   public WaterViewer(WaterSelectionModel waterSelectionModel) {
      blockListener = new BlockListener();
      imageViewGenerator = new ImageViewGenerator();
      
      SortedList<WaterEntry> sortedList = new SortedList<>(FXCollections.observableList(WaterType.getInstance().getWaters()));
      tableView = new TableView<>(sortedList);
      sortedList.comparatorProperty().bind(tableView.comparatorProperty());
      
      TableColumn<WaterEntry, ImageView> imageColumn = new TableColumn<>("Image");
      imageColumn.setCellValueFactory(
            cellData -> new ReadOnlyObjectWrapper<>(imageViewGenerator.createImageView(imageColumn, cellData.getValue().loadOrGetImage())));
      imageColumn.setSortable(false);
      
      TableColumn<WaterEntry, String> nameColumn = new TableColumn<>("Name");
      nameColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getName()));
      nameColumn.setSortable(true);
      
      TableColumn<WaterEntry, ImageView> colourColumn = new TableColumn<>("Colour");
      colourColumn.setCellValueFactory(
            cellData -> new ReadOnlyObjectWrapper<>(imageViewGenerator.createImageView(colourColumn, cellData.getValue().getColour())));
      colourColumn.setSortable(false);
      
      TableColumn<WaterEntry, Double> depthColumn = new TableColumn<>("Depth");
      depthColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDepth()));
      depthColumn.setSortable(true);
      
      tableView.getColumns().setAll(Arrays.asList(imageColumn, nameColumn, colourColumn, depthColumn));
      tableView.getSortOrder().setAll(Arrays.asList(nameColumn));
      tableView.getSelectionModel().select(waterSelectionModel.getSelectedWaterEntry());
      
      tableView.getSelectionModel().selectedItemProperty().addListener(
            (source, oldValue, newValue) -> blockListener.attemptBlockAndDo(() -> waterSelectionModel.setSelectedTerrainType(newValue)));
      waterSelectionModel.getObservableManager().addObserver(WaterSelectionModel.SELECTION_CHANGE,
            value -> blockListener.attemptBlockAndDo(() -> tableView.getSelectionModel().select(value)));
   }
   
   public TableView<WaterEntry> getTableView() {
      return tableView;
   }
   
}
