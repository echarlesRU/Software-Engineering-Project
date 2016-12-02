package webchase;

import java.util.*;
import java.util.concurrent.*;
import javafx.application.Application;

/**
 * WebChase Application v1.1
 * See README for more information.
 * @author John Filipowicz
 */
public class WebChase {

    /**
     * Launches the application
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Application.launch(PrimaryView.class, args);
    }
}
