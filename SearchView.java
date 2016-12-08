package webchase;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.*;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.*;
import javafx.geometry.*;

/**
 * This class is passed a PrimaryView, and sets up the SearchView for the application.
 * 		The main UI Element created by this class is a Tab, containing several Buttons,
 * 		Labels, TextFields, and Lists.
 * @author Mitchell Powell
 *
 */
public class SearchView implements Observer{
	private PrimaryView primaryView;				//Holds an instance of this SearchView's
													//PrimaryView.
	private BorderPane right;						//Node nested on right side of searchTab
	
	private Tab searchTab;							//The Tab created by this Class
	private TextField urlField, termField,			//TextFields accept URLs, Terms, and Depth
					  depthField;
	private Button addUrl, removeUrl,				//Buttons for adding/removing URLs/Terms,
				   addTerm, removeTerm,				//searching, and halting searches.
		   		   searchButton, haltButton;
	private ListView<String> urlList, termList;		//ListView containing URLs and Terms to search
	
	private ArrayList<String> urlArrayList,			//List of URLs and Terms passed to WebController
							  termArrayList;
	private int depth;								//Depth passed to WebController
	
	private WebController controller;				//Instance of the Web Crawler
	
	private final String urlKey = "-~-";			//Denotes a url in a plain text file
	private final String outputKey = "\t-@-";		//Denotes an output in a plain text file
	
	private final Insets PAD = new Insets(10);		//Padding for UI Nodes
	
	/**
	 * Takes an instance of a primaryView, and assigns it to this primaryView.
	 * 		Creates the UI Elements for this SearchView, creates view,
	 * 		configures SearchTab, and adds necessary ActionListeners.
	 * @param primaryView
	 */
	public SearchView(PrimaryView primaryView) {
		this.primaryView = primaryView;
		
		HBox view = createView();
		createSearchTab(view);
		addActionListeners();
	}
	
	//********************************************************************//
	//**********************SET***UP***SEARCH***TAB***********************//
	//********************************************************************//
	
	/**
	 * Configures settings of this searchTab.
	 * @param view The Region to set as content of the tab.
	 */
	private void createSearchTab(HBox view) {
		this.searchTab = new Tab("Search");
		this.searchTab.setClosable(false);
		this.searchTab.setContent(view);
	}
	
	//********************************************************************//
	//**********************CREATE***GUI***ELEMENTS***********************//
	//********************************************************************//
	
	/**
	 * Creates view, and adds view's children
	 * @return view
	 */
	private HBox createView() {
		HBox view = new HBox();
		
		VBox left = createLeft();
		this.right = createRight();
		
		configureHBox(view, false, 0, (Region)left, 
									  (Region)this.right);
		
		return view;
	}
	
	/**
	 * Creates left, and adds left's children
	 * @return left
	 */
	private VBox createLeft() {
		VBox left = new VBox();
		
		HBox urlFieldArea = createUrlFieldArea();
		HBox urlListButtonArea = createUrlListButtonArea();
		HBox termFieldArea = createTermFieldArea();
		HBox termListButtonArea = createTermListButtonArea();
		
		configureVBox(left, false, 0, (Region)urlFieldArea,
							   		  (Region)urlListButtonArea,
							   		  (Region)termFieldArea,
							   		  (Region)termListButtonArea);	
		return left;
	}
	
	/**
	 * Creates right, and adds right's children
	 * @return right
	 */
	private BorderPane createRight() {		
		BorderPane right = new BorderPane();
		
		VBox bottomRight = createBottomRight();
		
		right.setBottom(bottomRight);
		
		return right;
	}
	
	/**
	 * Creates urlFieldArea, and adds urlFieldArea's children
	 * @return urlFieldArea
	 */
	private HBox createUrlFieldArea() {
		HBox urlFieldArea = new HBox();
		
		Label urlFieldLabel = createLabel("URL:");
		this.urlField = createTextField("http://www.github.com");
		
		configureHBox(urlFieldArea, true, 10, (Region)urlFieldLabel,
											  (Region)urlField);
		return urlFieldArea;
	}
	
