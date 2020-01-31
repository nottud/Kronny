
package editor.terrainchooser;

import java.util.Arrays;
import java.util.List;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import terrain.TerrainEntry;
import terrain.TerrainGroup;
import terrain.TerrainType;
import utility.graphics.BlockListener;
import utility.graphics.table.ImageViewGenerator;
import utility.image.ImageLoaderEntryType;

public class TerrainViewer {
   
   private BlockListener blockListener;
   private ImageViewGenerator imageViewGenerator;
   private TableView<TerrainEntry> tableView;
   
   public TerrainViewer(TerrainSelectionModel terrainSelectionModel) {
      blockListener = new BlockListener();
      imageViewGenerator = new ImageViewGenerator();
      
      SortedList<TerrainEntry> sortedList = new SortedList<>(FXCollections.observableList(TerrainType.getInstance().getTerrains()));
      tableView = new TableView<>(sortedList);
      sortedList.comparatorProperty().bind(tableView.comparatorProperty());
      
      TableColumn<TerrainEntry, ImageView> imageColumn = new TableColumn<>("Image");
      imageColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
            imageViewGenerator.createImageView(imageColumn, cellData.getValue().loadOrGetImage().getImage(ImageLoaderEntryType.LARGE))));
      imageColumn.setSortable(false);
      
      TableColumn<TerrainEntry, String> nameColumn = new TableColumn<>("Name");
      nameColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getName()));
      nameColumn.setSortable(true);
      
      List<TerrainGroup> terrainGroups = TerrainType.getInstance().getTerrainGroups();
      TableColumn<TerrainEntry, String> groupColumn = new TableColumn<>("Group");
      groupColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(terrainGroups.get(cellData.getValue().getGroupIndex()).getGroupName()));
      groupColumn.setSortable(true);
      
      tableView.getColumns().setAll(Arrays.asList(imageColumn, nameColumn, groupColumn));
      tableView.getSortOrder().setAll(Arrays.asList(nameColumn));
      tableView.getSelectionModel().select(terrainSelectionModel.getSelectedTerrainEntry());
      
      tableView.getSelectionModel().selectedItemProperty().addListener(
            (source, oldValue, newValue) -> blockListener.attemptBlockAndDo(() -> terrainSelectionModel.setSelectedTerrainType(newValue)));
      terrainSelectionModel.getObservableManager().addObserver(TerrainSelectionModel.SELECTION_CHANGE,
            value -> blockListener.attemptBlockAndDo(() -> tableView.getSelectionModel().select(value)));
   }
   
   public TableView<TerrainEntry> getTableView() {
      return tableView;
   }
   
}
