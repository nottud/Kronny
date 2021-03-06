
package graphics.documentation;

import graphics.SelectedMethodModel;
import javafx.scene.control.TextArea;
import model.command.AomMethod;

public class DocumentationView {
   
   private TextArea textArea;
   
   public DocumentationView(SelectedMethodModel selectedMethodModel) {
      textArea = new TextArea();
      textArea.setWrapText(true);
      textArea.setEditable(false);
      selectedMethodModel.getObservableManager().addObserver(SelectedMethodModel.SELECTION_CHANGED, this::updateDocumentation);
      updateDocumentation(selectedMethodModel.getSelectedMethod());
   }
   
   private void updateDocumentation(AomMethod method) {
      if (method == null) {
         textArea.setText("");
         return;
      }
      String documentation = method.getDocumentation();
      if (documentation == null) {
         textArea.setText("");
         return;
      }
      textArea.setText(documentation);
   }
   
   public TextArea getNode() {
      return textArea;
   }
   
}
