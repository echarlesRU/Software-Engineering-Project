//package expertWebCrawler;

import java.util.List;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.TabPane.*;
import javafx.stage.*;

import java.util.*;

public class PrimaryView extends Application {
	Scene primaryScene;
	BorderPane mainLayout;
	
	MenuBar menuBar;
	Menu open, save, help;
	
	TabPane tabPane;
	
	SearchView searchView;
	
	List<ResultView> resultViews;
	
	//Tab testTab;
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		//Sets Title of Window
		primaryStage.setTitle("Expert Web Crawler");
		
		mainLayout = new BorderPane();
		
		menuBar = new MenuBar();
		
		open = new Menu("Open");
		save = new Menu("Save");
		help = new Menu("Help");
		
		tabPane = new TabPane();
		
		searchView = new SearchView(this);
		
		resultViews = new ArrayList<ResultView>();
		
		//testTab = new Tab("Test Tab");
		
		menuBar.getMenus().addAll(open, save, help);
		
		tabPane.getTabs().add(searchView.getSearchView());
		tabPane.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
		
		mainLayout.setTop(menuBar);
		mainLayout.setCenter(tabPane);
		
		primaryScene = new Scene(mainLayout, 800, 600);
		
		primaryStage.setScene(primaryScene);
		primaryStage.centerOnScreen();
		primaryStage.show();
		
		/*REWORK EVERYTHING FROM HERE DOWN INTO METHODS AND ACTIONS*/
		
		//SavePopup sp = new SavePopup(); //needs to be in an actionlistener.
		
		//Tab results = new Tab("ResultsX");
		
		//tabPane.getTabs().add(results);
	}
	
	public TabPane getTabPane() {
		return this.tabPane;
	}
}
