package webchase;

import javafx.application.Application;
import java.util.*;

/**
 *
 * @author John Filipowicz
 */
public class WebChase {

    /**
     * Launches the application
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Application.launch(PrimaryView.class, args);

        
        //TestClass notUsed = new TestClass("http://www.radford.edu/~nokie/classes/320/");
        List<String> initURLs = new ArrayList();
        //initURLs.add("http://www.radford.edu/~itec324/2016fall-ibarland/Lectures/");
        initURLs.add("http://www.radford.edu/~nokie/classes/320/");
        
        List<String> terms = new ArrayList();
        terms.add("java");
        terms.add("generic type");
        
        int depth = 1;
        
        //TestClass notUsed = new TestClass(testURL, terms);
        
        WebController wc = new WebController(initURLs, terms, depth);
        
        for(WebPage page: wc.getWebPages()){
            for(String output: page.getOutput()){
                System.out.println(output); 
            }
        }
    }
    
}
