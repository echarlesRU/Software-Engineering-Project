package webchase;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.util.*;

import javafx.geometry.*;
import javafx.scene.input.*;;

public class SearchView {
	PrimaryView primaryView;
	
	Tab searchTab;
	TextField urlField, termField, depthTextField;
	Button addUrl, removeUrl, addTerm, removeTerm, searchButton, haltButton;
	ListView<String> urlList, termList;
	
	ArrayList<String> urlArrayList, termArrayList;
	int depth;
	
	WebController controller;
	
	public SearchView(PrimaryView primaryView) {
		this.primaryView = primaryView;
		
		createUIElements();
		addActionListeners();
	}
	
	private void createUIElements() {
		searchTab = new Tab("Search");
		
		HBox view = new HBox();
		
		VBox left = new VBox();
		
		Insets pad = new Insets(10);
		
		HBox urlFieldArea = new HBox();
		Label urlFieldLabel = new Label("URL:");
		urlField = new TextField();
		
		HBox urlListButtonArea = new HBox();
		VBox urlButtonArea = new VBox();
		addUrl = new Button("Add To List");
		removeUrl = new Button("Remove Last");
		Label urlListLabel = new Label("             URL(s) to be used:");
		urlList = new ListView<>();
		
		HBox termFieldArea = new HBox();
		Label termFieldLabel = new Label("Search Term:");
		termField = new TextField();
		
		HBox termListButtonArea = new HBox();
		VBox termButtonArea = new VBox();
		addTerm = new Button("Add To List");
		removeTerm = new Button("Remove Last");
		Label termListLabel = new Label("Search Term(s) to be used:");
		termList = new ListView<>();
		
		BorderPane right = new BorderPane();		
		VBox bottomRight = new VBox();
		
		HBox depthFieldArea = new HBox();
		Label depthFieldLabel = new Label("Depth:");
		depthTextField = new TextField();
		
		HBox searchButtonArea = new HBox();
		searchButton = new Button("Search");
		haltButton = new Button("Halt Search");
				
		urlField.setPromptText("http://www.github.com");
		
		urlFieldArea.setPadding(pad);
		urlFieldArea.setSpacing(10);
		urlFieldArea.getChildren().addAll(urlFieldLabel, urlField);
		
		addUrl.setMaxWidth(Double.MAX_VALUE);
		removeUrl.setMaxWidth(Double.MAX_VALUE);
		
		urlButtonArea.setPadding(pad);
		urlButtonArea.setSpacing(15);
		urlButtonArea.getChildren().addAll(addUrl, removeUrl, urlListLabel);
		
		urlListButtonArea.setPadding(pad);
		urlListButtonArea.setSpacing(5);
		urlListButtonArea.getChildren().addAll(urlButtonArea, urlList);
		
		termField.setPromptText("Delay");
		
		termFieldArea.setPadding(pad);
		termFieldArea.setSpacing(10);
		termFieldArea.getChildren().addAll(termFieldLabel, termField);
		
		addTerm.setMaxWidth(Double.MAX_VALUE);
		removeTerm.setMaxWidth(Double.MAX_VALUE);
		
		termButtonArea.setPadding(pad);
		termButtonArea.setSpacing(15);
		termButtonArea.getChildren().addAll(addTerm, removeTerm, termListLabel);
		
		termListButtonArea.setPadding(pad);
		termListButtonArea.setSpacing(5);
		termListButtonArea.getChildren().addAll(termButtonArea, termList);
		
		left.getChildren().addAll(urlFieldArea, 
								  urlListButtonArea,
								  termFieldArea, 
								  termListButtonArea);
		
		depthTextField.setPromptText("4");
		
		depthFieldArea.setPadding(pad);
		depthFieldArea.setSpacing(10);
		depthFieldArea.getChildren().addAll(depthFieldLabel, depthTextField);
		
		searchButtonArea.setPadding(pad);
		searchButtonArea.setSpacing(15);
		searchButtonArea.getChildren().addAll(searchButton, haltButton);
		
		bottomRight.getChildren().addAll(depthFieldArea, searchButtonArea);
		//bottomRight.setAlignment(Pos.CENTER);
		
		right.setBottom(bottomRight);
		
		view.getChildren().addAll(left, right);
		
		searchTab.setClosable(false);
		searchTab.setContent(view);
	}
	
