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

public class ResultView {
	
	List<WebPage> pagesWithResults;
	List<String> urls;
	List<List<String>> outputs;
	
	Tab resultsTab;
	
	TreeView<String> resultTree;
	List<CheckBoxTreeItem<String>> branches;
	
	Button selectAll, deselectAll, save;
	
	public ResultView(List<String> urls, List<List<String>> outputs, int resultNum) {
		createUIElements(urls, outputs, resultNum);
		addActionListeners();
	}
	
	private void createUIElements(List<String> urls, List<List<String>> outputs, int resultNum) {
		this.urls = urls;
		this.outputs = outputs;
		
		resultsTab = new Tab("Result" + resultNum);
		
		resultTree = new TreeView<>();
		branches = new ArrayList<>();
		
		selectAll = new Button("Select All");
		deselectAll = new Button("Deselect All");
		save = new Button("Save");
		
		BorderPane view = new BorderPane();
		HBox bottom = new HBox();
		
		CheckBoxTreeItem<String> root = new CheckBoxTreeItem<>();
		root.setExpanded(true);
		resultTree.setShowRoot(false);
		resultTree.setFocusTraversable(false);
		//System.out.println(root.getValuse());

		for(int i = 0; i < this.urls.size(); i++) {	
			Hyperlink link = new Hyperlink(this.urls.get(i));
			
			link.setOnAction(e -> {
				if(Desktop.isDesktopSupported()) {
					try 						  {Desktop.getDesktop().browse(new URI(link.getText()));}
					catch(IOException ioe)		  {}
					catch(URISyntaxException use) {}
				}
			});
			
			CheckBoxTreeItem<String> box = new CheckBoxTreeItem<>(null, link);
			box.getGraphic().setFocusTraversable(false);
			//box.setGraphic(link);
		
			branches.add(makeBranch(box, root));
			//System.out.println(urls.get(i));
		
			for(int j = 0; j < this.outputs.get(i).size(); j++) {
				CheckBoxTreeItem<String> box2 = new CheckBoxTreeItem<>(outputs.get(i).get(j));
				
				makeBranch(box2, branches.get(i));
				//System.out.println(outputs.get(i).get(j));
			}
		}
		
		resultTree.setCellFactory(CheckBoxTreeCell.forTreeView());
		resultTree.setRoot(root);
		resultTree.setEditable(true);
		
		selectAll.setFocusTraversable(false);
		deselectAll.setFocusTraversable(false);
		save.setFocusTraversable(false);
		
		bottom.setPadding(new Insets(10));
		bottom.setSpacing(10);
		bottom.getChildren().addAll(selectAll, deselectAll, save);
		
		view.setCenter(resultTree);
		view.setBottom(bottom);
		
		resultsTab.setContent(view);
	}
	
	private void addActionListeners() {
		selectAll.setOnMouseClicked(e -> handleSelectAllButtonAction(e));
		deselectAll.setOnMouseClicked(e -> handleDeselectAllButtonAction(e));
		save.setOnMouseClicked(e -> handleSaveButtonAction(e));
	}
	
	private void handleSelectAllButtonAction(MouseEvent e) {
		CheckBoxTreeItem<String> root = (CheckBoxTreeItem<String>)resultTree.getRoot();
		int numBranches = root.getChildren().size();
		
		for(int i = 0; i < numBranches; i++) {
			if(!((CheckBoxTreeItem<String>)root.getChildren().get(i)).isSelected()) {
				((CheckBoxTreeItem<String>)root.getChildren().get(i)).setSelected(true);
			}
		}
	}
	
	private void handleDeselectAllButtonAction(MouseEvent e) {
		CheckBoxTreeItem<String> root = (CheckBoxTreeItem<String>)resultTree.getRoot();
		int numBranches = root.getChildren().size();
		
		for(int i = 0; i < numBranches; i++) {
			if(((CheckBoxTreeItem<String>)root.getChildren().get(i)).isSelected()) {
				((CheckBoxTreeItem<String>)root.getChildren().get(i)).setSelected(false);
			}
		}
	}
	
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
			
			CheckBoxTreeItem<String> root = (CheckBoxTreeItem<String>)resultTree.getRoot();
			
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
		
		CheckBoxTreeItem<String> root = (CheckBoxTreeItem<String>)resultTree.getRoot();
		int numBranches = root.getChildren().size();
	}
	
	public CheckBoxTreeItem<String> makeBranch(CheckBoxTreeItem<String> title, CheckBoxTreeItem<String> parent) {
		title.setExpanded(true);
		parent.getChildren().add(title);
		return title;
	}
	
	public Tab getResultTab() {
		return this.resultsTab;
	}
}
