package webchase;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class SavePopup {

	Scene popupScene;
	Stage stage;
	
	VBox savePopupLayout;
	
	TextArea specifiedSaves;
	Label specifiedSavesLabel;
	Label directToHelp;
	HBox saveButtonArea;
	
	Button saveButton;
	Button cancelButton;
	
	public SavePopup() {
		savePopupLayout = new VBox();
		
		specifiedSaves = new TextArea();
		specifiedSavesLabel = new Label("For more information about formats, see help menu.");
		directToHelp = new Label();
		saveButtonArea = new HBox();
		
		saveButton = new Button("Save");
		cancelButton = new Button("Cancel");
		
		specifiedSaves.setPadding(new Insets(10));
		specifiedSaves.setFocusTraversable(false);
		specifiedSaves.setPromptText("Save Specification Formats: 1(1,3,9) or 1 or 1(1..3) or 1,2 or 1..2");
		
		saveButtonArea.setPadding(new Insets(10));
		saveButtonArea.setSpacing(20);
		saveButtonArea.setAlignment(Pos.CENTER);
		saveButtonArea.getChildren().addAll(saveButton, cancelButton);
		
		savePopupLayout.setAlignment(Pos.CENTER);
		savePopupLayout.getChildren().addAll(specifiedSaves, specifiedSavesLabel, saveButtonArea);
		
		popupScene = new Scene(savePopupLayout,500,300);
		
		stage = new Stage();
		stage.setScene(popupScene);
		stage.setTitle("Save Options");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.centerOnScreen();
		stage.showAndWait();
	}
	
}
