package webchase;

import java.util.*;

import javafx.scene.control.*;

public class ResultView {
	
	List<WebPage> pagesWithResults;
	List<String> urls;
	List<List<String>> outputs;
	
	Tab resultsTab;
	
	TreeView<String> resultTree;
	List<TreeItem<String>> branches;
	
	public ResultView(List<WebPage> results, int resultNum) {
		this.pagesWithResults = results;
		urls = new ArrayList<String>();
		outputs = new ArrayList<>();
		
		resultsTab = new Tab("Result" + resultNum);
		
		resultTree = new TreeView<>();
		branches = new ArrayList<>();
		
		for(WebPage pageWithResults: pagesWithResults) {
			if(pageWithResults.getOutput() != null && pageWithResults.getOutput().size() != 0) {
				this.urls.add(pageWithResults.getURL());
				this.outputs.add(pageWithResults.getOutput());
			}
		}
		
		TreeItem<String> root = new TreeItem<>();
		root.setExpanded(true);
		resultTree.setShowRoot(false);
		
		for(int i = 0; i < urls.size(); i++) {	
			branches.add(makeBranch(urls.get(i), root));
			//System.out.println(urls.get(i));
			
			for(int j = 0; j < outputs.get(i).size(); j++) {
				makeBranch(outputs.get(i).get(j), branches.get(i));
				//System.out.println(outputs.get(i).get(j));
			}
		}
		
		resultsTab.setContent(resultTree);
		
		resultTree.setRoot(root);
	}
	
	public TreeItem<String> makeBranch(String title, TreeItem<String> parent) {
		TreeItem<String> item = new TreeItem<>(title);
		item.setExpanded(true);
		parent.getChildren().add(item);
		return item;
	}
	
	public Tab getResultTab() {
		return this.resultsTab;
	}

}
