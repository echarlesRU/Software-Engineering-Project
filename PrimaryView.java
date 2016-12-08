package webchase;

import java.util.List;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.TabPane.*;
import javafx.stage.*;

import java.util.*;

/**
 * This class is passed a Stage, and sets up the primary view. The class creates a Stage, 
 * 		for the application.
 * @author Mitchell Powell
 */
public class PrimaryView extends Application {
	private Scene primaryScene;				//Container for all JavaFX Nodes in GUI
	private BorderPane mainLayout;			//Main layout Node
	
	private TabPane tabPane;				//Node containing all Tab Nodes
	
	private SearchView searchView;			//A Tab containing Nodes used to search
	
	/**
	 * Initialize fields, and call several setup methods. 
	 * @param primaryStage Stage which holds the primaryScene
	 */
	@Override
	public void start(Stage primaryStage) throws Exception{
		mainLayout = new BorderPane();
		primaryScene = new Scene(this.mainLayout, 700, 600);
		tabPane = new TabPane();
		searchView = new SearchView(this);
		
		setupTabPane();
		setupMainLayout();
		setupStage(primaryStage);
	}
	
	/**
	 * Adds this searchView to this tabPane, and makes all Tabs closable.
	 * 		Note: The TabPane TabClosingPolicy does NOT override an individual
	 * 		Tab's TabClosingPolicy.
	 */
	private void setupTabPane() {
		tabPane.getTabs().add(this.searchView.getSearchTab());
		tabPane.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
	}
	
	/**
	 * Places this tabPane in the center of this mainLayout.
	 */
	private void setupMainLayout() {
		mainLayout.setCenter(this.tabPane);
	}
	
	/**
	 * Sets up the primary Stage.
	 * @param primaryStage the Stage to be set up.
	 */
	private void setupStage(Stage primaryStage) {
		primaryStage.setTitle("Expert Web Crawler");	//Set Title of window
		primaryStage.setScene(this.primaryScene);		//Add Scene to window
		primaryStage.centerOnScreen();					//Center window on screen
		primaryStage.show();							//Show window

	}
	
	/**
	 * Returns this tabPane
	 * @return this tabPane
	 */
	public TabPane getTabPane() {
		return this.tabPane;
	}
}
