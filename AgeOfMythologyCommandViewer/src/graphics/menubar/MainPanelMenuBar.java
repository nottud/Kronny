package graphics.menubar;

import context.Context;
import handler.HandlerManager;
import handler.io.load.LoadHandler;
import handler.io.save.SaveHandler;
import handler.parse.java.FileParserHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.util.Pair;

public class MainPanelMenuBar {
	
	private Context context;
	private HandlerManager handlerManager;
	private MenuBar menuBar;
	
	public MainPanelMenuBar(Context context) {
		this.context = context;
		handlerManager = context.getHandlerManager();
		menuBar = new MenuBar(buildFileMenu());
	}
	
	private Menu buildFileMenu() {
		MenuItem importItem = new MenuItem("Import");
		importItem.setOnAction(event -> handlerManager.handle(FileParserHandler.HANDLER_TYPE, new Pair<>(context, menuBar.getScene().getWindow())));
		
		MenuItem loadItem = new MenuItem("Load");
		loadItem.setOnAction(event -> handlerManager.handle(LoadHandler.HANDLER_TYPE, new Pair<>(context, menuBar.getScene().getWindow())));
		
		MenuItem saveItem = new MenuItem("Save");
		saveItem.setOnAction(event -> handlerManager.handle(SaveHandler.HANDLER_TYPE, new Pair<>(context, menuBar.getScene().getWindow())));
		
		return new Menu("File", null, importItem, loadItem, saveItem);
	}
	
	public MenuBar getMenuBar() {
		return menuBar;
	}

}