	/**
	 * Creates urlListButtonArea, and adds urlListButtonArea's children
	 * @return urlListButtonArea
	 */
	private HBox createUrlListButtonArea() {
		HBox urlListButtonArea = new HBox();
		
		VBox urlButtonArea = createUrlButtonArea();
		this.urlList = createListView();
		
		configureHBox(urlListButtonArea, true, 5, (Region)urlButtonArea,
												  (Region)urlList);
		return urlListButtonArea;
	}
	
	/**
	 * Creates termFieldArea, and adds termFieldArea's children
	 * @return termFieldArea
	 */
	private HBox createTermFieldArea() {
		HBox termFieldArea = new HBox();
		
		Label termFieldLabel = createLabel("Search Term:");
		this.termField = createTextField("Delay");
		
		configureHBox(termFieldArea, true, 10, (Region)termFieldLabel,
											   (Region)termField);
		return termFieldArea;
	}
	
	/**
	 * Creates termListButtonArea, and adds termListButtonArea's children
	 * @return termListButtonArea
	 */
	private HBox createTermListButtonArea() {
		HBox termListButtonArea = new HBox();
		
		VBox termButtonArea = createTermButtonArea();
		this.termList = createListView();
		
		configureHBox(termListButtonArea, true, 5, (Region)termButtonArea,
												   (Region)termList);
		return termListButtonArea;
	}
	
	/**
	 * Creates bottomRight, and adds bottomRight's children
	 * @return bottomRight
	 */
	private VBox createBottomRight() {
		VBox bottomRight = new VBox();
		
		HBox depthFieldArea = createDepthFieldArea();
		HBox searchButtonArea = createSearchButtonArea();
				
		configureVBox(bottomRight, false, 0, (Region)depthFieldArea,
											 (Region)searchButtonArea);
		return bottomRight;
	}
	
	/**
	 * Creates urlButtonArea, and adds urlButtonArea's children
	 * @return urlButtonArea
	 */
	private VBox createUrlButtonArea() {
		VBox urlButtonArea = new VBox();
		
		this.addUrl = createButton("Add To List", true);
		this.removeUrl = createButton("Remove Last", true);
		Label urlListLabel = createLabel("             URL(s) to be used:");

		configureVBox(urlButtonArea, true, 15, (Region)addUrl,
											   (Region)removeUrl,
											   (Region)urlListLabel);
		return urlButtonArea;
	}
	
	/**
	 * Creates termButtonArea, and adds termButtonArea's children
	 * @return termButtonArea
	 */
	private VBox createTermButtonArea() {
		VBox termButtonArea = new VBox();
		
		this.addTerm = createButton("Add To List", true);
		this.removeTerm = createButton("Remove Last", true);
		Label termListLabel = createLabel("Search Term(s) to be used:");
		
		configureVBox(termButtonArea, true, 15, (Region)addTerm,
												(Region)removeTerm,
												(Region)termListLabel);
		return termButtonArea;
	}
	
	/**
	 * Creates searchButtonArea, and adds searchButtonArea's children
	 * @return searchButtonArea
	 */
	private HBox createSearchButtonArea() {
		HBox searchButtonArea = new HBox();
		
		this.searchButton = createButton("Search", false);
		this.haltButton = createButton("Halt Search", false);
		
		configureHBox(searchButtonArea, true, 15, (Region)searchButton,
												  (Region)haltButton);
		return searchButtonArea;
	}
	
	/**
	 * Creates depthFieldArea, and adds depthFieldArea's children
	 * @return depthFieldArea
	 */
	private HBox createDepthFieldArea() {
		HBox depthFieldArea = new HBox();
		
		Label depthFieldLabel = createLabel("Depth:");
		this.depthField = createTextField("4");
		
		configureHBox(depthFieldArea, true, 10, (Region)depthFieldLabel,
												(Region)depthField);
		return depthFieldArea;
	}
	
