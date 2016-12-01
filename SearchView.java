package webchase;

import javafx.scene.layout.*;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import javafx.geometry.*;
import javafx.scene.input.*;;

public class SearchView implements Observer{
	PrimaryView primaryView;
	
	BorderPane right;
	
	Tab searchTab;
	TextField urlField, termField, depthField;
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
		
		right = new BorderPane();		
		VBox bottomRight = new VBox();
		
		HBox depthFieldArea = new HBox();
		Label depthFieldLabel = new Label("Depth:");
		depthField = new TextField();
		
		HBox searchButtonArea = new HBox();
		searchButton = new Button("Search");
		haltButton = new Button("Halt Search");
				
		urlField.setPromptText("http://www.github.com");
		
		urlFieldArea.setPadding(pad);
		urlFieldArea.setSpacing(10);
		urlFieldArea.getChildren().addAll(urlFieldLabel, urlField);
		
		addUrl.setMaxWidth(Double.MAX_VALUE);
		addUrl.setFocusTraversable(false);
		removeUrl.setMaxWidth(Double.MAX_VALUE);
		removeUrl.setFocusTraversable(false);
		
		urlButtonArea.setPadding(pad);
		urlButtonArea.setSpacing(15);
		urlButtonArea.getChildren().addAll(addUrl, removeUrl, urlListLabel);
		
		urlList.setFocusTraversable(false);
		
		urlListButtonArea.setPadding(pad);
		urlListButtonArea.setSpacing(5);
		urlListButtonArea.getChildren().addAll(urlButtonArea, urlList);
		
		termField.setPromptText("Delay");
		
		termFieldArea.setPadding(pad);
		termFieldArea.setSpacing(10);
		termFieldArea.getChildren().addAll(termFieldLabel, termField);
		
		addTerm.setMaxWidth(Double.MAX_VALUE);
		addTerm.setFocusTraversable(false);
		removeTerm.setMaxWidth(Double.MAX_VALUE);
		removeTerm.setFocusTraversable(false);
		
		termButtonArea.setPadding(pad);
		termButtonArea.setSpacing(15);
		termButtonArea.getChildren().addAll(addTerm, removeTerm, termListLabel);
		
		termList.setFocusTraversable(false);
		
		termListButtonArea.setPadding(pad);
		termListButtonArea.setSpacing(5);
		termListButtonArea.getChildren().addAll(termButtonArea, termList);
		
		left.getChildren().addAll(urlFieldArea, 
								  urlListButtonArea,
								  termFieldArea, 
								  termListButtonArea);
		
		depthField.setPromptText("4");
		
		depthFieldArea.setPadding(pad);
		depthFieldArea.setSpacing(10);
		depthFieldArea.getChildren().addAll(depthFieldLabel, depthField);
		
		searchButton.setFocusTraversable(false);
		haltButton.setFocusTraversable(false);
		
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
		
		searchButton.setOnMouseClicked(e -> handleSearchButtonAction(e));
		haltButton.setOnMouseClicked(e -> handleHaltAction(e));
		
		urlField.setOnKeyPressed(e -> handleEnterUrlButtonAction(e));
		termField.setOnKeyPressed(e -> handleEnterTermButtonAction(e));
		depthField.setOnKeyPressed(e -> handleEnterDepthButtonAction(e));
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
	
	private void handleSearchButtonAction(MouseEvent e) {
		search();
	}
	
	private void handleHaltAction(MouseEvent e) {
		controller.halt();
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
	
	private void handleEnterDepthButtonAction(KeyEvent e) {
		if(e.getCode().equals(KeyCode.ENTER)) {
			search();
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
	
	private void search() {
		if(depthField.getText() != null && !depthField.getText().trim().isEmpty()) {
			try {
				depth = Integer.parseInt(depthField.getText());
			} catch (NumberFormatException e) {
				createAlert("Illegal Depth Argument: " + depthField.getText(), 
											"Depth must be an integer 1, 7, 92, etc.");
				return;
			}
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
				depthField.setText("");
				
				right.setCenter(new ProgressIndicator(-1.0f));
				right.getCenter().setScaleX(.5);
				right.getCenter().setScaleY(.5);
				
				controller = new WebController(urlArrayList, termArrayList, depth);
				controller.addObserver(this);
				Thread t = new Thread(controller);
				t.start();
				
				//try {System.out.println(controller.getWebPages().get(0).get().getURL());} catch(Exception ee){}
			}
			
			else {
				createAlert("Illegal Depth Argument: " + depthField.getText(), 
										"Depth must be an integer 1, 7, 92, etc.");
			}
				
		}
	}
	
	public void update(Observable obs, Object msg) {
		if((msg instanceof List)) {
			if(msg == null) {System.out.println("null");return;}
			else if(((List<String>)msg).size() == 0) {
                resetSearchView();
				createResultView();
			} 
			else {
				List<String> unfoundWebsites = (List<String>)msg;
				resetSearchView();
				createResultView();
				createAlert("Some Websites Not Found", unfoundWebsites.toString());
			}
		}
	}
	
	private void resetSearchView() {
		right.setCenter(null);
	}
	
	private void createResultView() {
		int numTabs = primaryView.getTabPane().getTabs().size();
		ResultView resultView = readFile(numTabs);

		primaryView.getTabPane().getTabs().add(resultView.getResultTab());
		primaryView.getTabPane().getSelectionModel().select(primaryView.getTabPane().getTabs().size() - 1);
	}
	
	private void createAlert(String warning, String content) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setHeaderText(warning);
		alert.setContentText(content);
                alert.showAndWait();
	}
	
	private String readUnfoundWebsites(List<String> unfoundWebsites) {
		String unfoundWebsitesString = new String();
		
		for(int i = 0; i < unfoundWebsites.size(); i++) {
			if (i < unfoundWebsites.size() - 1) {
				unfoundWebsitesString = unfoundWebsites.get(i) + ", ";
			}
			else {
				unfoundWebsitesString = unfoundWebsites.get(i);
			}
		}
		
		return unfoundWebsitesString;
	}
	
	private ResultView readFile(int numTabs) {
		try {
			List<String> urls = new ArrayList<>();
			List<List<String>> output = new ArrayList<>();
			
			Scanner s = new Scanner(new File("writeTest.txt"));
			
		    String resultString = new String("");
		    
		    int counter = -1;
			
			while(s.hasNext()) {
				//System.out.println(resultString);
				String line = s.nextLine();
				
				if(line.startsWith("-~-")) {
					output.add(new ArrayList<String>());
					
					if(resultString.startsWith("-~-")) {
						urls.add(resultString.substring(3));
						resultString = line;
						counter++;
					}
					else if(resultString.startsWith("\t-@-")) {
						output.get(counter).add(resultString.substring(4));
						resultString = line;
						counter++;
					}
					else {
						resultString += line;
						counter++;
					}
				}
				else if(line.startsWith("\t-@-")) {
					if(resultString.startsWith("-~-")) {
						urls.add(resultString.substring(3));
						resultString = line;
					}
					else if(resultString.startsWith("\t-@-")) {
						output.get(counter).add(resultString.substring(4));
						resultString = line;
					}
					else {/*Should never happen*/}
				}
				else {resultString += "\n" + line;}
			}
			if(resultString != null && resultString.length() > 4)
                            output.get(counter).add(resultString.substring(4));
			
			s.close();
			
			return new ResultView(urls, output, numTabs);
		} catch (FileNotFoundException e) {
			return new ResultView(null, null, numTabs);
		}
	}
	
	public Tab getSearchView() {
		return searchTab;
	}
}
