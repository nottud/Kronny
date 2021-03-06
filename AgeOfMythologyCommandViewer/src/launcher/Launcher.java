package launcher;

import context.Context;
import graphics.MainPanel;
import graphics.menubar.MainPanelMenuBar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Launcher extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Context context = new Context();
		
		MainPanelMenuBar mainPanelMenuBar = new MainPanelMenuBar(context);
		MainPanel mainPanel = new MainPanel(context);
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(mainPanelMenuBar.getMenuBar());
		borderPane.setCenter(mainPanel.getNode());
		
		Scene scene = new Scene(borderPane);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Command Viewer");
		primaryStage.show();
	}
	
	public static final void main(String[] args) {
		Application.launch(args);
	}

}
