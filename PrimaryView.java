package webchase;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * The primary frame of the application
 * @author John Filipowicz
 */
public class PrimaryView extends Application{
    Scene mainScene;
    BorderPane mainLayout;
    TextArea left, right, top, bottom, center;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Sets title
        primaryStage.setTitle("WebChase: Expert Web Crawler");
        
        // Node initialization (textArea is child of Node)
        left = new TextArea("This is the left");
        right = new TextArea("This is the right");
        top = new TextArea("This is the top");
        bottom = new TextArea("This is the bottom");
        center = new TextArea("This is the center");
        
        // Layout instantiation
        mainLayout = new BorderPane();
        mainLayout.setLeft(left);
        mainLayout.setRight(right);
        mainLayout.setTop(top);
        mainLayout.setBottom(bottom);
        mainLayout.setCenter(center);
        mainLayout.setPadding(new Insets(15));
        
        // Scene initialization (layout, width, height)
        mainScene = new Scene(mainLayout, 1900, 900);
        
        // Stage (like a frame) initialization
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
    
}
