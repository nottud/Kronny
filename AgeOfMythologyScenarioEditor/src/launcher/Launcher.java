package launcher;

import editor.EditorContext;
import editor.EditorPane;
import editor.FileModel;
import editor.StageTitleProvider;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		EditorContext editorContext = new EditorContext(stage);

		new StageTitleProvider(editorContext);
		editorContext.getFileModel().getObservableManager().addObserver(FileModel.DATA_CHANGED,
				value -> editorContext.getRootModel().readAllModels(value, 0));

		EditorPane editorPane = new EditorPane(editorContext);

		Scene scene = new Scene(editorPane.getNode(), 1024, 768);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
