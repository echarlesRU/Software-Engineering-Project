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
        
        urls.add("http://www.radford.edu/~nokie/classes/320/");
        terms.add("generic packages");
        
        WebController wc = new WebController(urls,terms,4);
        wc.start();
        wc.join();
        
        //System.out.println(wc.getWebPages().get(0).pageHTML);
        int counter = 0;
        
        
        if(wc.getWebPages() != null){
        for(Future<WebPage> page: wc.getWebPages()){
            if(page.get().getOutput() != null){
            for(String output: page.get().getOutput()){
                //System.out.println(output);
                counter++;
            }
            }
        }
        }
        
        System.out.println("\nHits: " + counter);

    }
    
}