	/**
	 * Creates a Label with the provided String as the text.
	 * @param labelText the text the label should have.
	 * @return a new label with the specified text.
	 */
	private Label createLabel(String labelText) {
		return new Label(labelText);
	}
	
	/**
	 * Creates a TextField with the provided String as the prompt.
	 * @param promptText the String to set the prompt text to.
	 * @return a text field with the specified prompt text
	 */
	private TextField createTextField(String promptText) {
		TextField textField = new TextField();
		textField.setPromptText(promptText);
		return textField;
	}
	
	/**
	 * Creates a Button with the specified String as the text on the button,
	 * 		sets the button to the maximum width allowed (Double.MAX_VALUE), and
	 * 		sets focusTraversable to false. Setting the maximum width to Double.MAX
	 * 		means the button will stretch horizontally to fit it's parent.
	 * @param text the String to set the text on the button
	 * @param isMaxWidth specifies whether to set the button to the maximum width allowed
	 * @return a button with the specified text on the button, and a max width of
	 * 		   Double.MAX_VALUE if specified.
	 */
	private Button createButton(String text, boolean isMaxWidth) {
		Button button = new Button(text);
		if(isMaxWidth) {button.setMaxWidth(Double.MAX_VALUE);}
		button.setFocusTraversable(false);
		
		return button;
	}
	
	/**
	 * Creates a ListView<String>, which is not focus traversable.
	 * @return a ListView<String>, which is not focus traversable. 
	 */
	private ListView<String> createListView() {
		ListView<String> listView = new ListView<>();
		listView.setFocusTraversable(false);
		
		return listView;
	}
	
	/**
	 * Configures an HBox
	 * @param hBox HBox to be configured
	 * @param pad specifies whether to add padding
	 * @param space spacing to add to hBox
	 * @param regions children to add to hBox
	 */
	private void configureHBox(HBox hBox, boolean pad, int space, Region... regions) {
		if(pad) {hBox.setPadding(PAD);}
		hBox.setSpacing(space);
		hBox.getChildren().addAll(regions);	
	}
	
	/**
	 * Configures a VBox
	 * @param vBox VBox to be configured
	 * @param pad specifies whether to add padding
	 * @param space spacing to add to vBox
	 * @param regions children to add to vBox
	 */
	private void configureVBox(VBox vBox, boolean pad, int space, Region... regions) {
		if(pad) {vBox.setPadding(PAD);}
		vBox.setSpacing(space);
		vBox.getChildren().addAll(regions);
	}

	//********************************************************************//
	//********************CREATE***ACTION***LISTENERS*********************//
	//********************************************************************//
	
	/**
	 * Adds ActionListeners to the proper UI Elements.
	 */
	private void addActionListeners() {
		this.addUrl.setOnMouseClicked(e -> handleAddUrlButtonAction(e));
		this.removeUrl.setOnMouseClicked(e -> handleRemoveUrlButtonAction(e));
		
		this.addTerm.setOnMouseClicked(e -> handleAddTermButtonAction(e));
		this.removeTerm.setOnMouseClicked(e -> handleRemoveTermButtonAction(e));
		
		this.searchButton.setOnMouseClicked(e -> handleSearchButtonAction(e));
		this.haltButton.setOnMouseClicked(e -> handleHaltAction(e));
		
		this.urlField.setOnKeyPressed(e -> handleEnterUrlButtonAction(e));
		this.termField.setOnKeyPressed(e -> handleEnterTermButtonAction(e));
		this.depthField.setOnKeyPressed(e -> handleEnterDepthButtonAction(e));
	}
	
	/**
	 * When Add URL button is pressed, call addUrl()
	 * @param e MouseEvent which triggers this method.
	 */
	private void handleAddUrlButtonAction(MouseEvent e) {
		addUrl();
	}
	
	/**
	 * When Remove URL button is pressed, callRemoveUrl() 
	 * @param e MouseEvent which triggers this method.
	 */
	private void handleRemoveUrlButtonAction(MouseEvent e) {
		removeUrl();
	}
	
