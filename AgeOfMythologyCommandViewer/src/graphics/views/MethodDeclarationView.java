
package graphics.views;

import graphics.SelectedMethodModel;
import javafx.scene.control.TextField;
import model.command.AomMethod;

public class MethodDeclarationView {
   
   private TextField textField;
   
   public MethodDeclarationView(SelectedMethodModel selectedMethodModel) {
      textField = new TextField();
      textField.setEditable(false);
      
      selectedMethodModel.getObservableManager().addObserver(SelectedMethodModel.SELECTION_CHANGED, this::displayMethod);
      displayMethod(selectedMethodModel.getSelectedMethod());
   }
   
   private void displayMethod(AomMethod method) {
      if (method == null) {
         textField.setText("");
         return;
      }
      textField.setText(method.toString());
   }
   
   public TextField getNode() {
      return textField;
   }
   
}