	private void addActionListeners() {
		addUrl.setOnMouseClicked(e -> handleAddUrlButtonAction(e));
		removeUrl.setOnMouseClicked(e -> handleRemoveUrlButtonAction(e));
		
		addTerm.setOnMouseClicked(e -> handleAddTermButtonAction(e));
		removeTerm.setOnMouseClicked(e -> handleRemoveTermButtonAction(e));
		
		searchButton.setOnMouseClicked(e -> handleSearchAction(e));
		haltButton.setOnMouseClicked(e -> handleHaltAction(e));
		
		urlField.setOnKeyPressed(e -> handleEnterUrlButtonAction(e));
		termField.setOnKeyPressed(e -> handleEnterTermButtonAction(e));
	}
	
	private void handleAddUrlButtonAction(MouseEvent e) {
		addUrl();
	}
	
	private void handleRemoveUrlButtonAction(MouseEvent e) {
		if(!urlList.getItems().isEmpty()) {
			urlList.getItems().remove(urlList.getItems().size() - 1);
		}
	}
	
	private void handleAddTermButtonAction(MouseEvent e) {
		addTerm();
	}
	
	private void handleRemoveTermButtonAction(MouseEvent e) {
		if(!termList.getItems().isEmpty()) {
			termList.getItems().remove(termList.getItems().size() - 1);
		}
	}
	
	private void handleSearchAction(MouseEvent e) {
		if(depthTextField.getText() != null && !depthTextField.getText().trim().isEmpty()) {

			depth = Integer.parseInt(depthTextField.getText());
			
			int urlListSize = urlList.getItems().size();
			int termListSize = termList.getItems().size();
			
			if(urlListSize != 0 && termListSize != 0) {
				urlArrayList = new ArrayList<String>();
				termArrayList = new ArrayList<String>();
		
				for(String item: urlList.getItems()) {
					urlArrayList.add(item);
				}
			
				for(String item: termList.getItems()) {
					termArrayList.add(item);
				}
		
				urlList.getItems().clear();
				termList.getItems().clear();
				depthTextField.setText("");
				
				controller = new WebController(urlArrayList, termArrayList, depth);
				
				int numTabs = primaryView.getTabPane().getTabs().size();
				ResultView resultView = new ResultView(controller.getWebPages(), numTabs);
				
				primaryView.getTabPane().getTabs().add(resultView.getResultTab());
			}
				
		}
	}
	
	private void handleHaltAction(MouseEvent e) {
		//halt
	}
	
	private void handleEnterUrlButtonAction(KeyEvent e) {
		if(e.getCode().equals(KeyCode.ENTER)) {
			addUrl();
		}
	}
	
	private void handleEnterTermButtonAction(KeyEvent e) {
		if(e.getCode().equals(KeyCode.ENTER)) {
			addTerm();
		}
	}
	
	private void addUrl() {
		if(urlField.getText() != null && !urlField.getText().trim().isEmpty()){
			for(String item: urlList.getItems()) {
				if(urlField.getText().equals(item)) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Expert Web Crawler");
					alert.setHeaderText("This website is already on the list.");
					alert.setContentText("Do you wish to add the website anyway?");
					
					Optional<ButtonType> result = alert.showAndWait();
					
					if(result.get() != ButtonType.OK) {
						urlField.setText("");
						return;
					}
					
					else {
						urlList.getItems().add(urlField.getText());
						urlList.scrollTo(urlList.getItems().size() - 1);
						urlField.setText("");
						return;
					}
				}
			}
			
			urlList.getItems().add(urlField.getText());
			urlList.scrollTo(urlList.getItems().size() - 1);
			urlField.setText("");
		}
		
		return;
	}
	
	private void addTerm() {
		if(termField.getText() != null && !termField.getText().trim().isEmpty()){
			for(String item: termList.getItems()) {
				if(termField.getText().equals(item)) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Expert Web Crawler");
					alert.setHeaderText("This search term is already on the list.");
					alert.setContentText("Do you wish to add the search term anyway?");
					
					Optional<ButtonType> result = alert.showAndWait();
					
					if(result.get() != ButtonType.OK) {
						termField.setText("");
						return;
					}
					
					else {
						termList.getItems().add(termField.getText());
						termList.scrollTo(termList.getItems().size() - 1);
						termField.setText("");
						return;
					}
				}
			}
			
			termList.getItems().add(termField.getText());
			termList.scrollTo(termList.getItems().size() - 1);
			termField.setText("");
		}
		
		return;
	}
	
	public Tab getSearchView() {
		return searchTab;
	}
}