	/**
	 * When Add Term button is pressed, call addTerm()
	 * @param e MouseEvent which triggers this method.
	 */
	private void handleAddTermButtonAction(MouseEvent e) {
		addTerm();
	}
	
	/**
	 * When Remove Term button is pressed, call removeTerm()
	 * @param e MouseEvenet which triggers this method.
	 */
	private void handleRemoveTermButtonAction(MouseEvent e) {
		removeTerm();
	}
	
	/**
	 * When Search button is pressed, call validateInput()
	 * @param e MouseEvent which triggers this method.
	 */
	private void handleSearchButtonAction(MouseEvent e) {
		validateInput();
	}
	
	/**
	 * When Halt button is pressed, call controller.halt()
	 * @param e MouseEvent which triggers this method.
	 */
	private void handleHaltAction(MouseEvent e) {
		this.controller.halt();
	}
	
	/**
	 * When Enter key is pressed in URL Field, call addUrl()
	 * @param e KeyEvent which triggers this method.
	 */
	private void handleEnterUrlButtonAction(KeyEvent e) {
		if(e.getCode().equals(KeyCode.ENTER)) {
			addUrl();
		}
	}
	
	/**
	 * When Enter key is pressed in Term Field, call addTerm()
	 * @param e KeyEvent which triggers this method.
	 */
	private void handleEnterTermButtonAction(KeyEvent e) {
		if(e.getCode().equals(KeyCode.ENTER)) {
			addTerm();
		}
	}
	
	/**
	 * When Enter key is pressed in Depth Field, call validateIntput()
	 * @param e KeyEvent which triggers this method.
	 */
	private void handleEnterDepthButtonAction(KeyEvent e) {
		if(e.getCode().equals(KeyCode.ENTER)) {
			validateInput();
		}
	}
	
	/**
	 * Checks urlField to see if it has input to add, scans previous
	 * 		inputs in urlList for duplicate inputs, and adds the input to urlList if no duplicate
	 * 		inputs are found, or the user specifies adding a duplicate input is acceptable.
	 */
	private void addUrl() {
		if(!fieldIsEmpty(this.urlField)){
			boolean duplicateFound = checkListDuplicates(this.urlList, this.urlField);
			
			if(duplicateFound) {
				boolean addAnyway = createConfirmation(
						"This URL is already on the list.",
		  			  		"Do you wish to add the URL anyway?");
				if(addAnyway) {
					addItemToList(this.urlList, this.urlField);
					clearField(this.urlField);
				}
				else {
					clearField(this.urlField);
				}
			}
			else {
				addItemToList(this.urlList, this.urlField);
				clearField(this.urlField);
			}
		}
		else {
			createWarning("No url provided.",
					"There is no url in the url "
					+ "field (a url that is only"
					+ "\nwhite space is ignored).");
		}
	}
	
	/**
	 * Removes the last URL on the urlList if the urlList is not empty.
	 */
	private void removeUrl() {
		if(!this.urlList.getItems().isEmpty()) {
			this.urlList.getItems().remove(this.urlList.getItems().size() - 1);
		}
	}
	
	/**
	 * Checks termField to see if it has input to add, scans previous
	 * 		inputs in termList for duplicate inputs, and adds the input to termList if no duplicate
	 * 		inputs are found, or the user specifies adding a duplicate input is acceptable.
	 */
	private void addTerm() {
		if(!fieldIsEmpty(this.termField)){
			boolean duplicateFound = checkListDuplicates(this.termList, this.termField);
			
			if(duplicateFound) {
				boolean addAnyway = createConfirmation(
						"This search term is already on the list.",
		  			  		"Do you wish to add the search term anyway?");
				if(addAnyway) {
					addItemToList(this.termList, this.termField);
					clearField(this.termField);
				}
				else {
					clearField(this.termField);
				}
			}
			else {
				addItemToList(this.termList, this.termField);
				clearField(this.termField);
			}
		}
		else {
			createWarning("No search term provided.",
					"There is no search term in the search term "
					+ "field (a search term that is only"
					+ "\nwhite space is ignored).");
		}
	}
	
