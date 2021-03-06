
package graphics;

import context.Context;
import graphics.methodsearcher.MethodSelectionView;
import graphics.views.DocumentationView;
import graphics.views.MethodDeclarationView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainPanel {
   
   private BorderPane borderPane;
   private SelectedMethodModel selectedMethodModel;
   
   public MainPanel(Context context) {
      selectedMethodModel = new SelectedMethodModel();
      DocumentationView documentationView = new DocumentationView(selectedMethodModel);
      MethodDeclarationView methodDeclarationView = new MethodDeclarationView(selectedMethodModel);
      MethodSelectionView methodSelectionView = new MethodSelectionView(context.getCommandModel(), selectedMethodModel);
      
      borderPane = new BorderPane();
      borderPane.setCenter(documentationView.getNode());
      borderPane.setBottom(new VBox(methodDeclarationView.getNode(), methodSelectionView.getNode()));
   }
   
   public BorderPane getNode() {
      return borderPane;
   }
   
}
