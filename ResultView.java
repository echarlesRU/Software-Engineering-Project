package webchase;

import java.util.*;
import java.util.concurrent.*;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class ResultView {
	
	List<WebPage> pagesWithResults;
	List<String> urls;
	List<List<String>> outputs;
	
	Tab resultsTab;
	
	TreeView<String> resultTree;
	List<CheckBoxTreeItem<String>> branches;
	
	Button selectAll, save;
	
	public ResultView(List<Future<WebPage>> results, int resultNum) {
		createUIElements(results, resultNum);
		addActionListeners();
	}
	
	private void createUIElements(List<Future<WebPage>> results, int resultNum) {
		pagesWithResults = new ArrayList<WebPage>();
		
		try 				  {for(Future<WebPage> result: results) {pagesWithResults.add(result.get());}
		} catch (Exception e) {System.out.println(e.getClass());}
		
		urls = new ArrayList<String>();
		outputs = new ArrayList<>();
		
		resultsTab = new Tab("Result" + resultNum);
		
		resultTree = new TreeView<>();
		branches = new ArrayList<>();
		
		selectAll = new Button("Select All");
		save = new Button("Save");
		
		BorderPane view = new BorderPane();
		HBox bottom = new HBox();
		
		if(pagesWithResults != null) {
			for(WebPage pageWithResults: pagesWithResults) {
				if(pageWithResults.getOutput() != null && pageWithResults.getOutput().size() != 0) {
					this.urls.add(pageWithResults.getURL());
					this.outputs.add(pageWithResults.getOutput());
				}
			}
		}
		
		CheckBoxTreeItem<String> root = new CheckBoxTreeItem<>();
		root.setExpanded(true);
		resultTree.setShowRoot(false);
		System.out.println(root.getValue());
		

		for(int i = 0; i < urls.size(); i++) {	
			Hyperlink link = new Hyperlink(urls.get(i));
			
			link.setOnAction(e -> {
				if(Desktop.isDesktopSupported()) {
					try 						  {Desktop.getDesktop().browse(new URI(link.getText()));}
					catch(IOException ioe)		  {}
					catch(URISyntaxException use) {}
				}
			});
			
			CheckBoxTreeItem<String> box = new CheckBoxTreeItem<>(null, link);
			//box.setGraphic(link);
		
			branches.add(makeBranch(box, root));
			//System.out.println(urls.get(i));
		
			for(int j = 0; j < outputs.get(i).size(); j++) {
				CheckBoxTreeItem<String> box2 = new CheckBoxTreeItem<>(outputs.get(i).get(j));
					
				makeBranch(box2, branches.get(i));
				//System.out.println(outputs.get(i).get(j));
			}
		}

		resultTree.setCellFactory(CheckBoxTreeCell.forTreeView());
		resultTree.setRoot(root);
		
		bottom.setPadding(new Insets(10));
		bottom.setSpacing(10);
		bottom.getChildren().addAll(selectAll, save);
		
		view.setCenter(resultTree);
		view.setBottom(bottom);
		
		resultsTab.setContent(view);
	}
	
	private void addActionListeners() {
		selectAll.setOnMouseClicked(e -> handleSelectAllButtonAction(e));
		save.setOnMouseClicked(e -> handleSaveButtonAction(e));
	}
	
	private void handleSelectAllButtonAction(MouseEvent e) {
		//resultTree
	}
	
	private void handleSaveButtonAction(MouseEvent e) {
		
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