	/**
	 * Removes the last term on the termList if the termList is not empty.
	 */
	private void removeTerm() {
		if(!this.termList.getItems().isEmpty()) {
			this.termList.getItems().remove(this.termList.getItems().size() - 1);
		}
	}
	
	/**
	 * Verifies both the urlList and the termList contain at least one item each, and
	 * 		depthField contains input, validates depthField contains proper input (integer),
	 * 		and if all conditions are met, calls several methods to set up for searching.
	 */
	private void validateInput() {
		int urlListSize = this.urlList.getItems().size();
		int termListSize = this.termList.getItems().size();
		
		if(urlListSize == 0){
			createWarning("No search urls provided.",
						"There are no urls in the url list.");
			this.urlField.requestFocus();
		}
	
		else if(termListSize == 0){
			createWarning("No search terms provided.",
						"There are no terms in the term list.");
			this.termField.requestFocus();
		}
		
		else if(fieldIsEmpty(this.depthField)) {
			createWarning("No search depth provided.",
					"There is no depth in the depth "
					+ "field (a depth that is only"
					+ "\nwhite space is ignored).");
			this.depthField.requestFocus();
		}
		
		else {
			try {assignInputs();}
			catch (NumberFormatException e) {
				createWarning("Illegal Depth Argument: " + this.depthField.getText(), 
							"Depth must be an integer 1, 7, 92, etc.");
				return;
			}
			clearInputs();
			addProgressIndicator();
			search();
		}
	}
	
	/**
	 * Checks if a provided TextField is empty
	 * @param field TextField to be checked
	 * @return is field empty
	 */
	private boolean fieldIsEmpty(TextField field) {
		return field.getText() == null || field.getText().trim().isEmpty();
	}
	
