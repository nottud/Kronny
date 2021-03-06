
package graphics;

import context.Context;
import graphics.documentation.DocumentationView;
import graphics.methodsearcher.MethodSelectionView;
import javafx.scene.layout.BorderPane;

public class MainPanel {
   
   private BorderPane borderPane;
   private SelectedMethodModel selectedMethodModel;
   
   public MainPanel(Context context) {
      selectedMethodModel = new SelectedMethodModel();
      DocumentationView documentationView = new DocumentationView(selectedMethodModel);
      MethodSelectionView methodSelectionView = new MethodSelectionView(context.getCommandModel(), selectedMethodModel);
      
      borderPane = new BorderPane();
      borderPane.setCenter(documentationView.getNode());
      borderPane.setBottom(methodSelectionView.getNode());
   }
   
   public BorderPane getNode() {
      return borderPane;
   }
   
}
