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
        //Application.launch(PrimaryView.class, args);
        List<String> urls = new ArrayList();
        List<String> terms = new ArrayList();
        
        //urls.add("http://www.radford.edu/~nokie/classes/320/");
        //terms.add("generic packages");
        urls.add("http://www.google.com");
        terms.add("google");
        
        WebController wc = new WebController(urls,terms,2);
        Thread t = new Thread(wc);
        t.start();
        t.join();
    }
}
