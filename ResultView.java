/**
* License: CC Attribution Non-Commercial 4.0 International
*    See short hand summary here:
*        http://creativecommons.org/licenses/by-nc/4.0/
*    See legal specifications here:
*        http://creativecommons.org/licenses/by-nc/4.0/legalcode
*
*    Reuse of code allowed under the conditions in the link above.
*/
package webchase;

import java.util.*;
import java.awt.Desktop;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.stage.FileChooser.*;

/**
 * This Class is passed a list of Strings, a list of lists of outputs, and an integer,
 * 		representing urls with outputs, the collection of outputs for a url, and the number
 * 		of result tabs already created. The main UI Element created by this class is a Tab,
 * 		containing a CheckBoxTreeView, and several Buttons.
 * @author Mitchell Powell
 */
public class ResultView {
	List<String> urls;							//List of URLs with output
	List<List<String>> outputs;					//List of Lists of outputs for each URL
	
	Tab resultsTab;								//The Tab created by this class
	
	TreeView<String> resultTree;				//The TreeView containing each URL & URL's outputs
	List<CheckBoxTreeItem<String>> branches;	//List of Branches for the TreeView (each contains a URL)
	
	Button selectAll, deselectAll, save;		//Buttons for selecting/deselecting all checkboxes
												//& saving
	
	/**
	 * Takes a list of urls, and a list of lists of outputs, and an int. Assigns urls
	 * to this urls, assigns ouputs to this outputs, 
	 * @param urls list of urls with results
	 * @param outputs list of lists of outputs which correspond to each url
	 * @param resultNum the number of resultTab's already open.
	 */
	public ResultView(List<String> urls, List<List<String>> outputs, int resultNum) {
		this.urls = urls;
		this.outputs = outputs;
		
		BorderPane view = createView();
		createResultTab(view, resultNum);
		addActionListeners();
	}
	
	/**
	 * Creates a result Tab titled based on resultNum, and sets the content
	 * of the Tab to the provided BorderPane.
	 * @param view
	 * @param resultNum
	 */
	private void createResultTab(BorderPane view, int resultNum) {
		this.resultsTab = new Tab("Result" + resultNum);
		this.resultsTab.setContent(view);
	}
	
	/**
	 * Creates view and adds bottom to the bottom, and resultTree to the center.
	 * @return the BorderPane created by this method.
	 */
	private BorderPane createView() {
		BorderPane view = new BorderPane();
		
		HBox bottom = createBottom();
		this.resultTree = createTreeView();
		
		view.setCenter(this.resultTree);
		view.setBottom(bottom);
		
		return view;
	}
	
	/**
	 * Creates bottom and adds buttons.
	 * @return the HBox created by this method.
	 */
	private HBox createBottom() {
		HBox bottom = new HBox();
		
		List<Button> buttons = createButtons();
		
		bottom.setPadding(new Insets(10));
		bottom.setSpacing(10);
		bottom.getChildren().addAll(buttons);
		
		return bottom;
	}
	
	/**
	 * Creates a TreeView containing the specified urls and outputs.
	 * @return the TreeView created by this method.
	 */
	private TreeView<String> createTreeView() {
		TreeView<String> resultTree = new TreeView<String>();
		
		this.branches = new ArrayList<>();
		
		CheckBoxTreeItem<String> root = new CheckBoxTreeItem<>();
		root.setExpanded(true);
		resultTree.setShowRoot(false);
		resultTree.setFocusTraversable(false);

		for(int i = 0; i < this.urls.size(); i++) {	
			Hyperlink link = createLink(this.urls.get(i));
			
			CheckBoxTreeItem<String> box = new CheckBoxTreeItem<>(null, link);
			box.getGraphic().setFocusTraversable(false);
		
			this.branches.add(makeBranch(box, root));
		
			for(int j = 0; j < this.outputs.get(i).size(); j++) {
				CheckBoxTreeItem<String> box2 = new CheckBoxTreeItem<>(this.outputs.get(i).get(j));
				
				makeBranch(box2, this.branches.get(i));
			}
		}
		
		resultTree.setCellFactory(CheckBoxTreeCell.forTreeView());
		resultTree.setRoot(root);
		resultTree.setEditable(true);

		return resultTree;
	}
	
	/**
	 * Creates a generic link that navigates to the specified URL.
	 * @param linkText the text with which to create the HyperLink
	 * @return the HyperLink created by this method
	 */
	private Hyperlink createLink(String linkText) {
		Hyperlink link = new Hyperlink(linkText);
	
		link.setOnAction(e -> {
			if(Desktop.isDesktopSupported()) {
				try 						  {Desktop.getDesktop().browse(new URI(link.getText()));}
				catch(IOException ioe)		  {}
				catch(URISyntaxException use) {}
			}
		});
		
		return link;
	}
	