	private boolean checkListDuplicates(ListView<String> list, TextField field) {
		for(String item: list.getItems()) {
			if(field.getText().equals(item)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Adds an item to the provided list, by pulling text from the provided
	 * 		TextField.
	 * @param list list to add an item to
	 * @param field field to pull text from
	 */
	private void addItemToList(ListView<String> list, TextField field) {
		list.getItems().add(field.getText());
		list.scrollTo(list.getItems().size() - 1);
	}
	
	/**
	 * Clears the text in the specified field.
	 * @param field field to be cleared
	 */
	private void clearField(TextField field) {
		field.setText("");
	}
	
	/**
	 * Assigns inputs in depthField, urlList, and termList to variables.
	 * @throws NumberFormatException when depthField.getText() cannot be parsed
	 * 		   to an Integer.
	 */
	private void assignInputs() throws NumberFormatException{
		this.depth = Integer.parseInt(this.depthField.getText());
		
		this.urlArrayList = new ArrayList<String>();
		this.termArrayList = new ArrayList<String>();
		
		for(String item: this.urlList.getItems()) {
			this.urlArrayList.add(item);
		}
		
		for(String item: this.termList.getItems()) {
			this.termArrayList.add(item);
		}
	}
	
	/**
	 * Clears urlList, termList, and depthField.
	 */
	private void clearInputs() {
		this.urlList.getItems().clear();
		this.termList.getItems().clear();
		this.depthField.setText("");
	}
	
	/**
	 * Adds a ProgressIndicator to the center right of this searchTab.
	 */
	private void addProgressIndicator() {
		this.right.setCenter(new ProgressIndicator(-1.0f));
		this.right.getCenter().setScaleX(.5);
		this.right.getCenter().setScaleY(.5);
	}
	
	/**
	 * Removes the ProgressIndicator from the center right of this searchTab.
	 */
	private void removeProgressIndicator() {
		this.right.setCenter(null);
	}
	
	/**
	 * Creates a new WebController Object, and a new Thread Object with the WebController.
	 * 		Starts the new thread.
	 */
	private void search() {
		this.controller = new WebController(urlArrayList, termArrayList, depth);
		this.controller.addObserver(this);
		Thread t = new Thread(this.controller);
		t.start();
	}
	
	/**
	 * When updated by an Observable object, if obs is WebController
	 * 		and msg is List, check if the List contains any unfound websites.
	 * 		If the List contains unfound websites, create an Alert. Call
	 * 		methods to remove the progress indicator and create a result view.
	 */
	public void update(Observable obs, Object msg) {
		if((msg instanceof List && obs instanceof WebController)) {
			if(((List<String>)msg).size() != 0) {
				List<String> unfoundWebsites = (List<String>)msg;
				createWarning("Some Websites Not Found", unfoundWebsites.toString());
			}
            
			removeProgressIndicator();
			createResultView();
		}
	}
	
	/**
	 * Creates a ResultView and adds it to the tabPane in this primaryView.
	 */
	private void createResultView() {
		int numTabs = this.primaryView.getTabPane().getTabs().size();
		ResultView resultView = readWebControllerOutput(numTabs);

		this.primaryView.getTabPane().getTabs().add(resultView.getResultTab());
		this.primaryView.getTabPane().getSelectionModel().select(this.primaryView.getTabPane().getTabs().size() - 1);
	}
	
	/**
	 * Creates and shows a Warning with the specified header and content.
	 * @param header header to be added to the Warning
	 * @param content content to be added to the Warning
	 */
	private void createWarning(String header, String content) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setHeaderText(header);
		alert.setContentText(content);
        alert.showAndWait();
	}
	
	/**
	 * Creates a Confirmation with the specified header and content. Returns
	 * 		a boolean representing whether the user pressed the Ok button.
	 * @param header header to be added to the Confirmation
	 * @param content content to be added to the Confirmation
	 * @return true if user pressed Ok button, false else.
	 */
	private boolean createConfirmation(String header, String content) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(header);
		alert.setContentText(content);
		
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {return true;}
		else {return false;}
	}
	
	/**
	 * Reads the WebController output from a predetermined file, and creates a ResultView
	 * 		from the output.
	 * @param numTabs number of tabs in this primaryView tabPane.
	 * @return the resultView created by this method.
	 */
	private ResultView readWebControllerOutput(int numTabs) {
		try {
			List<String> urls = new ArrayList<>();
			List<List<String>> output = new ArrayList<>();
			
			Scanner s = new Scanner(new File("writeTest.txt"));
			
		    String resultString = new String("");
		    
		    int counter = -1;
			
			while(s.hasNext()) {
				String line = s.nextLine();
				
				if(line.startsWith(this.urlKey)) {
					output.add(new ArrayList<String>());
					
					if(resultString.startsWith(this.urlKey)) {
						urls.add(resultString.substring(3));
						resultString = line;
						counter++;
					}
					else if(resultString.startsWith(this.outputKey)) {
						output.get(counter).add(resultString.substring(4));
						resultString = line;
						counter++;
					}
					else {
						resultString += line;
						counter++;
					}
				}
				else if(line.startsWith(this.outputKey)) {
					if(resultString.startsWith(this.urlKey)) {
						urls.add(resultString.substring(3));
						resultString = line;
					}
					else if(resultString.startsWith(this.outputKey)) {
						output.get(counter).add(resultString.substring(4));
						resultString = line;
					}
					else {/*Should never happen*/}
				}
				else {resultString += "\n" + line;}
			}
			
			if(resultString != null && resultString.length() > 4) {
				output.get(counter).add(resultString.substring(4));
			}
			
			s.close();
			
			return new ResultView(urls, output, numTabs);
		} catch (FileNotFoundException e) {
			return new ResultView(null, null, numTabs);
		}
	}
	
	/**
	 * Returns this searchTab
	 * @return this searchTab
	 */
	public Tab getSearchTab() {
		return this.searchTab;
	}
}