	/**
	 * Creates the Buttons for this resultTab
	 * @return a list of Buttons created.
	 */
	private List<Button> createButtons() {
		List<Button> buttons = new ArrayList<>();
		
		this.selectAll = new Button("Select All");
		this.deselectAll = new Button("Deselect All");
		this.save = new Button("Save");
		
		this.selectAll.setFocusTraversable(false);
		this.deselectAll.setFocusTraversable(false);
		this.save.setFocusTraversable(false);
		
		buttons.add(this.selectAll);
		buttons.add(this.deselectAll);
		buttons.add(this.save);
		
		return buttons;
	}
	
	/**
	 * Adds ActionListeners to the proper UI Elements.
	 */
	private void addActionListeners() {
		this.selectAll.setOnMouseClicked(e -> handleSelectAllButtonAction(e));
		this.deselectAll.setOnMouseClicked(e -> handleDeselectAllButtonAction(e));
		this.save.setOnMouseClicked(e -> handleSaveButtonAction(e));
	}
	
	/**
	 * When Select All button is pressed, check all unchecked boxes
	 * @param e MouseEvent which triggers this method
	 */
	private void handleSelectAllButtonAction(MouseEvent e) {
		CheckBoxTreeItem<String> root = (CheckBoxTreeItem<String>)this.resultTree.getRoot();
		int numBranches = root.getChildren().size();
		
		for(int i = 0; i < numBranches; i++) {
			if(!((CheckBoxTreeItem<String>)root.getChildren().get(i)).isSelected()) {
				((CheckBoxTreeItem<String>)root.getChildren().get(i)).setSelected(true);
			}
		}
	}
	
	/**
	 * When Deselect All button is pressed, uncheck all checked boxes
	 * @param e MouseEvent which triggers this method
	 */
	private void handleDeselectAllButtonAction(MouseEvent e) {
		CheckBoxTreeItem<String> root = (CheckBoxTreeItem<String>)this.resultTree.getRoot();
		int numBranches = root.getChildren().size();
		
		for(int i = 0; i < numBranches; i++) {
			if(((CheckBoxTreeItem<String>)root.getChildren().get(i)).isSelected()) {
				((CheckBoxTreeItem<String>)root.getChildren().get(i)).setSelected(false);
			}
		}
	}
	
	/**
	 * Creates a FileChooser for the user to specify a save file and
	 * saves it to the specified save file.
	 * @param e MouseEvent which triggered this method
	 */
	private void handleSaveButtonAction(MouseEvent e) {
		Stage stage = new Stage();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Result");
		fileChooser.setInitialFileName("*.txt");
		
		File file = fileChooser.showSaveDialog(stage);
		
		if(file != null) {
			if(!file.getName().endsWith(".txt")) {
				file = new File(file.getName() + ".txt");
			}
		}
		else {return; /*User chose cancel*/}
		 
		try {
			/*See WebController.write(int stopWriteIndex)*/
			PrintWriter printWriter = 
					new PrintWriter(
					new BufferedWriter(
					new FileWriter(file)));
			
			CheckBoxTreeItem<String> root = (CheckBoxTreeItem<String>)this.resultTree.getRoot();
			
			for (int i = 0; i < root.getChildren().size(); i++) {
				CheckBoxTreeItem<String> branch = (CheckBoxTreeItem<String>)root.getChildren().get(i);
				
				if (branch.isSelected() || branch.isIndeterminate()) {
					printWriter.println("-~-" + ((Hyperlink)branch.getGraphic()).getText());
					
					for (int j = 0; j < branch.getChildren().size(); j++) {
						CheckBoxTreeItem<String> leaf = (CheckBoxTreeItem<String>)branch.getChildren().get(j);
						
						if(leaf.isSelected()) {
							printWriter.println("\t-@-" + leaf.getValue());
						}
					}
				}
			}
			printWriter.close();
		} catch (IOException ioe) {
			
		}
		
		CheckBoxTreeItem<String> root = (CheckBoxTreeItem<String>)this.resultTree.getRoot();
		int numBranches = root.getChildren().size();
	}
	
	/**
	 * Creates a branch with the specified parent and child.
	 * @param child The child CheckBoxTreeItem to be added
	 * @param parent the parent CheckBoxTreeItem to add to.
	 * @return the child
	 */
	public CheckBoxTreeItem<String> makeBranch(CheckBoxTreeItem<String> child, CheckBoxTreeItem<String> parent) {
		child.setExpanded(true);
		parent.getChildren().add(child);
		return child;
	}
	
	/**
	 * Returns this resultTab
	 * @return this resultTab
	 */
	public Tab getResultTab() {
		return this.resultsTab;
	}
}
